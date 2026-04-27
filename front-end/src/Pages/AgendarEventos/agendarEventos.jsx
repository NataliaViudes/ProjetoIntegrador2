import { useEffect, useMemo, useState } from "react";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "moment/locale/pt-br";
import "react-big-calendar/lib/css/react-big-calendar.css";
import api from "../../services/api";
import Menu from "../../Components/Menu/Menu";
import "./agendarEventos.css";

moment.locale("pt-br");
const localizer = momentLocalizer(moment);

function Eventos() {
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
    // const [respEventos, respCategorias, respFuncionarios] = await Promise.all([
    //   api.get("/eventos/nome"),
    //   api.get("/cat-eventos/categoria"),
    //   api.get("/funcionarios")
    // ]);

    const [respEventos, respCategorias, ] = await Promise.all([
      api.get("/eventos/nome"),
      api.get("/cat-eventos/categoria"),
    ]);

    setEventosApi(Array.isArray(respEventos.data) ? respEventos.data : []);
    setCategorias(Array.isArray(respCategorias.data) ? respCategorias.data : []);

    console.log(respEventos);
    console.log(respCategorias);
    
    
   // setFuncionarios(Array.isArray(respFuncionarios.data) ? respFuncionarios.data : []);
  } catch (e) {
    console.error("Erro ao carregar dados:", e);
  }
}

  const eventos = useMemo(() => {
    return eventosApi.map((ev) => ({
      id: ev.id,
      title: `${ev.nome} (Func: ${ev.idFuncionario})`,
      start: new Date(ev.dataInicio),
      end: new Date(ev.dataFim),
      resource: ev
    }));
  }, [eventosApi]);

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
    setEventoEditando(null);
    setNome("");
    setDataInicio("");
    setDataFim("");
    setLocal("");
    setQtd("");
    setIdCatEvento("");
    setIdFuncionario("");
  }

  async function salvar() {
    if (!nome || !dataInicio || !dataFim || !local || !qtd || !idCatEvento || !idFuncionario) {
      alert("Preencha nome, data inicial e final, local, quantidade, categoria e funcionario.");
      return;
    }

    const payload = {
      nome,
      dataInicio,
      dataFim,
      local,
      qtd: Number(qtd),
      idCatEvento: Number(idCatEvento),
      idFuncionario: Number(idFuncionario)
    };

    try {
      if (eventoEditando) {
        await api.put(`/eventos/${eventoEditando.id}`, payload);
      } else {
        await api.post("/eventos", payload);
      }

      limparFormulario();
      //carregarEventos();
    } catch (e) {
      console.error("Erro ao salvar evento:", e);
      alert("Erro ao salvar evento.");
    }
  }

  function editar(ev) {
    setEventoEditando(ev);
    setNome(ev.nome || "");
    setDataInicio(formatarDatetimeLocal(ev.dataInicio));
    setDataFim(formatarDatetimeLocal(ev.dataFim));
    setLocal(ev.local || "");
    setQtd(ev.qtd || "");
    setIdCatEvento(ev.idCatEvento || "");
    setIdFuncionario(ev.idFuncionario || "");
  }

  async function excluir(id) {
    console.log(id);
    
    const confirmou = window.confirm("Deseja excluir este evento?");
    if (!confirmou) return;

    try {
      await api.delete(`/eventos/${id}`);

      if (eventoEditando && eventoEditando.id === id) {
        limparFormulario();
      }

      //carregarEventos();
    } catch (e) {
      console.error("Erro ao excluir evento:", e);
      alert("Erro ao excluir evento.");
    }
  }

  function selecionarEvento(evento) {
    editar(evento.resource);
  }

  function selecionarSlot(slotInfo) {
    setDataInicio(formatarDatetimeLocal(slotInfo.start));
    setDataFim(formatarDatetimeLocal(slotInfo.end));
  }

  return (
    <div className="pagina-agendamentos" translate="no">
      <Menu />

      <div className="conteudo-agendamentos">
        <section className="painel-formulario">
          <h2>Eventos</h2>

          <label>Nome do Evento</label>
          <input value={nome} onChange={(e) => setNome(e.target.value)} />

          <label>Categoria do evento</label>
            <select value={idCatEvento} onChange={(e) => setIdCatEvento(e.target.value)}>
            <option value="">Selecione a categoria</option>
            {categorias.map((cat) => (
                <option key={cat.id} value={cat.id}>
                {cat.descricao}
                </option>
            ))}
            </select>

          <label>Funcionário</label>
            <select value={idFuncionario} onChange={(e) => setIdFuncionario(e.target.value)}>
            <option value="">Selecione o funcionário</option>
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
            onChange={(e) => setDataInicio(e.target.value)}
          />

          <label>Data e hora final</label>
          <input
            type="datetime-local"
            value={dataFim}
            onChange={(e) => setDataFim(e.target.value)}
          />

          <label>Local</label>
          <textarea
            rows="2"
            value={local}
            onChange={(e) => setLocal(e.target.value)}
          />

          <label>Quantidade</label>
          <input
            type="number"
            value={qtd}
            onChange={(e) => setQtd(e.target.value)}
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>
              {eventoEditando ? "Atualizar" : "Salvar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>
          </div>

          <div className="lista-agendamentos">
            <h3>Eventos</h3>

            {eventosApi.length === 0 ? (
              <p>Nenhum evento cadastrado.</p>
            ) : (
              eventosApi.map((ev) => {

                const nomeCategoria = categorias.find(c => c.id === ev.idCatEvento)?.descricao;
                const nomeFuncionario = funcionarios.find(f => f.id === ev.idFuncionario)?.nome;

                return (
                <div key={ev.id} className="item-agendamento">
                  <div>
                    <strong>{ev.nome}</strong>
                    <div>Categoria: {nomeCategoria || ev.idCatEvento}</div>
                    <div>Funcionário: {nomeFuncionario || ev.idFuncionario}</div>
                    <div>Local: {ev.local}</div>
                    <div>Qtd: {ev.qtd}</div>
                    <div>
                      {new Date(ev.dataInicio).toLocaleString()} -{" "}
                      {new Date(ev.dataFim).toLocaleString()}
                    </div>
                  </div>

                  <div className="acoes-item">
                    <button onClick={() => editar(ev)}>Editar</button>
                    <button onClick={() => excluir(ev.id)}>Excluir</button>
                  </div>
                </div>
              )})
            )}
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
              noEventsInRange: "Nenhum evento neste período",
              allDay: "Dia inteiro"
            }}
            style={{ height: "80vh" }}
          />
        </section>
      </div>
    </div>
  );
}

export default Eventos;

