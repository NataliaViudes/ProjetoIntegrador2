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
            <Link to="/eventos">Eventos</Link> 
            <Link to="/auxilios">Auxílios</Link>
            <Link to="/beneficiarios">Beneficiários</Link>
            <Link to="/funcionarios">Funcionários</Link>
            <Link to="/cargos">Cargos</Link>
            <Link to="/familiares">Familiares</Link>
          </div>
        </div>

        <button type="button">Vincular</button>

        <div className="menu-dropdown">
          <button type="button">Agendas</button>

          <div className="dropdown-conteudo">
            <Link to="/agendarEventos">Calendario eventos</Link>
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