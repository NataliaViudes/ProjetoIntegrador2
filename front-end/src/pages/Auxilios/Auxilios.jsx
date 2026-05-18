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
      const resp = await api.get(`/beneficiarios/buscar?cpf=${cpfBusca}`);
      setBeneficiario(resp.data);

      if (!resp.data) {
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
        await api.put("/auxilios", {
          id: auxilioEditando.id,
          ...payload
        });
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

  function editarAuxilio(auxilio) {
    setAuxilioEditando(auxilio);
    setDescricao(auxilio.descricao || "");
    setCategoriaId(auxilio.categoria?.id ? String(auxilio.categoria.id) : "");
    setBeneficiario(auxilio.beneficiario || null);
    setCpfBusca(auxilio.beneficiario?.cpf || "");
  }

  async function excluirAuxilio(id) {
    try {
      await api.delete(`/auxilios/${id}`);

      if (auxilioEditando && auxilioEditando.id === id) {
        limparFormulario();
      }

      carregarTudo();
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
    return cpf.includes(buscaListaCpf);
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
          </div>
        </section>

        <section className="painel-direito">
          <div className="cabecalho-lista">
            <h2>Auxílios cadastrados</h2>
            <input
              type="text"
              placeholder="Buscar auxílio por CPF..."
              value={buscaListaCpf}
              onChange={(e) => setBuscaListaCpf(e.target.value)}
            />
          </div>

          <div className="lista-auxilios">
            {auxiliosFiltrados.length === 0 ? (
              <p>Nenhum auxílio encontrado.</p>
            ) : (
              auxiliosFiltrados.map((auxilio) => (
                <div className="item-auxilio" key={auxilio.id}>
                  <div className="info-auxilio">
                    <strong>{auxilio.descricao}</strong>
                    <span>
                      Beneficiário: {auxilio.beneficiario?.nome || "Não informado"}
                    </span>
                    <span>
                      CPF: {auxilio.beneficiario?.cpf || "Não informado"}
                    </span>
                    <span>
                      Categoria: {auxilio.categoria?.nome || "Não informada"}
                    </span>
                  </div>

                  <div className="botoes-item">
                    <button onClick={() => editarAuxilio(auxilio)}>Editar</button>
                    <button onClick={() => excluirAuxilio(auxilio.id)}>Excluir</button>
                  </div>
                </div>
              ))
            )}
          </div>
        </section>
      </main>
    </div>
  );
}

export default Auxilios;