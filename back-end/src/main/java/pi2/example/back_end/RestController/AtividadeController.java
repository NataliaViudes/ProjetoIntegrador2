package pi2.example.back_end.RestController;

import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.DAO.AtividadeDAO;
import pi2.example.back_end.DAO.AtividadeDAOImpl;
import pi2.example.back_end.Modelo.Atividade;

import java.util.List;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {
    AtividadeDAO dao = new AtividadeDAOImpl();

    @PostMapping
    public String salvar(@RequestBody Atividade a) {
        if(dao.salvar(a))
            return "Salvo com sucesso";
        else
            return "Erro ao salvar";
    }

    @GetMapping
    public List<Atividade> listar() {
        return dao.listar();
    }

    @GetMapping("/buscar")
    public List<Atividade> buscar(@RequestParam("nome") String nome) {
        return dao.buscarPorNome(nome);
    }

    @DeleteMapping("/{id}")
    public String excluir(@PathVariable int id) {
        if(dao.excluir(id))
            return "Excluído com sucesso";
        else
            return "Erro ao excluir";
    }

    @PutMapping
    public String atualizar(@RequestBody Atividade a) {

        if(dao.atualizar(a))
            return "Atualizado com sucesso";
        else
            return "Erro ao atualizar";
    }
}
