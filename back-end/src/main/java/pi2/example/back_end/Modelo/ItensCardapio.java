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

    // getters e setters
    public Alimento getAlimento() { return alimento; }
    public void setAlimento(Alimento alimento) { this.alimento = alimento; }
    public Cardapio getCardapio() { return cardapio; }
    public void setCardapio(Cardapio cardapio) { this.cardapio = cardapio; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    // métodos de persistência usando o DAO internamente
    public ItensCardapio incluir(Conexao con) {
        return new ItensCardapioDAO(con).gravar(this);
    }

    public ItensCardapio alterar(Conexao con) {
        return new ItensCardapioDAO(con).alterar(this);
    }

    public boolean apagar(Conexao con) {
        return new ItensCardapioDAO(con).apagar(this);
    }

    public ItensCardapio buscarPorIds(Conexao db, int idAlimento, int idCardapio) {
        return new ItensCardapioDAO(db).getByIds(idCardapio, idAlimento);
    }

    public List<ItensCardapio> buscarPorCardapio(Conexao db, int idCardapio) {
        return new ItensCardapioDAO(db).buscarPorCardapio(idCardapio);
    }

    /**
     * Salvar ou atualizar item, considerando quantidade 0 = remover
     */
    public ItensCardapio salvarOuAtualizar(Conexao db) throws Exception {
        ItensCardapio existente = buscarPorIds(db, alimento.getId(), cardapio.getId());

        if (quantidade <= 0) {
            if (existente != null) {
                existente.apagar(db);
            }
            return null; // null significa que foi removido
        } else {
            if (existente == null) {
                return incluir(db);
            } else {
                existente.setQuantidade(quantidade);
                return existente.alterar(db);
            }
        }
    }
}