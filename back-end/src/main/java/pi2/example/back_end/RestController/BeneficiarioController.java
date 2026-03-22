package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.DALBeneficiario;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/beneficiario")
public class BeneficiarioController {

    private final DALBeneficiario dal;

    public BeneficiarioController() {
        if (!Banco.conectar()) {
            throw new RuntimeException("Erro ao conectar com o banco");
        }
        this.dal = new DALBeneficiario(Banco.getCon());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Beneficiario> getId(@PathVariable int id) {
        Beneficiario b = dal.get(id);

        if (b != null)
            return ResponseEntity.ok(b);

        return ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<List<Beneficiario>> get(
            @RequestParam(required = false) String filtro) {

        List<Beneficiario> lista = dal.get(filtro);

        return ResponseEntity.ok(lista);
    }


    @GetMapping("/buscar")
    public ResponseEntity<Beneficiario> buscarPorCpf(@RequestParam String cpf) {

        List<Beneficiario> lista = dal.get("cpf = '" + cpf + "'");

        if (lista != null && !lista.isEmpty())
            return ResponseEntity.ok(lista.get(0));

        return ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity<String> gravar(@RequestBody Beneficiario beneficiario) {

        boolean ok = dal.gravar(beneficiario);

        if (ok)
            return ResponseEntity.ok("Inserido com sucesso");

        return ResponseEntity.badRequest().body("Erro ao inserir");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> apagar(@PathVariable int id) {

        Beneficiario b = new Beneficiario();
        b.setId(id);

        boolean ok = dal.apagar(b);

        if (ok)
            return ResponseEntity.ok("Removido com sucesso");

        return ResponseEntity.notFound().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> alterar(
            @PathVariable int id,
            @RequestBody Beneficiario beneficiario) {

        beneficiario.setId(id);

        boolean ok = dal.alterar(beneficiario);

        if (ok)
            return ResponseEntity.ok("Atualizado com sucesso");

        return ResponseEntity.badRequest().body("Erro ao atualizar");
    }
}