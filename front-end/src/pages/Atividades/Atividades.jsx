import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "./Atividades.css";


function Atividades() {
  const [descricao, setDescricao] = useState("");
  const [funcionarios, setFuncionarios] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [atividades, setAtividades] = useState([]);
  const [busca, setBusca] = useState("");

  const [funcionarioId, setFuncionarioId] = useState("");
  const [categoriaId, setCategoriaId] = useState("");

  const [atividadeEditando, setAtividadeEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
  try {
    const respAtividades = await api.get("/atividades");
  

    const respFuncionarios = await api.get("/funcionario");
   

    const respCategorias = await api.get("/categoriaAtividade");
    

    setAtividades(Array.isArray(respAtividades.data) ? respAtividades.data : []);
    setFuncionarios(Array.isArray(respFuncionarios.data) ? respFuncionarios.data : []);
    setCategorias(Array.isArray(respCategorias.data) ? respCategorias.data : []);
  } catch (error) {
    console.error("Erro ao carregar dados:", error);
  }
}

  async function salvarOuAtualizar() {
    if (!descricao || !funcionarioId || !categoriaId) {
      alert("Preencha nome, funcionário e categoria.");
      return;
    }

    const payload = {
      descricao: descricao,
      funcionario: { id: Number(funcionarioId) },
      categoria: { id: Number(categoriaId) }
    };

    try {
      if (atividadeEditando) {
        await api.put("/atividades", {
          id: atividadeEditando.id,
          ...payload
        });
      } else {
        await api.post("/atividades", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (error) {
      console.error("Erro ao salvar/atualizar:", error);
      alert("Erro ao salvar atividade.");
    }
  }

  function editarAtividade(atividade) {
    setAtividadeEditando(atividade);
    setDescricao(atividade.descricao || "");
    setFuncionarioId(atividade.funcionario?.id || "");
    setCategoriaId(atividade.categoria?.id || "");
  }

  async function excluirAtividade(id) {
    try {
      await api.delete(`/atividades/${id}`);
      if (atividadeEditando && atividadeEditando.id === id) {
        limparFormulario();
      }
      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      alert("Erro ao excluir atividade.");
    }
  }

  function limparFormulario() {
    setAtividadeEditando(null);
    setDescricao("");
    setFuncionarioId("");
    setCategoriaId("");
  }

  const atividadesFiltradas = atividades.filter((atividade) =>
    (atividade.descricao || "").toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <div className="pagina-atividades" translate="no">
      <Menu />

      <main className="conteudo-atividades">
        <section className="painel-esquerdo">
          <h2>Cadastro de Atividade</h2>

          <label>Nome da atividade</label>
          <input
            type="text"
            placeholder="Digite o nome da atividade"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />

          <label>Funcionário</label>
          <select
            value={funcionarioId}
            onChange={(e) => setFuncionarioId(e.target.value)}
          >
            <option value="">Selecione um funcionário</option>
            {funcionarios.map((funcionario) => (
              <option key={funcionario.id} value={funcionario.id}>
                {funcionario.nome}
              </option>
            ))}
          </select>

          <label>Categoria</label>
          <select
            value={categoriaId}
            onChange={(e) => setCategoriaId(e.target.value)}
          >
            <option value="">Selecione uma categoria</option>
            {categorias.map((categoria) => (
              <option key={categoria.id} value={categoria.id}>
                {categoria.nome}
              </option>
            ))}
          </select>

          <div className="acoes-formulario">
            <button onClick={salvarOuAtualizar}>
              {atividadeEditando ? "Atualizar" : "Confirmar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>
          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">
            <h2>Atividades cadastradas</h2>
            <input
              type="text"
              placeholder="Buscar por nome..."
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />
          </div>

          <div className="lista-atividades">
            {atividadesFiltradas.length === 0 ? (
              <p>Nenhuma atividade encontrada.</p>
            ) : (
              atividadesFiltradas.map((atividade) => (
                <div className="item-atividade" key={atividade.id}>
                  <div className="info-atividade">
                    <strong>{atividade.descricao}</strong>
                    <span>
                      Funcionário: {atividade.funcionario?.nome || "Não informado"}
                    </span>
                    <span>
                      Categoria: {atividade.categoria?.nome || "Não informada"}
                    </span>
                  </div>

                  <div className="botoes-item">
                    <button onClick={() => editarAtividade(atividade)}>
                      Editar
                    </button>
                    <button onClick={() => excluirAtividade(atividade.id)}>
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

export default Atividades;