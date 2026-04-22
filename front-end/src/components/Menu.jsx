import { Link } from "react-router-dom";
import "./Menu.css";

function Menu() {
  return (
    <header className="topo-menu">
      <div className="grupo-botoes">
        <div className="menu-dropdown">
          <button type="button">Cadastros</button>

          <div className="dropdown-conteudo">
            <Link to="/atividades">Atividades</Link>
            <Link to="/auxilios">Auxílios</Link>
            <Link to="/funcionario">Funcionários</Link>
          </div>
        </div>

        <button type="button">Vincular</button>

        <div className="menu-dropdown">
          <button type="button">Agendador</button>

          <div className="dropdown-conteudo">
            <Link to="/agendamentos">Calendário</Link>
          </div>
        </div>

        <button type="button">Relatórios</button>
      </div>

      <div className="area-pesquisa-topo">
        <input type="text" placeholder="Pesquisar..." />
      </div>
    </header>
  );
}


export default Menu;