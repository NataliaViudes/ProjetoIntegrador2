import { useState, useEffect, useMemo } from "react";

export default function CampoFiltro({
  listaDados = [],
  listaFiltros = [],
  onChange,
  filtroDefault = "",
  ordemDefault = "asc",
  style,
}) {
  const [filtro, setFiltro] = useState(filtroDefault);
  const [busca, setBusca] = useState("");
  const [ordem, setOrdem] = useState(ordemDefault);

  function toggleOrdem() {
    setOrdem((prev) => (prev === "asc" ? "desc" : "asc"));
  }

  const dadosFiltrados = useMemo(() => {
    return listaDados
      .filter((item) => {
        if (!filtro) return true;

        const valor = (item[filtro] || "").toString().toLowerCase();

        if (!busca) return true;

        return valor.includes(busca.toLowerCase());
      })
      .sort((a, b) => {
        if (!filtro) return 0;

        const valorA = (a[filtro] || "").toString().toLowerCase();
        const valorB = (b[filtro] || "").toString().toLowerCase();

        return ordem === "asc"
          ? valorA.localeCompare(valorB)
          : valorB.localeCompare(valorA);
      });
  }, [listaDados, filtro, busca, ordem]);

  useEffect(() => {
    onChange && onChange(dadosFiltrados);
  }, [dadosFiltrados]); 

  return (
    <div className={style["filtro-box"]}>
      <span>Filtros:</span>

      <select value={filtro} onChange={(e) => setFiltro(e.target.value)}>
        <option value="">Selecione</option>

        {listaFiltros.map((item) => (
          <option key={item.value} value={item.value}>
            {item.label}
          </option>
        ))}
      </select>

      <input
        type="text"
        placeholder="Digite para filtrar"
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        disabled={!filtro}
      />

      <button
        type="button"
        onClick={toggleOrdem}
        className={style["btn-ordem"]}
      >
        {ordem === "asc" ? "⬇️" : "⬆️"}
      </button>
    </div>
  );
}
