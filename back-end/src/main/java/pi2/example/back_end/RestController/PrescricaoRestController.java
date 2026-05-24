package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.PrescricaoControl;
import pi2.example.back_end.Modelo.Prescricao;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/prescricoes")
public class PrescricaoRestController {

    private final PrescricaoControl controll = new PrescricaoControl();

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

    @GetMapping("/beneficiario")
    public ResponseEntity<?> getBeneficiario(
            @RequestParam(required = false) Integer idBeneficiario) {

        return controll.buscaPorBeneficiario(idBeneficiario);
    }

    @GetMapping("/remedio")
    public ResponseEntity<?> getRemedio(
            @RequestParam(required = false) Integer idRemedio) {

        return controll.buscaPorRemedio(idRemedio);
    }

    // -------------------- CRUD --------------------

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Prescricao p) {
        return controll.incluir(p);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Prescricao p) {
        return controll.update(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return controll.delete(id);
    }
}