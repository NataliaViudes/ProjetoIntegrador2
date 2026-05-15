package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.EstoqueControl;
import pi2.example.back_end.Modelo.Estoque;

@CrossOrigin
@RestController
@RequestMapping("/estoque")
public class EstoqueRestController {

    private final EstoqueControl control = new EstoqueControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return  control.getById(id);
    }

    @GetMapping("tipo")
    public ResponseEntity<?> getCategoria(@RequestParam (required = false) String tipo) {
        return control.buscaPorTipo(tipo);
    }

    @GetMapping("descricao")
    public ResponseEntity<?> getDescricao(@RequestParam (required = false) String descricao) {
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
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return control.delete(id);
    }
}
