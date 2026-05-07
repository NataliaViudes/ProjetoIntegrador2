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

      const alimentosData = Array.isArray(respAlimentos.data) ? respAlimentos.data : [];
      setAlimentos(alimentosData);

      const itensData = Array.isArray(respItens.data)
        ? respItens.data.map(item => ({
            ...item,
            alimento: alimentosData.find(a => a.id === item.alimento.id) || item.alimento,
            quantidade: item.quantidade || 0,
          }))
        : [];
      setItens(itensData);
    } catch (e) {
      console.error(e);
    }
  }

  function alterarQuantidade(alimento, quantidade) {
    setItens(estadoAtual => {
      const copia = [...estadoAtual];
      const itemExistente = copia.find(i => i.alimento?.id === alimento.id);

      if (itemExistente) {
        itemExistente.quantidade = quantidade;
      } else {
        copia.push({ alimento, quantidade });
      }

      return copia;
    });
  }

  async function salvarTudo() {
    try {
      // Envia todos os itens de uma vez
      for (const item of itens) {
        if (item.quantidade == null) continue; // ignora itens sem quantidade
        await api.post("/itens-cardapio", {
          cardapio: { id: cardapio.id },
          alimento: { id: item.alimento.id },
          quantidade: item.quantidade,
        });
      }
      alert("Itens salvos com sucesso!");
      carregarTudo(); // atualiza do backend
    } catch (e) {
      console.error(e);
      alert("Erro ao salvar itens");
    }
  }

  return (
    <div className="pagina-cardapio">
      <section className="painel-itens">
        <button className="botao-itens" onClick={voltar}>
          ⬅ Voltar
        </button>

        <div className="painel-itens2">
          <strong>{cardapio?.nome}</strong>
          <div>{cardapio?.data}</div>
          <div>{cardapio?.hora}</div>
          <div>Atividade: {agendamentos?.find(a => a.id === cardapio?.agendamento?.id)?.atividade?.descricao || "Sem atividade"}</div>
        </div>
      </section>

      <section className="painel-itens2">
        <div className="lista-alimentos">
          {alimentos.map(alimento => {
            const item = itens.find(i => i.alimento?.id === alimento.id);
            const quantidade = item?.quantidade || 0;

            return (
              <div key={alimento.id} className="item-alimento">
                <div className="nome-alimento">{alimento.nome}</div>

                <div className="controle-quantidade">
                  <button type="button" onClick={() => alterarQuantidade(alimento, Math.max(0, quantidade - 1))}>-</button>
                  <input type="number" min="0" value={quantidade} onChange={e => alterarQuantidade(alimento, Number(e.target.value))} className="input-quantidade" />
                  <button type="button" onClick={() => alterarQuantidade(alimento, quantidade + 1)}>+</button>
                </div>
              </div>
            );
          })}
        </div>

        {/* Botão único de salvar */}
        <button className="botao-itens" onClick={salvarTudo}>
          Salvar tudo
        </button>
      </section>
    </div>
  );
}

export default ItensCardapio;