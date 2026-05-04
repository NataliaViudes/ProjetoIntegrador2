package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CardapioDAO;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Cardapio {

    private Integer id;
    private String descricao;
    private String hora;

    private AgendamentoAtividade agendamento;

    public Cardapio() {}

    public Cardapio(Integer id) {
        this.id = id;
    }

    public Cardapio(Integer id, String descricao, String hora, AgendamentoAtividade agendamento) {
        this.id = id;
        this.descricao = descricao;
        this.hora = hora;
        this.agendamento = agendamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public AgendamentoAtividade getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoAtividade agendamento) {
        this.agendamento = agendamento;
    }

    public Cardapio incluir(Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.gravar(this);
    }

    public Cardapio alterar(Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.apagar(this);
    }

    public Cardapio buscarPorId(Integer id, Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.getPorId(id);
    }

    public List<Cardapio> buscarTodos(Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.getAll();
    }
}