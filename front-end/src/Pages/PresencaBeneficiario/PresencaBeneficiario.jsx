import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import api from "../../services/api.js";
import Menu from "../../components/Menu/Menu.jsx";
import "../VincularBeneficiario/VincularBeneficiario.css";
import "./PresencaBeneficiario.css";

function PresencaBeneficiario() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  const nivelUsuario = usuario?.funcionario?.cargo?.nivelAcesso || 1;

  const location = useLocation();
  const navigate = useNavigate();
  const dados = location.state || {};

  const [atividade, setAtividade] = useState(dados.atividade || "");
  const [funcionario, setFuncionario] = useState(dados.funcionario || "");
  const [dataInicio, setDataInicio] = useState(dados.dataInicio || "");
  const [dataFim, setDataFim] = useState(dados.dataFim || "");
  const [idAgendamento] = useState(dados.idAgendamento || null);

  const [beneficiarios, setBeneficiarios] = useState([]);
  const [presencas, setPresencas] = useState({});
  const [busca, setBusca] = useState("");

  useEffect(() => {
    async function carregar() {
      if (!idAgendamento) return;

      try {
        const [respAgendamento, respBeneficiarios, respPresencas] = await Promise.all([
          api.get(`/agendamentos/${idAgendamento}`),
          api.get(`/vincularBeneficiario/agendamento/${idAgendamento}`),
          api.get(`/presencas/${idAgendamento}`).catch(() => ({ data: [] }))
        ]);

        const agendamento = respAgendamento.data;
        setAtividade(valor => agendamento.atividade?.descricao || valor);
        setFuncionario(valor => agendamento.atividade?.funcionario?.nome || valor);
        setDataInicio(valor => agendamento.dataInicio || valor);
        setDataFim(valor => agendamento.dataFim || valor);

        const listaBeneficiarios = Array.isArray(respBeneficiarios.data)
          ? respBeneficiarios.data.map(b => ({ ...b, id: Number(b.id) }))
          : [];

        const mapaPresencas = {};
        listaBeneficiarios.forEach(b => {
          mapaPresencas[Number(b.id)] = false;
        });

        if (Array.isArray(respPresencas.data)) {
          respPresencas.data.forEach(p => {
            mapaPresencas[Number(p.idBeneficiario)] = !!p.presente;
          });
        }

        setBeneficiarios(listaBeneficiarios);
        setPresencas(mapaPresencas);
      } catch (e) {
        console.error("Erro ao carregar presencas:", e);
        alert("Erro ao carregar presencas.");
      }
    }

    carregar();
  }, [idAgendamento]);

  function togglePresenca(id) {
    const idNum = Number(id);

    setPresencas(prev => ({
      ...prev,
      [idNum]: !prev[idNum]
    }));
  }

  function marcarTodos(valor) {
    const novoMapa = {};
    beneficiarios.forEach(b => {
      novoMapa[Number(b.id)] = valor;
    });
    setPresencas(novoMapa);
  }

  async function salvar() {
    if (!idAgendamento) {
      alert("Agendamento invalido.");
      return;
    }

    if (beneficiarios.length === 0) {
      alert("Nenhum beneficiario vinculado a este agendamento.");
      return;
    }

    const payload = beneficiarios.map(b => ({
      idAgendamento: Number(idAgendamento),
      idBeneficiario: Number(b.id),
      presente: !!presencas[Number(b.id)]
    }));

    try {
      await api.post("/presencas", payload);
      alert("Presencas salvas com sucesso!");
      navigate("/agendamentos");
    } catch (e) {
      console.error(e.response?.data || e);
      alert("Erro ao salvar presencas.");
    }
  }

  const beneficiariosFiltrados = beneficiarios.filter(b =>
    (b.nome || "").toLowerCase().includes(busca.toLowerCase())
  );

  if (nivelUsuario < 2) {
    return (
      <div>
        <Menu />
        <h2 style={{ padding: "20px" }}>
          Voce nao possui acesso a esta pagina.
        </h2>
      </div>
    );
  }

  return (
    <div className="pagina-agendamentos">
      <Menu />

      <div className="conteudo-agendamentos">
        <section className="painel-formulario">
          <h2>Presenca / Falta</h2>

          <label>Atividade</label>
          <input value={atividade} disabled />

          <label>Funcionario</label>
          <input value={funcionario} disabled />

          <label>Data inicio</label>
          <input type="datetime-local" value={dataInicio} disabled />

          <label>Data fim</label>
          <input type="datetime-local" value={dataFim} disabled />

          <div className="acoes-formulario presenca-acoes">
            <button type="button" onClick={() => marcarTodos(true)}>
              Todos presentes
            </button>

            <button type="button" onClick={() => marcarTodos(false)}>
              Dar falta para todos
            </button>
          </div>

          <div className="acoes-formulario">
            <button type="button" onClick={salvar}>Salvar</button>
            <Link to="/agendamentos"><button type="button">Voltar</button></Link>
          </div>
        </section>

        <section className="painel-calendario">
          <h3 style={{ marginTop: "1%" }}>BENEFICIARIOS</h3>

          <input
            type="text"
            placeholder="Pesquisar beneficiario..."
            value={busca}
            onChange={(e) => setBusca(e.target.value)}
            style={{
              width: "50%",
              padding: "8px",
              marginBottom: "10px"
            }}
          />

          {beneficiariosFiltrados.map((b) => (
            <div key={b.id} className="item-agendamento item-presenca">
              <div>
                <strong>{b.nome}</strong>
                <div>CPF: {b.cpf}</div>
                <div>{presencas[Number(b.id)] ? "Presente" : "Falta"}</div>
              </div>

              <label className="presenca-checkbox">
                <span>Presente</span>
                <input
                  type="checkbox"
                  checked={!!presencas[Number(b.id)]}
                  onChange={() => togglePresenca(b.id)}
                />
              </label>
            </div>
          ))}
        </section>
      </div>
    </div>
  );
}

export default PresencaBeneficiario;
