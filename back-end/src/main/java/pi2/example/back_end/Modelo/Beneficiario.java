package pi2.example.back_end.Modelo;

public class Beneficiario {

    private int id;
    private String nome;
    private String nascimento;
    private int idade;
    private String rg;
    private String cpf;
    private String nis;
    private double renda;
    private String endereco;
    private String bairro;
    private String tipoResidencia;
    private String telefone;
    private String celular;
    private String celularRecado;
    private Beneficiario beneficiario;
    //infos gerais - fazer
    private String alergias;
    private String tratamentos;
    private String medicamentos;
    private String situacao;


    public Beneficiario(int id, String nome, String nascimento, int idade, String rg, String cpf, String nis, double renda, String endereco, String bairro,
                        String tipoResidencia, String telefone, String celular, String celularRecado, Beneficiario beneficiario, String alergias, String tratamentos,
                        String medicamentos, String situacao) {
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
        this.beneficiario = beneficiario;
        this.alergias = alergias;
        this.tratamentos = tratamentos;
        this.medicamentos = medicamentos;
        this.situacao = situacao;
    }



    public int getId() {
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

    public int getIdade() {
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

    public double getRenda() {
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

    public Beneficiario getResponsavel() {
        return beneficiario;
    }

    public void setResponsavel(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
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

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
