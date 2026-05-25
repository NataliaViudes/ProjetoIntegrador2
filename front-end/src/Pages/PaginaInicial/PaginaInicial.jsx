import { useCallback, useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

import api from "../../services/api";
import Menu from "../../components/Menu/Menu";

import "./PaginaInicial.css";

moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function PaginaInicial() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const [agendamentos, setAgendamentos] = useState([]);
  const [cardapios, setCardapios] = useState([]);
  const [prescricoes, setPrescricoes] = useState([]);
  const [notificacaoPrescricao, setNotificacaoPrescricao] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("month");

  const [open, setOpen] = useState(false);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAgendamentos, respCardapios, respPrescricoes] = await Promise.all([
        api.get("/agendamentos"),
        api.get("/cardapio"),
        api.get("/prescricoes"),
      ]);

      setAgendamentos(
        Array.isArray(respAgendamentos.data)
          ? respAgendamentos.data
          : []
      );

      setCardapios(
        Array.isArray(respCardapios.data)
          ? respCardapios.data
          : []
      );

      setPrescricoes(
        Array.isArray(respPrescricoes.data)
          ? respPrescricoes.data
          : []
      );
    } catch (e) {
      console.error("Erro ao carregar dados:", e);
    }
  }

  function getChavePrescricaoConfirmada(prescricao) {
    return `prescricao-confirmada-${prescricao.id}-${prescricao.horario}`;
  }

  function tocarAlarmePrescricao() {
    try {
      const AudioContext = window.AudioContext || window.webkitAudioContext;
      const audioContext = new AudioContext();
      const oscillator = audioContext.createOscillator();
      const gain = audioContext.createGain();

      oscillator.type = "sine";
      oscillator.frequency.setValueAtTime(880, audioContext.currentTime);
      gain.gain.setValueAtTime(0.18, audioContext.currentTime);

      oscillator.connect(gain);
      gain.connect(audioContext.destination);

      oscillator.start();
      oscillator.stop(audioContext.currentTime + 0.35);
    } catch (e) {
      console.warn("Nao foi possivel tocar o alarme da prescricao:", e);
    }
  }

  const verificarPrescricoes = useCallback(() => {
    if (notificacaoPrescricao) return;

    const agora = new Date();

    const prescricaoDaHora = prescricoes.find((p) => {
      if (!p?.horario) return false;

      const horario = new Date(p.horario);

      if (Number.isNaN(horario.getTime())) return false;

      const mesmaData =
        horario.getFullYear() === agora.getFullYear() &&
        horario.getMonth() === agora.getMonth() &&
        horario.getDate() === agora.getDate() &&
        horario.getHours() === agora.getHours() &&
        horario.getMinutes() === agora.getMinutes();

      if (!mesmaData) return false;

      return localStorage.getItem(getChavePrescricaoConfirmada(p)) !== "ok";
    });

    if (prescricaoDaHora) {
      tocarAlarmePrescricao();
      setNotificacaoPrescricao(prescricaoDaHora);
    }
  }, [prescricoes, notificacaoPrescricao]);

  useEffect(() => {
    verificarPrescricoes();

    const intervalo = setInterval(() => {
      verificarPrescricoes();
    }, 15000);

    return () => clearInterval(intervalo);
  }, [verificarPrescricoes]);

  useEffect(() => {
    if (!notificacaoPrescricao) return;

    tocarAlarmePrescricao();

    const intervalo = setInterval(() => {
      tocarAlarmePrescricao();
    }, 5000);

    return () => clearInterval(intervalo);
  }, [notificacaoPrescricao]);

  function confirmarPrescricao() {
    if (notificacaoPrescricao) {
      localStorage.setItem(
        getChavePrescricaoConfirmada(notificacaoPrescricao),
        "ok"
      );
    }

    setNotificacaoPrescricao(null);

    setTimeout(() => {
      verificarPrescricoes();
    }, 300);
  }

  const eventos = useMemo(() => {
    const eventosList = [];

    agendamentos.forEach((ag) => {
      eventosList.push({
        id: `atividade-${ag.id}`,

        title:
          `${ag.atividade?.descricao || "Atividade"}` +
          `${ag.atividade?.funcionario
            ? " - " + ag.atividade.funcionario.nome
            : ""
          }`,

        start: new Date(ag.dataInicio),
        end: new Date(ag.dataFim),

        resource: ag,
        tipo: "atividade",
      });
    });

    cardapios.forEach((c) => {
      if (!c.data || !c.hora) return;

      const dataHora = new Date(`${c.data}T${c.hora}`);

      eventosList.push({
        id: `cardapio-${c.id}`,

        title: c.nome,

        start: dataHora,
        end: new Date(dataHora.getTime() + 60 * 60 * 1000),

        resource: c,
        tipo: "cardapio",
      });
    });

    return eventosList;
  }, [agendamentos, cardapios]);

  function selecionarEvento(evento) {
    if (evento.tipo === "atividade") {
      alert(
        `Atividade: ${evento.resource.atividade?.descricao}
Funcionário: ${evento.resource.atividade?.funcionario?.nome ||
        "Não informado"
        }
Data/Hora: ${new Date(
          evento.resource.dataInicio
        ).toLocaleString()} - ${new Date(
          evento.resource.dataFim
        ).toLocaleString()}`
      );
    }

    if (evento.tipo === "cardapio") {
      alert(
        `Cardápio: ${evento.resource.nome}
Data/Hora: ${evento.resource.data} ${evento.resource.hora}`
      );
    }
  }

  function selecionarSlot(slotInfo) {
    alert(
      `Selecionou período:
${slotInfo.start.toLocaleString()}
${slotInfo.end.toLocaleString()}`
    );
  }

  function estiloEvento(evento) {
    if (evento.tipo === "atividade") {
      return {
        className: "evento-atividade",
      };
    }

    if (evento.tipo === "cardapio") {
      return {
        className: "evento-cardapio",
      };
    }

    return {};
  }

  return (
    <div className="pagina-inicial" translate="no">
      <Menu />

      <section className="painel-calendario">
        <Calendar
          localizer={localizer}
          events={eventos}
          startAccessor="start"
          endAccessor="end"
          selectable
          popup
          date={dataAtual}
          view={viewAtual}
          onNavigate={setDataAtual}
          onView={setViewAtual}
          views={["month", "week", "day", "agenda"]}
          defaultView="month"
          onSelectEvent={selecionarEvento}
          onSelectSlot={selecionarSlot}
          eventPropGetter={estiloEvento}
          messages={{
            next: "Próximo",
            previous: "Anterior",
            today: "Hoje",
            month: "Mês",
            week: "Semana",
            day: "Dia",
            agenda: "Agenda",
            date: "Data",
            time: "Hora",
            event: "Evento",
            noEventsInRange:
              "Nenhum agendamento neste período",
            allDay: "Dia inteiro",
          }}
          style={{ height: "70vh" }}
        />
      </section>


      {nivelUsuario >= 3 && (
        <section>
          <div className="container">
            <div className={`menu ${open ? "open" : ""}`}>
              <div className="items">
                <div className="item">
                  <div className="circle">🍔</div>
                  <span>Planejar Cardápio</span>
                </div>

                <div className="item">
                  <div className="circle">📅</div>
                  <span>Agendar Atividade</span>
                </div>
              </div>

              <button
                className="main-btn"
                onClick={() => setOpen(!open)}
              >
                {open ? "✕" : "+"}
              </button>
            </div>
          </div>
        </section>
      )}

      {notificacaoPrescricao && (
        <div className="prescricao-alerta-overlay">
          <div className="prescricao-alerta-modal">
            <div className="prescricao-alerta-icone">!</div>

            <h2>Horario de prescricao</h2>

            <p>
              <strong>Beneficiario:</strong>{" "}
              {notificacaoPrescricao.beneficiario?.nome || "Nao informado"}
            </p>

            <p>
              <strong>Remedio:</strong>{" "}
              {notificacaoPrescricao.remedio?.nome || "Nao informado"}
            </p>

            <p>
              <strong>Dosagem:</strong>{" "}
              {notificacaoPrescricao.dosagem || "Nao informada"}
            </p>

            <p>
              <strong>Quantidade:</strong>{" "}
              {notificacaoPrescricao.quantidade || "Nao informada"}
            </p>

            <p>
              <strong>Horario:</strong>{" "}
              {new Date(notificacaoPrescricao.horario).toLocaleTimeString([], {
                hour: "2-digit",
                minute: "2-digit",
              })}
            </p>

            <button type="button" onClick={confirmarPrescricao}>
              OK
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default PaginaInicial;
