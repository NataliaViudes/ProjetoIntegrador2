package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Atividade;
import pi2.example.back_end.Modelo.Auxilio;

import java.util.List;

public interface AuxilioDAO {

    boolean salvar(Auxilio a);

    List<Auxilio> listar();

    boolean excluir(int id);
    boolean atualizar(Auxilio a);
}
