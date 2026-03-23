package pi2.example.back_end.Service;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;
import java.util.List;
public class EventoService { // É uma classe intermediaria só para desacoplar as regras de negocio das rotas do spring boot q estão no restController

    public Evento salvar(Evento evento, Conexao conexao) {
        //  REGRA DE NEGÓCIO
        if (evento.getCategoria() == null || evento.getCategoria().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        return evento.incluir(conexao);
    }

    public Evento buscarPorId(int id, Conexao conexao) {

        if (id <= 0) {
            throw new RuntimeException("ID inválido");
        }
        Evento model = new Evento();
        return model.consultarId(id, conexao);
    }

    public List<Evento> buscaPorCategoria(String categoria, Conexao conexao) {
        Evento model = new Evento();
        if (categoria == null || categoria.isEmpty()) {
            return model.consultarCategoria("", conexao);
        }
        return model.consultarCategoria(categoria, conexao);
    }


    public Evento alterar(Evento evento, Conexao conexao) {

        // ID obrigatório
        if (evento.getId() == null) {
            throw new RuntimeException("ID é obrigatório para alteração");
        }
        // ID inválido
        if (evento.getId() <= 0) {
            throw new RuntimeException("ID inválido");
        }
        // nome obrigatório
        if (evento.getCategoria() == null || evento.getCategoria().isEmpty()) {
            throw new RuntimeException("Nome é obrigatório");
        }
        // verificar se existe no banco
        Evento existente = new Evento().consultarId(evento.getId(), conexao);
        if (existente == null) {
            throw new RuntimeException("Evento não encontrado");
        }
        // ALTERAÇÃO
        return evento.alterar(conexao);
    }

    public boolean deletar(Integer id, Conexao con) {
        Evento model = new Evento(id);
        return model.deletar(con);
    }
}
