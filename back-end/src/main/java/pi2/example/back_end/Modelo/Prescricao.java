package pi2.example.back_end.Modelo;

import java.sql.Date;

public class Prescricao {
    private int id;
    private String dosagem;
    private int quantidade;
    private Date horario;
    private Beneficiario idBeneficiario;
    private Remedio idRemedio;
    private int intervalo;

    public Prescricao() {

    }

    public Prescricao(int id, String dosagem, int quantidade, Date horario, Beneficiario idBeneficiario, Remedio idRemedio, int intervalo) {
        this.id = id;
        this.dosagem = dosagem;
        this.quantidade = quantidade;
        this.horario = horario;
        this.idBeneficiario = idBeneficiario;
        this.idRemedio = idRemedio;
        this.intervalo = intervalo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }

    public Beneficiario getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(Beneficiario idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public Remedio getIdRemedio() {
        return idRemedio;
    }

    public void setIdRemedio(Remedio idRemedio) {
        this.idRemedio = idRemedio;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }
}
