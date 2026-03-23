import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import toast from "react-hot-toast";

import style from "./styles.module.css";
export default function Eventos() {
  const [categoria, setCategoria] = useState("");
  const [descricao, setDescricao] = useState("");

  const [busca, setBusca] = useState("");
  const [eventos, setEventos] = useState([]);
  const [eventoEditando, setEventoEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/eventos");
      setEventos(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar eventos:", error);
    }
  }

  async function salvarOuAtualizar() {
    if (!categoria || !descricao) {
      toast.error("Preencha todos os campos!", {
        style: {
          background: "#2f2f2f",
          color: "#fff",
          borderLeft: "5px solid #e53935",
        },
        icon: "⚠️",
      });
      return;
    }

    const payload = { categoria, descricao };

    try {
      if (eventoEditando) {
        await api.put("/eventos", {
          id: eventoEditando.id,
          ...payload,
        });
      } else {
        await api.post("/eventos", payload);
      }

      toast.success("Evento cadastrado com sucesso!", {
        style: {
          background: "#2f2f2f",
          color: "#fff",
          borderLeft: "5px solid #43a047",
        },
        icon: "✔",
      });

      limparFormulario();
      carregarTudo();
    } catch (error) {
      console.error("Erro ao salvar:", error);
      alert("Erro ao salvar evento.");
    }
  }

  function editarEvento(evento) {
    setEventoEditando(evento);
    setCategoria(evento.categoria || "");
    setDescricao(evento.descricao || "");
  }

  async function excluirEvento(id) {
    try {
      await api.delete(`/eventos/${id}`);
      if (eventoEditando && eventoEditando.id === id) {
        limparFormulario();
      }
      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      alert("Erro ao excluir evento.");
    }
  }

  function limparFormulario() {
    setEventoEditando(null);
    setCategoria("");
    setDescricao("");
  }

  const eventosFiltrados = eventos.filter((evento) =>
    (evento.categoria || "").toLowerCase().includes(busca.toLowerCase()),
  );

  return (
    <div className={style["pagina-eventos"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Eventos</h2>

        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Categoria"
            value={categoria}
            onChange={(e) => setCategoria(e.target.value)}
          />

          <input
            type="text"
            placeholder="Descrição"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
          />
        </div>

        <div className={style["barra-busca"]}>
          <input
            type="text"
            placeholder="Pesquisar..."
            value={busca}
            onChange={(e) => setBusca(e.target.value)}
          />
          <button>Pesquisar</button>
        </div>

        <div className={style["lista"]}>
          {eventosFiltrados.map((evento) => (
            <div key={evento.id} className={style["item"]}>
              {evento.categoria} - {evento.descricao}
              <div className={style["acoes"]}>
                <button onClick={() => editarEvento(evento)}>Editar</button>
                <button onClick={() => excluirEvento(evento.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {eventoEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
