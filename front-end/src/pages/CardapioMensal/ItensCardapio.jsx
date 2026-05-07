import { useEffect, useState } from "react";
import api from "../../services/api";

function ItensCardapio({ cardapio, voltar, agendamentos }) {
  const [alimentos, setAlimentos] = useState([]);
  const [itens, setItens] = useState([]);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAlimentos, respItens] = await Promise.all([
        api.get("/alimento/descricao"),
        api.get(`/itens-cardapio/${cardapio.id}`),
      ]);

      setAlimentos(Array.isArray(respAlimentos.data) ? respAlimentos.data : []);
      setItens(Array.isArray(respItens.data) ? respItens.data : []);
      console.log(alimentos, itens);
    } catch (e) {
      console.error("Erro ao carregar itens:", e);
    }
  }

  function getNomeAtividade() {
    const ag = agendamentos?.find(
      (a) => a.id === cardapio?.agendamento?.id
    );

    return ag?.atividade?.descricao || "Sem atividade";
  }


  function itemJaExiste(idAlimento) {
    return itens.find((i) => i.alimento?.id === idAlimento);
  }

  function alterarQuantidade(idAlimento, valor) {
    const novo = [...itens];

    const item = novo.find((i) => i.alimento?.id === idAlimento);

    if (item) {
      item.quantidade = valor;
      setItens(novo);
    }
  }

  async function salvarItem(alimento) {
    const itemExistente = itemJaExiste(alimento.id);

    const quantidade = itemExistente?.quantidade || 0;

    if (!quantidade || quantidade <= 0) {
      alert("Digite a quantidade");
      return;
    }

    const payload = {
      cardapio: { id: cardapio.id },
      alimento: { id: alimento.id },
      quantidade,
    };

    try {
      await api.post("/itens-cardapio", payload);

      carregarTudo();
    } catch (e) {
      console.error(e);
      alert("Erro ao salvar item");
    }
  }

  async function excluirItem(idItem) {
    try {
      await api.delete(`/itens-cardapio/${idItem}`);

      carregarTudo();
    } catch (e) {
      console.error(e);
      alert("Erro ao excluir item");
    }
  }

  return (
    <div style={{ padding: 20 }}>
      <section className="painel-itens">
        <button onClick={voltar}>⬅ Voltar</button>

        <div className="painel-itens2">
          <strong>{cardapio?.nome}</strong>

          <div>{cardapio?.data}</div>

          <div>{cardapio?.hora}</div>

          <div>
            Atividade: {getNomeAtividade()}
          </div>
        </div>

      </section>

      <section className="painel-itens2">
        <div style={{ marginTop: 20 }}>
          {alimentos.map((alimento) => {
            const itemSalvo = itemJaExiste(alimento.id);

            return (
              <div
                key={alimento.id}
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: 10,
                  marginBottom: 10,
                  borderBottom: "1px solid #ccc",
                  paddingBottom: 10,
                }}
              >
                <div style={{ width: 200 }}>
                  {alimento.nome}
                </div>

                <input
                  type="number"
                  min="0"
                  value={itemSalvo?.quantidade || 0}
                  onChange={(e) =>
                    alterarQuantidade(
                      alimento.id,
                      Number(e.target.value)
                    )
                  }
                  style={{ width: 60 }}
                />

                {!itemSalvo ? (
                  <button
                    onClick={() =>
                      setItens([
                        ...itens,
                        {
                          alimento,
                          quantidade: 1,
                        },
                      ])
                    }
                  >
                    salvar
                  </button>
                ) : (
                  <>
                    <span>salvo</span>

                    <button
                      onClick={() => salvarItem(alimento)}
                    >
                      atualizar
                    </button>

                    <button
                      onClick={() => excluirItem(itemSalvo.id)}
                    >
                      excluir
                    </button>
                  </>
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