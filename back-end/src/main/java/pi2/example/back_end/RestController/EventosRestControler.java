package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.Modelo.Remedio;
import pi2.example.back_end.DAO.DAORemedio;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/remedios")
public class EventosRestControler {

    private DAORemedio dao = new DAORemedio(Banco.getCon());

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(cat_evento_id) invalido"));
        }
        else
        {
            Remedio eve= new Remedio(id);
            eve = dao.get(id);
            if(eve!=null)
                return ResponseEntity.ok(eve);
            else
                return ResponseEntity.badRequest().body(new Erro("Id(cat_evento_id) não encontrado"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        List<Remedio> eventos;
        if(nome != null && !nome.isEmpty())
        {
            eventos = dao.buscarPorCategoria(nome);
            if(!eventos.isEmpty())
            {
                return ResponseEntity.ok(eventos);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum evento nessa categoria!"));
            }
        }
        else
        {
            eventos = dao.buscarPorCategoria("");
            if(!eventos.isEmpty())
            {
                return ResponseEntity.ok(eventos);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum evento cadastrado"));
            }

        }
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Remedio remedio) {
        if (remedio.getNome() != null && !remedio.getNome().isEmpty()) {

            if (remedio.getDescricao() != null && !remedio.getDescricao().isEmpty()) {
                Remedio eve = dao.gravar(remedio);
                if(eve!=null)
                    return ResponseEntity.ok(remedio);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao cadastrar Evento"));
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Descricao(cat_descricao) é obrigatoria"));
            }

        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Nome da categoria(cat_nome) é obrigatoria"));
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Remedio evento) {
        //id invalido
        if (evento.getId() != null && evento.getId()>0) {

            // categoria obrigatória
            if (evento.getNome() != null && !evento.getNome().isEmpty()) {

                // verificar se existe no banco
                Remedio existente = dao.get(evento.getId());
                if (existente != null) {
                    Remedio eve = dao.alterar(evento);
                    if(eve!=null)
                        return ResponseEntity.ok(evento);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao alterar evento"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Categoria é obrigatória para alteração"));
            }
            else
                return ResponseEntity.badRequest().body(new Erro("Categoria é obrigatória para alteração"));
        }
         else
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Remedio eve = new Remedio(id);
        if(dao.apagar(eve))
            return ResponseEntity.ok(String.format("Evento id:[%d] apagado com sucesso!",id));
        else
            return ResponseEntity.badRequest().body(new Erro("id não encontrado"));
    }

}