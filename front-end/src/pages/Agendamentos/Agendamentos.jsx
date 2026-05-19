import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "./Agendamentos.css";


moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function Agendamentos() {
  const [agendamentos, setAgendamentos] = useState([]);
  const [atividades, setAtividades] = useState([]);

  const [atividadeId, setAtividadeId] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [observacao, setObservacao] = useState("");

  const [agendamentoEditando, setAgendamentoEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("week");

  const [agendamentoSelecionado, setAgendamentoSelecionado] = useState(null);
const [mostrarOpcoes, setMostrarOpcoes] = useState(false);

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
      start: parseData(ag.dataInicio),
      end: parseData(ag.dataFim),
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

  function parseData(data) {
    return new Date(data.replace(" ", "T"));
  }

 async function salvar() {
  if (!atividadeId || !dataInicio || !dataFim) {
    alert("Preencha atividade, data e hora inicial e data e hora final.");
    return;
  }

  const inicio = new Date(dataInicio);
  const fim = new Date(dataFim);
  const agora = new Date();

  if (inicio < agora) {
    alert("Não é possível cadastrar agendamento em data ou horário que já passou.");
    return;
  }

  if (fim <= inicio) {
    alert("A data final deve ser maior que a data inicial.");
    return;
  }

  const conflito = agendamentos.some((ag) => {
    if (agendamentoEditando && ag.id === agendamentoEditando.id) {
      return false;
    }

    const inicioExistente = parseData(ag.dataInicio);
    const fimExistente = parseData(ag.dataFim);

    return inicio < fimExistente && fim > inicioExistente;
  });

  if (conflito) {
    alert("Já existe um agendamento nesse intervalo de horário.");
    return;
  }

  const payload = {
    atividade: { id: Number(atividadeId) },
    dataInicio: dataInicio + ":00",
    dataFim: dataFim + ":00",
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
    alert(agendamentoEditando ? "Agendamento atualizado com sucesso!" : "Agendamento cadastrado com sucesso!");
  } catch (e) {
  console.error("Erro ao salvar agendamento:", e);

  if (e.response?.data?.mensagem) {
    alert(e.response.data.mensagem);
  } else if (e.response?.data?.mensage) {
    alert(e.response.data.mensage);
  } else if (e.response?.data) {
    alert(JSON.stringify(e.response.data));
  } else {
    alert("Erro ao salvar agendamento.");
  }
}
}

  function editar(ag) {
    setAgendamentoEditando(ag);
    setAtividadeId(String(ag.atividade?.id || ""));
    setDataInicio(formatarDatetimeLocal(ag.dataInicio));
    setDataFim(formatarDatetimeLocal(ag.dataFim));
    setObservacao(ag.observacao || "");
  }

  async function excluir(id) {
    const confirmou = window.confirm("Deseja excluir este agendamento?");
    if (!confirmou) return;

    try {
      await api.delete(`/agendamentos/${id}`);

      if (agendamentoEditando && agendamentoEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (e) {
      console.error("Erro ao excluir agendamento:", e);
      alert("Erro ao excluir agendamento.");
    }
  }

 function selecionarEvento(evento) {
  setAgendamentoSelecionado(evento.resource);
  setMostrarOpcoes(true);
}

  function selecionarSlot(slotInfo) {
  const agora = new Date();

  if (slotInfo.start < agora) {
    alert("Não é possível selecionar uma data ou horário que já passou.");
    return;
  }

  setDataInicio(formatarDatetimeLocal(slotInfo.start));
  setDataFim(formatarDatetimeLocal(slotInfo.end));
}
  return (
    <div className="pagina-agendamentos" translate="no">
      <Menu />

      <div className="conteudo-agendamentos">
        <section className="painel-formulario">
          <h2>Agendar atividade</h2>

          <label>Atividade</label>
          <select value={atividadeId} onChange={(e) => setAtividadeId(e.target.value)}>
            <option value="">Selecione uma atividade</option>
            {atividades.map((atividade) => (
              <option key={atividade.id} value={atividade.id}>
                {atividade.descricao} - {atividade.funcionario?.nome || ""}
              </option>
            ))}
          </select>

          <label>Data e hora inicial</label>
          <input
            type="datetime-local"
            value={dataInicio}
            min={formatarDatetimeLocal(new Date())}
            onChange={(e) => setDataInicio(e.target.value)}
          />

          <label>Data e hora final</label>
          <input
            type="datetime-local"
            value={dataFim}
            min={dataInicio || formatarDatetimeLocal(new Date())}
            onChange={(e) => setDataFim(e.target.value)}
          />


          <div className="acoes-formulario">
            <button onClick={salvar}>
              {agendamentoEditando ? "Atualizar" : "Salvar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>

            {agendamentoEditando && (
              <Link to={`/planejar-etapa/${agendamentoEditando.id}`}>
                <button type="button">Planejar</button>
              </Link>
            )}

            <Link
              to="/vincular"
              state={{
                atividadeId,
                dataInicio,
                dataFim,
                observacao,
                idAgendamento: agendamentoEditando?.id || null
              }}
            >
              <button type="button">Ir para Vinculação</button>
            </Link>
        </div>


        </section>

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
            defaultView="week"
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
            style={{ height: "80vh" }}
          />
        </section>
      </div>

      {mostrarOpcoes && agendamentoSelecionado && (
  <div className="modal-overlay">
    <div className="modal-opcoes">

      <h3>Agendamento</h3>

      <p>
        <strong>Atividade:</strong>{" "}
        {agendamentoSelecionado.atividade?.descricao}
      </p>

      <p>
        <strong>Funcionário:</strong>{" "}
        {agendamentoSelecionado.atividade?.funcionario?.nome}
      </p>

      <div className="acoes-modal">

        <button
          onClick={() => {
            editar(agendamentoSelecionado);
            setMostrarOpcoes(false);
          }}
        >
          Editar
        </button>

        <button
          onClick={() => {
            excluir(agendamentoSelecionado.id);
            setMostrarOpcoes(false);
          }}
        >
          Excluir
        </button>

        <button
          onClick={() => setMostrarOpcoes(false)}
        >
          Fechar
        </button>

      </div>
    </div>
  </div>
)}
    </div>
  );
}

export default Agendamentos;