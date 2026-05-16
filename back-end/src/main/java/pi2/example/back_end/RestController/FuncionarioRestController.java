package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.FuncionarioControl;
import pi2.example.back_end.Modelo.Funcionario;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRestController {

    private final FuncionarioControl control = new FuncionarioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable(value = "id") int id) {
        return  control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam(value = "nome", required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(value = "tipo", required = false) String tipo,@RequestParam(value = "filtro", required = false) String filtro) {
        return control.getAllOrFilter(tipo, filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Funcionario funcionario){
        return control.incluir(funcionario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable(value = "id") int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable(value = "id") int id, @RequestBody Funcionario funcionario) {
        funcionario.setId(id);
        return control.update(funcionario);
    }
}
