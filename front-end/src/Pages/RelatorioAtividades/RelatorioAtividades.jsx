import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "./RelatorioAtividades.css";

function RelatorioAtividades() {
  const [status, setStatus] = useState("TODAS");
  const [idCategoria, setIdCategoria] = useState("");
  const [categorias, setCategorias] = useState([]);
  const [atividades, setAtividades] = useState([]);

  useEffect(() => {
    carregarCategorias();
  }, []);

  async function carregarCategorias() {
    try {
      const resp = await api.get("/categoriaAtividade");
      setCategorias(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar categorias:", error);
      setCategorias([]);
    }
  }

  async function buscarRelatorio() {
    try {
      const params = new URLSearchParams();

      if (status && status !== "TODAS") {
        params.append("status", status);
      }

      if (idCategoria) {
        params.append("idCategoria", idCategoria);
      }

      const resp = await api.get(`/atividades/relatorio?${params.toString()}`);

      setAtividades(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao buscar relatório:", error);
      setAtividades([]);
    }
  }

  function limparFiltros() {
    setStatus("TODAS");
    setIdCategoria("");
    setAtividades([]);
  }

  function imprimirRelatorio() {
    window.print();
  }

  return (
    <div className="pagina-relatorio-atividades" translate="no">
      <Menu />

      <main className="conteudo-relatorio-atividades">
        <section className="painel-filtros area-nao-imprimir">
          <h2>Relatório de Atividades</h2>

          <label>Status</label>
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="TODAS">Todas</option>
            <option value="ATIVA">Ativas</option>
            <option value="ENCERRADA">Encerradas</option>
          </select>

          <label>Categoria de atividade</label>
          <select
            value={idCategoria}
            onChange={(e) => setIdCategoria(e.target.value)}
          >
            <option value="">Todas</option>

            {categorias.map((categoria) => (
              <option key={categoria.id} value={categoria.id}>
                {categoria.nome}
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

            <button type="button" onClick={imprimirRelatorio}>
              Gerar para impressão
            </button>
          </div>
        </section>

        <section className="painel-resultados area-impressao">
          <h2>Relatório de Atividades</h2>

          <div className="resumo-relatorio">
            <p>
              <strong>Status:</strong> {status}
            </p>

            <p>
              <strong>Categoria:</strong>{" "}
              {idCategoria
                ? categorias.find((c) => String(c.id) === String(idCategoria))
                    ?.nome
                : "Todas"}
            </p>

            <p>
              <strong>Total encontrado:</strong> {atividades.length}
            </p>
          </div>

          {atividades.length === 0 ? (
            <p>Nenhuma atividade encontrada.</p>
          ) : (
            <table className="tabela-relatorio">
              <thead>
                <tr>
                  <th>Atividade</th>
                  <th>Categoria</th>
                  <th>Funcionário</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {atividades.map((atividade) => (
                  <tr key={atividade.id}>
                    <td>{atividade.descricao}</td>
                    <td>{atividade.categoria?.nome || "Não informada"}</td>
                    <td>{atividade.funcionario?.nome || "Não informado"}</td>
                    <td>{atividade.statusAtividade || "-"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>
      </main>
    </div>
  );
}

export default RelatorioAtividades;