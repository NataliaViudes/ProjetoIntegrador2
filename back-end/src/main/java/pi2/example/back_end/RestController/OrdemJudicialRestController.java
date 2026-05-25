package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.OrdemJudicialControl;
import pi2.example.back_end.Modelo.OrdemJudicial;

@RestController
@RequestMapping("/ordem-judicial")
@CrossOrigin(origins = "http://localhost:3000")
public class OrdemJudicialRestController {

    private final OrdemJudicialControl control = new OrdemJudicialControl();

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody OrdemJudicial o) {
        return control.salvar(o);
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return control.listarTodos();
    }

    @GetMapping("/{beneficiarioId}")
    public ResponseEntity<?> get(@PathVariable Integer beneficiarioId) {
        return control.getByBeneficiario(beneficiarioId);
    }

    @DeleteMapping("/{beneficiarioId}")
    public ResponseEntity<?> delete(@PathVariable Integer beneficiarioId) {
        return control.delete(beneficiarioId);
    }
}