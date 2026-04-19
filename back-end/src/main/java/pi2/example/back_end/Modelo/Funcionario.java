package pi2.example.back_end.Modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import pi2.example.back_end.DAO.DAOCargo;
import pi2.example.back_end.DAO.DAOFuncionario;
import pi2.example.back_end.db.Conexao;

import java.util.Date;
import java.util.List;

public class Funcionario {
    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;
    private String nis;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nascimento;
    private String sexo;
    private String endereco;
    private Cargo cargo;

    public Funcionario() {}
    public Funcionario(Integer id, String nome, String cpf, String telefone, String NIS, Date nascimento, String sexo, String endereco, Cargo cargo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.nis = NIS;
        this.nascimento = nascimento;
        this.sexo = sexo;
        this.endereco = endereco;
        this.cargo = cargo;
    }
    public Funcionario(Integer id) {
        this.id = id;
        this.nome = "";
        this.cpf = "";
        this.telefone = "";
        this.nis = "";
        this.nascimento = null;
        this.sexo = "";
        this.endereco = "";
        this.cargo = null;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Cargo getCargo() {
        return cargo;
    }
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }


    public Funcionario incluir(Conexao con)
    {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.gravar(this);
    }

    public Funcionario alterar(Conexao con)
    {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con)
    {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.apagar(this);
    }

    public Funcionario buscarporId(Integer id,Conexao con)
    {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.get(id);
    }

    public List<Funcionario> buscarPorNome(String nome, Conexao con)
    {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.buscarPorNome(nome);
    }

    public List<Funcionario> buscarTodos(Conexao con) {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.getAll();
    }

    public List<Funcionario> buscarComFiltro(String filtro, Conexao con) {
        DAOFuncionario dao = new DAOFuncionario(con);
        return dao.get(filtro);
    }
}
