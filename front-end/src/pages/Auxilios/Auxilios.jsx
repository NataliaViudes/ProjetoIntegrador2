import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "./Auxilios.css";

function Auxilios() {
  const [cpfBusca, setCpfBusca] = useState("");
  const [beneficiario, setBeneficiario] = useState(null);

  const [categorias, setCategorias] = useState([]);
  const [categoriaId, setCategoriaId] = useState("");
  const [descricao, setDescricao] = useState("");

  const [filtroStatus, setFiltroStatus] = useState("Todos");

  const [auxilios, setAuxilios] = useState([]);
  const [buscaListaCpf, setBuscaListaCpf] = useState("");

  const [auxilioEditando, setAuxilioEditando] = useState(null);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAuxilios, respCategorias] = await Promise.all([
        api.get("/auxilios"),
        api.get("/categorias")
      ]);

      setAuxilios(Array.isArray(respAuxilios.data) ? respAuxilios.data : []);
      setCategorias(Array.isArray(respCategorias.data) ? respCategorias.data : []);
    } catch (error) {
      console.error("Erro ao carregar dados:", error);
    }
  }

  async function buscarBeneficiarioPorCpf() {
    if (!cpfBusca.trim()) {
      alert("Digite o CPF.");
      return;
    }

    try {
      const resp = await api.get(`/beneficiarios/cpf?cpf=${cpfBusca}`);

      if (Array.isArray(resp.data) && resp.data.length > 0) {
        setBeneficiario(resp.data[0]);
      } else {
        setBeneficiario(null);
        alert("Beneficiário não encontrado.");
      }

    } catch (error) {
      console.error("Erro ao buscar beneficiário:", error);
      setBeneficiario(null);
      alert("Beneficiário não encontrado.");
    }
  }

  async function salvarOuAtualizar() {
    if (!beneficiario || !beneficiario.id) {
      alert("Busque e selecione um beneficiário pelo CPF.");
      return;
    }

    if (!categoriaId || !descricao.trim()) {
      alert("Preencha categoria e descrição.");
      return;
    }

    const payload = {
      descricao: descricao,
      beneficiario: { id: Number(beneficiario.id) },
      categoria: { id: Number(categoriaId) }
    };

    try {
      if (auxilioEditando) {
        await api.put(`/auxilios/${auxilioEditando.id}`, payload);
      } else {
        await api.post("/auxilios", payload);
      }

      limparFormulario();
      carregarTudo();
    } catch (error) {
      console.error("Erro ao salvar/atualizar auxílio:", error);
      alert("Erro ao salvar auxílio.");
    }
  }

  async function alterarStatus(novoStatus) {

    if (!auxilioEditando) return;

    const confirmar = window.confirm(
      `Tem certeza que deseja ${novoStatus.toLowerCase()} este auxílio?\n\nApós isso ele não poderá mais ser alterado.`
    );

    if (!confirmar) return;

    const payload = {
      descricao: descricao,
      status: novoStatus,
      beneficiario: { id: Number(beneficiario.id) },
      categoria: { id: Number(categoriaId) }
    };

    try {

      await api.put(`/auxilios/${auxilioEditando.id}`, payload);

      alert(`Auxílio ${novoStatus.toLowerCase()} com sucesso!`);

      limparFormulario();
      carregarTudo();

    } catch (error) {

      console.error(`Erro ao ${novoStatus.toLowerCase()} auxílio:`, error);

      alert(`Erro ao ${novoStatus.toLowerCase()} auxílio.`);
    }
  }

  function editarAuxilio(auxilio) {
    setAuxilioEditando(auxilio);
    setDescricao(auxilio.descricao || "");
    setCategoriaId(auxilio.categoria?.id ? String(auxilio.categoria.id) : "");
    setBeneficiario(auxilio.beneficiario || null);
    setCpfBusca(auxilio.beneficiario?.cpf || "");
  }

  async function excluirAuxilio(id) {

    const confirmar = window.confirm(
      "Tem certeza que deseja excluir este auxílio?"
    );

    if (!confirmar) return;

    try {

      await api.delete(`/auxilios/${id}`);

      if (auxilioEditando && auxilioEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();

      alert("Auxílio excluído com sucesso!");

    } catch (error) {

      console.error("Erro ao excluir auxílio:", error);

      alert("Erro ao excluir auxílio.");
    }
  }

  function limparFormulario() {
    setCpfBusca("");
    setBeneficiario(null);
    setCategoriaId("");
    setDescricao("");
    setAuxilioEditando(null);
  }

  const auxiliosFiltrados = auxilios.filter((auxilio) => {

    const cpf = auxilio.beneficiario?.cpf || "";
    const status = auxilio.status || "Processando";

    const filtroCpf = cpf.includes(buscaListaCpf);

    const filtroStatusValido =
      filtroStatus === "Todos"
        ? true
        : status === filtroStatus;

    return filtroCpf && filtroStatusValido;
  });

  return (
    <div className="pagina-auxilios" translate="no">
      <Menu />

      <main className="conteudo-auxilios">
        <section className="painel-esquerdo">
          <h2>Cadastro de Auxílio</h2>

          <label>Buscar beneficiário por CPF</label>
          <div className="linha-busca-cpf">
            <input
              type="text"
              placeholder="Digite o CPF"
              value={cpfBusca}
              onChange={(e) => setCpfBusca(e.target.value)}
            />
            <button onClick={buscarBeneficiarioPorCpf}>Buscar</button>
          </div>

          <div className="box-beneficiario">
            {beneficiario ? (
              <>
                <p><strong>Nome:</strong> {beneficiario.nome}</p>
                <p><strong>CPF:</strong> {beneficiario.cpf}</p>
              </>
            ) : (
              <p>Nenhum beneficiário selecionado.</p>
            )}
          </div>

          <label>Categoria do auxílio</label>
          <select
            value={categoriaId}
            onChange={(e) => setCategoriaId(e.target.value)}
          >
            <option value="">Selecione uma categoria</option>
            {categorias.map((categoria) => (
              <option key={categoria.id} value={categoria.id}>
                {categoria.nome}
              </option>
            ))}
          </select>

          <label>Descrição</label>
          <textarea
            placeholder="Digite a descrição do auxílio"
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            rows={6}
          />

          <div className="acoes-formulario">

            <button onClick={salvarOuAtualizar}>
              {auxilioEditando ? "Atualizar" : "Confirmar"}
            </button>

            <button type="button" onClick={limparFormulario}>
              Limpar
            </button>

            {auxilioEditando && (
              <>
                <button
                  type="button"
                  className="botao-aprovar"
                  onClick={() => alterarStatus("Aprovado")}
                >
                  Aprovar
                </button>

                <button
                  type="button"
                  className="botao-rejeitar"
                  onClick={() => alterarStatus("Rejeitado")}
                >
                  Rejeitar
                </button>
              </>
            )}

          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">

            <h2>Auxílios cadastrados</h2>

            <div className="filtros-status">

              <button onClick={() => setFiltroStatus("Todos")}>
                Todos
              </button>

              <button onClick={() => setFiltroStatus("Processando")}>
                Processando
              </button>

              <button onClick={() => setFiltroStatus("Aprovado")}>
                Aprovados
              </button>

              <button onClick={() => setFiltroStatus("Rejeitado")}>
                Rejeitados
              </button>

            </div>

            <input
              type="text"
              placeholder="Buscar auxílio por CPF..."
              value={buscaListaCpf}
              onChange={(e) => setBuscaListaCpf(e.target.value)}
            />

          </div>

          <div className="lista-auxilios">
            {auxiliosFiltrados.map((auxilio) => {

              const status = auxilio.status || "Processando";

              const bloqueado =
                status === "Aprovado" || status === "Rejeitado";

              return (
                <div
                  className={`item-auxilio ${status === "Aprovado"
                    ? "auxilio-aprovado"
                    : status === "Rejeitado"
                      ? "auxilio-rejeitado"
                      : ""
                    }`}
                  key={auxilio.id}
                >

                  <div className="topo-auxilio">

                    <div className="info-resumida">

                      <span>
                        <strong>Beneficiário:</strong>{" "}
                        {auxilio.beneficiario?.nome || "Não informado"}
                      </span>

                      <span>
                        <strong>CPF:</strong>{" "}
                        {auxilio.beneficiario?.cpf || "Não informado"}
                      </span>

                      <span>
                        <strong>Categoria:</strong>{" "}
                        {auxilio.categoria?.nome || "Não informada"}
                      </span>

                      <span>
                        <strong>Data:</strong>{" "}
                        {
                          auxilio.data
                            ? new Date(auxilio.data).toLocaleDateString()
                            : "Não informada"
                        }
                      </span>

                      <span>
                        <strong>Status:</strong>{" "}
                        {status}
                      </span>

                    </div>

                    <div className="botoes-item">
                      {!bloqueado && (
                        <>
                          <button onClick={() => editarAuxilio(auxilio)}>
                            Editar
                          </button>
                        </>
                      )}
                      <button onClick={() => excluirAuxilio(auxilio.id)}>
                        Excluir
                      </button>
                    </div>

                  </div>

                  <div className="descricao-auxilio">
                    {auxilio.descricao}
                  </div>

                </div>
              );
            })}
          </div>
        </section>
      </main>
    </div>
  );
}

export default Auxilios;