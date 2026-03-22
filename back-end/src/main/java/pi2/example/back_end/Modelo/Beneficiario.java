package pi2.example.back_end.Modelo;

public class Beneficiario {

    private Integer id;
    private String nome;
    private String nascimento;
    private Integer idade;
    private String rg;
    private String cpf;
    private String nis;
    private Double renda;
    private String endereco;
    private String bairro;
    private String tipoResidencia;
    private String telefone;
    private String celular;
    private String celularRecado;
    //private Familiares familiares;
    
    //infos gerais - fazer
    private String alergias;
    private String tratamentos;
    //private String medicamentos;
    private String participacao;
    private String situacao;



    public Beneficiario(){

    }


    public Beneficiario(int id, String nome, String nascimento, int idade, String rg, String cpf, String nis, double renda, String endereco, String bairro, String tipoResidencia, String telefone, String celular, String celularRecado, String alergias, String tratamentos, String participacao, String situacao) {
        this.id = id;
        this.nome = nome;
        this.nascimento = nascimento;
        this.idade = idade;
        this.rg = rg;
        this.cpf = cpf;
        this.nis = nis;
        this.renda = renda;
        this.endereco = endereco;
        this.bairro = bairro;
        this.tipoResidencia = tipoResidencia;
        this.telefone = telefone;
        this.celular = celular;
        this.celularRecado = celularRecado;
        this.alergias = alergias;
        this.tratamentos = tratamentos;
        this.participacao = participacao;
        this.situacao = situacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public Double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getTipoResidencia() {
        return tipoResidencia;
    }

    public void setTipoResidencia(String tipoResidencia) {
        this.tipoResidencia = tipoResidencia;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCelularRecado() {
        return celularRecado;
    }

    public void setCelularRecado(String celularRecado) {
        this.celularRecado = celularRecado;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getTratamentos() {
        return tratamentos;
    }

    public void setTratamentos(String tratamentos) {
        this.tratamentos = tratamentos;
    }

    public String getParticipacao() {
        return participacao;
    }

    public void setParticipacao(String participacao) {
        this.participacao = participacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
