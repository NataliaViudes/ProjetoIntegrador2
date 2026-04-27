package pi2.example.back_end.Modelo;

public class ItensDoEvento {
    private Estoque estoque;
    private Evento evento;
    private int qtd;

    public ItensDoEvento() {
    }

    public ItensDoEvento(Estoque id_estoque, Evento evento, int qtd) {
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

}
