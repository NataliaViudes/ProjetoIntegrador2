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
  const [idMaterial, setIdMaterial] = useState("");

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
      setMateriais(Array.isArray(resp.data) ? resp.data : []);
    } catch (error) {
      console.error("Erro ao carregar materiais:", error);
      setMateriais([]);
    }
  }

  function alterarModoBusca(valor) {
    setModoBusca(valor);

    setIdTipo("");
    setIdMaterial("");
    setItens([]);
  }

  function selecionarMaterial(id) {
    setIdMaterial(id);

    if (!id) {
      setIdTipo("");
      return;
    }

    const material = materiais.find(
      (m) => String(m.id) === String(id)
    );

    if (material?.tipo?.id) {
      setIdTipo(String(material.tipo.id));
    }
  }

  async function buscarRelatorio() {
  try {
    let resp;

    // BUSCA POR MATERIAL
    if (modoBusca === "MATERIAL") {

      if (!idMaterial) {
        alert("Selecione um material.");
        return;
      }

      resp = await api.get(`/estoque/${idMaterial}`);

      const dados = Array.isArray(resp.data)
        ? resp.data
        : [resp.data];

      setItens(dados);
    }

    // BUSCA POR CATEGORIA
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

      const dados = Array.isArray(resp.data)
        ? resp.data
        : [];

      setItens(dados);
    }

  } catch (error) {
    console.error("Erro ao buscar relatório:", error);
    setItens([]);
  }
}
  function limparFiltros() {
    setIdTipo("");
    setIdMaterial("");
    setItens([]);
  }

  function imprimirRelatorio() {
    window.print();
  }

  const materiaisDaCategoria = materiais.filter(
    (m) =>
      String(m.tipo?.id) === String(idTipo)
  );

  return (
    <div className="pagina-relatorio-estoque" translate="no">
      <Menu />

      <main className="conteudo-relatorio-estoque">

        <section className="painel-filtros area-nao-imprimir">
          <h2>Relatório de Estoque</h2>

          <label>Modo de busca</label>

          <select
            value={modoBusca}
            onChange={(e) =>
              alterarModoBusca(e.target.value)
            }
          >
            <option value="MATERIAL">
              Buscar por material
            </option>

            <option value="CATEGORIA">
              Buscar por categoria
            </option>
          </select>

          {/* BUSCA POR MATERIAL */}

          {modoBusca === "MATERIAL" && (
            <>
              <label>Material</label>

              <select
                value={idMaterial}
                onChange={(e) =>
                  selecionarMaterial(e.target.value)
                }
              >
                <option value="">Selecione</option>

                {materiais.map((material) => (
                  <option
                    key={material.id}
                    value={material.id}
                  >
                    {material.descricao}
                  </option>
                ))}
              </select>

              <label>Categoria</label>

              <input
                type="text"
                readOnly
                value={
                  tipos.find(
                    (t) =>
                      String(t.id) === String(idTipo)
                  )?.tipo || ""
                }
              />
            </>
          )}

          {/* BUSCA POR CATEGORIA */}

          {modoBusca === "CATEGORIA" && (
            <>
              <label>Categoria</label>

              <select
                value={idTipo}
                onChange={(e) =>
                  setIdTipo(e.target.value)
                }
              >
                <option value="">Selecione</option>

                {tipos.map((tipo) => (
                  <option
                    key={tipo.id}
                    value={tipo.id}
                  >
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
                    ? `Todos os materiais da categoria (${materiaisDaCategoria.length})`
                    : ""
                }
              />
            </>
          )}

          <div className="acoes-formulario">
            <button
              type="button"
              onClick={buscarRelatorio}
            >
              Buscar
            </button>

            <button
              type="button"
              onClick={limparFiltros}
            >
              Limpar
            </button>

            <button
              type="button"
              onClick={imprimirRelatorio}
            >
              Gerar para impressão
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
              <strong>Total encontrado:</strong>{" "}
              {itens.length}
            </p>
          </div>

          {itens.length === 0 ? (
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
                {itens.map((item) => (
                  <tr key={item.id}>
                    <td>{item.descricao}</td>

                    <td>{item.qtd}</td>

                    <td>
                      {item.tipo?.tipo ||
                        "Não informada"}
                    </td>
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