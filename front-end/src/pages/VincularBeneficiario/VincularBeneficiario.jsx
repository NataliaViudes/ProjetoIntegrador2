import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Agendamentos/Agendamentos.css";
import { useLocation } from "react-router-dom";

function VincularBeneficiario() {
  const location = useLocation();
  const dados = location.state;
  const [atividades, setAtividades] = useState([]);
  const [beneficiarios, setBeneficiarios] = useState([]);

  const [atividadeId, setAtividadeId] = useState(dados?.atividadeId || "");
  const [dataInicio, setDataInicio] = useState(dados?.dataInicio || "");
  const [dataFim, setDataFim] = useState(dados?.dataFim || "");
  const [observacao, setObservacao] = useState(dados?.observacao || "");

  const [selecionados, setSelecionados] = useState([]);

  useEffect(() => {
    carregarTudo();
  }, []);

  async function carregarTudo() {
    try {
      const [respAtividades, respBeneficiarios] = await Promise.all([
        api.get("/atividades"),
        api.get("/beneficiarios")
      ]);

      setAtividades(Array.isArray(respAtividades.data) ? respAtividades.data : []);
      setBeneficiarios(Array.isArray(respBeneficiarios.data) ? respBeneficiarios.data : []);
    } catch (e) {
      console.error("Erro ao carregar dados:", e);
    }
  }

  function toggleSelecionado(id) {
    if (selecionados.includes(id)) {
      setSelecionados(selecionados.filter(b => b !== id));
    } else {
      setSelecionados([...selecionados, id]);
    }
  }

  function limparFormulario() {
    setAtividadeId("");
    setDataInicio("");
    setDataFim("");
    setObservacao("");
    setSelecionados([]);
  }

  async function salvar() {
    if (!atividadeId || !dataInicio || !dataFim || selecionados.length === 0) {
      alert("Preencha tudo e selecione pelo menos um beneficiário.");
      return;
    }

    const payload = {
      atividade: { id: Number(atividadeId) },
      dataInicio,
      dataFim,
      observacao,
      beneficiarios: selecionados.map(id => ({ id }))
    };

    try {
      await api.post("/agendamentos/vincular", payload);
      alert("Beneficiários vinculados com sucesso!");
      limparFormulario();
    } catch (e) {
      console.error(e);
      alert("Erro ao vincular beneficiários.");
    }
  }

  return (
    <div className="pagina-agendamentos">
      <Menu />

      <div className="conteudo-agendamentos">

        {/* FORMULÁRIO */}
        <section className="painel-formulario">
          <h2>Vincular Beneficiários</h2>

          <label>Atividade</label>
          <select value={atividadeId} disabled>
            <option value="">Selecione</option>
            {atividades.map((a) => (
              <option key={a.id} value={a.id}>
                {a.descricao} - {a.funcionario?.nome || ""}
              </option>
            ))}
          </select>

          <label>Data início</label>
          <input
            type="datetime-local"
            value={dataInicio}
            disabled
          />

          <label>Data fim</label>
          <input
            type="datetime-local"
            value={dataFim}
            disabled
          />

          <label>Observação</label>
          <textarea
            rows="4"
            value={observacao}
            disabled
          />

          <div className="acoes-formulario">
            <button onClick={salvar}>Vincular</button>
          </div>
        </section>

        {/* LISTA DE BENEFICIÁRIOS */}
        <section className="painel-calendario">
          <h3>Beneficiários</h3>

          {beneficiarios.length === 0 ? (
            <p>Nenhum beneficiário encontrado.</p>
          ) : (
            beneficiarios.map((b) => (
              <div key={b.id} className="item-agendamento">
                <div>
                  <strong>{b.nome}</strong>
                  <div>CPF: {b.cpf}</div>
                  <div>Situação: {b.situacao}</div>
                </div>

                <div>
                  <input
                    type="checkbox"
                    checked={selecionados.includes(b.id)}
                    onChange={() => toggleSelecionado(b.id)}
                  />
                </div>
              </div>
            ))
          )}
        </section>

      </div>
    </div>
  );
}

export default VincularBeneficiario;