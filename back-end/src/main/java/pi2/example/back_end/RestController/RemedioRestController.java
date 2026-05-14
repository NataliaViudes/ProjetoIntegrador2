package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.RemedioControl;
import pi2.example.back_end.Modelo.Remedio;

@CrossOrigin
@RestController
@RequestMapping("/remedios")
public class RemedioRestController {

    private final RemedioControl control = new RemedioControl();

    // -------------------- GET ALL --------------------

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(required = false) String nome) {

        if(nome != null && !nome.isEmpty())
        {
            return control.buscaPorNome(nome);
        }

        return control.getAll();
    }

    // -------------------- GET POR ID --------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable Integer id) {
        return control.getById(id);
    }

    // -------------------- BUSCA POR NOME --------------------

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    // -------------------- CRUD --------------------

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Remedio r) {
        return control.incluir(r);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Remedio r) {
        return control.update(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return control.delete(id);
    }
}