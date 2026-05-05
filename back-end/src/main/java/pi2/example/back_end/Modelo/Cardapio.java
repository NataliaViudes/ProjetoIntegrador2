package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CardapioDAO;
import pi2.example.back_end.db.Conexao;

import java.util.Date;
import java.util.List;


public class Cardapio {

    private Integer id;
    private String nome;
    private String hora;
    private String data;

    private AgendamentoAtividade agendamento;

    public Cardapio() {}

    public Cardapio(Integer id) {
        this.id = id;
    }

    public Cardapio(Integer id, String nome, String hora, String data, AgendamentoAtividade agendamento) {
        this.id = id;
        this.nome = nome;
        this.hora = hora;
        this.data = data;
        this.agendamento = agendamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHora() {
        return hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return new CardapioDAO(con).gravar(this);
    }

    public Cardapio alterar(Conexao con) {
        return new CardapioDAO(con).alterar(this);
    }

    public boolean apagar(Conexao con) {
        return new CardapioDAO(con).apagar(this);
    }

    public Cardapio buscarPorId(Integer id, Conexao con) {
        return new CardapioDAO(con).getPorId(id);
    }

    public List<Cardapio> buscarTodos(Conexao con) {
        return new CardapioDAO(con).getAll();
    }
}