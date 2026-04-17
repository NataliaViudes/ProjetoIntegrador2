package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pi2.example.back_end.Control.CargoControl;
import pi2.example.back_end.Control.FuncionarioControl;
import pi2.example.back_end.DAO.DAOFuncionario;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/funcionario")
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
        return control.incluir(funcionario);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> apagar(@PathVariable int id) {
        return control.delete(id);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> alterar(@PathVariable int id, @RequestBody Funcionario funcionario) {
        return control.update(funcionario);
    }
}
