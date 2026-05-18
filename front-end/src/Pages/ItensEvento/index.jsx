import { useEffect, useState } from "react";
import { Plus, Trash2, Save, ArrowLeft } from "lucide-react";
import Swal from "sweetalert2";

import api from "../../Services/api";

import "./ItensEvento.css";

export default function ItensEvento({ evento, voltar, somenteLeitura }) {
  const [itens, setItens] = useState([]);

  const [busca, setBusca] = useState({});
  const [sugestoes, setSugestoes] = useState({});

  const [estoqueLista, setEstoqueLista] = useState([]);

  useEffect(() => {
    carregarEstoque();
  }, []);

  useEffect(() => {
    if (evento?.idEvento) {
      carregarItens();
    } else {
      setItens([]);
    }
  }, [evento]);

  async function carregarEstoque() {
    try {
      const resp = await api.get("/estoque/descricao");

      if (Array.isArray(resp.data)) {
        setEstoqueLista(resp.data);
      } else {
        setEstoqueLista([]);
      }
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
      const resp = await api.get(`/itens-evento/evento/${evento.idEvento}`);

      if (Array.isArray(resp.data)) {
        resp.data.forEach((item) => {
          item.qtdOriginal = item.qtd;
        });

        setItens(resp.data);

        const buscaInicial = {};

        resp.data.forEach((item, index) => {
          buscaInicial[index] = item.estoque?.descricao || "";
        });

        setBusca(buscaInicial);
      } else {
        setItens([]);
      }
    } catch (e) {
      console.error(e);

      setItens([]);

      Swal.fire({
        icon: "error",
        title: "Erro",
        text: "Erro ao carregar itens do evento",
      });
    }
  }

  function adicionarLinha() {
    setItens((prev) => [
      ...prev,

      {
        estoque: {
          id: "",
          descricao: "",
          qtd: 0,
        },

        qtd: 1,
      },
    ]);
  }

  async function removerLinha(index) {
    const result = await Swal.fire({
      title: "Deseja remover?",
      text: "Essa ação não pode ser desfeita",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sim",
      cancelButtonText: "Cancelar",
    });

    if (!result.isConfirmed) return;

    const novaLista = [...itens];

    novaLista.splice(index, 1);

    setItens(novaLista);
  }

  function alterarQtd(index, valor) {
    const novaLista = [...itens];

    const item = novaLista[index];

    // =====================================
    // ESTOQUE DISPONÍVEL
    // =====================================

    const estoqueDisponivel = Number(item.estoque?.qtd || 0);

    // =====================================
    // QTD ORIGINAL DO EVENTO
    // =====================================

    const qtdOriginal = Number(item.qtdOriginal || 0);

    // =====================================
    // LIMITE REAL
    // =====================================

    const qtdMaxima = estoqueDisponivel + qtdOriginal;

    // =====================================
    // VALIDAÇÕES
    // =====================================

    if (valor < 1) {
      valor = 1;
    }

    if (valor > qtdMaxima) {
      valor = qtdMaxima;

      Swal.fire({
        icon: "warning",
        title: "Quantidade inválida",
        text: `Máximo disponível: ${qtdMaxima}`,
      });
    }

    item.qtd = valor;

    setItens(novaLista);
  }

  function buscarEstoque(index, texto) {
    setBusca((prev) => ({
      ...prev,
      [index]: texto,
    }));

    const idsSelecionados = itens.map((i) => i.estoque?.id).filter(Boolean);

    let filtrados = estoqueLista.filter(
      (item) => !idsSelecionados.includes(item.id) && item.qtd > 0,
    );

    if (texto?.trim()) {
      filtrados = filtrados.filter((item) =>
        item.descricao?.toLowerCase().includes(texto.toLowerCase()),
      );
    }

    setSugestoes((prev) => ({
      ...prev,
      [index]: filtrados,
    }));
  }

  function selecionarItem(index, itemSelecionado) {
    // =====================================
    // ESTOQUE ZERADO
    // =====================================

    if (Number(itemSelecionado.qtd || 0) <= 0) {
      Swal.fire({
        icon: "warning",
        title: "Sem estoque",
        text: "Esse item não possui estoque disponível",
      });

      return;
    }

    // =====================================
    // ITEM DUPLICADO
    // =====================================

    const itemDuplicado = itens.some(
      (i, iIndex) => i.estoque?.id === itemSelecionado.id && iIndex !== index,
    );

    if (itemDuplicado) {
      Swal.fire({
        icon: "warning",
        title: "Item duplicado",
        text: "Esse item já foi adicionado",
      });

      return;
    }

    const novaLista = [...itens];

    novaLista[index].estoque = {
      ...itemSelecionado,
    };

    novaLista[index].qtd = 1;

    setItens(novaLista);

    setBusca((prev) => ({
      ...prev,
      [index]: itemSelecionado.descricao,
    }));

    setSugestoes((prev) => ({
      ...prev,
      [index]: [],
    }));
  }

  async function salvarAlteracoes() {
    try {
      const itensValidos = itens.filter((i) => i.estoque?.id && i.qtd > 0);

      const payload = itensValidos.map((i) => ({
        evento: {
          idEvento: evento.idEvento,
        },

        estoque: {
          id: i.estoque.id,
        },

        qtd: Number(i.qtd),
      }));

      if (payload.length === 0) {
        await api.delete(`/itens-evento/evento/${evento.idEvento}`);
      } else {
        await api.put("/itens-evento", payload);
      }

      await carregarItens();
      await carregarEstoque();

      Swal.fire({
        icon: "success",
        title: "Sucesso",
        text: "Itens salvos com sucesso",
        timer: 1500,
        showConfirmButton: false,
      });
    } catch (e) {
      console.error(e);

      const erroBack =
        e?.response?.data?.message ||
        e?.response?.data?.erro ||
        e?.message ||
        "Erro ao salvar itens";

      Swal.fire({
        icon: "error",
        title: "Erro",
        text: erroBack,
      });
    }
  }

  if (!evento) {
    return (
      <div className="itens-container">
        <div className="topo-itens">
          <div>
            <h2>Nenhum evento selecionado</h2>

            <p>Selecione um evento para gerenciar os itens.</p>
          </div>

          <div className="acoes-topo">
            <button className="btn btn-voltar" onClick={voltar}>
              <ArrowLeft size={16} />
              Voltar
            </button>
          </div>
        </div>
      </div>
    );
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

          <button
            className="btn btn-add"
            onClick={adicionarLinha}
            disabled={somenteLeitura}
          >
            <Plus size={16} />
            Item
          </button>

          <button
            className="btn btn-save"
            onClick={salvarAlteracoes}
            disabled={somenteLeitura}
          >
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
            <th>Máx</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {itens.length === 0 ? (
            <tr>
              <td
                colSpan="4"
                style={{
                  textAlign: "center",
                }}
              >
                Nenhum item adicionado
              </td>
            </tr>
          ) : (
            itens.map((item, index) => (
              <tr
                key={`${item.estoque?.id || "novo"}-${index}`}
                className={item.estoque?.id ? "linha-selecionada" : ""}
              >
                <td
                  style={{
                    position: "relative",
                  }}
                >
                  <input
                    type="text"
                    placeholder="Buscar item..."
                    disabled={!!item.estoque?.id || somenteLeitura}
                    className={`input-busca ${
                      item.estoque?.id || somenteLeitura
                        ? "input-desabilitado"
                        : ""
                    }`}
                    value={busca[index] ?? item.estoque?.descricao ?? ""}
                    onChange={(e) => buscarEstoque(index, e.target.value)}
                    onFocus={() => buscarEstoque(index, "")}
                    onBlur={() => {
                      setTimeout(() => {
                        setSugestoes((prev) => ({
                          ...prev,
                          [index]: [],
                        }));
                      }, 200);
                    }}
                  />

                  {sugestoes[index]?.length > 0 && (
                    <div className="sugestoes-box">
                      {sugestoes[index].map((s) => (
                        <div
                          key={s.id}
                          className="sugestao-item"
                          onClick={() => selecionarItem(index, s)}
                        >
                          {s.descricao}
                          {" - "}
                          Estoque: {s.qtd}
                        </div>
                      ))}
                    </div>
                  )}
                </td>

                <td>
                  <input
                    type="number"
                    min="1"
                    disabled={!item.estoque?.id || somenteLeitura}
                    className="input-qtd"
                    value={item.qtd}
                    onChange={(e) => alterarQtd(index, Number(e.target.value))}
                  />
                </td>

                <td>{(item.estoque?.qtd || 0) + (item.qtdOriginal || 0)}</td>

                <td>
                  <button
                    className="btn-delete"
                    disabled={somenteLeitura}
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
