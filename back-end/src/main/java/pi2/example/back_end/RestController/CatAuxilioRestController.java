package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.CatAuxilioControl;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categorias")
public class CatAuxilioRestController {

    private final CatAuxilioControl control = new CatAuxilioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam(value = "nome", required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }
}
