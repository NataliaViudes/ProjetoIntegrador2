import { useEffect, useState } from "react";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu.jsx";
import "./RelatorioFaltas.css";

function RelatorioFaltas() {
  const [beneficiarios, setBeneficiarios] = useState([]);
  const [idBeneficiario, setIdBeneficiario] = useState("");
  const [busca, setBusca] = useState("");
  const [registros, setRegistros] = useState([]);
  const [relatorioBuscado, setRelatorioBuscado] = useState(false);

  useEffect(() => {
    carregarBeneficiarios();
  }, []);

  async function carregarBeneficiarios() {
    try {
      const resp = await api.get("/beneficiarios");
      setBeneficiarios(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar beneficiarios:", error);
    }
  }

  async function buscarRelatorio() {
    if (!idBeneficiario) {
      alert("Selecione um beneficiario.");
      return;
    }

    try {
      console.log("Buscando relatorio para beneficiario ID:", idBeneficiario);

      const resp = await api.get(`/presencas/relatorio/beneficiario/${idBeneficiario}`);

      console.log("Resposta da API:", resp);  

      setRegistros(Array.isArray(resp.data) ? resp.data : []);
      setRelatorioBuscado(true);
    } catch (error) {
      console.error("Erro ao buscar relatorio de faltas:", error);
      setRegistros([]);
      setRelatorioBuscado(true);
    }
  }

  async function baixarPdf() {
    if (!idBeneficiario) {
      alert("Selecione um beneficiario.");
      return;
    }

    try {
      const response = await api.get(
        `/presencas/relatorio/beneficiario/${idBeneficiario}/pdf`,
        { responseType: "blob" }
      );

      const url = window.URL.createObjectURL(new Blob([response.data], { type: "application/pdf" }));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `relatorio_faltas_${idBeneficiario}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Erro ao baixar PDF:", error);
      alert("Erro ao baixar PDF.");
    }
  }

  function limparFiltros() {
    setIdBeneficiario("");
    setBusca("");
    setRegistros([]);
    setRelatorioBuscado(false);
  }

  function formatarData(valor) {
    if (!valor) return "Nao informada";
    return new Date(valor).toLocaleString();
  }

  const beneficiariosFiltrados = beneficiarios.filter(b =>
    (b.nome || "").toLowerCase().includes(busca.toLowerCase())
      || (b.cpf || "").includes(busca)
  );

  const beneficiarioSelecionado = beneficiarios.find(b => String(b.id) === String(idBeneficiario));
  const faltas = registros.filter(r => !r.presente);
  const presentes = registros.filter(r => r.presente);

  return (
    <div className="pagina-relatorio-faltas" translate="no">
      <Menu />

      <main className="conteudo-relatorio-faltas">
        <section className="painel-filtros">
          <h2>Relatorio de Faltas</h2>

          <label>Buscar beneficiario</label>
          <input
            type="text"
            placeholder="Nome ou CPF"
            value={busca}
            onChange={(e) => setBusca(e.target.value)}
          />

          <label>Beneficiario</label>
          <select
            value={idBeneficiario}
            onChange={(e) => setIdBeneficiario(e.target.value)}
          >
            <option value="">Selecione</option>
            {beneficiariosFiltrados.map((b) => (
              <option key={b.id} value={b.id}>
                {b.nome} - CPF: {b.cpf}
              </option>
            ))}
          </select>

          <div className="acoes-formulario">
            <button type="button" onClick={buscarRelatorio}>
              Buscar
            </button>

            <button type="button" onClick={limparFiltros}>
              Limpar
            </button>

            <button type="button" onClick={baixarPdf} disabled={!idBeneficiario}>
              Baixar PDF
            </button>
          </div>
        </section>

        <section className="painel-resultados">
          <h2>Resultados</h2>

          {beneficiarioSelecionado && (
            <div className="resumo-faltas">
              <strong>{beneficiarioSelecionado.nome}</strong>
              <span>CPF: {beneficiarioSelecionado.cpf}</span>
              <span>Total de registros: {registros.length}</span>
              <span>Total de faltas: {faltas.length}</span>
              <span>Total de presencas: {presentes.length}</span>
            </div>
          )}

          {registros.length === 0 ? (
            <p>
              {relatorioBuscado
                ? "Nenhum registro de presenca ou falta encontrado para este beneficiario."
                : "Selecione um beneficiario para buscar o relatorio."}
            </p>
          ) : (
            <div className="lista-faltas">
              {registros.map((falta, index) => (
                <div
                  key={`${falta.idAgendamento}-${index}`}
                  className={`item-falta ${falta.presente ? "item-presente" : "item-ausente"}`}
                >
                  <strong>{falta.atividade || "Atividade nao informada"}</strong>
                  <span>Status: {falta.presente ? "Presente" : "Falta"}</span>
                  <span>Funcionario: {falta.funcionario || "Nao informado"}</span>
                  <span>Inicio: {formatarData(falta.dataInicio)}</span>
                  <span>Fim: {formatarData(falta.dataFim)}</span>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default RelatorioFaltas;
