package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Control.EventoControl;
import pi2.example.back_end.Modelo.Evento;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequestMapping("/evento")
public class EventoRestController {

    private final EventoControl control = new EventoControl();

    // =====================================================
    // LISTAR TODOS (READ GERAL)
    // =====================================================

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        return control.listarTodos();
    }

    // =====================================================
    // GET POR ID
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        return control.getById(id);
    }

    // =====================================================
    // BUSCA POR NOME
    // =====================================================

    @GetMapping("/nome")
    public ResponseEntity<?> getByNome(
            @RequestParam(required = false, defaultValue = "") String nome
    ) {
        return control.buscarPorNome(nome);
    }

    // =====================================================
    // BUSCA POR LOCAL
    // =====================================================

    @GetMapping("/local")
    public ResponseEntity<?> getByLocal(
            @RequestParam(required = false, defaultValue = "") String local
    ) {
        return control.buscarPorLocal(local);
    }

    // =====================================================
    // BUSCA POR PERÍODO (AGENDA)
    // =====================================================

    @GetMapping("/periodo")
    public ResponseEntity<?> getPorPeriodo(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim
    ) {
        return control.buscarPorPeriodo(inicio, fim);
    }

    // =====================================================
    // VERIFICAR CONFLITO (PRÉVIA)
    // =====================================================

    @PostMapping("/conflito")
    public ResponseEntity<?> verificarConflito(
            @RequestBody Evento evento
    ) {
        return control.verificarConflito(evento);
    }

    // =====================================================
    // CRIAR EVENTO
    // =====================================================

    @PostMapping
    public ResponseEntity<?> salvar(
            @RequestBody Evento evento
    ) {
        return control.incluir(evento);
    }

    // =====================================================
    // ATUALIZAR EVENTO
    // =====================================================

    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody Evento evento
    ) {
        return control.alterar(evento);
    }

    // =====================================================
    // DELETAR EVENTO
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id
    ) {
        return control.delete(id);
    }
}
