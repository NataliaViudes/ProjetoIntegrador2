package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAORemedio;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Remedio {

    private Integer id;
    private String nome;
    private String descricao;

    public Remedio() {
    }

    public Remedio(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Remedio(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // =====================================================
    // GETTERS E SETTERS
    // =====================================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // =====================================================
    // VALIDAÇÃO
    // =====================================================

    public String validar() {

        String erros = "";

        if (nome == null || nome.trim().isEmpty())
            erros += "Nome inválido\n";

        if (descricao == null || descricao.trim().isEmpty())
            erros += "Descrição inválida\n";

        return erros;
    }

    // =====================================================
    // DAO
    // =====================================================

    public List<Remedio> listar(Conexao con) {
        DAORemedio dao = new DAORemedio(con);
        return dao.listar();
    }

    public Remedio buscarPorId(Conexao con, int id) {
        DAORemedio dao = new DAORemedio(con);
        return dao.buscarPorId(id);
    }

    public List<Remedio> buscarPorNome(Conexao con, String nome) {
        DAORemedio dao = new DAORemedio(con);
        return dao.buscarPorNome(nome);
    }

    public List<Remedio> buscarPorDescricao(Conexao con, String descricao) {
        DAORemedio dao = new DAORemedio(con);
        return dao.buscarPorDescricao(descricao);
    }

    public Remedio incluir(Conexao con) {
        DAORemedio dao = new DAORemedio(con);
        return dao.gravar(this);
    }

    public Remedio alterar(Conexao con) {
        DAORemedio dao = new DAORemedio(con);
        return dao.alterar(this);
    }

    public Boolean apagar(Conexao con) {
        DAORemedio dao = new DAORemedio(con);
        return dao.apagar(this);
    }
}