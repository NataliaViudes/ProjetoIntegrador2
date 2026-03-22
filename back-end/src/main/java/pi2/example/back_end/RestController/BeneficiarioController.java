package pi2.example.back_end.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pi2.example.back_end.DAO.BeneficiarioDAO;
import pi2.example.back_end.DAO.BeneficiarioDAOImpl;
import pi2.example.back_end.Modelo.Beneficiario;

import java.util.List;

@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioController {
    BeneficiarioDAO dao = new BeneficiarioDAOImpl();

    @GetMapping
    public List<Beneficiario> listar() {
        return dao.listar();
    }

    @GetMapping("/buscar")
    public Beneficiario buscarPorCpf(@RequestParam String cpf) {
        return dao.buscarPorCpf(cpf);
    }
}
