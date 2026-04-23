import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Alimentos/Alimentos.css"; 

function Estoque() {
  const [descricao, setDescricao] = useState("");
  const [qtd, setQtd] = useState("");
  const [tipoId, setTipoId] = useState("");

  const [tipos, setTipos] = useState([]);
  const [itens, setItens] = useState([]);
  const [busca, setBusca] = useState("");

  const [editando, setEditando] = useState(null);

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
    if (!descricao || !qtd || !tipoId) {
      alert("Preencha todos os campos");
      return;
    }

    if (qtd < 0) {
      alert("Quantidade não pode ser negativa");
      return;
    }

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

  const filtrados = itens.filter((i) =>
    (i.descricao || "").toLowerCase().includes(busca.toLowerCase())
  );

  return (
    <div className="pagina-alimentos">
      <Menu />

      <main className="conteudo-alimentos">
        <section className="painel-esquerdo">
          <h2>Cadastro de Materiais</h2>

          <label>Descrição</label>
          <input
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />

          <label>Quantidade</label>
          <input
            type="number"
            min="0"
            value={qtd}
            onChange={(e) => {
              const valor = e.target.value;

              // bloqueia negativo
              if (valor < 0) return;

              setQtd(valor);
            }}
          />

          <label>Categoria</label>
          <select
            value={tipoId}
            onChange={(e) => setTipoId(e.target.value)}
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

            <button onClick={limpar}>Limpar</button>
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
          </div>

          <div className="lista-alimentos">
            {filtrados.length === 0 ? (
              <p>Nenhum item encontrado.</p>
            ) : (
              filtrados.map((i) => (
                <div className="item-alimento" key={i.id}>
                  <div>
                    <strong>{i.descricao}</strong>
                    <div>Qtd: {i.qtd}</div>
                    <div>Tipo: {i.tipo.tipo}</div>
                  </div>

                  <div className="botoes-item">
                    <button onClick={() => editar(i)}>Editar</button>
                    <button onClick={() => excluir(i.id)}>Excluir</button>
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