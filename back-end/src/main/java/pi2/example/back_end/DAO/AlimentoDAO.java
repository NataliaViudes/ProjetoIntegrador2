package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Alimento;

import java.util.List;

public interface AlimentoDAO {
    boolean salvar(Alimento a);

    List<Alimento> listar();

    List<Alimento> buscarPorNome(String nome);

    boolean excluir(int id);

    boolean atualizar(Alimento a);
}
