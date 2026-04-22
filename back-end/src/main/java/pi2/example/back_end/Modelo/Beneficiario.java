package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.BeneficiarioDAOImpl;
import pi2.example.back_end.db.Conexao;

import java.util.List;

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
    private String alergias;
    private String tratamentos;
    private String participacao;
    private String situacao;

    public Beneficiario() {
    }

    public Beneficiario(Integer id, String nome, String nascimento, Integer idade, String rg, String cpf, String nis,
                        Double renda, String endereco, String bairro, String tipoResidencia, String telefone,
                        String celular, String celularRecado, String alergias, String tratamentos,
                        String participacao, String situacao) {
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

    public Beneficiario(Integer id) {
        this.id = id;
        this.nome = "";
        this.nascimento = "";
        this.idade = null;
        this.rg = "";
        this.cpf = "";
        this.nis = "";
        this.renda = null;
        this.endereco = "";
        this.bairro = "";
        this.tipoResidencia = "";
        this.telefone = "";
        this.celular = "";
        this.celularRecado = "";
        this.alergias = "";
        this.tratamentos = "";
        this.participacao = "";
        this.situacao = "";
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


    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
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

    public void setRenda(Double renda) {
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

    public Beneficiario incluir(Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.gravar(this);
    }

    public Beneficiario alterar(Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.apagar(this);
    }

    public Beneficiario buscarPorId(Integer id, Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.get(id);
    }

    public Beneficiario buscarPorCpf(String cpf, Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.getByCpf(cpf);
    }

    public List<Beneficiario> buscarTodos(Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.getAll();
    }

    public List<Beneficiario> buscarComFiltro(String filtro, Conexao con) {
        BeneficiarioDAOImpl dao = new BeneficiarioDAOImpl(con);
        return dao.get(filtro);
    }
}
