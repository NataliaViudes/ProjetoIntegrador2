import { Link } from "react-router-dom";
import "./Menu.css";

function Menu() {

  return (
    <header className="topo-menu">

      {/* HOME */}
      <div className="icone-home">
        <Link to="/pagina-inicial">

          <svg
            width="20"
            height="20"
            viewBox="0 0 30 30"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M27.9167 30H20.4167C19.2658 30 18.3333 29.1392 18.3333 28.0769V21.1538C18.3333 20.3038 17.5875 19.6154 16.6667 19.6154H13.3333C12.4125 19.6154 11.6667 20.3038 11.6667 21.1538V28.0769C11.6667 29.1392 10.7342 30 9.58333 30H2.08333C0.9325 30 0 29.1392 0 28.0769V13.3946C0 11.6262 0.878334 9.95539 2.3825 8.86154L14.2258 0.246923C14.68 -0.0823077 15.32 -0.0823077 15.7733 0.246923L27.6183 8.86154C29.1225 9.95539 30 11.6254 30 13.3931V28.0769C30 29.1392 29.0675 30 27.9167 30Z"
              fill="white"
            />
          </svg>

        </Link>
      </div>

      {/* MENU CENTRAL */}
      <div className="grupo-botoes">

        {/* CADASTROS */}
        <div className="menu-dropdown">

          <button type="button">
            Cadastros
          </button>

          <div className="dropdown-conteudo">

            <Link to="/alimentos">Alimentos</Link>

            <Link to="/atividades">Atividades</Link>

            <Link to="/eventos">Eventos</Link>

            <Link to="/auxilios">Auxílios</Link>

            <Link to="/beneficiarios">Beneficiários</Link>

            <Link to="/funcionarios">Funcionários</Link>

            <Link to="/cargos">Cargos</Link>

            <Link to="/estoque">Estoque</Link>

            <Link to="/tipo-estoque">
              Tipo de Estoque
            </Link>

            <Link to="/categoriaAtividade">
              Categoria de Atividade
            </Link>

            <Link to="/categoriaAuxilio">
              Categoria Auxílio
            </Link>

          </div>
        </div>

        {/* AGENDAS */}
        <div className="menu-dropdown">

          <button type="button">
            Agendas
          </button>

          <div className="dropdown-conteudo">

            <Link to="/agendamentos">
              Calendário Atividades
            </Link>

            <Link to="/agendarEventos">
              Calendário Eventos
            </Link>

            <Link to="/cardapio">
              Agendar Cardápio
            </Link>

          </div>
        </div>

        {/* VINCULAR */}
        <div className="menu-dropdown">

          <button type="button">
            Vincular
          </button>

          <div className="dropdown-conteudo">

            <Link to="/vincular">
              Vincular Beneficiário
            </Link>

          </div>
        </div>

        {/* OCORRÊNCIAS */}
        <Link to="/ocorrencias">
          <button type="button">
            Ocorrências
          </button>
        </Link>

        {/* RELATÓRIOS */}
        <div className="menu-dropdown">

          <button type="button">
            Relatórios
          </button>

          <div className="dropdown-conteudo">

            <Link to="/relatorioOcorrencias">
              Relatório de Ocorrências
            </Link>

            <Link to="/relatorioFaltas">
              Relatorio de Faltas
            </Link>

            <Link to="/funcionarios">
              Relatório de Funcionários
            </Link>

            <Link to="/estoque">
              Relatório de Estoque
            </Link>
            <Link to="/relatorioAtividades">
              Relatório de Atividades
            </Link>

          </div>
        </div>

      </div>

      {/* PESQUISA */}
      <div className="area-pesquisa-topo">

        <input
          type="text"
          placeholder="Pesquisar..."
        />

      </div>

    </header>
  );
}

export default Menu;
