package pi2.example.back_end.Modelo;

import pi2.example.back_end.DAO.CardapioDAO;
import pi2.example.back_end.DAO.ItensCardapioDAO;
import pi2.example.back_end.db.Conexao;

import java.util.List;

public class ItensCardapio {
    private Alimento alimento;
    private Cardapio cardapio;
    private Integer quantidade;

    public ItensCardapio() {}

    public ItensCardapio(Alimento alimento, Cardapio cardapio, int quantidade) {
        this.alimento = alimento;
        this.cardapio = cardapio;
        this.quantidade = quantidade;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public Cardapio getCardapio() {
        return cardapio;
    }

    public void setCardapio(Cardapio cardapio) {
        this.cardapio = cardapio;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ItensCardapio incluir(Conexao con) {
        ItensCardapioDAO dao = new ItensCardapioDAO(con);
        return dao.gravar(this);
    }

    public ItensCardapio alterar(Conexao con) {
        ItensCardapioDAO dao = new ItensCardapioDAO(con);
        return dao.alterar(this);
    }

    public boolean apagar(Conexao con) {
        ItensCardapioDAO dao = new ItensCardapioDAO(con);
        return dao.apagar(this);
    }

    public ItensCardapio buscarPorIds(Conexao db, int idAlimento, int idCardapio) {
        ItensCardapioDAO dao = new ItensCardapioDAO(db);
        return dao.getByIds(idAlimento, idCardapio);
    }

    public List<ItensCardapio> buscarPorCardapio(Conexao db, int idCardapio) {
        ItensCardapioDAO dao = new ItensCardapioDAO(db);
        return dao.buscarPorCardapio(idCardapio);
    }
}
