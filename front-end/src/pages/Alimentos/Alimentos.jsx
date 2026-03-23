import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "./Alimentos.css";

function Alimentos() {
  const [nome, setNome] = useState("");
  const [tipo, setTipo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [busca, setBusca] = useState("");
  const [alimentos, setAlimentos] = useState([]);
  const [alimentoEditando, setAlimentoEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/alimentos");
      setAlimentos(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar alimentos:", error);
    }
  }

  async function salvarOuAtualizar() {
    if (!nome || !tipo || !descricao) {
      alert("Preencha nome, tipo e descrição.");
      return;
    }

    const payload = { nome, tipo, descricao };

    try {
      if (alimentoEditando) {
        await api.put("/alimentos", {
          id: alimentoEditando.id,
          ...payload,
        });
      } else {
        await api.post("/alimentos", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (error) {
      console.error("Erro ao salvar:", error);
      alert("Erro ao salvar alimento.");
    }
  }

  function editarAlimento(alimento) {
    setAlimentoEditando(alimento);
    setNome(alimento.nome || "");
    setTipo(alimento.tipo || "");
    setDescricao(alimento.descricao || "");
  }

  async function excluirAlimento(id) {
    try {
      await api.delete(`/alimentos/${id}`);
      if (alimentoEditando && alimentoEditando.id === id) {
        limparFormulario();
      }
      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      alert("Erro ao excluir alimento.");
    }
  }

  function limparFormulario() {
    setAlimentoEditando(null);
    setNome("");
    setTipo("");
    setDescricao("");
  }

  const alimentosFiltrados = alimentos.filter((alimento) =>
    (alimento.nome || "").toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <div className="pagina-alimentos" translate="no">
      <Menu />

      <main className="conteudo-alimentos">
        <section className="painel-esquerdo">
          <h2>Cadastro de Alimentos</h2>

          <label>Nome</label>
          <input
            type="text"
            placeholder="Digite o nome do alimento"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />

          <label>Tipo</label>
          <input
            type="text"
            placeholder="Ex: Prato, Salgado, Doce, Bebida..."
            value={tipo}
            onChange={(e) => setTipo(e.target.value)}
          />

          <label>Descrição</label>
          <input
            type="text"
            placeholder="Descrição do alimento"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />

          <div className="acoes-formulario">
            <button onClick={salvarOuAtualizar}>
              {alimentoEditando ? "Atualizar" : "Confirmar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>
          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">
            <h2>Alimentos cadastrados</h2>
            <input
              type="text"
              placeholder="Buscar por nome..."
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />
          </div>

          <div className="lista-alimentos">
            {alimentosFiltrados.length === 0 ? (
              <p>Nenhum alimento encontrado.</p>
            ) : (
              alimentosFiltrados.map((alimento) => (
                <div className="item-alimento" key={alimento.id}>
                  <div className="info-alimento">
                    <strong>{alimento.nome}</strong>
                    <span>Tipo: {alimento.tipo}</span>
                    <span>Descrição: {alimento.descricao}</span>
                  </div>

                  <div className="botoes-item">
                    <button onClick={() => editarAlimento(alimento)}>
                      Editar
                    </button>
                    <button onClick={() => excluirAlimento(alimento.id)}>
                      Excluir
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default Alimentos;