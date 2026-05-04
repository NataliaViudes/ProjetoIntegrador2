import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu/Menu.jsx";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
import CampoFiltro from "../../Components/CampoFiltro";

export default function Eventos() {
  const [categoria, setCategoria] = useState("");
  const [descricao, setDescricao] = useState("");
  const [eventos, setEventos] = useState([]);
  const [eventosFiltrados, setEventosFiltrados] = useState([]);
  const [eventoEditando, setEventoEditando] = useState(null);
  const [erros, setErros] = useState({ categoria: false, descricao: false });

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("cat-eventos/descricao");
      const dados = Array.isArray(resp.data) ? resp.data : [];

      setEventos(dados);
      setEventosFiltrados(dados); // 🔥 inicializa lista filtrada
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
    const novosErros = { categoria: !categoria, descricao: !descricao };

    setErros(novosErros);

    if (novosErros.categoria || novosErros.descricao) {
      Swal.fire({
        title: "Atenção!",
        text: "Preencha todos os campos!",
        icon: "warning",
        background: "#ffffff",
        color: "#111111",
        confirmButtonColor: "#d33",
        confirmButtonText: "OK",
      });
    } else {
      const dados = { categoria, descricao };
      Swal.fire({
        title: `Tem certeza que deseja ${eventoEditando ? "atualizar" : "cadastrar"} a categoria: [${categoria}]`,
        showDenyButton: true,
        confirmButtonText: eventoEditando ? "Atualizar" : "Cadastrar",
        denyButtonText: "Cancelar",
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            if (eventoEditando) {
              await api.put("/cat-eventos", {
                id: eventoEditando.id,
                ...dados,
              });
            } else {
              await api.post("/cat-eventos", dados);
            }

            limparFormulario();
            carregarTudo();

            Swal.fire(
              `Categoria do evento foi ${eventoEditando ? "atualizada" : "cadastrada"}!`,
              "",
              "success",
            );
          } catch (error) {
            console.error("Erro ao salvar:", error);
            Swal.fire({
              icon: "error",
              title: "Erro",
              text: "Algo deu errado!",
            });
          }
        }
      });
    }
  }

  function editarEvento(evento) {
    setEventoEditando(evento);
    setCategoria(evento.categoria || "");
    setDescricao(evento.descricao || "");
  }

  async function excluirEvento(id) {
    try {
      await api.delete(`/cat-eventos/${id}`);

      if (eventoEditando && eventoEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      Swal.fire("Erro", "Erro ao excluir categoria.", "error");
    }
  }

  function limparFormulario() {
    setEventoEditando(null);
    setCategoria("");
    setDescricao("");
  }

  return (
    <div className={style["pagina-eventos"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Eventos</h2>

        {/* FORM */}
        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Categoria"
            value={categoria}
            onChange={(e) => {
              setCategoria(e.target.value);
              setErros((prev) => ({ ...prev, categoria: false }));
            }}
            className={erros.categoria ? style["input-erro"] : ""}
          />

          <input
            type="text"
            placeholder="Descrição"
            value={descricao}
            onChange={(e) => {
              setDescricao(e.target.value);
              setErros((prev) => ({ ...prev, descricao: false }));
            }}
            className={erros.descricao ? style["input-erro"] : ""}
          />
        </div>

        {/* FILTRO */}
        <div className={style["form-linha"]}>
          <CampoFiltro
            listaDados={eventos}
            listaFiltros={[
              { label: "Categoria", value: "categoria" },
              { label: "Descrição", value: "descricao" },
            ]}
            filtroDefault="categoria"
            onChange={setEventosFiltrados}
            style={style}
          />
        </div>

        {/* LISTA */}
        <div className={style["lista"]}>
          {eventosFiltrados.map((evento) => (
            <div key={evento.id} className={style["item"]}>
              {evento.categoria} - {evento.descricao}
              <div className={style["acoes"]}>
                <button onClick={() => editarEvento(evento)}>Editar</button>

                <button onClick={() => confirmarExclusao(evento.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* BOTÃO */}
        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {eventoEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
