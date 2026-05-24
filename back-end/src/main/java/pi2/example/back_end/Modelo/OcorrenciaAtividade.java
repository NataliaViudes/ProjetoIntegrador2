package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.OcorrenciaAtividadeDAOImpl;
import pi2.example.back_end.db.Conexao;

public class OcorrenciaAtividade {
    private Integer id;
    private AgendamentoAtividade agendamento;
    private Beneficiario beneficiario;
    private String tipo;
    private String observacao;
    private String dataRegistro;

    public OcorrenciaAtividade() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AgendamentoAtividade getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoAtividade agendamento) {
        this.agendamento = agendamento;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public OcorrenciaAtividade incluir(Conexao con) {
        OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(con);
        return dao.gravar(this);
    }

    public OcorrenciaAtividade alterar(Conexao con) {
        OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(con);
        return dao.apagar(this.id);
    }

    public OcorrenciaAtividade buscarPorId(Integer id, Conexao con) {
        OcorrenciaAtividadeDAOImpl dao = new OcorrenciaAtividadeDAOImpl(con);
        return dao.get(id);
    }
}

