package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOPrescricao;
import pi2.example.back_end.db.Conexao;

import java.sql.Date;
import java.util.List;

public class Prescricao {

    private Integer id;
    private String dosagem;
    private Integer quantidade;
    private Date horario;
    private Beneficiario beneficiario;
    private Remedio remedio;
    private Integer intervalo;

    public Prescricao() {
    }

    public Prescricao(Integer id, String dosagem, Integer quantidade, Date horario,
                      Beneficiario beneficiario, Remedio remedio, Integer intervalo) {

        this.id = id;
        this.dosagem = dosagem;
        this.quantidade = quantidade;
        this.horario = horario;
        this.beneficiario = beneficiario;
        this.remedio = remedio;
        this.intervalo = intervalo;
    }

    public Prescricao(String dosagem, Integer quantidade, Date horario,
                      Beneficiario beneficiario, Remedio remedio, Integer intervalo) {

        this.dosagem = dosagem;
        this.quantidade = quantidade;
        this.horario = horario;
        this.beneficiario = beneficiario;
        this.remedio = remedio;
        this.intervalo = intervalo;
    }

    // =====================================================
    // GETTERS E SETTERS
    // =====================================================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Beneficiario beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Remedio getRemedio() {
        return remedio;
    }

    public void setRemedio(Remedio remedio) {
        this.remedio = remedio;
    }

    public Integer getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(Integer intervalo) {
        this.intervalo = intervalo;
    }

    // =====================================================
    // VALIDAÇÃO
    // =====================================================

    public String validar() {

        String erros = "";

        if (dosagem == null || dosagem.trim().isEmpty())
            erros += "Dosagem inválida\n";

        if (quantidade == null || quantidade <= 0)
            erros += "Quantidade inválida\n";

        if (horario == null)
            erros += "Horário obrigatório\n";

        if (intervalo == null || intervalo <= 0)
            erros += "Intervalo inválido\n";

        if (beneficiario == null || beneficiario.getId() == null || beneficiario.getId() <= 0)
            erros += "Beneficiário inválido\n";

        if (remedio == null || remedio.getId() == null || remedio.getId() <= 0)
            erros += "Remédio inválido\n";

        return erros;
    }

    // =====================================================
    // DAO
    // =====================================================

    public List<Prescricao> listar(Conexao con) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.listar();
    }

    public Prescricao buscarPorId(Conexao con, int id) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.buscarPorId(id);
    }

    public List<Prescricao> buscarPorBeneficiario(Conexao con, int idBeneficiario) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.buscarPorBeneficiario(idBeneficiario);
    }

    public List<Prescricao> buscarPorRemedio(Conexao con, int idRemedio) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.buscarPorRemedio(idRemedio);
    }

    public Prescricao incluir(Conexao con) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.gravar(this);
    }

    public Prescricao alterar(Conexao con) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.alterar(this);
    }

    public Boolean apagar(Conexao con) {
        DAOPrescricao dao = new DAOPrescricao(con);
        return dao.apagar(this);
    }

    // =====================================================
    // AUXILIARES
    // =====================================================

    public Integer getIdBeneficiario() {

        if (beneficiario == null)
            return null;

        return beneficiario.getId();
    }

    public Integer getIdRemedio() {

        if (remedio == null)
            return null;

        return remedio.getId();
    }

    public boolean possuiBeneficiario() {
        return getIdBeneficiario() != null;
    }

    public boolean possuiRemedio() {
        return getIdRemedio() != null;
    }
}