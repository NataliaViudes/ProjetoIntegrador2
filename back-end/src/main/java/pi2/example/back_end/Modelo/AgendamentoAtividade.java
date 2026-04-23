package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.AgendamentoAtividadeDAO;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class AgendamentoAtividade {

    private Integer id;
    private Atividade atividade;
    private String dataInicio;
    private String dataFim;
    private String observacao;

    public AgendamentoAtividade() {}

    public AgendamentoAtividade(Integer id, Atividade atividade, String dataInicio, String dataFim, String observacao) {
        this.id = id;
        this.atividade = atividade;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.observacao = observacao;
    }

    public AgendamentoAtividade(Integer id) {
        this.id = id;
        this.atividade = null;
        this.dataInicio = "";
        this.dataFim = "";
        this.observacao = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public AgendamentoAtividade incluir(Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.gravar(this);
    }

    public AgendamentoAtividade alterar(Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.apagar(this);
    }

    public AgendamentoAtividade buscarPorId(Integer id, Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.get(id);
    }

    public List<AgendamentoAtividade> buscarTodos(Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.getAll();
    }

    public List<AgendamentoAtividade> buscarComFiltro(String filtro, Conexao con) {
        AgendamentoAtividadeDAO dao = new AgendamentoAtividadeDAO(con);
        return dao.get(filtro);
    }
}
