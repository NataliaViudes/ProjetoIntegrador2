package pi2.example.back_end.Control;

import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.AgendamentoAtividade;
import pi2.example.back_end.Modelo.Cardapio;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

public class CardapioControl {

    private boolean invalido(Cardapio c) {
        return c.getDescricao() == null || c.getDescricao().isEmpty()
                || c.getData() == null || c.getData().isEmpty()
                || c.getHora() == null || c.getHora().isEmpty()
                || c.getQuantidade() == null || c.getQuantidade() <= 0
                || c.getAlimento() == null || c.getAlimento().getId() <= 0
                || c.getAgendamento() == null || c.getAgendamento().getId() <= 0;
    }

    public ResponseEntity<?> incluir(Cardapio cardapio) {

        if (invalido(cardapio)) {
            return ResponseEntity.badRequest().body(new Erro("Dados inválidos"));
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar())
                throw new Exception();

            AgendamentoAtividade ag = new AgendamentoAtividade()
                    .buscarPorId(cardapio.getAgendamento().getId(), db);

            if (ag == null) {
                return ResponseEntity.badRequest()
                        .body(new Erro("Não existe atividade para esse dia"));
            }

            Cardapio resultado = cardapio.incluir(db);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro"));
        } finally {
            db.desconectar();
        }
    }
}