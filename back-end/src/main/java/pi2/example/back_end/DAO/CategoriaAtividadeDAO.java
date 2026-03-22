package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.CategoriaAtividade;

import java.util.List;

public interface CategoriaAtividadeDAO {
    boolean salvar(CategoriaAtividade c);

    List<CategoriaAtividade> listar();

    CategoriaAtividade buscarPorId(int id);

    boolean atualizar(CategoriaAtividade c);

    boolean excluir(int id);
}
