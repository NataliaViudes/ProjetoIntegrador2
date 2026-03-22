package pi2.example.back_end.DAO;

import java.util.List;

import pi2.example.back_end.Modelo.Beneficiario;

public interface BeneficiarioDAO {

    boolean salvar(Beneficiario b);

    List<Beneficiario> listar();

    Beneficiario buscarPorCpf(String cpf);

    boolean excluir(int id);

    boolean atualizar(Beneficiario b);
}