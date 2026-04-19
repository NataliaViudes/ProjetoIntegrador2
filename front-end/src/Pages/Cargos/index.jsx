import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
import CampoFiltro from "../../Components/CampoFiltro";

export default function Cargos() {
  const [nome, setNome] = useState("");
  const [cargos, setCargos] = useState([]);
  const [cargosFiltrados, setCargosFiltrados] = useState([]);
  const [cargoEditando, setCargoEditando] = useState(null);
  const [erros, setErros] = useState({ nome: false });

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/cargos");
      const dados = Array.isArray(resp.data) ? resp.data : [];

      setCargos(dados);
      setCargosFiltrados(dados);
    } catch (error) {
      console.error("Erro ao carregar cargos:", error);
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
        excluirCargo(id);
        Swal.fire("Excluído!", "Seu item foi excluído.", "success");
      }
    });
  };

  async function salvarOuAtualizar() {
    const novosErros = { nome: !nome };

    setErros(novosErros);

    if (novosErros.nome) {
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
      const dados = { nome };
      Swal.fire({
        title: `Tem certeza que deseja ${cargoEditando ? "atualizar" : "cadastrar"} o cargo: [${nome}]`,
        showDenyButton: true,
        confirmButtonText: cargoEditando ? "Atualizar" : "Cadastrar",
        denyButtonText: "Cancelar",
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            if (cargoEditando) {
              await api.put("/cargos", {
                id: cargoEditando.id,
                ...dados,
              });
            } else {
              await api.post("/cargos", dados);
            }

            limparFormulario();
            carregarTudo();

            Swal.fire(
              `Cargo foi ${cargoEditando ? "atualizado" : "cadastrado"}!`,
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

  function editarCargo(cargo) {
    setCargoEditando(cargo);
    setNome(cargo.nome || "");
  }

  async function excluirCargo(id) {
    try {
      await api.delete(`/cargos/${id}`);

      if (cargoEditando && cargoEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      Swal.fire("Erro", "Erro ao excluir cargo.", "error");
    }
  }

  function limparFormulario() {
    setCargoEditando(null);
    setNome("");
  }

  return (
    <div className={style["pagina-eventos"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Cargos</h2>

        {/* FORM */}
        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Nome"
            value={nome}
            onChange={(e) => {
              setNome(e.target.value);
              setErros((prev) => ({ ...prev, nome: false }));
            }}
            className={erros.nome ? style["input-erro"] : ""}
          />
        </div>

        {/* FILTRO */}
        <div className={style["form-linha"]}>
          <CampoFiltro
            listaDados={cargos}
            listaFiltros={[
              { label: "Nome", value: "nome" },
            ]}
            filtroDefault="nome"
            onChange={setCargosFiltrados}
            style={style}
          />
        </div>

        {/* LISTA */}
        <div className={style["lista"]}>
          {cargosFiltrados.map((cargo) => (
            <div key={cargo.id} className={style["item"]}>
              {cargo.nome}
              <div className={style["acoes"]}>
                <button onClick={() => editarCargo(cargo)}>Editar</button>

                <button onClick={() => confirmarExclusao(cargo.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* BOTÃO */}
        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {cargoEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
