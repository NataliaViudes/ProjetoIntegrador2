package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.ItensEvento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItensEventoControl {

    public ItensEventoControl() {}

    // =====================================================
    // TRATAR ERROS SQL
    // =====================================================

    private ResponseEntity<?> tratarErroSQL(
            SQLException e,
            Conexao db
    ) {

        db.desfazerTransacao();

        String msg = e.getMessage();

        System.out.println(msg);

        // =============================================
        // ERRO TRIGGER ESTOQUE
        // =============================================

        if (
                msg != null &&
                        msg.contains(
                                "Quantidade insuficiente no estoque"
                        )
        ) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Quantidade insuficiente no estoque"
                            )
                    );
        }

        return ResponseEntity
                .badRequest()
                .body(
                        new Erro("Erro no banco")
                );
    }

    // =====================================================
    // INSERT
    // =====================================================

    public ResponseEntity<?> incluir(
            ItensEvento item
    ) {

        if (item == null)
            return ResponseEntity
                    .badRequest()
                    .body(new Erro("Item nulo"));

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception("Erro conexão");

            db.iniciarTransacao();

            ItensEvento resultado =
                    item.incluir(db);

            if (resultado == null)
                throw new Exception(
                        "Erro ao inserir item"
                );

            db.confirmarTransacao();

            return ResponseEntity.ok(
                    resultado
            );

        } catch (SQLException e) {

            return tratarErroSQL(e, db);

        } catch (Exception e) {

            db.desfazerTransacao();

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    e.getMessage()
                            )
                    );

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // LIMPAR TODOS ITENS
    // =====================================================

    public ResponseEntity<?> limparTudo(
            Integer eventoId
    ) {

        if (eventoId == null
                || eventoId <= 0)
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Evento inválido"
                            )
                    );

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro conexão"
                );

            db.iniciarTransacao();

            ItensEvento model =
                    new ItensEvento();

            boolean ok =
                    model.limparItens(
                            db,
                            eventoId
                    );

            if (!ok)
                throw new Exception(
                        "Erro ao limpar itens"
                );

            db.confirmarTransacao();

            return ResponseEntity.ok(
                    "Itens removidos"
            );

        } catch (SQLException e) {

            return tratarErroSQL(e, db);

        } catch (Exception e) {

            db.desfazerTransacao();

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    e.getMessage()
                            )
                    );

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public ResponseEntity<?> alterar(
            List<ItensEvento> itens
    ) {

        if (itens == null
                || itens.isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Lista vazia"
                            )
                    );

        Integer eventoId =
                itens.getFirst().getEventoId();

        if (eventoId == null
                || eventoId <= 0)
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Evento inválido"
                            )
                    );

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro conexão"
                );

            db.iniciarTransacao();

            ItensEvento model =
                    new ItensEvento();

            boolean ok =
                    model.syncItensEvento(
                            db,
                            eventoId,
                            itens
                    );

            if (!ok)
                throw new Exception(
                        "Erro ao atualizar itens"
                );

            db.confirmarTransacao();

            return ResponseEntity.ok(
                    "Itens atualizados com sucesso"
            );

        } catch (SQLException e) {

            return tratarErroSQL(e, db);

        } catch (Exception e) {

            db.desfazerTransacao();

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    e.getMessage()
                            )
                    );

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // DELETE
    // =====================================================

    public ResponseEntity<?> delete(
            Integer idEvento,
            Integer idEstoque
    ) {

        if (idEvento == null
                || idEvento <= 0)
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Evento inválido"
                            )
                    );

        if (idEstoque == null
                || idEstoque <= 0)
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Estoque inválido"
                            )
                    );

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro conexão"
                );

            db.iniciarTransacao();

            ItensEvento model =
                    new ItensEvento();

            ItensEvento itemBanco =
                    model.buscarPorChave(
                            db,
                            idEvento,
                            idEstoque
                    );

            if (itemBanco == null)
                throw new Exception(
                        "Item não encontrado"
                );

            boolean ok =
                    itemBanco.apagar(db);

            if (!ok)
                throw new Exception(
                        "Erro ao apagar item"
                );

            db.confirmarTransacao();

            return ResponseEntity.ok(true);

        } catch (SQLException e) {

            return tratarErroSQL(e, db);

        } catch (Exception e) {

            db.desfazerTransacao();

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    e.getMessage()
                            )
                    );

        } finally {

            db.desconectar();
        }
    }

    // =====================================================
    // GET POR EVENTO
    // =====================================================

    public ResponseEntity<?> getPorEvento(
            Integer idEvento
    ) {

        if (idEvento == null
                || idEvento <= 0)
            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    "Evento inválido"
                            )
                    );

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar())
                throw new Exception(
                        "Erro conexão"
                );

            ItensEvento model =
                    new ItensEvento();

            List<ItensEvento> lista =
                    model.buscarItensDoEvento(
                            db,
                            idEvento
                    );

            if (lista == null)
                lista = new ArrayList<>();

            return ResponseEntity.ok(
                    lista
            );

        } catch (SQLException e) {

            return tratarErroSQL(e, db);

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            new Erro(
                                    e.getMessage()
                            )
                    );

        } finally {

            db.desconectar();
        }
    }
}