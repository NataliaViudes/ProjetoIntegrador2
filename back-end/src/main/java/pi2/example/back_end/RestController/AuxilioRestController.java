package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.AuxilioControl;
import pi2.example.back_end.Modelo.Auxilio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/auxilios")
public class AuxilioRestController {

    private final AuxilioControl control = new AuxilioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return control.getById(id);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Auxilio auxilio) {
        return control.incluir(auxilio);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable(value = "id") int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable(value = "id") int id, @RequestBody Auxilio auxilio) {
        auxilio.setId(id);
        return control.update(auxilio);
    }

}