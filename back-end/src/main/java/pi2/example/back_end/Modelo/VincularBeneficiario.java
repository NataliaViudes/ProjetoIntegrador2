package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOVincularBeneficiario;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class VincularBeneficiario {
    private Integer idBeneficiario;
    private Integer idAgendamento;

    public VincularBeneficiario() {

    }

    public VincularBeneficiario(Integer idBeneficiario, Integer idAgendamento) {
        this.idBeneficiario = idBeneficiario;
        this.idAgendamento = idAgendamento;
    }

    public Integer getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(Integer idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public VincularBeneficiario incluir(Conexao con){
        DAOVincularBeneficiario dao = new DAOVincularBeneficiario(con);
        return dao.gravar(this);
    }

    public boolean apagar(Conexao con){
        DAOVincularBeneficiario dao = new DAOVincularBeneficiario(con);
        return dao.apagar(this);
    }

    public VincularBeneficiario BuscarElem(Conexao con){
        DAOVincularBeneficiario dao = new DAOVincularBeneficiario(con);
        return dao.BuscaElemento(this);
    }

    public List<VincularBeneficiario> BuscarPorIdAgendamento(Conexao con,Integer id){
        DAOVincularBeneficiario dao = new DAOVincularBeneficiario(con);
        return dao.getByIdAgendamento(id);
    }

}
