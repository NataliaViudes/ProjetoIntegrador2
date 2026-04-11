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
public class RemedioRestControler {

    private DAORemedio dao = new DAORemedio(Banco.getCon());

    @GetMapping("/{id}")
    public ResponseEntity<?> getId(@PathVariable int id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(id) invalido"));
        }
        else
        {
            Remedio rem= new Remedio(id);
            rem = dao.get(id);
            if(rem!=null)
                return ResponseEntity.ok(rem);
            else
                return ResponseEntity.badRequest().body(new Erro("Id(id) não encontrado"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getNome(@RequestParam (required = false) String nome) {
        List<Remedio> remedios;
        if(nome != null && !nome.isEmpty())
        {
            remedios = dao.buscarPorNome(nome);
            if(!remedios.isEmpty())
            {
                return ResponseEntity.ok(remedios);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum remedio com esse nome: "+nome));
            }
        }
        else
        {
            remedios = dao.buscarPorNome("");
            if(!remedios.isEmpty())
            {
                return ResponseEntity.ok(remedios);
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
                Remedio rem = dao.gravar(remedio);
                if(rem!=null)
                    return ResponseEntity.ok(remedio);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao cadastrar Remedio"));
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Descricao(descricao) é obrigatoria"));
            }

        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Nome do Remedio(nome) é obrigatoria"));
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
                    Remedio rem = dao.alterar(evento);
                    if(rem!=null)
                        return ResponseEntity.ok(rem);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao alterar remedio"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Nome é obrigatória para alteração"));
            }
            else
                return ResponseEntity.badRequest().body(new Erro("Nome é obrigatória para alteração"));
        }
         else
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Remedio rem = new Remedio(id);
        if(dao.apagar(rem))
            return ResponseEntity.ok(String.format("Evento id:[%d] apagado com sucesso!",id));
        else
            return ResponseEntity.badRequest().body(new Erro("id não encontrado"));
    }

}