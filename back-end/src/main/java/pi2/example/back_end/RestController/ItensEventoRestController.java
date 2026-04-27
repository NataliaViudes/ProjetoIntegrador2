package pi2.example.back_end.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Controller.ItensEventoControl;
import pi2.example.back_end.Modelo.ItensEvento;

@CrossOrigin
@RestController
@RequestMapping("/itens-evento")
public class ItensEventoRestController {
    private final ItensEventoControl control = new ItensEventoControl();

    //  Buscar todos os itens de um evento
    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<?> getPorEvento(@PathVariable Integer idEvento) {
        return control.getPorEvento(idEvento);
    }

    //  Inserir item no evento
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody ItensEvento item) {
        return control.incluir(item);
    }

    //  Atualizar quantidade do item
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ItensEvento item) {
        return control.alterar(item);
    }

    //  Deletar item (chave composta)
    @DeleteMapping
    public ResponseEntity<?> delete(
            @RequestParam Integer idEvento,
            @RequestParam Integer idEstoque
    ) {
        return control.delete(idEvento, idEstoque);
    }
}
