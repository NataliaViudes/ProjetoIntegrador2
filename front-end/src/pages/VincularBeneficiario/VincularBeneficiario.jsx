import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Agendamentos/Agendamentos.css";
import { useLocation, useNavigate } from "react-router-dom";

function VincularBeneficiario() {
  const location = useLocation();
  const navigate = useNavigate();

  const dados = location.state || {}; 
  const [modoEdicao, setModoEdicao] = useState(false);
  const [atividades, setAtividades] = useState([]);
  const [beneficiarios, setBeneficiarios] = useState([]);

  const [atividadeId, setAtividadeId] = useState(dados.atividadeId || "");
  const [dataInicio, setDataInicio] = useState(dados.dataInicio || "");
  const [dataFim, setDataFim] = useState(dados.dataFim || "");
  const [observacao, setObservacao] = useState(dados.observacao || "");

  const[idAgendamento, setIdAgendamento] = useState(dados.idAgendamento || null);

  const [selecionados, setSelecionados] = useState([]);

  useEffect(() => {
    const dados = location.state || {};

    if (dados.idAgendamento) {
        setModoEdicao(true);
        carregarBeneficiariosVinculados(dados.idAgendamento);
    }

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

  async function carregarBeneficiariosVinculados(idAgendamento) {
  try {
    const resp = await api.get(`/vincularBeneficiario/${idAgendamento}`);

    const ids = resp.data.map(item => item.idBeneficiario);
    setSelecionados(ids); // o erro está ak dentro arrumar depois !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! LEIA ISSO !!!
  } catch (e) {
    console.error("Erro ao carregar vinculados:", e);
  }
}

  function toggleSelecionado(id) {
    setSelecionados(prev =>
      prev.includes(id)
        ? prev.filter(b => b !== id)
        : [...prev, id]
    );
  }

  async function salvar() {
    if (!atividadeId || !dataInicio || !dataFim) {
        alert("Preencha os dados.");
        return;
    }

    if (selecionados.length === 0) {
        alert("Selecione beneficiários.");
        return;
    }

    try {
        
        const respAg = await api.post("/agendamentos", {
        atividade: { id: Number(atividadeId) },
        dataInicio,
        dataFim,
        observacao
        });
        
        const idAgendamento = respAg.id;

        console.log("ID do agendamento a alterar:", idAgendamento);
        const payloadVinculo = {
            lista: selecionados.map(id => ({
                idBeneficiario: id,
                idAgendamento: idAgendamento
            }))
        };

        await api.post("/vincularBeneficiario", payloadVinculo);

        alert("Agendamento e vínculos salvos!");
        navigate("/agendamentos");

    } catch (e) {
        console.error("ERRO REAL:", e.response?.data || e);
        alert("Erro ao salvar.");
    }
  }

  async function alterar() {
  try {
    const idAgendamento = dados.idAgendamento;
    console.log("ID do agendamento a alterar:", idAgendamento);
    // 🔥 apagar antigos (precisa endpoint GET antes, se não tiver me fala)
    
    // 🔥 recriar todos
    const payload = selecionados.map(id => ({
  idBeneficiario: id,
  idAgendamento
}));

    console.log("Payload enviado para vincularBeneficiario:", payload);
    await api.post("/vincularBeneficiario", payload);
    alert("Atualizado!");
    navigate("/agendamentos");

  } catch (e) {
    console.error(e.response?.data || e);
    alert("Erro ao atualizar.");
  }
}

  return (
    <div className="pagina-agendamentos">
      <Menu />

      <div className="conteudo-agendamentos">

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
          <input type="datetime-local" value={dataInicio} disabled />

          <label>Data fim</label>
          <input type="datetime-local" value={dataFim} disabled />

          <label>Observação</label>
          <textarea rows="4" value={observacao} disabled />

          <div className="acoes-formulario">
            {modoEdicao ? (
                <button onClick={alterar}>Atualizar Vínculos</button>
                ) : (
                <button onClick={salvar}>Vincular</button>
            )}
          </div>
        </section>

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

                <input
                  type="checkbox"
                  checked={selecionados.includes(b.id)}
                  onChange={() => toggleSelecionado(b.id)}
                />
              </div>
            ))
          )}
        </section>

      </div>
    </div>
  );
}

export default VincularBeneficiario;