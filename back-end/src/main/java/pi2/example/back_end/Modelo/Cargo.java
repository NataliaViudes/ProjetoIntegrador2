package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import pi2.example.back_end.DAO.DAOCargo;
import pi2.example.back_end.db.Conexao;

import java.util.List;

@JsonPropertyOrder({"id", "nome", "nivelAcesso"})
public class Cargo {
    private Integer id;
    private String nome;
    private Integer nivelAcesso;

    public Cargo() {
    }

    public Cargo(int id) {
        this.id = id;
        this.nome="";
        this.nivelAcesso = 1;
    }

    public Cargo(String nome, Integer nivelAcesso) {
        this.id =0;
        this.nome=nome;
        this.nivelAcesso=nivelAcesso;
    }

    public Cargo(int id, String nome, Integer nivelAcesso) {
        this.id = id;
        this.nome = nome;
        this.nivelAcesso = nivelAcesso;
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

    public Integer getNivelAcesso() {
        return nivelAcesso;
    }
    public void setNivelAcesso(Integer nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
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

    public List<Cargo> buscarPorNome(String nome,Conexao con)
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