package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.CargoControl;
import pi2.example.back_end.Modelo.Cargo;

@CrossOrigin
@RestController
@RequestMapping("/cargos")
public class CargoRestController {
    private final CargoControl control = new CargoControl();

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return  control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam (value = "nome", required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam (value = "filtro", required = false) String filtro){
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Cargo cargo) {
        return control.incluir(cargo);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Cargo cargo) {
        return control.update(cargo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Integer id) {
        return control.delete(id);
    }
}
