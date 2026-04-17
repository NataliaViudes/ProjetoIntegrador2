package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOFamiliar;
import pi2.example.back_end.DAO.DAOFuncionario;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Familiar {
    private Integer id;
    private String nome;
    private String parentesco;
    private String profissao;
    private double renda;
    private String telefone;

    public Familiar(){}
    public Familiar(Integer id, String nome, String parentesco, String profissao, double renda, String telefone) {
        this.id = id;
        this.nome = nome;
        this.parentesco = parentesco;
        this.profissao = profissao;
        this.renda = renda;
        this.telefone = telefone;
    }
    public Familiar(Integer id) {
        this.id = id;
        this.nome = "";
        this.parentesco = "";
        this.profissao = "";
        this.renda = 0;
        this.telefone = "";
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

    public String getParentesco() {
        return parentesco;
    }
    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getProfissao() {
        return profissao;
    }
    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public double getRenda() {
        return renda;
    }
    public void setRenda(double renda) {
        this.renda = renda;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Familiar incluir(Conexao con)
    {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.gravar(this);
    }

    public Familiar alterar(Conexao con)
    {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con)
    {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.apagar(this);
    }

    public Familiar buscarporId(Integer id,Conexao con)
    {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.get(id);
    }

    public List<Familiar> buscarPorNome(String nome, Conexao con)
    {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.buscarPorNome(nome);
    }

    public List<Familiar> buscarTodos(Conexao con) {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.getAll();
    }

    public List<Familiar> buscarComFiltro(String filtro, Conexao con) {
        DAOFamiliar dao = new DAOFamiliar(con);
        return dao.buscarPorNome(filtro);
    }
}
