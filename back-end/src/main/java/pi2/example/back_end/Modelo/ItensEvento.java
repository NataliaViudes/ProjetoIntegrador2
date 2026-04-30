package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.DAOItensEvento;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class ItensEvento {
    private Estoque estoque;
    private Evento evento;
    private int qtd;

    public ItensEvento() {
    }

    public ItensEvento(Estoque id_estoque, Evento evento, int qtd) {
        this.estoque = id_estoque;
        this.evento = evento;
        this.qtd = qtd;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Integer getEventoId()
    {
        return this.evento.getId();
    }
    public Integer getEstoqueId()
    {
        return this.estoque.getId();
    }

    public List<ItensEvento> buscarItensDoEvento(Conexao con , Integer idEvento)
    {
        DAOItensEvento dao = new DAOItensEvento(con);
        return dao.getPorIdEvento(idEvento);
    }

}
