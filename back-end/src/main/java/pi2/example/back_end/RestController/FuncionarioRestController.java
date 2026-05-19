package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pi2.example.back_end.Control.FuncionarioControl;
import pi2.example.back_end.Modelo.Funcionario;

@CrossOrigin()
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioRestController {
    private final FuncionarioControl control = new FuncionarioControl();

    @GetMapping("{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        return  control.getById(id);
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        return control.buscaPorNome(nome);
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String filtro) {
        return control.getAllOrFilter(filtro);
    }

    @PostMapping
    public ResponseEntity<?> gravar(@RequestBody Funcionario funcionario){
        System.out.println("FUNCIONARIO: " + funcionario);
        System.out.println("CARGO: " + funcionario.getCargo());
        System.out.println("NASCIMENTO: " + funcionario.getNascimento());
        if (funcionario.getCargo() != null) {
            System.out.println("CARGO ID: " + funcionario.getCargo().getId());
        }

        return control.incluir(funcionario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable int id, @RequestBody Funcionario funcionario) {
        funcionario.setId(id);
        return control.update(funcionario);
    }
}
