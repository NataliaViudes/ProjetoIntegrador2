import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../Services/api";
import Menu from "../../Components/Menu/Menu.jsx";
import "./Ocorrencia.css";

function Ocorrencias() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const navigate = useNavigate();

  const [agendamentos, setAgendamentos] = useState([]);
  const [beneficiarios, setBeneficiarios] = useState([]);

  const [idAgendamento, setIdAgendamento] = useState("");
  const [tipo, setTipo] = useState("GERAL");
  const [observacao, setObservacao] = useState("");
  const [beneficiarioId, setBeneficiarioId] = useState("");

  const [ocorrencias, setOcorrencias] = useState([]);
const [ocorrenciaEditando, setOcorrenciaEditando] = useState(null);

  useEffect(() => {
    carregarDados();
    carregarOcorrencias();
  }, []);

  async function carregarDados() {
    try {
      const respAgendamentos = await api.get("/agendamentos");

      const todosAgendamentos = Array.isArray(respAgendamentos.data)
        ? respAgendamentos.data
        : [];

      const agora = new Date();

      const agendamentosEmAndamento = todosAgendamentos.filter((ag) => {
        const inicio = new Date(ag.dataInicio);
        const fim = new Date(ag.dataFim);

        return agora >= inicio && agora <= fim;
      });

      setAgendamentos(agendamentosEmAndamento);

    } catch (error) {
      console.error("Erro ao carregar dados:", error);
      alert("Erro ao carregar dados da tela de ocorrências.");
    }
  }

  async function carregarOcorrencias() {
  try {
    const resp = await api.get("/ocorrencias/relatorio");

    const lista = Array.isArray(resp.data) ? resp.data : [];

    const agora = new Date();

    const ocorrenciasNoPrazo = lista.filter((o) => {
      if (!o.dataRegistro) {
        return false;
      }

      const dataRegistro = new Date(o.dataRegistro);
      const diferencaMinutos = (agora - dataRegistro) / 1000 / 60;

      return diferencaMinutos <= 30;
    });

    setOcorrencias(ocorrenciasNoPrazo);
  } catch (error) {
    console.error("Erro ao carregar ocorrências:", error);
    setOcorrencias([]);
  }
}

  async function carregarBeneficiariosVinculados(idAgendamentoSelecionado) {
    if (!idAgendamentoSelecionado) {
      setBeneficiarios([]);
      return;
    }

    try {
      const resp = await api.get(
        `/vincularBeneficiario/agendamento/${idAgendamentoSelecionado}`
      );

      setBeneficiarios(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar beneficiários vinculados:", error);
      setBeneficiarios([]);
    }
  }

  async function registrarOcorrencia() {
    if (!idAgendamento) {
      alert("Selecione um agendamento em andamento.");
      return;
    }

    if (!observacao.trim()) {
      alert("Digite a observação.");
      return;
    }

    if (tipo === "INDIVIDUAL" && !beneficiarioId) {
      alert("Selecione um beneficiário.");
      return;
    }

    const payload = {
      agendamento: { id: Number(idAgendamento) },
      tipo,
      observacao
    };

    if (tipo === "INDIVIDUAL") {
      payload.beneficiario = { id: Number(beneficiarioId) };
    }

    try {
      if (ocorrenciaEditando) {
        await api.put(`/ocorrencias/${ocorrenciaEditando.id}`, payload);
      } else {
        await api.post("/ocorrencias", payload);
      }

      alert(ocorrenciaEditando ? "Ocorrência alterada com sucesso!" : "Ocorrência registrada com sucesso!");

      setOcorrenciaEditando(null);
      setIdAgendamento("");
      setTipo("GERAL");
      setObservacao("");
      setBeneficiarioId("");

      carregarDados();
      carregarOcorrencias();
    } catch (error) {
      console.error("Erro ao registrar ocorrência:", error);

      if (error.response?.data?.mensage) {
        alert(error.response.data.mensage);
      } else if (error.response?.data?.mensagem) {
        alert(error.response.data.mensagem);
      } else if (error.response?.data) {
        alert(error.response.data);
      } else {
        alert("Erro ao registrar ocorrência.");
      }
    }
  }

    function editarOcorrencia(o) {
    setOcorrenciaEditando(o);
    setIdAgendamento(String(o.agendamento?.id || ""));
    setTipo(o.tipo || "GERAL");
    setObservacao(o.observacao || "");

    if (o.beneficiario?.id) {
      setBeneficiarioId(String(o.beneficiario.id));
      carregarBeneficiariosVinculados(o.agendamento?.id);
    } else {
      setBeneficiarioId("");
    }
  }
  async function excluirOcorrencia(id) {
  const confirmar = window.confirm("Deseja realmente excluir esta ocorrência?");

  if (!confirmar) {
    return;
  }

  try {
    await api.delete(`/ocorrencias/${id}`);
    alert("Ocorrência excluída com sucesso!");

    if (ocorrenciaEditando && ocorrenciaEditando.id === id) {
      setOcorrenciaEditando(null);
      setObservacao("");
      setBeneficiarioId("");
      setTipo("GERAL");
    }

    carregarOcorrencias();
  } catch (error) {
    console.error("Erro ao excluir ocorrência:", error);

    if (error.response?.data?.mensage) {
      alert(error.response.data.mensage);
    } else if (error.response?.data?.mensagem) {
      alert(error.response.data.mensagem);
    } else {
      alert("Erro ao excluir ocorrência.");
    }
  }
}

  if (nivelUsuario < 2) {
        return (
            <div>
                <Menu />
                <h2 style={{ padding: "20px" }}>
                    Você não possui acesso a esta página.
                </h2>
            </div>
        );
    }

  return (
    <div className="pagina-ocorrencias" translate="no">
      <Menu />

      <main className="conteudo-ocorrencias">
        <section className="painel-ocorrencia">
          <h2>Registrar Ocorrência</h2>

          <label>Agendamento em andamento</label>
          <select
            value={idAgendamento}
            onChange={(e) => {
              const id = e.target.value;
              setIdAgendamento(id);
              setBeneficiarioId("");
              carregarBeneficiariosVinculados(id);
            }}
          >
            <option value="">Selecione um agendamento</option>
            {agendamentos.map((ag) => (
              <option key={ag.id} value={ag.id}>
                {ag.atividade?.descricao} - {ag.atividade?.funcionario?.nome || "Funcionário não informado"}
              </option>
            ))}
          </select>

          {agendamentos.length === 0 && (
            <p className="aviso-ocorrencia">
              Não há agendamentos acontecendo neste horário.
            </p>
          )}

          <label>Tipo de ocorrência</label>
          <select value={tipo} onChange={(e) => setTipo(e.target.value)}>
            <option value="GERAL">Geral</option>
            <option value="INDIVIDUAL">Individual</option>
          </select>

          {tipo === "INDIVIDUAL" && (
            <>
              <label>Beneficiário vinculado</label>

              <select
                value={beneficiarioId}
                onChange={(e) => setBeneficiarioId(e.target.value)}
                disabled={!idAgendamento}
              >
                <option value="">
                  {idAgendamento
                    ? "Selecione o beneficiário"
                    : "Selecione primeiro um agendamento"}
                </option>

                {beneficiarios.map((b) => (
                  <option key={b.id} value={b.id}>
                    {b.nome} - CPF: {b.cpf}
                  </option>
                ))}
              </select>

              {idAgendamento && beneficiarios.length === 0 && (
                <p className="aviso-ocorrencia">
                  Nenhum beneficiário vinculado a este agendamento.
                </p>
              )}
            </>
          )}

          <label>Observação</label>
          <textarea
            rows="6"
            placeholder="Descreva a ocorrência..."
            value={observacao}
            onChange={(e) => setObservacao(e.target.value)}
          />

          <div className="acoes-formulario">
            <button type="button" onClick={registrarOcorrencia}>
              {ocorrenciaEditando ? "Atualizar" : "Registrar"}
            </button>

            <button type="button" onClick={() => navigate("/agendamentos")}>
              Voltar
            </button>
          </div>

          
        </section>
          <section className="painel-ocorrencias">
    <h2>Ocorrências Registradas</h2>

    {ocorrencias.length === 0 ? (
      <p>Nenhuma ocorrência registrada.</p>
    ) : (
      ocorrencias.map((o) => (
        <div key={o.id} className="item-ocorrencia">
          <strong>{o.tipo}</strong>

          <span>
            Beneficiário: {o.beneficiario?.nome || "Ocorrência geral"}
          </span>

          <span>
            Data:{" "}
            {o.dataRegistro
              ? new Date(o.dataRegistro).toLocaleString()
              : "Não informada"}
          </span>

          <p>{o.observacao}</p>

          <div className="acoes-item">
            <button type="button" onClick={() => editarOcorrencia(o)}>
              Editar
            </button>

            <button type="button" onClick={() => excluirOcorrencia(o.id)}>
              Excluir
            </button>
          </div>
        </div>
      ))
    )}
  </section>

      </main>
    </div>
  );
}

export default Ocorrencias;