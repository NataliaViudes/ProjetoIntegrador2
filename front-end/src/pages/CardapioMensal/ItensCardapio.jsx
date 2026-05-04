import { useEffect, useState } from "react";
import Menu from "../../components/Menu";
import "../Alimentos/Alimentos.css";  
import api from "../../services/api";

function ItensCardapio({ cardapio, voltar }) {
  const [alimentos, setAlimentos] = useState([]);
  const [itens, setItens] = useState([]);
  const [quantidade, setQuantidade] = useState("");

  const [idAlimento, setIdAlimento] = useState("");

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

  async function adicionarItem() {
    if (!idAlimento) {
      alert("Selecione um alimento.");
      return;
    }

    try {
      await api.post("/itens-cardapio", {
        cardapio: { id: cardapio.id },
        alimento: { id: Number(idAlimento) },
      });

      setIdAlimento("");
      carregarTudo();
    } catch (e) {
      console.error("Erro ao adicionar item:", e);
    }
  }

  async function removerItem(item) {
    try {
      await api.delete("/itens-cardapio", {
        data: item,
      });

      carregarTudo();
    } catch (e) {
      console.error("Erro ao remover item:", e);
    }
  }

  return (
    <div style={{ padding: 20 }}>
      <button onClick={voltar}>⬅ Voltar</button>

      <h2>Itens do Cardápio: {cardapio.descricao}</h2>

      <div>
        <select value={idAlimento} onChange={(e) => setIdAlimento(e.target.value)}>
          <option value="">Selecione alimento</option>
          {alimentos.map((a) => (
            <option key={a.id} value={a.id}>
              {a.descricao}
            </option>
          ))}
        </select>

        <button onClick={adicionarItem}>Adicionar</button>
      </div>

      <hr />

      <h3>Itens cadastrados</h3>

      {itens.map((item, i) => (
        <div key={i}>
          {item.alimento?.descricao}

          <button onClick={() => removerItem(item)}>
            Remover
          </button>
        </div>
      ))}
    </div>
  );
}

export default ItensCardapio;