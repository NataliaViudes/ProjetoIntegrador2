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
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return  control.getById(id);
    }

    @GetMapping("tipo")
    public ResponseEntity<?> getCategoria(@RequestParam (value = "tipo", required = false) String tipo) {
        return control.buscaPorTipo(tipo);
    }

    @GetMapping("descricao")
    public ResponseEntity<?> getDescricao(@RequestParam (value = "descricao", required = false) String descricao) {
        return control.buscaPorDescricao(descricao);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Estoque estoque) {
        return control.incluir(estoque);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Estoque estoque) {
        return control.alterar(estoque);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        return control.delete(id);
    }

    @GetMapping
    public ResponseEntity<?> listarTodos(@RequestParam(value = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }
    @PostMapping("/agendamento-material")
    public ResponseEntity<?> salvarMateriaisEtapa(@RequestBody List<MaterialAtividade> materiais) {
        return control.salvarMateriaisEtapa(materiais);
    }

    @GetMapping("/agendamento-material/{idAgendamento}")
    public ResponseEntity<?> buscarMateriais(@PathVariable(value = "idAgendamento") int idAgendamento) {
        return control.buscarMateriaisPorAgendamento(idAgendamento);
    }

    @DeleteMapping("/agendamento-material")
    public ResponseEntity<?> deletarMaterial(@RequestBody MaterialAtividade mat) {
        return control.removerMaterial(mat);
    }
}
