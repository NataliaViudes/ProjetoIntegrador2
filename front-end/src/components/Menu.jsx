function Menu() {
  return (
    <div className="menu">
      <button>Cadastros</button>
      <button>Vincular</button>
      <button>Agendar</button>
      <button>Relatórios</button>

      <div className="menu-direita">
        <input placeholder="Pesquisar..." />
      </div>
    </div>
  );
}

export default Menu;