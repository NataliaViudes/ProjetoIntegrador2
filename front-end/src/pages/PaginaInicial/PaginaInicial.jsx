import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";
import api from "../../services/api";
import Menu from "../../components/Menu";
import Button from "../../components/Button/Button"


moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function PaginaInicial() {
const [agendamentos, setAgendamentos] = useState([]);
  const [atividades, setAtividades] = useState([]);

  const [atividadeId, setAtividadeId] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [observacao, setObservacao] = useState("");

  const [agendamentoEditando, setAgendamentoEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("month");

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAgendamentos, respAtividades] = await Promise.all([
        api.get("/agendamentos"),
        api.get("/atividades")
      ]);

      setAgendamentos(Array.isArray(respAgendamentos.data) ? respAgendamentos.data : []);
      setAtividades(Array.isArray(respAtividades.data) ? respAtividades.data : []);
    } catch (e) {
      console.error("Erro ao carregar agendamentos:", e);
    }
  }

  const eventos = useMemo(() => {
    return agendamentos.map((ag) => ({
      id: ag.id,
      title: `${ag.atividade?.descricao || "Atividade"} - ${ag.atividade?.funcionario?.nome || ""}`,
      start: new Date(ag.dataInicio),
      end: new Date(ag.dataFim),
      resource: ag
    }));
  }, [agendamentos]);

  function formatarDatetimeLocal(valor) {
    const d = new Date(valor);
    const ano = d.getFullYear();
    const mes = String(d.getMonth() + 1).padStart(2, "0");
    const dia = String(d.getDate()).padStart(2, "0");
    const hora = String(d.getHours()).padStart(2, "0");
    const minuto = String(d.getMinutes()).padStart(2, "0");

    return `${ano}-${mes}-${dia}T${hora}:${minuto}`;
  }

  function limparFormulario() {
    setAgendamentoEditando(null);
    setAtividadeId("");
    setDataInicio("");
    setDataFim("");
    setObservacao("");
  }

  async function salvar() {
    if (!atividadeId || !dataInicio || !dataFim) {
      alert("Preencha atividade, data e hora inicial e data e hora final.");
      return;
    }

    const payload = {
      atividade: { id: Number(atividadeId) },
      dataInicio: dataInicio,
      dataFim: dataFim,
      observacao: observacao
    };

    try {
      if (agendamentoEditando) {
        await api.put(`/agendamentos/${agendamentoEditando.id}`, payload);
      } else {
        await api.post("/agendamentos", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (e) {
      console.error("Erro ao salvar agendamento:", e);
      alert("Erro ao salvar agendamento.");
    }
  }

  function editar(ag) {
    setAgendamentoEditando(ag);
    setAtividadeId(String(ag.atividade?.id || ""));
    setDataInicio(formatarDatetimeLocal(ag.dataInicio));
    setDataFim(formatarDatetimeLocal(ag.dataFim));
    setObservacao(ag.observacao || "");
  }


  function selecionarEvento(evento) {
    editar(evento.resource);
  }

  function selecionarSlot(slotInfo) {
    setDataInicio(formatarDatetimeLocal(slotInfo.start));
    setDataFim(formatarDatetimeLocal(slotInfo.end));
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
            onNavigate={(novaData) => setDataAtual(novaData)}
            onView={(novaView) => setViewAtual(novaView)}
            views={["month", "week", "day", "agenda"]}
            defaultView="month"
            onSelectEvent={selecionarEvento}
            onSelectSlot={selecionarSlot}
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
              noEventsInRange: "Nenhum agendamento neste período",
              allDay: "Dia inteiro"
            }}
            style={{ height: "70vh" }}
          />
          
        </section>
        <Button/>
        
      </div>
  );
}
export default PaginaInicial;