import { useEffect, useMemo, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Agendamentos/Agendamentos.css";

import ItensCardapio from "./ItensCardapio";

import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function Cardapio() {
  const [modo, setModo] = useState("lista");
  const [cardapioSelecionado, setCardapioSelecionado] = useState(null);

  const [cardapio, setCardapio] = useState([]);
  const [agendamentos, setAgendamentos] = useState([]);

  const [descricao, setDescricao] = useState("");
  const [data, setData] = useState("");
  const [hora, setHora] = useState("");
  const [idAgendamento, setIdAgendamento] = useState("");

  const [cardapioEditando, setCardapioEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("month");

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respCardapio, respAgendamentos] = await Promise.all([
        api.get("/cardapio"),
        api.get("/agendamentos"),
      ]);

      setCardapio(Array.isArray(respCardapio.data) ? respCardapio.data : []);
      setAgendamentos(Array.isArray(respAgendamentos.data) ? respAgendamentos.data : []);
    } catch (e) {
      console.error("Erro ao carregar:", e);
    }
  }

  function onlyDate(date) {
    return moment(date).format("YYYY-MM-DD");
  }

  const atividadesDia = useMemo(() => {
    if (!data) return [];

    return agendamentos.filter((ag) => {
      return onlyDate(ag.dataInicio) === data;
    });
  }, [data, agendamentos]);

  const eventosCardapio = useMemo(() => {
    return cardapio.map((c) => {
      const dataHora = new Date(`${c.data}T${c.hora}`);

      return {
        id: c.id,
        title: `${c.descricao} (${c.agendamento?.atividade?.descricao || "Sem atividade"})`,
        start: dataHora,
        end: new Date(dataHora.getTime() + 60 * 60 * 1000),
        resource: c,
        tipo: "cardapio",
      };
    });
  }, [cardapio]);

  const eventosAtividades = useMemo(() => {
    return agendamentos.map((ag) => ({
      id: `ag-${ag.id}`,
      title: ` ${ag.atividade?.descricao || "Atividade"}`,
      start: new Date(ag.dataInicio),
      end: new Date(ag.dataFim),
      resource: ag,
      tipo: "atividade",
    }));
  }, [agendamentos]);

  const eventosCalendario = useMemo(() => {
    return [...eventosAtividades, ...eventosCardapio].sort(
      (a, b) => new Date(a.start) - new Date(b.start)
    );
  }, [eventosAtividades, eventosCardapio]);

  function formatDate(date) {
    return moment(date).format("YYYY-MM-DD");
  }

  function limparFormulario() {
    setCardapioEditando(null);
    setDescricao("");
    setData("");
    setHora("");
    setIdAgendamento("");
  }

  async function salvar() {
    if (!descricao || !data || !hora || !idAgendamento) {
      alert("Preencha todos os campos.");
      return;
    }

    if (atividadesDia.length === 0) {
      alert("Não existe atividade neste dia.");
      return;
    }

    const payload = {
      id: cardapioEditando?.id || null,
      descricao,
      data,
      hora,
      agendamento: { id: Number(idAgendamento) },
    };

    try {
      if (cardapioEditando) {
        await api.put(`/cardapio/${cardapioEditando.id}`, payload);
      } else {
        await api.post("/cardapio", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (e) {
      console.error("Erro salvar:", e);
    }
  }

  function editar(c) {
    setCardapioEditando(c);
    setDescricao(c.descricao || "");
    setData(c.data || "");
    setHora(c.hora || "");
    setIdAgendamento(c.agendamento?.id || "");
  }

  async function excluir(id) {
    if (!window.confirm("Deseja excluir?")) return;

    await api.delete(`/cardapio/${id}`);
    carregarTudo();
  }

  function abrirItens(c) {
    setCardapioSelecionado(c);
    setModo("itens");
  }

  function selecionarSlot(slotInfo) {
    setData(formatDate(slotInfo.start));
  }

  function selecionarEvento(evento) {
    const ag = evento.resource;

    if (!ag?.dataInicio) return;

    setData(formatDate(ag.dataInicio));
    setHora(moment(ag.dataInicio).format("HH:mm"));
    setIdAgendamento(ag.id);
  }

  return (
    <div className="pagina-cardapio">
      <Menu />

      <div className="conteudo-cardapio">

        {/* FORM */}
        <section className="painel-formulario">
          <h2>Cardápios</h2>

          <label>Data</label>
          <input
            type="date"
            value={data}
            onChange={(e) => setData(e.target.value)}
          />

          <label>Hora</label>
          <input
            type="time"
            value={hora}
            onChange={(e) => setHora(e.target.value)}
          />

          {/* ATIVIDADES DO DIA */}
          <label>Atividades do dia</label>

          <select
            value={idAgendamento}
            onChange={(e) => setIdAgendamento(e.target.value)}
          >
            <option value="">Selecione uma atividade</option>

            {atividadesDia.length === 0 ? (
              <option disabled>Nenhuma atividade neste dia</option>
            ) : (
              atividadesDia.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.atividade?.descricao}
                </option>
              ))
            )}
          </select>

          <label>Descrição</label>
          <textarea
            rows="4"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>
              {cardapioEditando ? "Atualizar" : "Salvar"}
            </button>

            <button onClick={limparFormulario}>Limpar</button>

            <button
              onClick={() => {
                if (!cardapioSelecionado) {
                  alert("Selecione um cardápio");
                  return;
                }
                setModo("itens");
              }}
            >
              Itens
            </button>
          </div>

          {/* LISTA */}
          <div className="lista">
            <h3>Cardápios</h3>

            {cardapio.map((c) => (
              <div key={c.id} className="item">
                <div>
                  <strong>{c.descricao}</strong>
                  <div>{c.data} {c.hora}</div>
                  <div>Atividade: {c.agendamento?.id}</div>
                </div>

                <div>
                  <button onClick={() => editar(c)}>Editar</button>
                  <button onClick={() => excluir(c.id)}>Excluir</button>
                  <button onClick={() => abrirItens(c)}>Itens</button>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* ITENS */}
        {modo === "itens" && cardapioSelecionado && (
          <ItensCardapio
            cardapio={cardapioSelecionado}
            voltar={() => setModo("lista")}
          />
        )}

        {/* CALENDÁRIO */}
        <section className="painel-calendario">
          <Calendar
            localizer={localizer}
            events={eventosCalendario}
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

            onSelectSlot={selecionarSlot}
            onSelectEvent={selecionarEvento}

            style={{ height: "80vh" }}
          />
        </section>

      </div>
    </div>
  );
}

export default Cardapio;