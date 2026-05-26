import { useEffect, useState } from "react";
import api from "../../services/api";
import Menu from "../../components/Menu/Menu";
import "./RelatorioEstoque.css";

function RelatorioEstoque() {
  const [tipos, setTipos] = useState([]);
  const [materiais, setMateriais] = useState([]);
  const [itens, setItens] = useState([]);

  const [modoBusca, setModoBusca] = useState("MATERIAL");

  const [idTipo, setIdTipo] = useState("");
  const [idMaterial, setIdMaterial] = useState("Todos");

  const [filtroQtd, setFiltroQtd] = useState("Todos");

  useEffect(() => {
    carregarTipos();
    carregarMateriais();
  }, []);

  async function carregarTipos() {
    try {
      const resp = await api.get("/tipo-estoque/tipo");
      setTipos(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar categorias:", error);
      setTipos([]);
    }
  }

  async function carregarMateriais() {
    try {
      const resp = await api.get("/estoque/descricao");

      const dados = Array.isArray(resp.data) ? resp.data : [];

      dados.sort((a, b) =>
        a.descricao.localeCompare(b.descricao, "pt-BR", {
          sensitivity: "base",
        })
      );

      setMateriais(dados);
    } catch (error) {
      console.error("Erro ao carregar materiais:", error);
      setMateriais([]);
    }
  }

  function alterarModoBusca(valor) {
    setModoBusca(valor);
    setIdTipo("");
    setItens([]);

    if (valor === "MATERIAL") {
      setIdMaterial("Todos");
      setFiltroQtd("Todos");
    } else {
      setIdMaterial("");
    }
  }

  function selecionarMaterial(id) {
    setIdMaterial(id);

    if (id === "Todos") {
      setIdTipo("");
      return;
    }

    if (!id) {
      setIdTipo("");
      return;
    }

    const material = materiais.find((m) => String(m.id) === String(id));

    if (material?.tipo?.id) {
      setIdTipo(String(material.tipo.id));
    }
  }

  async function buscarRelatorio() {
    try {
      let resp;

      if (modoBusca === "MATERIAL") {

        if (idMaterial === "TODOS" || idMaterial === "Todos") {

          const dados = [...materiais];

          dados.sort((a, b) =>
            a.descricao.localeCompare(b.descricao, "pt-BR", {
              sensitivity: "base",
            })
          );

          setItens(dados);
          return;
        }

        if (!idMaterial) {
          alert("Selecione um material.");
          return;
        }

        resp = await api.get(`/estoque/${idMaterial}`);

        const dados = Array.isArray(resp.data)
          ? resp.data
          : [resp.data];

        dados.sort((a, b) =>
          a.descricao.localeCompare(b.descricao, "pt-BR", {
            sensitivity: "base",
          })
        );

        setItens(dados);
      }

      if (modoBusca === "CATEGORIA") {
        if (!idTipo) {
          alert("Selecione uma categoria.");
          return;
        }

        const tipoSelecionado = tipos.find(
          (t) => String(t.id) === String(idTipo)
        );

        resp = await api.get(
          `/estoque/tipo?tipo=${tipoSelecionado.tipo}`
        );

        const dados = Array.isArray(resp.data) ? resp.data : [];

        dados.sort((a, b) =>
          a.descricao.localeCompare(b.descricao, "pt-BR", {
            sensitivity: "base",
          })
        );

        setItens(dados);
      }
    } catch (error) {
      console.error("Erro ao buscar relatório:", error);
      setItens([]);
    }
  }

  function limparFiltros() {
    setIdTipo("");

    if (modoBusca === "MATERIAL") {
      setIdMaterial("Todos");
      setFiltroQtd("Todos");
    } else {
      setIdMaterial("");
      setFiltroQtd("Todos");
    }

    setItens([]);
  }

  function imprimirRelatorio() {
    window.print();
  }

  const materiaisDaCategoria = materiais.filter(
    (m) => String(m.tipo?.id) === String(idTipo)
  );

  const itensFiltrados = itens.filter((item) => {
    if (modoBusca === "MATERIAL") return true;

    if (filtroQtd === "Todos") return true;
    if (filtroQtd === "Sem_estoque") return item.qtd === 0;
    if (filtroQtd === "Com_estoque") return item.qtd > 0;

    return true;
  });

  return (
    <div className="pagina-relatorio-estoque" translate="no">
      <Menu />

      <main className="conteudo-relatorio-estoque">

        <section className="painel-filtros area-nao-imprimir">
          <h2>Relatório de Estoque</h2>

          <label>Modo de busca</label>

          <select
            value={modoBusca}
            onChange={(e) => alterarModoBusca(e.target.value)}
          >
            <option value="CATEGORIA">Buscar por categoria</option>
            <option value="MATERIAL">Buscar por material</option>
          </select>

          <label>Filtro de estoque</label>

          <select
            value={filtroQtd}
            onChange={(e) => setFiltroQtd(e.target.value)}
            disabled={modoBusca === "MATERIAL"}
          >
            <option value="Todos">Todos</option>

            {modoBusca === "CATEGORIA" && (
              <>
                <option value="Sem_estoque">Apenas sem estoque</option>

                <option value="Com_estoque">
                  Apenas com estoque
                </option>
              </>
            )}
          </select>

          {modoBusca === "CATEGORIA" && (
            <>
              <label>Categoria</label>

              <select
                value={idTipo}
                onChange={(e) => setIdTipo(e.target.value)}
              >
                <option value="">Selecione</option>

                {tipos.map((tipo) => (
                  <option key={tipo.id} value={tipo.id}>
                    {tipo.tipo}
                  </option>
                ))}
              </select>

              <label>Materiais</label>

              <input
                type="text"
                readOnly
                value={
                  idTipo
                    ? `Materiais encontrados: ${materiaisDaCategoria.length}`
                    : ""
                }
              />
            </>
          )}

          {modoBusca === "MATERIAL" && (
            <>
              <label>Material</label>

              <select
                value={idMaterial}
                onChange={(e) => selecionarMaterial(e.target.value)}
              >
                <option value="TODOS">Todos</option>

                {materiais.map((material) => (
                  <option key={material.id} value={material.id}>
                    {material.descricao}
                  </option>
                ))}
              </select>

              <label>Categoria</label>

              <input
                type="text"
                readOnly
                value={
                  idMaterial === "TODOS" || idMaterial === "Todos"
                    ? "Todas"
                    : tipos.find((t) => String(t.id) === String(idTipo))
                        ?.tipo || ""
                }
              />
            </>
          )}

          <div className="acoes-formulario">
            <button onClick={buscarRelatorio}>Buscar</button>

            <button onClick={limparFiltros}>Limpar</button>

            <button onClick={imprimirRelatorio}>
              Imprimir
            </button>
          </div>
        </section>

        <section className="painel-resultados area-impressao">
          <h2>Relatório de Estoque</h2>

          <div className="resumo-relatorio">
            <p>
              <strong>Modo:</strong>{" "}
              {modoBusca === "MATERIAL"
                ? "Busca por material"
                : "Busca por categoria"}
            </p>

            <p>
              <strong>Total:</strong>{" "}
              {itensFiltrados.length}
            </p>

            <p>
              <strong>Filtro:</strong>{" "}
              {filtroQtd === "Todos"
                ? "Todos"
                : filtroQtd === "Sem_estoque"
                ? "Sem estoque"
                : filtroQtd === "Com_estoque"
                ? "Com estoque"
                : filtroQtd}
            </p>
          </div>

          {itensFiltrados.length === 0 ? (
            <p>Nenhum item encontrado.</p>
          ) : (
            <table className="tabela-relatorio">
              <thead>
                <tr>
                  <th>Descrição</th>
                  <th>Quantidade</th>
                  <th>Categoria</th>
                </tr>
              </thead>

              <tbody>
                {itensFiltrados.map((item) => (
                  <tr key={item.id}>
                    <td>{item.descricao}</td>
                    <td>{item.qtd}</td>
                    <td>{item.tipo?.tipo || "Não informada"}</td>
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

export default RelatorioEstoque;