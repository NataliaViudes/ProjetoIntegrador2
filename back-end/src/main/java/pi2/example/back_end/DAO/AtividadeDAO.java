package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Atividade;

import java.util.List;

public interface AtividadeDAO {
    boolean salvar(Atividade a);

    List<Atividade> listar();

    List<Atividade> buscarPorNome(String nome);

    boolean excluir(int id);

    boolean atualizar(Atividade a);
}
