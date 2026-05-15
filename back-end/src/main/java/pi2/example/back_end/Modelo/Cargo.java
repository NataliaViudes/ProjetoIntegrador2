package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pi2.example.back_end.DAO.DAOCargo;
import pi2.example.back_end.db.Conexao;

import java.util.List;

@JsonPropertyOrder({ "id", "nome"})
public class Cargo {
    private Integer id;
    private String nome;

    public Cargo() {
    }

    public Cargo(int id) {
        this.id = id;
        this.nome="";
    }

    public Cargo(String nome) {
        this.id =0;
        this.nome=nome;
    }

    public Cargo(int id, String nome) {
        this.id = id;
        this.nome=nome;
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

    public Cargo incluir(Conexao con)
    {
        DAOCargo dao = new DAOCargo(con);
        return dao.gravar(this);
    }

    public Cargo alterar(Conexao con)
    {
        DAOCargo dao = new DAOCargo(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con)
    {
        DAOCargo dao = new DAOCargo(con);
        return dao.apagar(this);
    }

    public Cargo buscarporId(Integer id,Conexao con)
    {
        DAOCargo dao = new DAOCargo(con);
        return dao.get(id);
    }

    public List<Cargo> buscarPorNome(String nome, Conexao con)
    {
        DAOCargo dao = new DAOCargo(con);
        return dao.buscarPorNome(nome);
    }

    public List<Cargo> buscarTodos(Conexao con) {
        DAOCargo dao = new DAOCargo(con);
        return dao.getAll();
    }

    public List<Cargo> buscarComFiltro(String filtro, Conexao con) {
        DAOCargo dao = new DAOCargo(con);
        return dao.buscarPorNome(filtro);
    }
}
