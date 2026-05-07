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
        api.get("/alimento"),
        api.get(`/itens-cardapio/${cardapio.id}`),
      ]);

      setAlimentos(Array.isArray(respAlimentos.data) ? respAlimentos.data : []);
      setItens(Array.isArray(respItens.data) ? respItens.data : []);
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
    </div>
  );
}

export default ItensCardapio;