package pi2.example.back_end.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.Modelo.Cat_Evento;
import pi2.example.back_end.DAO.DAOEvento;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eventos")
public class EventosRestControler {

    private DAOEvento dao = new DAOEvento(Banco.getCon());

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(cat_evento_id) invalido"));
        }
        else
        {
            Cat_Evento eve= new Cat_Evento(id);
            eve = dao.get(id);
            if(eve!=null)
                return ResponseEntity.ok(eve);
            else
                return ResponseEntity.badRequest().body(new Erro("Id(cat_evento_id) não encontrado"));
        }
    }

    @GetMapping("nome")
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        List<Cat_Evento> eventos;
        if(nome != null && !nome.isEmpty())
        {
            eventos = dao.buscarPorNome(nome);
            if(!eventos.isEmpty())
            {
                return ResponseEntity.ok(eventos);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum evento nessa categoria: ")+nome);
            }
        }
        else
        {
            eventos = dao.buscarPorNome("");
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

    @GetMapping("descricao")
    public ResponseEntity<?> getDescricao(@RequestParam (required = false) String nome) {
        List<Cat_Evento> eventos;
        if(nome != null && !nome.isEmpty())
        {
            eventos = dao.buscarPorNome(nome);
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
            eventos = dao.buscarPorNome("");
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
    public ResponseEntity<?> salvar(@RequestBody Cat_Evento cat_evento) {
        if (cat_evento.getNome() != null && !cat_evento.getNome().isEmpty()) {

            if (cat_evento.getDescricao() != null && !cat_evento.getDescricao().isEmpty()) {
                Cat_Evento eve = dao.gravar(cat_evento);
                if(eve!=null)
                    return ResponseEntity.ok(cat_evento);
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
    public ResponseEntity<?> update(@RequestBody Cat_Evento evento) {
        //id invalido
        if (evento.getId() != null && evento.getId()>0) {

            // categoria obrigatória
            if (evento.getNome() != null && !evento.getNome().isEmpty()) {

                // verificar se existe no banco
                Cat_Evento existente = dao.get(evento.getId());
                if (existente != null) {
                    Cat_Evento eve = dao.alterar(evento);
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
        Cat_Evento eve = new Cat_Evento(id);
        if(dao.apagar(eve))
            return ResponseEntity.ok(String.format("Evento id:[%d] apagado com sucesso!",id));
        else
            return ResponseEntity.badRequest().body(new Erro("id não encontrado"));
    }

}