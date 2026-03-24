import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
export default function Eventos() {
  const [categoria, setCategoria] = useState("");
  const [descricao, setDescricao] = useState("");
  const [busca, setBusca] = useState("");
  const [eventos, setEventos] = useState([]);
  const [eventoEditando, setEventoEditando] = useState(null);
  const [filtro, setFiltro] = useState("descricao");

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
  const confirmarExclusao = (id) => {
    Swal.fire({
      title: "Tem certeza?",
      text: "Você não poderá reverter isso!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      color: "#111111",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Sim, excluir!",
      cancelButtonText: "Cancelar",
    }).then((result) => {
      if (result.isConfirmed) {
        excluirEvento(id);
        Swal.fire("Excluído!", "Seu item foi excluído.", "success");
      }
    });
  };

  async function salvarOuAtualizar() {
    if (!categoria || !descricao) {
      Swal.fire({
        title: "Atenção!",
        text: "Preencha todos os campos!",
        icon: "warning",
        background: "#ffffff",
        color: "#111111",
        confirmButtonColor: "#d33",
        confirmButtonText: "OK",
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

      Swal.fire({
        title: "Sucesso!",
        text: "Evento cadastrado com sucesso!",
        icon: "success",
        background: "#ffffff",
        color: "#111111",
        confirmButtonColor: "#3085d6",
        confirmButtonText: "OK",
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

  const eventosFiltrados = eventos
    .filter((evento) => {
      if (filtro === "categoria") {
        return (evento.categoria || "")
          .toLowerCase()
          .includes(busca.toLowerCase());
      } else {
        return (evento.descricao || "")
          .toLowerCase()
          .includes(busca.toLowerCase());
      }
    })
    .sort((a, b) => {
      if (filtro === "categoria") {
        return (a.categoria || "").localeCompare(b.categoria || "");
      } else {
        return (a.descricao || "").localeCompare(b.descricao || "");
      }
    });

  return (
    <>
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

          <div className={style["form-linha"]}>
            <input
              type="text"
              placeholder={
                filtro === "categoria"
                  ? "Pesquisar Categoria"
                  : "Pesquisar Descrição"
              }
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
            />

            <div className={style["filtro-box"]}>
              <span>Filtros:</span>
              <label>
                <input
                  type="radio"
                  value="descricao"
                  checked={filtro === "descricao"}
                  onChange={(e) => setFiltro(e.target.value)}
                />
                Descrição
              </label>

              <label>
                <input
                  type="radio"
                  value="categoria"
                  checked={filtro === "categoria"}
                  onChange={(e) => setFiltro(e.target.value)}
                />
                Categoria
              </label>
            </div>
          </div>

          <div className={style["lista"]}>
            {eventosFiltrados.map((evento) => (
              <div key={evento.id} className={style["item"]}>
                {evento.categoria} - {evento.descricao}
                <div className={style["acoes"]}>
                  <button onClick={() => editarEvento(evento)}>Editar</button>
                  <button
                    onClick={() => {
                      confirmarExclusao(evento.id);
                    }}
                  >
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
    </>
  );
}
