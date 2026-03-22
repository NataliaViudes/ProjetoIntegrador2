package pi2.example.back_end.RestController;

import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.DAO.AuxilioDAO;
import pi2.example.back_end.DAO.AuxilioDAOImpl;
import pi2.example.back_end.Modelo.Auxilio;

import java.util.List;

@RestController
@RequestMapping("/auxilios")
public class AuxilioController {
    AuxilioDAO dao = new AuxilioDAOImpl();

    @PostMapping
    public String salvar(@RequestBody Auxilio a) {

        if(dao.salvar(a))
            return "Auxílio cadastrado com sucesso";
        else
            return "Erro ao cadastrar auxílio";
    }

    @GetMapping
    public List<Auxilio> listar() {
        return dao.listar();
    }

    @DeleteMapping("/{id}")
    public String excluir(@PathVariable int id) {

        if(dao.excluir(id))
            return "Excluído com sucesso";
        else
            return "Erro ao excluir";
    }
    @PutMapping
    public String atualizar(@RequestBody Auxilio a) {

        if(dao.atualizar(a))
            return "Atualizado com sucesso";
        else
            return "Erro ao atualizar";
    }

}
