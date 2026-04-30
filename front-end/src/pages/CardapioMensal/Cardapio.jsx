import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Alimentos/Alimentos.css";    

function Cardapio() {
  const [cardapios, setCardapios] = useState([]);
  const [alimentos, setAlimentos] = useState([]);
  const [agendamentos, setAgendamentos] = useState([]);

  const [descricao, setDescricao] = useState("");
  const [data, setData] = useState("");
  const [hora, setHora] = useState("");
  const [quantidade, setQuantidade] = useState("");

  const [alimentoId, setAlimentoId] = useState("");
  const [agendamentoId, setAgendamentoId] = useState("");

  const [editando, setEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respCardapio, respAlimentos, respAgendamentos] =
        await Promise.all([
          api.get("/cardapio"),
          api.get("/alimentos"),
          api.get("/agendamentos"),
        ]);

      setCardapios(respCardapio.data || []);
      setAlimentos(respAlimentos.data || []);
      setAgendamentos(respAgendamentos.data || []);
    } catch (e) {
      console.error("Erro ao carregar dados:", e);
    }
  }

  function limpar() {
    setDescricao("");
    setData("");
    setHora("");
    setQuantidade("");
    setAlimentoId("");
    setAgendamentoId("");
    setEditando(null);
  }

  async function salvar() {
    if (!descricao || !data || !hora) {
      alert("Preencha descrição, data e hora.");
      return;
    }

    const payload = {
      descricao,
      data,
      hora,
      quantidade: Number(quantidade),
      alimento: { id: Number(alimentoId) },
      agendamento: { id: Number(agendamentoId) },
    };

    try {
      if (editando) {
        await api.put(`/cardapio/${editando.id}`, payload);
      } else {
        await api.post("/cardapio", payload);
      }

      limpar();
      carregarTudo();
    } catch (e) {
      console.error("Erro ao salvar:", e);
      alert("Erro ao salvar cardápio.");
    }
  }

  function editar(item) {
    setEditando(item);
    setDescricao(item.descricao || "");
    setData(item.data || "");
    setHora(item.hora || "");
    setQuantidade(item.quantidade || "");
    setAlimentoId(item.alimento?.id || "");
    setAgendamentoId(item.agendamento?.id || "");
  }

  async function excluir(id) {
    if (!window.confirm("Deseja excluir?")) return;

    try {
      await api.delete(`/cardapio/${id}`);
      carregarTudo();
    } catch (e) {
      console.error("Erro ao excluir:", e);
    }
  }

  return (
    <div className="pagina-cardapio">
      <Menu />

      <div className="conteudo-cardapio">
        <section className="formulario">
          <h2>Cadastro de Cardápio</h2>

          <label>Descrição</label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />

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

          <label>Quantidade</label>
          <input
            type="number"
            value={quantidade}
            onChange={(e) => setQuantidade(e.target.value)}
          />

          <label>Alimento</label>
          <select
            value={alimentoId}
            onChange={(e) => setAlimentoId(e.target.value)}
          >
            <option value="">Selecione</option>
            {alimentos.map((a) => (
              <option key={a.id} value={a.id}>
                {a.descricao}
              </option>
            ))}
          </select>

          <label>Agendamento</label>
          <select
            value={agendamentoId}
            onChange={(e) => setAgendamentoId(e.target.value)}
          >
            <option value="">Selecione</option>
            {agendamentos.map((ag) => (
              <option key={ag.id} value={ag.id}>
                {ag.atividade?.descricao}
              </option>
            ))}
          </select>

          <div className="acoes">
            <button onClick={salvar}>
              {editando ? "Atualizar" : "Salvar"}
            </button>

            <button onClick={limpar}>Limpar</button>
          </div>
        </section>

        <section className="lista">
          <h3>Cardápios cadastrados</h3>

          {cardapios.length === 0 ? (
            <p>Nenhum cardápio encontrado.</p>
          ) : (
            cardapios.map((c) => (
              <div key={c.id} className="item">
                <strong>{c.descricao}</strong>

                <div>Data: {c.data}</div>
                <div>Hora: {c.hora}</div>
                <div>Quantidade: {c.quantidade}</div>
                <div>Alimento: {c.alimento?.descricao}</div>
                <div>
                  Atividade: {c.agendamento?.atividade?.descricao}
                </div>

                <div className="acoes-item">
                  <button onClick={() => editar(c)}>Editar</button>
                  <button onClick={() => excluir(c.id)}>Excluir</button>
                </div>
              </div>
            ))
          )}
        </section>
      </div>
    </div>
  );
}

export default Cardapio;