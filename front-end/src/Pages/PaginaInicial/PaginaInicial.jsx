import { useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu";

import "./PaginaInicial.css";

moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function PaginaInicial() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const [agendamentos, setAgendamentos] = useState([]);
  const [cardapios, setCardapios] = useState([]);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("month");

  const [open, setOpen] = useState(false);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAgendamentos, respCardapios] = await Promise.all([
        api.get("/agendamentos"),
        api.get("/cardapio"),
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
    } catch (e) {
      console.error("Erro ao carregar dados:", e);
    }
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
        style: {
          backgroundColor: "#3174ad",
          color: "white",
          borderRadius: "5px",
        },
      };
    }

    if (evento.tipo === "cardapio") {
      return {
        style: {
          backgroundColor: "#28a745",
          color: "white",
          borderRadius: "5px",
        },
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
      )};
    </div>
  );
}

export default PaginaInicial;