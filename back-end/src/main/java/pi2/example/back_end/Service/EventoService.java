package pi2.example.back_end.Service;

import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;
import java.util.List;
public class EventoService { // É uma classe intermediaria só para desacoplar as regras de negocio das rotas do spring boot q estão no restController

    public boolean salvar(Evento evento,Conexao conexao) {

        //  REGRA DE NEGÓCIO
        if (evento.getNome() == null || evento.getNome().isEmpty()) {
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

    public List<Evento> buscarPorNome(String nome, Conexao conexao) {

        Evento model = new Evento();

        if (nome == null || nome.isEmpty()) {
            return model.consultarNome("", conexao);
        }

        return model.consultarNome(nome, conexao);
    }




}
