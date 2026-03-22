package pi2.example.back_end.RestController;

import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Modelo.Alimento;


import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/alimentos")

public class AlimentoController {

    DALAlimento dao = new DALAlimento();

    @PostMapping
    public String salvar(@RequestBody Alimento a) {
        if(dao.salvar(a))
            return "Salvo com sucesso";
        else
            return "Erro ao salvar";
    }

    @GetMapping
    public List<Alimento> listar() {
        return dao.listar();
    }

    @GetMapping("/buscar")
    public List<Alimento> buscar(@RequestParam("nome") String nome) {
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
    public String atualizar(@RequestBody Alimento a) {

        if(dao.atualizar(a))
            return "Atualizado com sucesso";
        else
            return "Erro ao atualizar";
    }

}
