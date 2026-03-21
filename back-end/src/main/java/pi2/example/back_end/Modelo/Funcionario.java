package pi2.example.back_end.Modelo;

import java.util.Date;

public class Funcionario {
    private Integer id;
    private String nome;
    private String cpf;
    private String telefone;
    private String nis;
    private Date nascimento;
    private String sexo;
    private String endereco;
    private String cargo;

    public Funcionario() {}
    public Funcionario(Integer id, String nome, String cpf, String telefone, String NIS, Date nascimento, String sexo, String endereco, String cargo) {
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
