import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
import CampoFiltro from "../../Components/CampoFiltro";

export default function Remedios() {
  const [nome, setNome] = useState("");
  const [descricao, setDescricao] = useState("");
  const [remedios, setRemedios] = useState([]);
  const [remediosFiltrados, setRemediosFiltrados] = useState([]);
  const [remedioEditando, setRemedioEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/remedios");
      const dados = Array.isArray(resp.data) ? resp.data : [];

      setRemedios(dados);
      setRemediosFiltrados(dados); // 🔥 inicializa lista filtrada
    } catch (error) {
      console.error("Erro ao carregar remedios:", error);
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
        excluirRemedio(id);
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
      title: `Tem certeza que deseja ${remedioEditando ? "atualizar" : "cadastrar"} a categoria: [${nome}]`,
      showDenyButton: true,
      confirmButtonText: remedioEditando ? "Atualizar" : "Cadastrar",
      denyButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          if (remedioEditando) {
            await api.put("/remedios", {
              id: remedioEditando.id,
              ...dados,
            });
          } else {
            await api.post("/remedios", dados);
          }

          limparFormulario();
          carregarTudo();

          Swal.fire(
            `Categoria do remedio foi ${remedioEditando ? "atualizada" : "cadastrada"}!`,
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

  function editarRemedio(remedio) {
    setRemedioEditando(remedio);
    setNome(remedio.nome || "");
    setDescricao(remedio.descricao || "");
  }

  async function excluirRemedio(id) {
    try {
      await api.delete(`/remedios/${id}`);

      if (remedioEditando && remedioEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      Swal.fire("Erro", "Erro ao excluir remedio.", "error");
    }
  }

  function limparFormulario() {
    setRemedioEditando(null);
    setNome("");
    setDescricao("");
  }

  return (
    <div className={style["pagina-remedios"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Remedios</h2>

        {/* FORM */}
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

        {/* FILTRO */}
        <div className={style["form-linha"]}>
          <CampoFiltro
            listaDados={remedios}
            listaFiltros={[
              { label: "Nome", value: "nome" },
              { label: "Descrição", value: "descricao" },
            ]}
            filtroDefault="nome"
            onChange={setRemediosFiltrados}
            style={style}
          />
        </div>

        {/* LISTA */}
        <div className={style["lista"]}>
          {remediosFiltrados.map((remedio) => (
            <div key={remedio.id} className={style["item"]}>
              {remedio.nome} - {remedio.descricao}
              <div className={style["acoes"]}>
                <button onClick={() => editarRemedio(remedio)}>Editar</button>

                <button onClick={() => confirmarExclusao(remedio.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* BOTÃO */}
        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {remedioEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
