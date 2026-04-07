import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
export default function Eventos() {
  const [nome, setNome] = useState("");
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
    if (!nome || !descricao) {
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
    const dados = { nome, descricao };

    Swal.fire({
      title: `Tem certeza que deseja ${eventoEditando ? "atualizar" : "cadastrar"} a categoria: [${nome}]`,
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: `${eventoEditando ? "Atualizar" : "Cadastrar"}`,
      denyButtonText: `Cancelar`,
    }).then(async (result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        try {
          if (eventoEditando) {
            await api.put("/eventos", {
              id: eventoEditando.id,
              ...dados,
            });
          } else {
            await api.post("/eventos", dados);
          }
          limparFormulario();
          carregarTudo();
        } catch (error) {
          console.error("Erro ao salvar:", error);
          Swal.fire({
            icon: "error",
            title: "Oops...",
            text: "Algo deu errado!",
          });
        }

        Swal.fire(
          `Categoria do evento foi ${eventoEditando ? "atualizada" : "cadastrada"}!`,
          "",
          "success",
        );
      } else if (result.isDenied) {
        Swal.fire(
          `Categoria do evento não foi ${eventoEditando ? "atualizada" : "cadastrada"}!`,
          "",
          "info",
        );
      }
    });
  }

  function editarEvento(evento) {
    setEventoEditando(evento);
    setNome(evento.nome || "");
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
    setNome("");
    setDescricao("");
  }

  const eventosFiltrados = eventos
    .filter((evento) => {
      if (filtro === "nome") {
        return (evento.nome || "").toLowerCase().includes(busca.toLowerCase());
      } else {
        return (evento.descricao || "")
          .toLowerCase()
          .includes(busca.toLowerCase());
      }
    })
    .sort((a, b) => {
      if (filtro === "nome") {
        return (a.nome || "").localeCompare(b.nome || "");
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
              placeholder="Nome"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
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
                filtro === "nome" ? "Pesquisar Nome" : "Pesquisar Descrição"
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
                  value="nome"
                  checked={filtro === "nome"}
                  onChange={(e) => setFiltro(e.target.value)}
                />
                Nome
              </label>
            </div>
          </div>

          <div className={style["lista"]}>
            {eventosFiltrados.map((evento) => (
              <div key={evento.id} className={style["item"]}>
                {evento.nome} - {evento.descricao}
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
