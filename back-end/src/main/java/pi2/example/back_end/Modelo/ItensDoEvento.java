package pi2.example.back_end.Modelo;

public class ItensDoEvento {
    private Integer id_estoque;
    private Integer id_evento;
    private int qtd;

    public ItensDoEvento() {
    }

    public ItensDoEvento(Integer id_estoque, Integer id_evento, int qtd) {
        this.id_estoque = id_estoque;
        this.id_evento = id_evento;
        this.qtd = qtd;
    }

    public Integer getId_estoque() {
        return id_estoque;
    }

    public void setId_estoque(Integer id_estoque) {
        this.id_estoque = id_estoque;
    }

    public Integer getId_evento() {
        return id_evento;
    }

    public void setId_evento(Integer id_evento) {
        this.id_evento = id_evento;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
