import { useEffect, useState } from "react";
import api from "../../services/api";

function ItensCardapio({
  cardapio,
  voltar,
  agendamentos,
}) {
  const [alimentos, setAlimentos] = useState([]);
  const [itens, setItens] = useState([]);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAlimentos, respItens] =
        await Promise.all([
          api.get("/alimento/descricao"),
          api.get(
            `/itens-cardapio/${cardapio.id}`
          ),
        ]);

      setAlimentos(
        Array.isArray(respAlimentos.data)
          ? respAlimentos.data
          : []
      );

      setItens(
        Array.isArray(respItens.data)
          ? respItens.data
          : []
      );
    } catch (e) {
      console.error(e);
    }
  }

  function getNomeAtividade() {
    const ag = agendamentos?.find(
      (a) =>
        a.id === cardapio?.agendamento?.id
    );

    return (
      ag?.atividade?.descricao ||
      "Sem atividade"
    );
  }

  function buscarItem(idAlimento) {
    return itens.find(
      (i) =>
        i.alimento?.id === idAlimento
    );
  }

  function alterarQuantidade(
    alimento,
    quantidade
  ) {
    setItens((estadoAtual) => {
      const copia = [...estadoAtual];

      const itemExistente = copia.find(
        (i) =>
          i.alimento?.id === alimento.id
      );

      if (itemExistente) {
        itemExistente.quantidade =
          quantidade;
      } else {
        copia.push({
          alimento,
          quantidade,
        });
      }

      return copia;
    });
  }

  async function salvarItem(alimento) {
    const item = buscarItem(alimento.id);

    if (
      !item?.quantidade ||
      item.quantidade <= 0
    ) {
      alert("Informe uma quantidade");
      return;
    }

    try {
      await api.post("/itens-cardapio", {
        cardapio: { id: cardapio.id },
        alimento: { id: alimento.id },
        quantidade: item.quantidade,
      });

      carregarTudo();
    } catch (e) {
      console.error(e);
      alert("Erro ao salvar");
    }
  }

  async function atualizarItem(item) {
    try {
      await api.put(
        `/itens-cardapio/${item.id}`,
        {
          ...item,
          quantidade: item.quantidade,
        }
      );

      carregarTudo();
    } catch (e) {
      console.error(e);
      alert("Erro ao atualizar");
    }
  }

  async function excluirItem(idItem) {
    try {
      await api.delete(
        `/itens-cardapio/${idItem}`
      );

      carregarTudo();
    } catch (e) {
      console.error(e);
      alert("Erro ao excluir");
    }
  }

  return (
    <div className="pagina-cardapio">
      <section className="painel-itens">
        <button
          className="botao-itens"
          onClick={voltar}
        >
          ⬅ Voltar
        </button>

        <div className="painel-itens2">
          <strong>{cardapio?.nome}</strong>

          <div>{cardapio?.data}</div>

          <div>{cardapio?.hora}</div>

          <div>
            Atividade:{" "}
            {getNomeAtividade()}
          </div>
        </div>
      </section>

      <section className="painel-itens2">
        <div className="lista-alimentos">
          {alimentos.map((alimento) => {
            const item =
              buscarItem(alimento.id);

            const quantidade =
              item?.quantidade || 0;

            const itemSalvo =
              Boolean(item?.id);

            return (
              <div
                key={alimento.id}
                className="item-alimento"
              >
                <div className="nome-alimento">
                  {alimento.nome}
                </div>

                <div className="controle-quantidade">
                  <button
                    type="button"
                    onClick={() =>
                      alterarQuantidade(
                        alimento,
                        Math.max(
                          0,
                          quantidade - 1
                        )
                      )
                    }
                  >
                    -
                  </button>

                  <input
                    type="number"
                    min="0"
                    value={quantidade}
                    onChange={(e) =>
                      alterarQuantidade(
                        alimento,
                        Number(
                          e.target.value
                        )
                      )
                    }
                    className="input-quantidade"
                  />

                  <button
                    type="button"
                    onClick={() =>
                      alterarQuantidade(
                        alimento,
                        quantidade + 1
                      )
                    }
                  >
                    +
                  </button>
                </div>

                {!itemSalvo ? (
                  <button
                    className="botao-itens"
                    onClick={() =>
                      salvarItem(alimento)
                    }
                  >
                    salvar
                  </button>
                ) : (
                  <div className="acoes-alimento">
                    <button
                      className="botao-itens"
                      onClick={() =>
                        atualizarItem(item)
                      }
                    >
                      atualizar
                    </button>

                    <button
                      className="botao-itens"
                      onClick={() =>
                        excluirItem(item.id)
                      }
                    >
                      excluir
                    </button>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </section>
    </div>
  );
}

export default ItensCardapio;