package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Banco;
import pi2.example.back_end.Modelo.Evento;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.DALFuncionarios;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/funcionario")
public class FuncionarioRestController {
    private final DALFuncionarios dalFuncionarios;

    public FuncionarioRestController (){
        Banco.conectar();
        this.dalFuncionarios = new DALFuncionarios(pi2.example.back_end.db.Banco.getCon());
    }

    @GetMapping("{id}")
    public ResponseEntity<Funcionario> getId(@PathVariable int id){
        Funcionario funcionario = dalFuncionarios.get(id);
        if(funcionario!=null)
            return ResponseEntity.ok(funcionario);
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> get(@RequestParam(required = false) String filtro) {
        List<Funcionario> funcionarios;
        if (filtro == null || filtro.isEmpty())
            funcionarios = dalFuncionarios.getAll();
        else
            funcionarios = dalFuncionarios.get(filtro);
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<String> gravar(@RequestBody Funcionario funcionario){
        boolean ok = dalFuncionarios.gravar(funcionario);
        if (ok)
            return ResponseEntity.ok("Inserido com sucesso");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> apagar(@PathVariable int id) {
        Funcionario f = new Funcionario();
        f.setId(id);
        boolean ok = dalFuncionarios.apagar(f);

        if (ok)
            return ResponseEntity.ok("Removido com sucesso");
        return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<String> alterar(@PathVariable int id, @RequestBody Funcionario funcionario) {
        funcionario.setId(id);
        boolean ok = dalFuncionarios.alterar(funcionario);

        if (ok)
            return ResponseEntity.ok("Atualizado com sucesso");
        return ResponseEntity.notFound().build();
    }
}
