package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.BeneficiarioControl;
import pi2.example.back_end.Modelo.Beneficiario;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/beneficiarios")
public class BeneficiarioRestController {
    private final BeneficiarioControl control = new BeneficiarioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return control.getById(id);
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCpf(@RequestParam String cpf) {
        return control.getByCpf(cpf);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Beneficiario beneficiario) {
        return control.incluir(beneficiario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable int id, @RequestBody Beneficiario beneficiario) {
        beneficiario.setId(id);
        return control.update(beneficiario);
    }

}
