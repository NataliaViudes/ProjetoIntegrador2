package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.TipoEstoqueControl;
import pi2.example.back_end.Modelo.TipoEstoque;


@CrossOrigin
@RestController
@RequestMapping("/tipo-estoque")
public class TipoEstoqueRestController {

    private final TipoEstoqueControl control = new TipoEstoqueControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
       return  control.getById(id);
    }

    @GetMapping("tipo")
    public ResponseEntity<?> getTipo(@RequestParam (value = "tipo", required = false) String tipo) {
       return control.buscaPorTipo(tipo);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody TipoEstoque tipoEstoque) {
        return control.incluir(tipoEstoque);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody TipoEstoque tipoEstoque) {
        return control.update(tipoEstoque);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        return control.delete(id);
    }

}