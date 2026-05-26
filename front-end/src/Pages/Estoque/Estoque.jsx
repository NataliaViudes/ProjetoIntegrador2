import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "../Alimentos/Alimentos.css";

function Estoque() {

  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const [descricao, setDescricao] = useState("");
  const [qtd, setQtd] = useState("");
  const [tipoId, setTipoId] = useState("");

  const [tipos, setTipos] = useState([]);
  const [itens, setItens] = useState([]);

  const [busca, setBusca] = useState("");
  const [filtroTipo, setFiltroTipo] = useState("Todos");

  const [editando, setEditando] = useState(null);

  const [erros, setErros] = useState({
    descricao: false,
    qtd: false,
    tipoId: false,
  });

  useEffect(() => {
    carregarTudo();
    carregarTipos();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/estoque/descricao");
      setItens(Array.isArray(resp.data) ? resp.data : []);
    } catch (e) {
      console.error(e);
    }
  }

  async function carregarTipos() {
    try {
      const resp = await api.get("/tipo-estoque/tipo");
      setTipos(Array.isArray(resp.data) ? resp.data : []);
    } catch (e) {
      console.error(e);
    }
  }

  async function salvar() {

    const novosErros = {
      descricao: !descricao.trim(),
      qtd: !qtd || qtd < 0,
      tipoId: !tipoId,
    };

    setErros(novosErros);

    if (Object.values(novosErros).some((e) => e)) return;

    const payload = {
      descricao,
      qtd: parseInt(qtd),
      tipo: { id: parseInt(tipoId) },
    };

    try {

      if (editando) {

        await api.put("/estoque", {
          id: editando.id,
          ...payload,
        });

      } else {

        await api.post("/estoque", payload);

      }

      limpar();
      carregarTudo();

    } catch (e) {
      console.error(e);
      alert("Erro ao salvar");
    }
  }

  function editar(item) {
    setEditando(item);
    setDescricao(item.descricao);
    setQtd(item.qtd);
    setTipoId(item.tipo.id);
  }

  async function excluir(id) {

    const confirmar = window.confirm(
      "Tem certeza que deseja excluir este item?"
    );

    if (!confirmar) return;

    try {

      await api.delete(`/estoque/${id}`);
      carregarTudo();

    } catch (e) {
      console.error(e);
      alert("Erro ao excluir");
    }
  }

  function limpar() {
    setDescricao("");
    setQtd("");
    setTipoId("");
    setEditando(null);
  }

  const filtrados = itens.filter((i) => {

    const descricaoValida =
      (i.descricao || "")
        .toLowerCase()
        .includes(busca.toLowerCase());

    const tipoValido =
      filtroTipo === "Todos"
        ? true
        : i.tipo?.tipo === filtroTipo;

    return descricaoValida && tipoValido;
  });

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

          <h2>Cadastro de Materiais</h2>

          <label>Descrição</label>

          <input
            value={descricao}
            onChange={(e) => {
              setDescricao(e.target.value);

              if (erros.descricao) {
                setErros({
                  ...erros,
                  descricao: false,
                });
              }
            }}
            className={erros.descricao ? "input-erro" : ""}
          />

          <label>Quantidade</label>

          <input
            className={erros.qtd ? "input-erro" : ""}
            type="number"
            min="0"
            value={qtd}
            onChange={(e) => {

              const valor = e.target.value;

              if (valor < 0) return;

              setQtd(valor);
            }}
          />

          <label>Categoria</label>

          <select
            value={tipoId}
            onChange={(e) => {
              setTipoId(e.target.value);

              if (erros.tipoId) {
                setErros({
                  ...erros,
                  tipoId: false,
                });
              }
            }}
            className={erros.tipoId ? "input-erro" : ""}
          >
            <option value="">Selecione</option>

            {tipos.map((t) => (
              <option key={t.id} value={t.id}>
                {t.tipo}
              </option>
            ))}
          </select>

          <div className="acoes-formulario">

            <button onClick={salvar}>
              {editando ? "Atualizar" : "Confirmar"}
            </button>

            <button onClick={limpar}>
              Limpar
            </button>

          </div>
        </section>

        <section className="painel-direito">

          <div className="cabecalho-lista">

            <h2>Materiais</h2>

            <input
              placeholder="Buscar..."
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />

            {/* 🔥 FILTRO POR CATEGORIA */}
            <select
              value={filtroTipo}
              onChange={(e) => setFiltroTipo(e.target.value)}
            >
              <option value="Todos">Todos</option>

              {tipos.map((t) => (
                <option key={t.id} value={t.tipo}>
                  {t.tipo}
                </option>
              ))}
            </select>

          </div>

          <div className="lista-alimentos">

            {filtrados.length === 0 ? (

              <p>Nenhum item encontrado.</p>

            ) : (

              filtrados.map((i) => (

                <div className="item-alimento" key={i.id}>

                  <div>
                    <strong>{i.descricao}</strong>

                    <div>
                      Qtd: {i.qtd}
                    </div>

                    <div>
                      Tipo: {i.tipo.tipo}
                    </div>
                  </div>

                  <div className="botoes-item">

                    <button onClick={() => editar(i)}>
                      Editar
                    </button>

                    <button onClick={() => excluir(i.id)}>
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

export default Estoque;