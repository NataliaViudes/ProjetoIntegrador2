package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CardapioDAO;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class Cardapio {

    private Integer id;
    private String descricao;
    private String data;
    private String hora;
    private Integer quantidade;

    private Alimento alimento;
    private AgendamentoAtividade agendamento;

    public Cardapio() {}

    public Cardapio(Integer id) {
        this.id = id;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Alimento getAlimento() { return alimento; }
    public void setAlimento(Alimento alimento) { this.alimento = alimento; }

    public AgendamentoAtividade getAgendamento() { return agendamento; }
    public void setAgendamento(AgendamentoAtividade agendamento) { this.agendamento = agendamento; }



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
        return dao.get(id);
    }

    public List<Cardapio> buscarTodos(Conexao con) {
        CardapioDAO dao = new CardapioDAO(con);
        return dao.getAll();
    }
}