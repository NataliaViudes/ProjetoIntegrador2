package pi2.example.back_end.Modelo;

public class OrdemJudicial {

    private Integer id;
    private Integer beneficiarioId;
    private Boolean possuiOrdem;
    private String descricao;

    public OrdemJudicial() {}

    public OrdemJudicial(Integer id, Integer beneficiarioId, Boolean possuiOrdem, String descricao) {
        this.id = id;
        this.beneficiarioId = beneficiarioId;
        this.possuiOrdem = possuiOrdem;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBeneficiarioId() {
        return beneficiarioId;
    }

    public void setBeneficiarioId(Integer beneficiarioId) {
        this.beneficiarioId = beneficiarioId;
    }

    public Boolean getPossuiOrdem() {
        return possuiOrdem;
    }

    public void setPossuiOrdem(Boolean possuiOrdem) {
        this.possuiOrdem = possuiOrdem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}