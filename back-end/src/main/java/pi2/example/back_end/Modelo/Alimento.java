package pi2.example.back_end.Modelo;

import org.jspecify.annotations.Nullable;
import pi2.example.back_end.DAO.DAOAlimento;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Alimento {
    private Integer id;
    private String nome;
    private String tipo;
    private String descricao;


    //os tipos vão ser: Salgado, Doce, Bebida


    public Alimento(Integer id) {
        this.id = id;
    }

    public Alimento() {
        this(0, "", "", "");
    }

    public Alimento( Integer id, String nome, String tipo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }





    public List<Alimento> buscarPorDescricao(Conexao db, String descricao) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.buscarPorDescricao(descricao);
    }

    public List<Alimento> buscarPorTipo(Conexao db, String tipo) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.buscarPorTipo(tipo);
    }

    public Alimento buscarPorId(Conexao db, int id) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.getPorId(id);
    }

    public Alimento incluir(Conexao db) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.gravar(this);
    }

    public @Nullable Object alterar(Conexao db) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao db) {
        DAOAlimento dao = new DAOAlimento(db);
        return dao.apagar(this);
    }
}