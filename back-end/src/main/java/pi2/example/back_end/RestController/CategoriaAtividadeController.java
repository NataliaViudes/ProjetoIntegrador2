package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.DAO.CategoriaAtividadeDAO;
import pi2.example.back_end.DAO.CategoriaAtividadeDAOImpl;
import pi2.example.back_end.Modelo.CategoriaAtividade;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/categoriaAtividade")
public class CategoriaAtividadeController {
    private final CategoriaAtividadeDAO dao;

    public CategoriaAtividadeController() {
        this.dao = new CategoriaAtividadeDAOImpl();
    }

    @GetMapping
    public ResponseEntity<List<CategoriaAtividade>> listar() {
        return ResponseEntity.ok(dao.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaAtividade> buscarPorId(@PathVariable int id) {
        CategoriaAtividade categoria = dao.buscarPorId(id);

        if (categoria != null) {
            return ResponseEntity.ok(categoria);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> salvar(@RequestBody CategoriaAtividade c) {
        boolean ok = dao.salvar(c);

        if (ok) {
            return ResponseEntity.ok("Categoria de atividade cadastrada com sucesso");
        }

        return ResponseEntity.badRequest().body("Erro ao cadastrar categoria de atividade");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody CategoriaAtividade c) {
        c.setId(id);

        boolean ok = dao.atualizar(c);

        if (ok) {
            return ResponseEntity.ok("Categoria de atividade atualizada com sucesso");
        }

        return ResponseEntity.badRequest().body("Erro ao atualizar categoria de atividade");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable int id) {
        boolean ok = dao.excluir(id);

        if (ok) {
            return ResponseEntity.ok("Categoria de atividade excluída com sucesso");
        }

        return ResponseEntity.badRequest().body("Erro ao excluir categoria de atividade");
    }
}
