package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.AtividadeDAOImpl;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Atividade {
    private Integer id;
    private String descricao;
    private CategoriaAtividade categoria;
    private Funcionario funcionario;

    public Atividade() {}

    public Atividade(Integer id, String descricao, CategoriaAtividade categoria, Funcionario funcionario) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
        this.funcionario = funcionario;
    }

    public Atividade(Integer id) {
        this.id = id;
        this.descricao = "";
        this.categoria = null;
        this.funcionario = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaAtividade getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAtividade categoria) {
        this.categoria = categoria;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Atividade incluir(Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.gravar(this);
    }

    public Atividade alterar(Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.apagar(this);
    }

    public Atividade buscarPorId(Integer id, Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.get(id);
    }

    public List<Atividade> buscarPorNome(String nome, Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.buscarPorNome(nome);
    }

    public List<Atividade> buscarTodos(Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.getAll();
    }

    public List<Atividade> buscarComFiltro(String filtro, Conexao con) {
        AtividadeDAOImpl dao = new AtividadeDAOImpl(con);
        return dao.get(filtro);
    }
}
