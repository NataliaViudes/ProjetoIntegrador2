package pi2.example.back_end.Modelo;

public class RelatorioFaltaBeneficiario {
    private Integer idAgendamento;
    private Integer idBeneficiario;
    private String beneficiario;
    private String cpf;
    private String atividade;
    private String funcionario;
    private String dataInicio;
    private String dataFim;
    private Boolean presente;

    public RelatorioFaltaBeneficiario() {
    }

    public RelatorioFaltaBeneficiario(Integer idAgendamento, Integer idBeneficiario, String beneficiario, String cpf, String atividade, String funcionario, String dataInicio, String dataFim, Boolean presente) {
        this.idAgendamento = idAgendamento;
        this.idBeneficiario = idBeneficiario;
        this.beneficiario = beneficiario;
        this.cpf = cpf;
        this.atividade = atividade;
        this.funcionario = funcionario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.presente = presente;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public Integer getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(Integer idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}
