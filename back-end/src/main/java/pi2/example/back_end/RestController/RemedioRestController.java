package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.RemedioControl;
import pi2.example.back_end.Modelo.Remedio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/remedios")
public class RemedioRestController {

    private final RemedioControl controll = new RemedioControl();

    // -------------------- GET ALL --------------------
    @GetMapping
    public ResponseEntity<?> getAll() {
        return controll.getAll();
    }

    // -------------------- GET POR ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable Integer id) {
        return controll.getById(id);
    }

    // -------------------- BUSCAS --------------------

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(required = false) String nome) {
        return controll.buscaPorNome(nome);
    }

    @GetMapping("/descricao")
    public ResponseEntity<?> getDescricao(@RequestParam(required = false) String descricao) {
        return controll.buscaPorDescricao(descricao);
    }

    // -------------------- CRUD --------------------

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Remedio r) {
        return controll.incluir(r);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Remedio r) {
        return controll.update(r);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return controll.delete(id);
    }
}