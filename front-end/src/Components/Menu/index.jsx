import { Link } from "react-router-dom";
import styles from "./styles.module.css";

export default function Menu() {
  return (
    <header className={styles["topo-menu"]}>
      <div className={styles["grupo-botoes"]}>
        
        {/* DROPDOWN CADASTROS */}
        <div className={styles["menu-dropdown"]}>
          <button>Cadastros</button>
          <div className={styles["dropdown-conteudo"]}>
            <Link to="/eventos">Eventos</Link>
            <Link to="/funcionarios">Funcionários</Link>
            <Link to="/cargos">Cargos</Link>
            <Link to="/familiares">Familiares</Link>
          </div>
        </div>

        <button type="button">Vincular</button>
        <button type="button">Agendador</button>
        <button type="button">Relatórios</button>
      </div>

      <div className={styles["area-pesquisa-topo"]}>
        <input type="text" placeholder="Pesquisar..." />
      </div>
    </header>
  );
}