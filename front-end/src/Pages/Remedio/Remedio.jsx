import { useEffect, useState } from "react";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu";
import Swal from "sweetalert2";
import "./Remedio.css";

export default function Remedio() {

  const [nome, setNome] = useState("");
  const [descricao, setDescricao] = useState("");

  const [busca, setBusca] = useState("");

  const [remedios, setRemedios] = useState([]);
  const [remediosFiltrados, setRemediosFiltrados] = useState([]);

  const [remedioEditando, setRemedioEditando] = useState(null);

  const [erros, setErros] = useState({
    nome: false,
    descricao: false
  });

  useEffect(() => {
    carregarRemedios();
  }, []);

  async function carregarRemedios() {

    try {

      const resp = await api.get("/remedios/nome");

      const dados =
        Array.isArray(resp.data)
          ? resp.data
          : [];

      setRemedios(dados);
      setRemediosFiltrados(dados);

    } catch (error) {

      console.error(
        "Erro ao carregar remédios:",
        error
      );
    }
  }

  function filtrar(valor) {

    setBusca(valor);

    const lista = remedios.filter((r) =>

      r.nome
        ?.toLowerCase()
        .includes(valor.toLowerCase())

      ||

      r.descricao
        ?.toLowerCase()
        .includes(valor.toLowerCase())
    );

    setRemediosFiltrados(lista);
  }

  async function salvarOuAtualizar() {

    const novosErros = {
      nome: !nome,
      descricao: !descricao
    };

    setErros(novosErros);

    if (
      novosErros.nome ||
      novosErros.descricao
    ) {

      Swal.fire({
        icon: "warning",
        title: "Atenção",
        text: "Preencha todos os campos"
      });

      return;
    }

    const dados = {
      nome,
      descricao
    };

    const confirmou = await Swal.fire({
      title:
        remedioEditando
          ? "Atualizar remédio?"
          : "Cadastrar remédio?",

      text: nome,

      icon: "question",

      showCancelButton: true,

      confirmButtonText:
        remedioEditando
          ? "Atualizar"
          : "Cadastrar",

      cancelButtonText: "Cancelar"
    });

    if (!confirmou.isConfirmed)
      return;

    try {

      if (remedioEditando) {

        await api.put(
          "/remedios",
          {
            id: remedioEditando.id,
            ...dados
          }
        );

      } else {

        await api.post(
          "/remedios",
          dados
        );
      }

      Swal.fire({
        icon: "success",
        title:
          remedioEditando
            ? "Remédio atualizado!"
            : "Remédio cadastrado!"
      });

      limparFormulario();

      carregarRemedios();

    } catch (error) {

      console.error(
        "Erro ao salvar:",
        error
      );

      Swal.fire({
        icon: "error",
        title: "Erro",
        text:
          "Erro ao salvar remédio"
      });
    }
  }

  function editar(remedio) {

    setRemedioEditando(remedio);

    setNome(remedio.nome || "");

    setDescricao(
      remedio.descricao || ""
    );
  }

  async function excluir(id) {

    const confirmou = await Swal.fire({
      title: "Excluir remédio?",
      icon: "warning",

      showCancelButton: true,

      confirmButtonText: "Excluir",

      cancelButtonText: "Cancelar"
    });

    if (!confirmou.isConfirmed)
      return;

    try {

      await api.delete(
        `/remedios/${id}`
      );

      Swal.fire({
        icon: "success",
        title: "Remédio excluído!"
      });

      if (
        remedioEditando &&
        remedioEditando.id === id
      ) {
        limparFormulario();
      }

      carregarRemedios();

    } catch (error) {

      console.error(
        "Erro ao excluir:",
        error
      );

      Swal.fire({
        icon: "error",
        title: "Erro",
        text:
          "Erro ao excluir remédio"
      });
    }
  }

  function limparFormulario() {

    setRemedioEditando(null);

    setNome("");
    setDescricao("");

    setErros({
      nome: false,
      descricao: false
    });
  }

  return (

    <div className="pagina-remedio">

      <Menu />

      <main className="container-remedio">

        <div className="topo-remedio">

          <div>

            <h1>
              Gerenciar Remédios
            </h1>

            <p>
              Cadastre, edite e
              consulte medicamentos
            </p>

          </div>

          <div className="badge-remedio">
            {remedios.length} cadastrados
          </div>

        </div>

        <section className="card-formulario">

          <div className="linha-inputs">

            <div className="grupo-input">

              <label>
                Nome do remédio
              </label>

              <input
                type="text"
                placeholder="Ex: Dipirona"

                value={nome}

                onChange={(e) => {

                  setNome(
                    e.target.value
                  );

                  setErros((prev) => ({
                    ...prev,
                    nome: false
                  }));
                }}

                className={
                  erros.nome
                    ? "input-erro"
                    : ""
                }
              />

            </div>

            <div className="grupo-input">

              <label>
                Descrição
              </label>

              <input
                type="text"

                placeholder="Descrição do remédio"

                value={descricao}

                onChange={(e) => {

                  setDescricao(
                    e.target.value
                  );

                  setErros((prev) => ({
                    ...prev,
                    descricao: false
                  }));
                }}

                className={
                  erros.descricao
                    ? "input-erro"
                    : ""
                }
              />

            </div>

          </div>

          <div className="acoes-formulario">

            <button
              className="btn-salvar"
              onClick={
                salvarOuAtualizar
              }
            >
              {remedioEditando
                ? "Atualizar"
                : "Cadastrar"}
            </button>

            <button
              className="btn-limpar"
              onClick={
                limparFormulario
              }
            >
              Limpar
            </button>

          </div>

        </section>

        <section className="card-lista">

          <div className="topo-lista">

            <h2>
              Lista de Remédios
            </h2>

            <input
              type="text"

              placeholder="Buscar remédio..."

              value={busca}

              onChange={(e) =>
                filtrar(
                  e.target.value
                )
              }
            />

          </div>

          <div className="lista-remedios">

            {remediosFiltrados.map((r) => (

              <div
                key={r.id}
                className="item-remedio"
              >

                <div className="info-remedio">

                  <h3>
                    {r.nome}
                  </h3>

                  <p>
                    {r.descricao}
                  </p>

                </div>

                <div className="acoes-item">

                  <button
                    className="btn-editar"

                    onClick={() =>
                      editar(r)
                    }
                  >
                    Editar
                  </button>

                  <button
                    className="btn-excluir"

                    onClick={() =>
                      excluir(r.id)
                    }
                  >
                    Excluir
                  </button>

                </div>

              </div>

            ))}

          </div>

        </section>

      </main>

    </div>
  );
}