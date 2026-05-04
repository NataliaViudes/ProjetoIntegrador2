import { useEffect, useState } from "react";
import { Plus, Trash2, Save, ArrowLeft } from "lucide-react";
import Swal from "sweetalert2";
import api from "../../Services/api";
import "./ItensEvento.css";

export default function ItensEvento({ evento, voltar }) {
  const [itens, setItens] = useState([]);
  const [busca, setBusca] = useState({});
  const [sugestoes, setSugestoes] = useState({});
  const [estoqueLista, setEstoqueLista] = useState([]);

  useEffect(() => {
    if (evento?.id) {
      carregarItens();
    }
    carregarEstoque();
  }, [evento]);

  async function carregarEstoque() {
    try {
      const resp = await api.get("/estoque/descricao");
      setEstoqueLista(resp.data);
    } catch (e) {
      console.error(e);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar estoque",
      });
    }
  }

  async function carregarItens() {
    try {
      const resp = await api.get(`/itens-evento/evento/${evento.id}`);
      setItens(resp.data);
    } catch (e) {
      console.error(e);
      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar itens do evento",
      });
    }
  }

  function adicionarLinha() {
    setItens([
      ...itens,
      {
        estoque: { id: "", descricao: "", qtd: 0 },
        qtd: 1,
        novo: true,
      },
    ]);
  }

  async function removerLinha(index) {
    const result = await Swal.fire({
      title: "Remover item?",
      text: "Essa ação não pode ser desfeita",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim, remover",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    const nova = [...itens];
    nova.splice(index, 1);
    setItens(nova);
  }

  function alterarQtd(index, valor) {
    const nova = [...itens];
    const max = nova[index].estoque?.qtd || 0;

    if (valor > max) {
      Swal.fire({
        icon: "warning",
        title: "Limite excedido",
        text: `Máximo disponível: ${max}`,
      });
      valor = max;
    }

    if (valor < 1) valor = 1;

    nova[index].qtd = valor;
    setItens(nova);
  }

  function buscarEstoque(index, texto) {
    setBusca({ ...busca, [index]: texto });

    const itensJaSelecionados = itens.map((i) => i.estoque?.id).filter(Boolean);

    let filtrados = estoqueLista.filter(
      (item) => !itensJaSelecionados.includes(item.id),
    );

    if (texto) {
      filtrados = filtrados.filter((item) =>
        item.descricao.toLowerCase().includes(texto.toLowerCase()),
      );
    }

    setSugestoes({
      ...sugestoes,
      [index]: filtrados,
    });
  }

  function selecionarItem(index, item) {
    const jaExiste = itens.some(
      (i, iIndex) => i.estoque?.id === item.id && iIndex !== index,
    );

    if (jaExiste) {
      Swal.fire({
        icon: "warning",
        title: "Item duplicado",
        text: "Esse item já foi adicionado",
      });
      return;
    }

    const nova = [...itens];
    nova[index].estoque = item;
    nova[index].qtd = 1;

    setItens(nova);

    setSugestoes({ ...sugestoes, [index]: [] });
    setBusca({ ...busca, [index]: item.descricao });
  }

  async function salvarAlteracoes() {
    try {
      const payload = itens
        .filter((i) => i.estoque?.id) // só itens válidos
        .map((i) => ({
          evento: { id: evento.id },
          estoque: { id: i.estoque.id },
          qtd: i.qtd,
        }));

      if (payload.length === 0)
        await api.delete(`/itens-evento/evento/${evento.id}`);
      else await api.put("/itens-evento", payload);

      Swal.fire({
        icon: "success",
        title: "Salvo!",
        text: "Itens atualizados com sucesso",
        timer: 1500,
        showConfirmButton: false,
      });
    } catch (e) {
      console.error(e);
      Swal.fire("Erro", "Erro ao salvar itens", "error");
    }
  }

  return (
    <div className="itens-container">
      <div className="topo-itens">
        <div>
          <h2>{evento.nome}</h2>
          <p>
            <strong>Local:</strong> {evento.local}
          </p>
        </div>

        <div className="acoes-topo">
          <button className="btn btn-voltar" onClick={voltar}>
            <ArrowLeft size={16} />
            Voltar
          </button>

          <button className="btn btn-add" onClick={adicionarLinha}>
            <Plus size={16} />
            Item
          </button>

          <button className="btn btn-save" onClick={salvarAlteracoes}>
            <Save size={16} />
            Salvar
          </button>
        </div>
      </div>

      <table className="itens-tabela">
        <thead>
          <tr>
            <th>Item</th>
            <th>Qtd</th>
            <th>Max</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {itens.length === 0 ? (
            <tr>
              <td colSpan="4" style={{ textAlign: "center" }}>
                Nenhum item
              </td>
            </tr>
          ) : (
            itens.map((item, index) => (
              <tr
                key={index}
                className={item.estoque?.id ? "linha-selecionada" : ""}
              >
                <td style={{ position: "relative" }}>
                  <input
                    className={`input-busca ${
                      item.estoque?.id ? "input-desabilitado" : ""
                    }`}
                    type="text"
                    disabled={!!item.estoque?.id}
                    value={busca[index] ?? item.estoque?.descricao ?? ""}
                    onChange={(e) => buscarEstoque(index, e.target.value)}
                    onFocus={() => buscarEstoque(index, "")}
                    onBlur={() =>
                      setTimeout(
                        () =>
                          setSugestoes({
                            ...sugestoes,
                            [index]: [],
                          }),
                        100,
                      )
                    }
                    placeholder="Buscar item..."
                  />

                  {sugestoes[index]?.length > 0 && (
                    <div className="sugestoes-box">
                      {sugestoes[index].map((s) => (
                        <div
                          key={s.id}
                          className="sugestao-item"
                          onClick={() => selecionarItem(index, s)}
                        >
                          {s.descricao} (Estoque: {s.qtd})
                        </div>
                      ))}
                    </div>
                  )}
                </td>

                <td>
                  <input
                    className="input-qtd"
                    type="number"
                    value={item.qtd}
                    min="1"
                    max={item.estoque?.qtd || 0}
                    onChange={(e) => alterarQtd(index, Number(e.target.value))}
                  />
                </td>

                <td>{item.estoque?.qtd ?? "-"}</td>

                <td>
                  <button
                    className="btn-delete"
                    onClick={() => removerLinha(index)}
                  >
                    <Trash2 size={16} />
                  </button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
