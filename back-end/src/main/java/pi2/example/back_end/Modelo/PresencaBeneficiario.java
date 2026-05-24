package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOPresencaBeneficiario;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class PresencaBeneficiario {
    private Integer idAgendamento;
    private Integer idBeneficiario;
    private Boolean presente;

    public PresencaBeneficiario() {
    }

    public PresencaBeneficiario(Integer idAgendamento, Integer idBeneficiario, Boolean presente) {
        this.idAgendamento = idAgendamento;
        this.idBeneficiario = idBeneficiario;
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

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    public PresencaBeneficiario incluir(Conexao con) {
        DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(con);
        return dao.gravar(this);
    }

    public List<PresencaBeneficiario> buscarPorAgendamento(Conexao con, Integer idAgendamento) {
        DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(con);
        return dao.getByIdAgendamento(idAgendamento);
    }

    public boolean apagarPorAgendamento(Conexao con, Integer idAgendamento) {
        DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(con);
        return dao.apagarPorAgendamento(idAgendamento);
    }
}
