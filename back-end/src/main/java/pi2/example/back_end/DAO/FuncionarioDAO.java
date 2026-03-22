package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Funcionario;

import java.util.List;

public interface FuncionarioDAO {
    boolean gravar(Funcionario entidade);
    boolean alterar(Funcionario entidade);
    boolean apagar(Funcionario entidade);
    Funcionario get(int id);
    List<Funcionario> get(String filtro);
}
