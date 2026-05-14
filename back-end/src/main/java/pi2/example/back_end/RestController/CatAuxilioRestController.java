package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.CatAuxilioControl;
import pi2.example.back_end.Modelo.CategoriaAuxilio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categorias")
public class CatAuxilioRestController {

    private final CatAuxilioControl control = new CatAuxilioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam(required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody CategoriaAuxilio categoria) {
        return control.incluir(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable int id,
                                     @RequestBody CategoriaAuxilio categoria) {
        categoria.setId(id);
        return control.update(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }
}
