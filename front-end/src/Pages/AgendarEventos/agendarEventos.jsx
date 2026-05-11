import { useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

import ItensEvento from "../ItensEvento";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu";
import "./agendarEventos.css";

moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function Eventos() {
  const [erros, setErros] = useState({});
  const [modo, setModo] = useState("calendario");
  const [eventoSelecionado, setEventoSelecionado] = useState(null);

  const [eventosApi, setEventosApi] = useState([]);

  const [nome, setNome] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [local, setLocal] = useState("");
  const [qtd, setQtd] = useState("");
  const [idCatEvento, setIdCatEvento] = useState("");
  const [idFuncionario, setIdFuncionario] = useState("");

  const [categorias, setCategorias] = useState([]);
  const [funcionarios, setFuncionarios] = useState([]);

  const [eventoEditando, setEventoEditando] = useState(null);

  const [dataAtual, setDataAtual] = useState(new Date());
  const [viewAtual, setViewAtual] = useState("week");

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respEventos, respCategorias, respFuncionarios] = await Promise.all(
        [
          api.get("/evento/nome"),
          api.get("/cat-eventos/categoria"),
          api.get("/funcionarios"),
        ],
      );

      setEventosApi(Array.isArray(respEventos.data) ? respEventos.data : []);
      setCategorias(
        Array.isArray(respCategorias.data) ? respCategorias.data : [],
      );
      setFuncionarios(
        Array.isArray(respFuncionarios.data) ? respFuncionarios.data : [],
      );
    } catch (e) {
      console.error("Erro ao carregar dados:", e);
    }
  }

  function eventoJaPassou(dataEvento) {
    const [ano, mes, dia] = dataEvento.split("-").map(Number);

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const data = new Date(ano, mes - 1, dia);
    data.setHours(0, 0, 0, 0);

    return data < hoje;
  }

  //  BACK → FRONT (calendar)
  const eventos = useMemo(() => {
    return eventosApi
      .map((ev) => {
        if (!ev.data || !ev.horaInicio || !ev.horaFim) return null;

        const [ano, mes, dia] = ev.data.split("-").map(Number);
        const [hIni, mIni] = ev.horaInicio.split(":").map(Number);
        const [hFim, mFim] = ev.horaFim.split(":").map(Number);

        const inicio = new Date(ano, mes - 1, dia, hIni, mIni);
        const fim = new Date(ano, mes - 1, dia, hFim, mFim);

        return {
          id: ev.id,
          title: ev.nome,
          start: inicio,
          end: fim,
          resource: ev,
        };
      })
      .filter(Boolean); // remove null
  }, [eventosApi]);

  //  FRONT → BACK
  function separarDataHora(datetime) {
    const d = new Date(datetime);

    const data = d.toISOString().split("T")[0];
    const hora = d.toTimeString().split(" ")[0];

    return { data, hora };
  }

  function juntarDataHora(data, hora) {
    if (!data || !hora) return "";
    return `${data}T${hora.substring(0, 5)}`;
  }

  function limparFormulario() {
    setEventoSelecionado(null);
    setEventoEditando(null);
    setNome("");
    setDataInicio("");
    setDataFim("");
    setLocal("");
    setQtd("");
    setIdCatEvento("");
    setIdFuncionario("");
    setErros({});
  }

  async function salvar() {
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const dataEvento = new Date(dataInicio.split("T")[0] + "T00:00:00");

    dataEvento.setHours(0, 0, 0, 0);

    if (dataEvento < hoje) {
      alert("Não é permitido criar eventos em datas passadas.");
      return;
    }

    if (new Date(dataFim) <= new Date(dataInicio)) {
      alert("A data/hora final deve ser maior que a inicial.");
      return;
    }
    const novosErros = {};

    if (!idFuncionario) novosErros.idFuncionario = true;
    if (!nome) novosErros.nome = true;
    if (!dataInicio) novosErros.dataInicio = true;
    if (!dataFim) novosErros.dataFim = true;
    if (!local) novosErros.local = true;
    if (!qtd) novosErros.qtd = true;
    if (!idCatEvento) novosErros.idCatEvento = true;

    if (Object.keys(novosErros).length > 0) {
      setErros(novosErros);
      return;
    }

    const inicio = separarDataHora(dataInicio);
    const fim = separarDataHora(dataFim);

    const payload = {
      id: eventoEditando?.id || null,
      nome,
      local,
      qtd: Number(qtd),
      data: inicio.data,
      horaInicio: inicio.hora,
      horaFim: fim.hora,
      categoria: { id: Number(idCatEvento) },
      idFuncionario: idFuncionario ? Number(idFuncionario) : null,
    };

    try {
      if (eventoEditando) {
        await api.put("/evento", payload);
      } else {
        await api.post("/evento", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (e) {
      console.error("Erro ao salvar evento:", e);
      alert("Erro ao salvar evento.");
    }
  }

  function editar(ev) {
    if (eventoJaPassou(ev.data)) {
      alert("Eventos antigos não podem ser editados.");
      return;
    }
    setEventoSelecionado(ev);
    setEventoEditando(ev);
    setNome(ev.nome || "");
    setDataInicio(juntarDataHora(ev.data, ev.horaInicio));
    setDataFim(juntarDataHora(ev.data, ev.horaFim));
    setLocal(ev.local || "");
    setQtd(ev.qtd || "");
    setIdCatEvento(ev.categoria?.id || "");
    setIdFuncionario(ev.idFuncionario || "");
  }

  async function excluir(id) {
    const confirmou = window.confirm("Deseja excluir este evento?");
    if (!confirmou) return;

    try {
      await api.delete(`/evento/${id}`);

      if (eventoEditando && eventoEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (e) {
      console.error("Erro ao excluir evento:", e);
      alert("Erro ao excluir evento.");
    }
  }

  function selecionarEventoItens(evento) {
    setEventoSelecionado(evento.resource);
    setModo("itens");
  }

  function selecionarEvento(evento) {
    editar(evento.resource);
  }

  function selecionarSlot(slotInfo) {
    setErros((prev) => ({ ...prev, dataFim: false }));
    setErros((prev) => ({ ...prev, dataInicio: false }));
    setDataInicio(
      juntarDataHora(
        slotInfo.start.toISOString().split("T")[0],
        slotInfo.start.toTimeString().split(" ")[0],
      ),
    );
    setDataFim(
      juntarDataHora(
        slotInfo.end.toISOString().split("T")[0],
        slotInfo.end.toTimeString().split(" ")[0],
      ),
    );
  }

  return (
    <div className="pagina-agendamentos" translate="no">
      <Menu />

      <div className="conteudo-agendamentos">
        <section className="painel-formulario">
          <h2>Eventos</h2>

          <label>Nome do Evento</label>
          <input
            value={nome}
            onChange={(e) => {
              setNome(e.target.value);
              setErros({ ...erros, nome: false });
            }}
            className={erros.nome ? "input-erro" : ""}
          />

          <label>Categoria do evento</label>
          <select
            value={idCatEvento}
            onChange={(e) => {
              setIdCatEvento(e.target.value);
              setErros({ ...erros, idCatEvento: false });
            }}
            className={erros.idCatEvento ? "input-erro" : ""}
          >
            <option value="">Selecione a categoria</option>
            {categorias.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.categoria}
              </option>
            ))}
          </select>

          <label>Funcionário</label>

          <select
            value={idFuncionario}
            onChange={(e) => setIdFuncionario(e.target.value)}
            className={erros.idFuncionario ? "input-erro" : ""}
          >
            <option value="">Selecione um funcionário</option>

            {funcionarios.map((func) => (
              <option key={func.id} value={func.id}>
                {func.nome}
              </option>
            ))}
          </select>

          <label>Data e hora inicial</label>
          <input
            type="datetime-local"
            value={dataInicio}
            onChange={(e) => {
              setDataInicio(e.target.value);
              setErros({ ...erros, dataInicio: false });
            }}
            className={erros.dataInicio ? "input-erro" : ""}
          />

          <label>Data e hora final</label>
          <input
            type="datetime-local"
            value={dataFim}
            onChange={(e) => {
              setDataFim(e.target.value);
              setErros({ ...erros, dataFim: false });
            }}
            className={erros.dataFim ? "input-erro" : ""}
          />

          <label>Local</label>
          <textarea
            rows="2"
            value={local}
            onChange={(e) => {
              setLocal(e.target.value);
              setErros({ ...erros, local: false });
            }}
            className={erros.local ? "input-erro" : ""}
          />

          <label>Quantidade</label>
          <input
            type="number"
            value={qtd}
            onChange={(e) => {
              setQtd(e.target.value);
              setErros({ ...erros, qtd: false });
            }}
            className={erros.qtd ? "input-erro" : ""}
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>
              {eventoEditando ? "Atualizar" : "Salvar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>

            <button
              type="button"
              onClick={() => {
                if (!eventoSelecionado) {
                  alert("Selecione um evento no calendário primeiro.");
                  return;
                }
                setModo("itens");
              }}
            >
              Adicionar Itens
            </button>
          </div>

          <div className="lista-agendamentos">
            <h3>Eventos</h3>

            {eventosApi.length === 0 ? (
              <p>Nenhum evento cadastrado.</p>
            ) : (
              eventosApi.map((ev) => {
                return (
                  <div key={ev.id} className="item-agendamento">
                    <div>
                      <strong>{ev.nome}</strong>
                      <div>Categoria: {ev.categoria?.descricao}</div>
                      <div>
                        Funcionário:{" "}
                        {funcionarios.find((f) => f.id === ev.idFuncionario)
                          ?.nome || "Não informado"}
                      </div>
                      <div>Local: {ev.local}</div>
                      <div>Qtd: {ev.qtd}</div>
                      <div>
                        {ev.data} {ev.horaInicio} - {ev.horaFim}
                      </div>
                    </div>

                    <div className="acoes-item">
                      <button onClick={() => editar(ev)}>Editar</button>
                      <button onClick={() => excluir(ev.id)}>Excluir</button>
                    </div>
                  </div>
                );
              })
            )}
          </div>
        </section>
        {modo === "calendario" ? (
          <section className="painel-calendario">
            <Calendar
              localizer={localizer}
              culture="pt-BR"
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
              onSelectEvent={(evento) => {
                setEventoSelecionado(evento.resource);
                editar(evento.resource);
              }}
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
                noEventsInRange: "Nenhum evento neste período",
                allDay: "Dia inteiro",
                showMore: (total) => `+ Ver mais (${total})`,
              }}
              style={{ height: "80vh" }}
            />
          </section>
        ) : (
          <>
            {eventoSelecionado ? (
              <ItensEvento
                key={eventoSelecionado?.id}
                evento={eventoSelecionado}
                voltar={() => setModo("calendario")}
              />
            ) : (
              <div style={{ padding: "20px" }}>
                <button onClick={() => setModo("calendario")}>⬅ Voltar</button>

                <h2>Nenhum evento selecionado</h2>
                <p>Selecione um evento antes de adicionar itens.</p>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default Eventos;
