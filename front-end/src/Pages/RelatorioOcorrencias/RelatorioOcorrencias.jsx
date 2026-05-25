import { useEffect, useState } from "react";
import api from "../../services/api.js";
import Menu from "../../components/Menu/Menu.jsx";
import "./RelatorioOcorrencias.css";

function RelatorioOcorrencias() {
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [idBeneficiario, setIdBeneficiario] = useState("");

  const [beneficiarios, setBeneficiarios] = useState([]);
  const [ocorrencias, setOcorrencias] = useState([]);

  useEffect(() => {
    carregarBeneficiarios();
  }, []);

  async function carregarBeneficiarios() {
    try {
      const resp = await api.get("/beneficiarios");
      setBeneficiarios(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar beneficiários:", error);
    }
  }

  async function buscarRelatorio() {
    try {
      const params = new URLSearchParams();

      if (dataInicio) params.append("dataInicio", dataInicio);
      if (dataFim) params.append("dataFim", dataFim);
      if (idBeneficiario) params.append("idBeneficiario", idBeneficiario);

      const resp = await api.get(`/ocorrencias/relatorio?${params.toString()}`);

      setOcorrencias(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao buscar relatório:", error);
      setOcorrencias([]);
    }
  }

  function limparFiltros() {
    setDataInicio("");
    setDataFim("");
    setIdBeneficiario("");
    setOcorrencias([]);
  }

  return (
    <div className="pagina-relatorio-ocorrencias" translate="no">
      <Menu />

      <main className="conteudo-relatorio-ocorrencias">
        <section className="painel-filtros">
          <h2>Relatório de Ocorrências</h2>

          <label>Data inicial</label>
          <input
            type="date"
            value={dataInicio}
            onChange={(e) => setDataInicio(e.target.value)}
          />

          <label>Data final</label>
          <input
            type="date"
            value={dataFim}
            onChange={(e) => setDataFim(e.target.value)}
          />

          <label>Beneficiário</label>
          <select
            value={idBeneficiario}
            onChange={(e) => setIdBeneficiario(e.target.value)}
          >
            <option value="">Todos</option>
            {beneficiarios.map((b) => (
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
          </div>
        </section>

        <section className="painel-resultados">
          <h2>Resultados</h2>

          {ocorrencias.length === 0 ? (
            <p>Nenhuma ocorrência encontrada.</p>
          ) : (
            <div className="lista-ocorrencias">
              {ocorrencias.map((o) => (
                <div key={o.id} className="item-ocorrencia">
                  <strong>Tipo: {o.tipo}</strong>

                  <span>
                    Data:{" "}
                    {o.dataRegistro
                      ? new Date(o.dataRegistro).toLocaleString()
                      : "Não informada"}
                  </span>

                  <span>
                    Beneficiário:{" "}
                    {o.beneficiario?.nome || "Ocorrência geral"}
                  </span>

                  <p>{o.observacao}</p>
                </div>
              ))}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default RelatorioOcorrencias;