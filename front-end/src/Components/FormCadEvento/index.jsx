import React, { useState } from "react";
import styles from "./styles.module.css";

export function FormCadEvento() {
  const [categoria, setCategoria] = useState("");
  const [descricao, setDescricao] = useState("");
  const [tipos, setTipos] = useState(["", "", ""]);

  const handleTipoChange = (index, value) => {
    const novosTipos = [...tipos];
    novosTipos[index] = value;
    setTipos(novosTipos);
  };

  const cadastrar = () => {
    const data = {
      categoria,
      descricao,
      tipos,
    };

    console.log("Enviando:", data);

    // aqui você conecta com seu backend (Spring Boot)
    // api.post('/evento', data)
  };

  return (
    <div className={styles.container}>
      <h2>Gerenciar Eventos</h2>

      <div className={styles.tabs}>
        <button>Cadastros</button>
        <button>Vincular</button>
        <button>Agendar</button>
        <button>Relatórios</button>

        <div className={styles.searchTop}>
          <input placeholder="Pesquisar" />
          <span>🏠</span>
        </div>
      </div>

      <div className={styles.formRow}>
        <input
          className={styles.input}
          placeholder="Tipo do Evento"
          value={categoria}
          onChange={(e) => setCategoria(e.target.value)}
        />

        <input
          className={styles.input}
          placeholder="Descrição"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
        />
      </div>

      <div className={styles.listaBox}>
        <div className={styles.listaHeader}>
          <span>Tipos de evento existentes</span>
          <input placeholder="Pesquisar" />
        </div>

        {tipos.map((tipo, index) => (
          <input
            key={index}
            className={styles.inputLista}
            placeholder="Tipo do evento..."
            value={tipo}
            onChange={(e) => handleTipoChange(index, e.target.value)}
          />
        ))}
      </div>

      <button className={styles.btnCadastrar} onClick={cadastrar}>
        Cadastrar
      </button>
    </div>
  );
}