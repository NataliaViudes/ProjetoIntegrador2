import { useEffect, useState } from "react";
import api from "../../Services/api.js";
import Menu from "../../Components/Menu";
import "animate.css";
import Swal from "sweetalert2";
import style from "./styles.module.css";
import CampoFiltro from "../../Components/CampoFiltro";

export default function Funcionarios() {
  const [nome, setNome] = useState("");
  const [cpf, setCpf] = useState("");
  const [telefone, setTelefone] = useState("");
  const [nis, setNis] = useState("");
  const [nascimento, setNascimento] = useState("");
  const [sexo, setSexo] = useState("");
  const [endereco, setEndereco] = useState("");
  const [id_cargo, setIdCargo] = useState(null);
  const [funcionarios, setFuncionarios] = useState([]);
  const [cargos, setCargos] = useState([]);
  const [funcionariosFiltrados, setFuncionariosFiltrados] = useState([]);
  const [funcionarioEditando, setFuncionarioEditando] = useState(null);
  const [erros, setErros] = useState({ nome: false, cpf: false, telefone: false, nis: false, nascimento: false, sexo: false, endereco: false, id_cargo: false });

  useEffect(() => {
    carregarTudo();
    carregarCargos();
  }, []);

  async function carregarCargos() {
    try {
      const resp = await api.get("/cargos");
      const dados = Array.isArray(resp.data) ? resp.data : [];
      setCargos(dados);
    } catch (error) {
      console.error("Erro ao carregar cargos:", error);
    }
  }

  async function carregarTudo() {
    try {
      const resp = await api.get("/funcionarios");
      const dados = Array.isArray(resp.data) ? resp.data : [];

      setFuncionarios(dados);
      setFuncionariosFiltrados(dados);
    } catch (error) {
      console.error("Erro ao carregar funcionários:", error);
    }
  }

  function mascaraCPF(valor) {
    const apenasNumeros = valor.replace(/\D/g, "");

    return apenasNumeros
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d{1,2})$/, "$1-$2")
      .slice(0, 14);
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
        excluirFuncionario(id);
        Swal.fire("Excluído!", "Seu item foi excluído.", "success");
      }
    });
  };

  async function salvarOuAtualizar() {
    const novosErros = { nome: !nome, cpf: !cpf, telefone: !telefone, nis: !nis, nascimento: !nascimento, sexo: !sexo, endereco: !endereco, id_cargo: !id_cargo };

    setErros(novosErros);

    if (novosErros.nome || novosErros.cpf || novosErros.telefone || novosErros.nis || novosErros.nascimento || novosErros.sexo || novosErros.endereco || novosErros.id_cargo) {
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
        cpf: cpf.replace(/\D/g, ""),
        telefone: telefone.replace(/\D/g, ""),
        nis,
        nascimento,
        sexo,
        endereco,
        cargo: {
          id: Number(id_cargo)
        }
      };
      Swal.fire({
        title: `Tem certeza que deseja ${funcionarioEditando ? "atualizar" : "cadastrar"} o funcionário: [${nome}]`,
        showDenyButton: true,
        confirmButtonText: funcionarioEditando ? "Atualizar" : "Cadastrar",
        denyButtonText: "Cancelar",
      }).then(async (result) => {
        if (result.isConfirmed) {
          try {
            if (funcionarioEditando) {
              await api.put(`/funcionarios/${funcionarioEditando.id}`, dados);
            } else {
              console.log(dados);
              await api.post("/funcionarios", dados);
            }

            limparFormulario();
            carregarTudo();

            Swal.fire(
              `Funcionário foi ${funcionarioEditando ? "atualizado" : "cadastrado"}!`,
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

  function editarFuncionario(funcionario) {
    setFuncionarioEditando(funcionario);
    setNome(funcionario.nome || "");
    setCpf(mascaraCPF(funcionario.cpf || ""));
    setTelefone(mascaraTelefone(funcionario.telefone || ""));
    setNis(funcionario.nis || "");
    setNascimento(formatarDataParaInput(funcionario.nascimento));
    setSexo(funcionario.sexo || "");
    setEndereco(funcionario.endereco || "");
    setIdCargo(funcionario.cargo?.id ?? null);
  }

  async function excluirFuncionario(id) {
    try {
      await api.delete(`/funcionarios/${id}`);

      if (funcionarioEditando && funcionarioEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
    } catch (error) {
      console.error("Erro ao excluir:", error);
      Swal.fire("Erro", "Erro ao excluir funcionário.", "error");
    }
  }

  function limparFormulario() {
    setFuncionarioEditando(null);
    setNome("");
    setCpf("");
    setTelefone("");
    setNis("");
    setNascimento("");
    setSexo("");
    setEndereco("");
    setIdCargo("");
  }

  function formatarDataParaInput(data) {
    if (!data) return "";
    return data.split("T")[0];
  }

  return (
    <div className={style["pagina-eventos"]}>
      <Menu />

      <main className={style["container"]}>
        <h2 className={style["titulo"]}>Gerenciar Funcionários</h2>

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
            placeholder="CPF"
            value={cpf}
            onChange={(e) => {
              setCpf(mascaraCPF(e.target.value));
              setErros((prev) => ({ ...prev, cpf: false }));
            }}
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
            placeholder="NIS"
            value={nis}
            onChange={(e) => {
              setNis(e.target.value);
              setErros((prev) => ({ ...prev, nis: false }));
            }}
            className={erros.nis ? style["input-erro"] : ""}
          />
          <input
            type="date"
            placeholder="Nascimento"
            value={nascimento}
            onChange={(e) => {
              setNascimento(e.target.value);
              setErros((prev) => ({ ...prev, nascimento: false }));
            }}
            className={erros.nascimento ? style["input-erro"] : ""}
          />
          <select
            value={sexo}
            onChange={(e) => setSexo(e.target.value)}
            className={erros.sexo ? style["input-erro"] : ""}
          >
            <option value="">Sexo</option>
            <option value="M">Masculino</option>
            <option value="F">Feminino</option>
            <option value="O">Outro</option>
          </select>
        </div>
        <div className={style["form-linha"]}>
          <input
            type="text"
            placeholder="Endereço"
            value={endereco}
            onChange={(e) => {
              setEndereco(e.target.value);
              setErros((prev) => ({ ...prev, endereco: false }));
            }}
            className={erros.endereco ? style["input-erro"] : ""}
          />
          <select
            value={id_cargo}
            onChange={(e) => {
              setIdCargo(e.target.value); // STRING
              setErros((prev) => ({ ...prev, id_cargo: false }));
            }}
            className={erros.id_cargo ? style["input-erro"] : ""}
          >
            <option value="">Selecione o Cargo</option>

            {cargos.map((cargo) => (
              <option key={cargo.id} value={cargo.id}>
                {cargo.nome}
              </option>
            ))}
          </select>
        </div>

        {/* FILTRO */}
        <div className={style["form-linha"]}>
          <CampoFiltro
            listaDados={funcionarios}
            listaFiltros={[
              { label: "Nome", value: "nome" },
            ]}
            filtroDefault="nome"
            onChange={setFuncionariosFiltrados}
            style={style}
          />
        </div>

        {/* LISTA */}
        <div className={style["lista"]}>
          {funcionariosFiltrados.map((funcionario) => (
            <div key={funcionario.id} className={style["item"]}>
              {funcionario.nome}
              <div className={style["acoes"]}>
                <button onClick={() => editarFuncionario(funcionario)}>Editar</button>

                <button onClick={() => confirmarExclusao(funcionario.id)}>
                  Excluir
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* BOTÃO */}
        <div className={style["botao-central"]}>
          <button onClick={salvarOuAtualizar}>
            {funcionarioEditando ? "Atualizar" : "Cadastrar"}
          </button>
        </div>
      </main>
    </div>
  );
}
