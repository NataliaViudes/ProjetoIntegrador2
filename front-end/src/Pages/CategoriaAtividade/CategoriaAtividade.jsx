import { useEffect, useState } from "react";
import api from "../../services/api.js";
import Menu from "../../components/Menu/Menu.jsx";
import "./CategoriaAtividade.css";

function CategoriaAtividade() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const [nome, setNome] = useState("");
  const [categorias, setCategorias] = useState([]);
  const [filtro, setFiltro] = useState("");
  const [categoriaEditando, setCategoriaEditando] = useState(null);

  useEffect(() => {
    carregarCategorias();
  }, []);

  async function carregarCategorias() {
    try {
      const resp = await api.get("/categoriaAtividade");
      setCategorias(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar categorias:", error);
      setCategorias([]);
    }
  }

  async function buscarCategorias() {
    try {
      const resp = await api.get(`/categoriaAtividade?filtro=${filtro}`);
      setCategorias(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao buscar categorias:", error);
      setCategorias([]);
    }
  }

  async function salvarOuAtualizar() {
    if (!nome.trim()) {
      alert("Digite o nome da categoria.");
      return;
    }

    const payload = {
      nome: nome
    };

    try {
      if (categoriaEditando) {
        await api.put(`/categoriaAtividade/${categoriaEditando.id}`, payload);
      } else {
        await api.post("/categoriaAtividade", payload);
      }

      limparFormulario();
      carregarCategorias();
    } catch (error) {
      console.error("Erro ao salvar categoria:", error);
      alert("Erro ao salvar categoria de atividade.");
    }
  }

  function editarCategoria(categoria) {
    setCategoriaEditando(categoria);
    setNome(categoria.nome || "");
  }

  async function excluirCategoria(id) {

    const confirmar = window.confirm(
      "Deseja realmente excluir esta categoria?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await api.delete(`/categoriaAtividade/${id}`);

      if (categoriaEditando && categoriaEditando.id === id) {
        limparFormulario();
      }

      carregarCategorias();

      alert("Categoria excluída com sucesso!");

    } catch (error) {
      console.error("Erro ao excluir categoria:", error);

      if (error.response?.data) {
        alert(error.response.data);
      } else {
        alert("Erro ao excluir categoria de atividade.");
      }
    }
  }

  function limparFormulario() {
    setNome("");
    setCategoriaEditando(null);
  }

  if (nivelUsuario < 3) {
    return (
      <div>
        <Menu />
        <h2 style={{ padding: "20px" }}>
          Você não possui acesso a esta página.
        </h2>
      </div>
    );
  }
  return (
    <div className="pagina-categoria-atividade" translate="no">
      <Menu />

      <main className="conteudo-categoria-atividade">
        <section className="painel-esquerdo">
          <h2>Cadastro de Categoria de Atividade</h2>

          <label>Nome da categoria</label>
          <input
            type="text"
            placeholder="Digite o nome da categoria"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />

          <div className="acoes-formulario">
            <button onClick={salvarOuAtualizar}>
              {categoriaEditando ? "Atualizar" : "Confirmar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>
          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">
            <h2>Categorias cadastradas</h2>

            <div className="linha-busca-categoria">
              <input
                type="text"
                placeholder="Buscar categoria..."
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
              />

              <button type="button" onClick={buscarCategorias}>
                Buscar
              </button>

              <button type="button" onClick={carregarCategorias}>
                Listar todos
              </button>
            </div>
          </div>

          <div className="lista-categorias">
            {categorias.length === 0 ? (
              <p>Nenhuma categoria encontrada.</p>
            ) : (
              categorias.map((categoria) => (
                <div className="item-categoria" key={categoria.id}>
                  <div className="info-categoria">
                    <strong>{categoria.nome}</strong>
                  </div>

                  <div className="botoes-item">
                    <button onClick={() => editarCategoria(categoria)}>
                      Editar
                    </button>

                    <button onClick={() => excluirCategoria(categoria.id)}>
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

export default CategoriaAtividade;