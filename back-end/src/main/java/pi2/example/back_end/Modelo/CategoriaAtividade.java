package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CategoriaAtividadeDAOImpl;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class CategoriaAtividade {
    private Integer id;
    private String nome;

    public CategoriaAtividade() {}

    public CategoriaAtividade(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CategoriaAtividade(Integer id) {
        this.id = id;
        this.nome = "";
    }

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

    public CategoriaAtividade incluir(Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.gravar(this);
    }

    public CategoriaAtividade alterar(Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.apagar(this);
    }

    public CategoriaAtividade buscarPorId(Integer id, Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.get(id);
    }

    public List<CategoriaAtividade> buscarPorNome(String nome, Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.buscarPorNome(nome);
    }

    public List<CategoriaAtividade> buscarTodos(Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.getAll();
    }

    public List<CategoriaAtividade> buscarComFiltro(String filtro, Conexao con) {
        CategoriaAtividadeDAOImpl dao = new CategoriaAtividadeDAOImpl(con);
        return dao.get(filtro);
    }
}
