package pi2.example.back_end.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pi2.example.back_end.DAO.CatAuxilioDAO;
import pi2.example.back_end.DAO.CatAuxilioDAOImpl;
import pi2.example.back_end.Modelo.CategoriaAuxilio;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CatAuxilioController {
    CatAuxilioDAO dao = new CatAuxilioDAOImpl();

    @GetMapping
    public List<CategoriaAuxilio> listar() {
        return dao.listar();
    }
}
