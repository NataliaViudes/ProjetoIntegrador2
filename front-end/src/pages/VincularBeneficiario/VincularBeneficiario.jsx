import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu";
import "../Agendamentos/Agendamentos.css";
import { useLocation, useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";


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
  const [idAgendamento, setIdAgendamento] = useState(dados.idAgendamento || null);

  const [selecionados, setSelecionados] = useState([]);
  const [busca, setBusca] = useState("");

  useEffect(() => {
    async function init() {
      try {
        const [respAtividades, respBeneficiarios] = await Promise.all([
          api.get("/atividades"),
          api.get("/beneficiarios")
        ]);

        const listaBeneficiarios = Array.isArray(respBeneficiarios.data)
          ? respBeneficiarios.data.map(b => ({ ...b, id: Number(b.id) }))
          : [];

        setAtividades(Array.isArray(respAtividades.data) ? respAtividades.data : []);
        setBeneficiarios(listaBeneficiarios);

        if (dados.idAgendamento) {
          setModoEdicao(true);
          setIdAgendamento(dados.idAgendamento);

          const resp = await api.get(`/vincularBeneficiario/${dados.idAgendamento}`);

          const ids = resp.data.map(item => Number(item.idBeneficiario));
          setSelecionados(ids);
        }

      } catch (e) {
        console.error("Erro ao carregar:", e);
      }
    }

    init();
  }, []);

  function toggleSelecionado(id) {
    const idNum = Number(id);

    setSelecionados(prev =>
      prev.includes(idNum)
        ? prev.filter(b => b !== idNum)
        : [...prev, idNum]
    );
  }

  function desselecionarTodos() {
    setSelecionados([]);
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

      const novoId = respAg.data.id;

      await api.post("/vincularBeneficiario",
        selecionados.map(id => ({
          idBeneficiario: Number(id),
          idAgendamento: novoId
        }))
      );

      alert("Salvo com sucesso!");
      navigate("/agendamentos");

    } catch (e) {
      console.error(e.response?.data || e);
      alert("Erro ao salvar.");
    }
  }

  async function alterar() {
    if (selecionados.length === 0) {
      alert("Selecione beneficiários.");
      return;
    }

    try {
      await api.delete(`/vincularBeneficiario/${idAgendamento}`);

      await api.post("/vincularBeneficiario",
        selecionados.map(id => ({
          idBeneficiario: Number(id),
          idAgendamento: idAgendamento
        }))
      );

      alert("Atualizado com sucesso!");
      navigate("/agendamentos");

    } catch (e) {
      console.error(e.response?.data || e);
      alert("Erro ao atualizar.");
    }
  }

  const beneficiariosFiltrados = beneficiarios.filter(b =>
    b.nome.toLowerCase().includes(busca.toLowerCase())
  );

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
              <>
                <button onClick={alterar}>Atualizar Vínculos</button>

                <button 
                  onClick={desselecionarTodos}
                  style={{ marginLeft: "10px", backgroundColor: "#ccc" }}
                >
                  Limpar Seleção
                </button>
                <Link to={"/agendamentos"}><button>Voltar</button></Link>

              </>
            ) : (
              <>
                <button onClick={salvar}>Vincular</button>

                <button 
                  onClick={desselecionarTodos}
                  style={{ marginLeft: "10px", backgroundColor: "#ccc" }}
                >
                  Limpar Seleção
                </button>
                <Link to={"/agendamentos"}><button>Voltar</button></Link>
              </>
            )}
          </div>
        </section>

        <section className="painel-calendario">
          <h3 style={{
            marginTop: "1%",
          }}>BENEFICIÁRIOS</h3>

          <input
            type="text"
            placeholder="Pesquisar beneficiário..."
            value={busca}
            onChange={(e) => setBusca(e.target.value)}
            style={{
              width: "50%",
              padding: "8px",
              marginBottom: "10px"
            }}
          />

          {beneficiariosFiltrados.map((b) => (
            <div key={b.id} className="item-agendamento">
              <div>
                <strong>{b.nome}</strong>
                <div>CPF: {b.cpf}</div>
                <div>Situação: {b.situacao}</div>
              </div>

              <input
                type="checkbox"
                checked={selecionados.includes(Number(b.id))}
                onChange={() => toggleSelecionado(b.id)}
                style={{
                  transform: "scale(1.5)", 
                  cursor: "pointer"
                }}
              />
            </div>
          ))}
        </section>

      </div>
    </div>
  );
}

export default VincularBeneficiario;