package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.CategoriaAtividadeControl;
import pi2.example.back_end.Modelo.CategoriaAtividade;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categoriaAtividade")
public class CategoriaAtividadeRestController {

    private final CategoriaAtividadeControl control = new CategoriaAtividadeControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable("id") int id) {
        return control.getById(id);
    }

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(name = "nome", required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody CategoriaAtividade categoria) {
        return control.incluir(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable("id") int id) {
        return control.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable("id") int id,
                                     @RequestBody CategoriaAtividade categoria) {
        categoria.setId(id);
        return control.update(categoria);
    }
}