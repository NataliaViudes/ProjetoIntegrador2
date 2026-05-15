package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.BeneficiarioControl;
import pi2.example.back_end.Modelo.Beneficiario;

@CrossOrigin
@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioRestController {

    private final BeneficiarioControl controll = new BeneficiarioControl();

    // -------------------- GET ALL --------------------
    @GetMapping
    public ResponseEntity<?> getAll() {
        return controll.getAll();
    }

    // -------------------- GET POR ID --------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") Integer id) {
        return controll.getById(id);
    }

    // -------------------- BUSCAS --------------------

    @GetMapping("/nome")
    public ResponseEntity<?> getNome(@RequestParam(value = "nome", required = false) String nome) {
        return controll.buscaPorNome(nome);
    }

    @GetMapping("/cpf")
    public ResponseEntity<?> getCpf(@RequestParam(value = "cpf", required = false) String cpf) {
        return controll.buscaPorCpf(cpf);
    }

    @GetMapping("/nis")
    public ResponseEntity<?> getNis(@RequestParam(value = "nis", required = false) String nis) {
        return controll.buscaPorNis(nis);
    }


    // -------------------- CRUD --------------------

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Beneficiario b) {
        return controll.incluir(b);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Beneficiario b) {
        return controll.update(b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        return controll.delete(id);
    }
}