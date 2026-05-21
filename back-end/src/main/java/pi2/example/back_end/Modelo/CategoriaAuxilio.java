package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CatAuxilioDAOImpl;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class CategoriaAuxilio {
    private Integer id;
    private String nome;

    public CategoriaAuxilio() {
    }

    public CategoriaAuxilio(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public CategoriaAuxilio(Integer id) {
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

    public CategoriaAuxilio buscarPorId(Integer id, Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.get(id);
    }

    public List<CategoriaAuxilio> buscarTodos(Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.getAll();
    }

    public List<CategoriaAuxilio> buscarComFiltro(String filtro, Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.get(filtro);
    }

    public List<CategoriaAuxilio> buscarPorNome(String nome, Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.buscarPorNome(nome);
    }

    public CategoriaAuxilio incluir(Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.gravar(this);
    }

    public CategoriaAuxilio alterar(Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        CatAuxilioDAOImpl dao = new CatAuxilioDAOImpl(con);
        return dao.apagar(this);
    }
}
