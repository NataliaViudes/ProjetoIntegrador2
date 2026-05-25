import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "../Alimentos/Alimentos.css";

function TipoEstoque() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;




  const [tipo, setTipo] = useState("");
  const [busca, setBusca] = useState("");
  const [tipos, setTipos] = useState([]);
  const [editando, setEditando] = useState(null);
  const [erro, setErro] = useState(false);

  useEffect(() => {
    carregar();
  }, []);

  async function carregar() {
    try {
      const resp = await api.get("/tipo-estoque/tipo");
      setTipos(Array.isArray(resp.data) ? resp.data : []);
    } catch (e) {
      console.error(e);
    }
  }

  async function salvar() {
    if (!tipo) {
      setErro(true);
      console.log("aaaaa");
      return;
    }
    console.log(tipo);
    setErro(false);

    try {
      if (editando) {
        await api.put("/tipo-estoque", {
          id: editando.id,
          tipo,
        });
      } else {
        await api.post("/tipo-estoque", { tipo });
      }

      limpar();
      carregar();
    } catch (e) {
      console.error(e);
      alert("Erro ao salvar");
    }
  }

  function editar(item) {
    setEditando(item);
    setTipo(item.tipo);
  }

  async function excluir(id) {
    const confirmar = window.confirm("Tem certeza que deseja excluir esta categoria?");

    if (!confirmar) return;

    try {
      await api.delete(`/tipo-estoque/${id}`);
      carregar();
    } catch (e) {
      console.error(e);
      alert("Erro ao excluir");
    }
  }

  function limpar() {
    setTipo("");
    setEditando(null);
  }

  const filtrados = tipos.filter((t) =>
    t.tipo.toLowerCase().includes(busca.toLowerCase())
  );

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
    <div className="pagina-alimentos">
      <Menu />

      <main className="conteudo-alimentos">
        <section className="painel-esquerdo">
          <h2>Tipo de Estoque</h2>

          <label>Categoria</label>

          <input
            value={tipo}
            onChange={(e) => {
              setTipo(e.target.value);
              if (erro) setErro(false);
            }}
            className={erro ? "input-erro" : ""}
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>
              {editando ? "Atualizar" : "Confirmar"}
            </button>
            <button onClick={limpar}>Limpar</button>
          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">
            <h2>Categorias</h2>
            <input
              placeholder="Buscar..."
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />
          </div>

          <div className="lista-alimentos">
            {filtrados.map((t) => (
              <div className="item-alimento" key={t.id}>
                <strong>{t.tipo}</strong>

                <div className="botoes-item">
                  <button onClick={() => editar(t)}>Editar</button>
                  <button onClick={() => excluir(t.id)}>Excluir</button>
                </div>
              </div>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}

export default TipoEstoque;