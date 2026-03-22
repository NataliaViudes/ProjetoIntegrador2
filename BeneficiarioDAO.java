package pi2.example.back_end.DAO;

import pi2.example.back_end.Modelo.Beneficiario;

import java.util.List;

public interface BeneficiarioDAO {
    List<Beneficiario> listar();

    Beneficiario buscarPorCpf(String cpf);
}
