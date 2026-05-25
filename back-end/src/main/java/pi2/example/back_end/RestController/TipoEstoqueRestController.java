package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controler.TipoEstoqueControl;
import pi2.example.back_end.Modelo.TipoEstoque;


@CrossOrigin
@RestController
@RequestMapping("/tipo-estoque")
public class TipoEstoqueRestController {
    private final TipoEstoqueControl control = new TipoEstoqueControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return  control.getById(id);
    }

    @GetMapping("/tipo")
    public ResponseEntity<?> getTipo(@RequestParam (required = false) String tipo) {
        return control.buscaPorTipo(tipo);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "filtro", required = false) String filtro
    ) {
        return control.getAllOrFilter(filtro);
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
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        return control.delete(id);
    }

}