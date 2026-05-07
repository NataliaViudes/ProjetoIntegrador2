import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu/Menu.jsx";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
import CampoFiltro from "../../Components/CampoFiltro/index.jsx";

export default function Familiares() {
  const [nome, setNome] = useState("");
  const [parentesco, setParentesco] = useState("");
  const [profissao, setProfissao] = useState("");
  const [renda, setRenda] = useState("");
  const [telefone, setTelefone] = useState("");
  const [familiares, setFamiliares] = useState([]);
  const [familiaresFiltrados, setFamiliaresFiltrados] = useState([]);
  const [familiarEditando, setFamiliarEditando] = useState(null);
  const [erros, setErros] = useState({ nome: false, parentesco: false, profissao: false, renda: false, telefone: false });

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const resp = await api.get("/familiares");
      const dados = Array.isArray(resp.data) ? resp.data : [];

      setFamiliares(dados);
      setFamiliaresFiltrados(dados);
    } catch (error) {
      console.error("Erro ao carregar familiares:", error);
    }
  }

  function mascaraTelefone(valor) {
    const apenasNumeros = valor.replace(/\D/g, "");
    return apenasNumeros
      .replace(/(\d{2})(\d)/, "($1) $2")
      .replace(/(\d{5})(\d)/, "$1-$2")
      .slice(0, 15);
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
        excluirFamiliar(id);
        Swal.fire("Excluído!", "Seu item foi excluído.", "success");
      }
    });
  };

  async function salvarOuAtualizar() {
    const novosErros = { nome: !nome, parentesco: !parentesco, profissao: !profissao, renda: !renda, telefone: !telefone };

    setErros(novosErros);

    if (novosErros.nome || novosErros.parentesco || novosErros.profissao || novosErros.renda || novosErros.telefone) {
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
      const dados = {
        nome,
        parentesco,
        profissao,
        renda,
        telefone: telefone.replace(/\D/g, "")
      };
      Swal.fire({
        title: `Tem certeza que deseja ${familiarEditando ? "atualizar" : "cadastrar"} o familiar: [${nome}]`,
        showDenyButton: true,
        confirmButtonText: familiarEditando ? "Atualizar" : "Cadastrar",
        denyButtonText: "Cancelar",
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            if (familiarEditando) {
              await api.put(`/familiares/${familiarEditando.id}`, dados);
            } else {
              console.log(dados);
              await api.post("/familiares", dados);
            }

            limparFormulario();
            carregarTudo();

            Swal.fire(
              `Familiar foi ${familiarEditando ? "atualizado" : "cadastrado"}!`,
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

  function editarFamiliar(familiar) {
    setFamiliarEditando(familiar);
    setNome(familiar.nome);
    setParentesco(familiar.parentesco);
    setProfissao(familiar.profissao);
    setRenda(familiar.renda);
    setTelefone(mascaraTelefone(familiar.telefone || ""));
  }

  async function excluirFamiliar(id) {
    try {
      await api.delete(`/familiares/${id}`);

      if (familiarEditando && familiarEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      Swal.fire("Erro", "Erro ao excluir familiar.", "error");
    }
  }

  function limparFormulario() {
    setFamiliarEditando(null);
    setNome("");
    setParentesco("");
    setProfissao("");
    setRenda("");
    setTelefone("");
  }

  return (
    <div className={style["pagina-eventos"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Familiares</h2>

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
          <input
            type="text"
            placeholder="Telefone"
            value={telefone}
            onChange={(e) => {
              setTelefone(mascaraTelefone(e.target.value));
              setErros((prev) => ({ ...prev, telefone: false }));
            }}
          />
        </div>
        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Parentesco"
            value={parentesco}
            onChange={(e) => {
              setParentesco(e.target.value);
              setErros((prev) => ({ ...prev, parentesco: false }));
            }}
            className={erros.parentesco ? style["input-erro"] : ""}
          />
          <input
            type="text"
            placeholder="Profissão"
            value={profissao}
            onChange={(e) => {
              setProfissao(e.target.value);
              setErros((prev) => ({ ...prev, profissao: false }));
            }}
            className={erros.profissao ? style["input-erro"] : ""}
          />
        </div>
        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Renda"
            value={renda}
            onChange={(e) => {
              setRenda(e.target.value);
              setErros((prev) => ({ ...prev, renda: false }));
            }}
            className={erros.renda ? style["input-erro"] : ""}
          />
        </div>

        {/* FILTRO */}
        <div className={style["form-linha"]}>
          <CampoFiltro
            listaDados={familiares}
            listaFiltros={[
              { label: "Nome", value: "nome" },
            ]}
            filtroDefault="nome"
            onChange={setFamiliaresFiltrados}
            style={style}
          />
        </div>

        {/* LISTA */}
        <div className={style["lista"]}>
          {familiaresFiltrados.map((familiar) => (
            <div key={familiar.id} className={style["item"]}>
              {familiar.nome}
              <div className={style["acoes"]}>
                <button onClick={() => editarFamiliar(familiar)}>Editar</button>

                <button onClick={() => confirmarExclusao(familiar.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* BOTÃO */}
        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {familiarEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
