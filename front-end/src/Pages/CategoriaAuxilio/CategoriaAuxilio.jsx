import { useEffect, useState } from "react";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu.jsx";
import "./CategoriaAuxilio.css";

function CategoriaAuxilio() {
  const [nome, setNome] = useState("");
  const [categorias, setCategorias] = useState([]);
  const [filtro, setFiltro] = useState("");
  const [categoriaEditando, setCategoriaEditando] = useState(null);

  useEffect(() => {
    carregarCategorias();
  }, []);

  async function carregarCategorias() {
    try {
      const resp = await api.get("/categorias");
      setCategorias(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar categorias:", error);
      setCategorias([]);
    }
  }

  async function buscarCategorias() {
    try {
      const resp = await api.get(`/categorias?filtro=${filtro}`);
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
        await api.put(`/categorias/${categoriaEditando.id}`, payload);
      } else {
        await api.post("/categorias", payload);
      }

      limparFormulario();
      carregarCategorias();
    } catch (error) {
      console.error("Erro ao salvar categoria:", error);
      alert("Erro ao salvar categoria de auxílio.");
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
    await api.delete(`/categorias/${id}`);

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
      alert("Erro ao excluir categoria de auxílio.");
    }
  }
}

  function limparFormulario() {
    setNome("");
    setCategoriaEditando(null);
  }

  return (
    <div className="pagina-categoria-auxilio" translate="no">
      <Menu />

      <main className="conteudo-categoria-auxilio">
        <section className="painel-esquerdo">
          <h2>Cadastro de Categoria de Auxílio</h2>

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
            <h2>Categorias de Auxílio</h2>

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
                Listar todas
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

export default CategoriaAuxilio;