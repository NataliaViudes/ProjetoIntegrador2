import { useEffect, useMemo, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "../CardapioMensal/Cardapio.css";
import ItensCardapio from "./ItensCardapio";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";


//import { ReactComponent as Pencil } from "../../assets/icons/pencil.svg";
//import { ReactComponent as Trash } from "../../assets/icons/trash.svg";
//import { ReactComponent as Salad } from "../../assets/icons/salad.svg";

import { ReactComponent as Pencil } from "../../assets/icons/pencil.svg";
import { ReactComponent as Trash } from "../../assets/icons/trash.svg";
import { ReactComponent as Salad } from "../../assets/icons/salad.svg";


moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function Cardapio() {
  const [modo, setModo] = useState("lista");
  const [cardapioSelecionado, setCardapioSelecionado] = useState(null);

  const [cardapio, setCardapio] = useState([]);
  const [agendamentos, setAgendamentos] = useState([]);

  const [nome, setNome] = useState("");
  const [data, setData] = useState("");
  const [hora, setHora] = useState("");
  const [idAgendamento, setIdAgendamento] = useState("");

  const [cardapioEditando, setCardapioEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("month");

  const [erros, setErros] = useState({
    nome: false,
    data: false,
    hora: false,
    idAgendamento: false,
    horaForaAtividade: false,
  });

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
      setAgendamentos(
        Array.isArray(respAgendamentos.data) ? respAgendamentos.data : []
      );
    } catch (e) {
      console.error("Erro ao carregar:", e);
    }
  }

  function onlyDate(date) {
    return moment(date).format("YYYY-MM-DD");
  }

  const atividadesDia = useMemo(() => {
    if (!data) return [];
    return agendamentos.filter((ag) => onlyDate(ag.dataInicio) === data);
  }, [data, agendamentos]);

  const eventosCardapio = useMemo(() => {
    return cardapio.map((c) => {
      const dataHora = new Date(`${c.data}T${c.hora}`);
      return {
        id: c.id,
        title: `${c.nome}`,
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
      title: `${ag.atividade?.descricao || "Atividade"}`,
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
    setNome("");
    setData("");
    setHora("");
    setIdAgendamento("");
    setErros({
      nome: false,
      data: false,
      hora: false,
      idAgendamento: false,
      horaForaAtividade: false,
    });
  }

  function horaDentroDaAtividade() {
    const atividade = agendamentos.find((a) => a.id == idAgendamento);
    if (!atividade) return false;

    const inicio = moment(atividade.dataInicio);
    const fim = moment(atividade.dataFim);
    const horaSelecionada = moment(`${data}T${hora}`);

    return horaSelecionada.isBetween(inicio, fim, null, "[)");
  }

  function validarFormulario() {
    const novosErros = {
      nome: !nome,
      data: !data,
      hora: !hora,
      idAgendamento: !idAgendamento,
      horaForaAtividade: false,
    };

    if (!novosErros.data && !novosErros.hora && !novosErros.idAgendamento) {
      if (!horaDentroDaAtividade()) {
        novosErros.horaForaAtividade = true;
      }
    }

    setErros(novosErros);

    return !Object.values(novosErros).some(Boolean);
  }

  async function salvar() {
    if (!validarFormulario()) return;

    const payload = {
      id: cardapioEditando?.id || null,
      nome,
      data,
      hora: hora.length === 5 ? `${hora}:00` : hora,
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
    setNome(c.nome || "");
    setData(c.data || "");
    setHora(c.hora ? c.hora.substring(0, 5) : "");
    setIdAgendamento(c.agendamento?.id?.toString() || "");
    setModo("lista");
  }

  async function excluir(id) {
    if (!window.confirm("Deseja excluir?")) return;

    await api.delete(`/cardapio/${id}`);
    carregarTudo();
  }

  function abrirItens(c) {
    setCardapioSelecionado(c);
    limparFormulario();
    setModo("itens");
  }

  function selecionarSlot(slotInfo) {
    setData(formatDate(slotInfo.start));
  }

  function selecionarEvento(evento) {
    const item = evento.resource;

    if (evento.tipo === "atividade") {
      setData(formatDate(item.dataInicio));
      setHora(moment(item.dataInicio).format("HH:mm"));
      setIdAgendamento(item.id.toString());
      setCardapioEditando(null);
      setCardapioSelecionado(null);
    }

    if (evento.tipo === "cardapio") {
      abrirItens(item);
    }
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


  function getNomeAtividade(idAgendamento) {
    const ag = agendamentos.find((a) => a.id === idAgendamento);
    return ag?.atividade?.descricao || "Sem descrição";
  }

  return (
    <div className="pagina-cardapio">
      <Menu />

      <div className="conteudo-cardapio">
        <section className="painel-formulario">
          <h2>Cardápios</h2>

          <label>Data</label>
          <input
            type="date"
            value={data}
            onChange={(e) => setData(e.target.value)}
            className={erros.data ? "input-erro" : ""}
          />

          <label>Hora</label>
          <input
            type="time"
            value={hora}
            onChange={(e) => setHora(e.target.value)}
            className={erros.hora || erros.horaForaAtividade ? "input-erro" : ""}
          />

          <label>Atividades do dia</label>
          <select
            value={idAgendamento}
            onChange={(e) => setIdAgendamento(e.target.value)}
            className={erros.idAgendamento ? "input-erro" : ""}
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

          <label>Nome</label>
          <input
            type="text"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            className={erros.nome ? "input-erro" : ""}
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>
              {cardapioEditando ? "Atualizar" : "Salvar"}
            </button>

            <button onClick={limparFormulario}>Limpar</button>
          </div>

          <div className="lista-cardapio">
            <h3>Cardápios Agendados</h3>

            {cardapio.length === 0 ? (
              <p>Nenhum cardápio agendado.</p>
            ) : (
              cardapio.map((c) => (
                <div key={c.id} className="item-cardapio">
                  <div>
                    <strong>{c.nome}</strong>
                    <div>{c.data}</div>
                    <div>{c.hora}</div>
                    <div>Atividade: {getNomeAtividade(c.agendamento?.id)}</div>
                  </div>

                  <div className="acoes-item">
                    <button onClick={() => abrirItens(c)} title="Itens">
                      <Salad />
                    </button>

                    <button onClick={() => editar(c)} title="Editar">
                      <Pencil />
                    </button>

                    <button onClick={() => excluir(c.id)} title="Excluir">
                      <Trash />
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </section>

        {modo === "itens" && cardapioSelecionado && (
          <ItensCardapio
            cardapio={cardapioSelecionado}
            voltar={() => setModo("lista")}
            agendamentos={agendamentos}
          />
        )}

        {modo !== "itens" && (
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
              eventPropGetter={estiloEvento}
              style={{ height: "80vh" }}
            />
          </section>
        )}
      </div>
    </div>
  );
}

export default Cardapio;  