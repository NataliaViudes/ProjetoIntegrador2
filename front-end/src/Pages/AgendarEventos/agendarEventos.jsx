import { useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";

import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";

import Swal from "sweetalert2";

import ItensEvento from "../ItensEvento";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu";

import "./agendarEventos.css";

import moment from "moment";
import "moment/locale/pt-br";
moment.locale("pt-br");

const localizer = momentLocalizer(moment);
function Eventos() {
  const [erros, setErros] = useState({});
  const [somenteLeitura, setSomenteLeitura] = useState(false);

  const [modo, setModo] = useState("calendario");

  const [eventosApi, setEventosApi] = useState([]);

  const [eventoSelecionado, setEventoSelecionado] = useState(null);
  const [eventoEditando, setEventoEditando] = useState(null);

  const [categorias, setCategorias] = useState([]);
  const [funcionarios, setFuncionarios] = useState([]);

  const [nome, setNome] = useState("");
  const [local, setLocal] = useState("");
  const [qtd, setQtd] = useState("");

  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");

  const [idCatEvento, setIdCatEvento] = useState("");
  const [idFuncionario, setIdFuncionario] = useState("");

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

  function formatarDatetimeLocal(dataIso) {
    if (!dataIso) return "";

    return dataIso.substring(0, 16);
  }

  function eventoJaPassou(inicioEvento) {
    if (!inicioEvento) return false;

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const dataEvento = new Date(inicioEvento);
    dataEvento.setHours(0, 0, 0, 0);

    return dataEvento < hoje;
  }

  const eventos = useMemo(() => {
    return eventosApi
      .map((ev) => {
        if (!ev.inicio || !ev.fim) return null;

        return {
          id: ev.idEvento,
          title: ev.nome,
          start: new Date(ev.inicio),
          end: new Date(ev.fim),
          resource: ev,
        };
      })
      .filter(Boolean);
  }, [eventosApi]);

  function possuiConflitoHorario() {
    if (!dataInicio || !dataFim) return false;

    const inicioNovo = moment(dataInicio);
    const fimNovo = moment(dataFim);

    let possuiConflito = false;

    eventosApi.forEach((ev) => {
      // ignora o próprio evento no update
      if (
        eventoEditando &&
        Number(ev.idEvento) === Number(eventoEditando.idEvento)
      ) {
        return;
      }

      const inicioExistente = moment(ev.inicio);
      const fimExistente = moment(ev.fim);

      const conflito =
        inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente);

      if (conflito) {
        possuiConflito = true;
      }
    });

    return possuiConflito;
  }

  function limparFormulario() {
    setEventoSelecionado(null);
    setEventoEditando(null);

    setNome("");
    setLocal("");
    setQtd("");

    setDataInicio("");
    setDataFim("");

    setIdCatEvento("");
    setIdFuncionario("");

    setErros({});

    setSomenteLeitura(false);
  }

  async function salvar() {
    const novosErros = {};

    if (!nome) novosErros.nome = true;
    if (!local) novosErros.local = true;
    if (!qtd) novosErros.qtd = true;

    if (!dataInicio) novosErros.dataInicio = true;
    if (!dataFim) novosErros.dataFim = true;

    if (!idCatEvento) novosErros.idCatEvento = true;
    if (!idFuncionario) novosErros.idFuncionario = true;

    if (Object.keys(novosErros).length > 0) {
      setErros(novosErros);

      Swal.fire({
        icon: "warning",
        title: "Campos obrigatórios",
        text: "Preencha todos os campos",
      });

      return;
    }

    const inicio = new Date(dataInicio);
    const fim = new Date(dataFim);

    if (fim <= inicio) {
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "A data final deve ser maior que a inicial",
      });

      return;
    }

    if (possuiConflitoHorario()) {
      Swal.fire({
        icon: "error",
        title: "Conflito",
        text: "Já existe um evento nesse horário",
      });

      return;
    }

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const dataEvento = new Date(inicio);
    dataEvento.setHours(0, 0, 0, 0);

    if (dataEvento < hoje) {
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Não é permitido cadastrar eventos em datas passadas",
      });

      return;
    }

    const payload = {
      idEvento: eventoEditando?.idEvento || null,

      nome: nome,
      local: local,

      qtd: Number(qtd),

      inicio: moment(dataInicio).format("YYYY-MM-DDTHH:mm:ss"),
      fim: moment(dataFim).format("YYYY-MM-DDTHH:mm:ss"),

      categoria: {
        id: Number(idCatEvento),
      },

      funcionario: {
        id: Number(idFuncionario),
      },
    };

    try {
      console.log("PAYLOAD ENVIADO:");
      console.log(payload);

      if (eventoEditando) {
        await api.put("/evento", payload);
      } else {
        await api.post("/evento", payload);
      }

      await carregarTudo();

      limparFormulario();

      Swal.fire({
        icon: "success",
        title: "Sucesso",
        text: eventoEditando ? "Evento atualizado" : "Evento criado",
        timer: 1500,
        showConfirmButton: false,
      });
    } catch (e) {
      console.error("ERRO COMPLETO:", e);

      console.log("RESPOSTA BACK:");
      console.log(e?.response?.data);

      const erroBack =
        e?.response?.data?.message ||
        e?.response?.data?.erro ||
        e?.message ||
        "Erro ao salvar evento";

      Swal.fire({
        icon: "error",
        title: "Erro ao salvar",
        text: erroBack,
      });
    }
  }

  function editar(ev) {
    const eventoAntigo = eventoJaPassou(ev.inicio);

    setSomenteLeitura(eventoAntigo);

    setEventoSelecionado(ev);
    setEventoEditando(ev);

    setNome(ev.nome || "");
    setLocal(ev.local || "");
    setQtd(ev.qtd || "");

    setDataInicio(formatarDatetimeLocal(ev.inicio));
    setDataFim(formatarDatetimeLocal(ev.fim));

    setIdCatEvento(ev.idCatEvento || "");
    setIdFuncionario(ev.idFuncionario || "");
  }

  async function excluir(idEvento) {
    const result = await Swal.fire({
      title: "Deseja excluir esse evento?",
      text: "Essa ação não pode ser desfeita",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    try {
      await api.delete(`/evento/${idEvento}`);

      if (eventoEditando && eventoEditando.idEvento === idEvento) {
        limparFormulario();
      }

      await carregarTudo();

      Swal.fire({
        icon: "success",
        title: "Evento removido",
        timer: 1200,
        showConfirmButton: false,
      });
    } catch (e) {
      console.error(e);

      const erroBack =
        e?.response?.data?.message ||
        e?.response?.data?.erro ||
        e?.message ||
        "Erro ao excluir evento";

      Swal.fire({
        icon: "error",
        title: "Erro",
        text: erroBack,
      });
    }
  }

  function selecionarSlot(slotInfo) {
    setDataInicio(moment(slotInfo.start).format("YYYY-MM-DDTHH:mm"));

    setDataFim(moment(slotInfo.end).format("YYYY-MM-DDTHH:mm"));

    setErros((prev) => ({
      ...prev,
      dataInicio: false,
      dataFim: false,
    }));
  }

  return (
    <div className="pagina-agendamentos" translate="no">
      <Menu />

      <div className="conteudo-agendamentos">
        <section className="painel-formulario">
          <h2>Eventos</h2>

          <label>Nome do Evento</label>

          <input
            disabled={somenteLeitura}
            value={nome}
            onChange={(e) => {
              setNome(e.target.value);

              setErros({
                ...erros,
                nome: false,
              });
            }}
            className={erros.nome ? "input-erro" : ""}
          />

          <label>Categoria</label>

          <select
            disabled={somenteLeitura}
            value={idCatEvento}
            onChange={(e) => {
              setIdCatEvento(e.target.value);

              setErros({
                ...erros,
                idCatEvento: false,
              });
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
            disabled={somenteLeitura}
            value={idFuncionario}
            onChange={(e) => {
              setIdFuncionario(e.target.value);

              setErros({
                ...erros,
                idFuncionario: false,
              });
            }}
            className={erros.idFuncionario ? "input-erro" : ""}
          >
            <option value="">Selecione um funcionário</option>

            {funcionarios.map((func) => (
              <option key={func.id} value={func.id}>
                {func.nome}
              </option>
            ))}
          </select>

          <label>Data Inicial</label>

          <input
            type="datetime-local"
            disabled={somenteLeitura}
            value={dataInicio}
            onChange={(e) => {
              setDataInicio(e.target.value);

              setErros({
                ...erros,
                dataInicio: false,
              });
            }}
            className={erros.dataInicio ? "input-erro" : ""}
          />

          <label>Data Final</label>

          <input
            type="datetime-local"
            disabled={somenteLeitura}
            value={dataFim}
            onChange={(e) => {
              setDataFim(e.target.value);

              setErros({
                ...erros,
                dataFim: false,
              });
            }}
            className={erros.dataFim ? "input-erro" : ""}
          />

          <label>Local</label>

          <textarea
            rows="2"
            disabled={somenteLeitura}
            value={local}
            onChange={(e) => {
              setLocal(e.target.value);

              setErros({
                ...erros,
                local: false,
              });
            }}
            className={erros.local ? "input-erro" : ""}
          />

          <label>Quantidade</label>

          <input
            type="number"
            disabled={somenteLeitura}
            value={qtd}
            onChange={(e) => {
              setQtd(e.target.value);

              setErros({
                ...erros,
                qtd: false,
              });
            }}
            className={erros.qtd ? "input-erro" : ""}
          />

          <div className="acoes-formulario">
            <button onClick={salvar} disabled={somenteLeitura}>
              {eventoEditando ? "Atualizar" : "Salvar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>

            <button
              type="button"
              onClick={() => {
                if (!eventoSelecionado) {
                  Swal.fire({
                    icon: "warning",
                    title: "Selecione um evento",
                  });

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
              eventosApi.map((ev) => (
                <div key={ev.idEvento} className="item-agendamento">
                  <div>
                    <strong>{ev.nome}</strong>

                    <div>Categoria: {ev.categoria?.categoria}</div>

                    <div>
                      Funcionário:{" "}
                      {funcionarios.find((f) => f.id === ev.idFuncionario)
                        ?.nome || "Não informado"}
                    </div>

                    <div>Local: {ev.local}</div>

                    <div>Qtd: {ev.qtd}</div>

                    <div>
                      {moment(ev.inicio).format("DD/MM/YYYY HH:mm")}
                      {" - "}
                      {moment(ev.fim).format("DD/MM/YYYY HH:mm")}
                    </div>
                  </div>

                  <div className="acoes-item">
                    <button onClick={() => editar(ev)}>Editar</button>

                    <button onClick={() => excluir(ev.idEvento)}>
                      Excluir
                    </button>
                  </div>
                </div>
              ))
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
              formats={{
                weekdayFormat: (date) => moment(date).format("ddd"),

                dayFormat: (date) => moment(date).format("DD ddd"),

                monthHeaderFormat: (date) => moment(date).format("MMMM YYYY"),

                dayHeaderFormat: (date) => moment(date).format("dddd DD/MM"),

                dayRangeHeaderFormat: ({ start, end }) =>
                  `${moment(start).format("DD MMM")} — ${moment(end).format("DD MMM")}`,
              }}
              messages={{
                date: "Data",
                time: "Hora",
                event: "Evento",
                allDay: "Dia inteiro",
                week: "Semana",
                work_week: "Semana de trabalho",
                day: "Dia",
                month: "Mês",
                previous: "Anterior",
                next: "Próximo",
                yesterday: "Ontem",
                tomorrow: "Amanhã",
                today: "Hoje",
                agenda: "Agenda",
                noEventsInRange: "Nenhum evento neste período",
                showMore: (total) => `+${total} mais`,
              }}
              onSelectEvent={(evento) => {
                editar(evento.resource);
              }}
              onSelectSlot={selecionarSlot}
              style={{
                height: "80vh",
              }}
            />
          </section>
        ) : (
          <>
            {eventoSelecionado ? (
              <ItensEvento
                key={eventoSelecionado.idEvento}
                evento={eventoSelecionado}
                somenteLeitura={somenteLeitura}
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
