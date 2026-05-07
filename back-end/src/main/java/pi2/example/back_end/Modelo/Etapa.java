package pi2.example.back_end.Modelo;

public class Etapa {
    private Integer id;
    private Integer idAgendamento;
    private String descricao;
    private String dataHoraInicio;

    public Etapa() {}

    public Etapa(Integer id, Integer idAgendamento, String descricao, String dataHoraInicio) {
        this.id = id;
        this.idAgendamento = idAgendamento;
        this.descricao = descricao;
        this.dataHoraInicio = dataHoraInicio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAgendamento() {
        return idAgendamento;
    }

    public void setIdAgendamento(Integer idAgendamento) {
        this.idAgendamento = idAgendamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(String dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
}