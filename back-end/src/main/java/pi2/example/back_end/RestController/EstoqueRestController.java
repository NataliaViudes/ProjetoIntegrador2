package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.EstoqueControl;
import pi2.example.back_end.Modelo.Estoque;
import pi2.example.back_end.Modelo.MaterialAtividade;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/estoque")
public class EstoqueRestController {

    private final EstoqueControl control = new EstoqueControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable("id") int id) {
        return control.getById(id);
    }

    @GetMapping("/tipo")
    public ResponseEntity<?> getCategoria(@RequestParam(name = "tipo", required = false) String tipo) {
        return control.buscaPorTipo(tipo);
    }

    @GetMapping("/descricao")
    public ResponseEntity<?> getDescricao(@RequestParam(name = "descricao", required = false) String descricao) {
        return control.buscaPorDescricao(descricao);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Estoque estoque) {
        return control.incluir(estoque);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Estoque estoque) {
        estoque.setId(id);
        return control.alterar(estoque);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        return control.delete(id);
    }

    @PostMapping("/agendamento-material")
    public ResponseEntity<?> salvarMateriaisEtapa(@RequestBody List<MaterialAtividade> materiais) {
        for (MaterialAtividade m : materiais) {
            System.out.println("Agendamento: " + m.getIdAgendamento());
            System.out.println("Item: " + m.getIdItem());
            System.out.println("Qtd: " + m.getQuantidade());
        }
        return control.salvarMateriaisEtapa(materiais);
    }

    @GetMapping("/agendamento-material/{idAgendamento}")
    public ResponseEntity<?> buscarMateriais(@PathVariable int idAgendamento) {
        return control.buscarMateriaisPorAgendamento(idAgendamento);
    }

    @DeleteMapping("/agendamento-material")
    public ResponseEntity<?> deletarMaterial(@RequestBody MaterialAtividade mat) {
        return control.removerMaterial(mat);
    }
}