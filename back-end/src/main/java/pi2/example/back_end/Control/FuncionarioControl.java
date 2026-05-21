package pi2.example.back_end.Control;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.Funcionario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.Document;
import java.io.ByteArrayOutputStream;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioControl {
    public FuncionarioControl() {}

    private boolean campoVazio(Funcionario f){
        if(f.getNome()!=null && !f.getNome().isEmpty()
                && f.getCpf()!=null && !f.getCpf().isEmpty()
                && f.getTelefone()!=null && !f.getTelefone().isEmpty()
                && f.getSexo()!=null && !f.getSexo().isEmpty()
                && f.getEndereco()!=null && !f.getEndereco().isEmpty()
                && f.getCargo()!=null && f.getCargo().getId()!=null && f.getNascimento()!=null)
            return false;
        return true;
    }

    public ResponseEntity<?> incluir(Funcionario funcionario)
    {
        if (!campoVazio(funcionario)) {
            Funcionario resultado=null;
            Conexao db = Banco.getConexao(); //Abre a conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                resultado = funcionario.incluir(db);
                return ResponseEntity.ok(resultado);

            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
        {
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios!"));
        }
    }

    public ResponseEntity<?> getById(int id)
    {
        if (id < 0) {
            return ResponseEntity.badRequest().body(new Erro("Id(id_funcionario) invalido"));
        }
        else
        {
            Funcionario funcionario= new Funcionario();
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }

                Funcionario resultado = funcionario.buscarporId(id,db);
                if(resultado!=null) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(resultado);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Funcionario id: "+id));
                }
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro ao conectar com o banco de dados"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
    }

    public ResponseEntity<?> buscaPorNome(String nome)
    {
        List<Funcionario> funcionarios;
        Funcionario funcionario= new Funcionario();

        if(nome != null && !nome.isEmpty())
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                funcionarios = funcionario.buscarPorNome(nome,db);
                if(funcionarios!=null && !funcionarios.isEmpty()) { // se encontrar algum Cat_evento
                    return ResponseEntity.ok(funcionarios);
                }
                else {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao buscar Funcionario: "+nome));
                }
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }

        }

        Conexao db = Banco.getConexao(); // Abre conexao
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }
            funcionarios = funcionario.buscarPorNome("",db);
            if(funcionarios!=null && !funcionarios.isEmpty())
            {
                return ResponseEntity.ok(funcionarios);
            }
            else
            {
                return ResponseEntity.badRequest().body(new Erro("Nenhum funcionario com esse nome:: ")+nome);
            }
        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

        } catch (Exception e) {
            System.out.println("Erro geral: " + e.getMessage());
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar(); // não esquece de fechar a conexao com o banco!!
        }
    }


    public ResponseEntity<?> update(Funcionario funcionario)
    {
        Integer id = funcionario.getId();
        //id invalido
        if (id != null && id > 0) {
            // nome obrigatória
            if (!campoVazio(funcionario)) {
                Conexao db = Banco.getConexao(); // Abre conexao
                try {
                    if (!db.conectar()) {
                        throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                    }
                    Funcionario existente = funcionario.buscarporId(funcionario.getId(),db); // verificar se existe no banco
                    if (existente != null) {
                        Funcionario f = funcionario.alterar(db);
                        if(f!=null)
                            return ResponseEntity.ok(funcionario);
                        else
                            return ResponseEntity.badRequest().body(new Erro("Erro ao alterar Cargo"));
                    }
                    else
                        return ResponseEntity.badRequest().body(new Erro("Cargo não encontrado"));
                } catch (SQLException e) {
                    System.out.println("Erro SQL: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

                } catch (Exception e) {
                    System.out.println("Erro geral: " + e.getMessage());
                    return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                } finally {
                    db.desconectar(); // não esquece de fechar a conexao com o banco!!
                }
            }
            return ResponseEntity.badRequest().body(new Erro("Todos os campos são obrigatórios para alteração"));
        }
        else
            return ResponseEntity.badRequest().body(new Erro("ID é obrigatório para alteração"));
    }

    public ResponseEntity<?> delete(Integer id)
    {
        if(id!=null && id>0)
        {
            Conexao db = Banco.getConexao(); // Abre conexao
            try {
                if (!db.conectar()) {
                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                }
                Funcionario funcionario = new Funcionario(id);
                Funcionario existente = funcionario.buscarporId(funcionario.getId(),db); // verificar se existe no banco
                if (existente != null) {
                    if(funcionario.apagar(db))
                        return ResponseEntity.ok(true);
                    else
                        return ResponseEntity.badRequest().body(new Erro("Erro ao excluir funionario"));
                }
                else
                    return ResponseEntity.badRequest().body(new Erro("Funcionario não encontrado"));
            } catch (SQLException e) {
                System.out.println("Erro SQL: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro com o banco"));

            } catch (Exception e) {
                System.out.println("Erro geral: " + e.getMessage());
                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

            } finally {
                db.desconectar(); // não esquece de fechar a conexao com o banco!!
            }
        }
        else
            return ResponseEntity.badRequest().body(new Erro("id invalido"));
    }

    public ResponseEntity<?> getAllOrFilter(String tipo, String filtro) {
        Conexao db = Banco.getConexao();
        Funcionario funcionario = new Funcionario();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            List<Funcionario> lista;

            if (filtro == null || filtro.isEmpty()) {
                lista = funcionario.buscarTodos(db);
            } else {
                lista = funcionario.buscarComFiltro(tipo, filtro, db);
            }

            if (lista == null || lista.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar funcionários: "+e.getMessage());
        } finally {
            db.desconectar();
        }
    }

    public byte[] gerarPdf(Integer id) {
        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }
            Funcionario funcionario = new Funcionario().buscarporId(id, db);

            if (funcionario == null) {
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== FONTES =====
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA,22,Font.BOLD);
            Font subtituloFonte = new Font(Font.FontFamily.HELVETICA,14,Font.BOLD);
            Font labelFonte = new Font(Font.FontFamily.HELVETICA,12,Font.BOLD);
            Font textoFonte = new Font(Font.FontFamily.HELVETICA,12,Font.NORMAL);

            // ===== TITULO =====
            Paragraph titulo = new Paragraph("FICHA DO FUNCIONÁRIO",tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(25);
            document.add(titulo);

            // ===== LINHA =====
            LineSeparator linha = new LineSeparator();
            linha.setPercentage(100);
            document.add(linha);
            document.add(new Paragraph(" "));

            // ===== SUBTITULO =====
            Paragraph dados = new Paragraph("Dados do Funcionário",subtituloFonte);
            dados.setSpacingAfter(15);
            document.add(dados);

            // ===== TABELA =====
            PdfPTable tabela = new PdfPTable(2);
            tabela.setWidthPercentage(100);
            tabela.setSpacingBefore(10);
            tabela.setSpacingAfter(20);
            tabela.setWidths(new float[]{2f, 4f});

            adicionarLinhaTabela(tabela,"Nome",funcionario.getNome(),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"CPF",formatarCpf(funcionario.getCpf()),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"Telefone",formatarTelefone(funcionario.getTelefone()),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"NIS",funcionario.getNis(),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"Sexo",formatarSexo(funcionario.getSexo()),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"Cargo",funcionario.getCargo().getNome(),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"Nascimento",formatarData(funcionario.getNascimento()),labelFonte,textoFonte);
            adicionarLinhaTabela(tabela,"Endereço",funcionario.getEndereco(),labelFonte,textoFonte);

            document.add(tabela);

            // ===== RODAPÉ =====
            Paragraph rodape = new Paragraph(
                    "Documento gerado automaticamente pelo sistema.",
                    new Font(Font.FontFamily.HELVETICA, 10)
            );
            rodape.setAlignment(Element.ALIGN_CENTER);
            document.add(rodape);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.desconectar();
        }
    }

    public byte[] gerarRelatorioCargo(Integer cargoId) {
        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }
            Funcionario model = new Funcionario();
            List<Funcionario> funcionarios =  model.buscarPorCargo(cargoId, db);

            if (funcionarios == null || funcionarios.isEmpty()) {
                return null;
            }
            String nomeCargo = funcionarios.get(0).getCargo().getNome();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate(), 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            // ===== FONTES =====
            Font tituloFonte = new Font(Font.FontFamily.HELVETICA,20,Font.BOLD);
            Font cabecalhoFonte = new Font(Font.FontFamily.HELVETICA,12,Font.BOLD);
            Font textoFonte = new Font(Font.FontFamily.HELVETICA,11);

            // ===== TÍTULO =====
            Paragraph titulo = new Paragraph("RELATÓRIO DE FUNCIONÁRIOS",tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            // ===== INFO DO CARGO =====
            document.add(new Paragraph("Cargo: " + nomeCargo,cabecalhoFonte));
            document.add(new Paragraph("Total de Funcionários: " + funcionarios.size(),textoFonte));
            document.add(new Paragraph(" "));

            // ===== TABELA =====
            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{4f, 3f, 3f, 2f, 3f});

            adicionarCabecalhoTabela(tabela, "Nome", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "CPF", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Telefone", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Sexo", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Nascimento", cabecalhoFonte);

            for (Funcionario f : funcionarios) {
                tabela.addCell(new Phrase(f.getNome(),textoFonte));
                tabela.addCell(new Phrase(formatarCpf(f.getCpf()),textoFonte));
                tabela.addCell(new Phrase(formatarTelefone(f.getTelefone()),textoFonte));
                tabela.addCell(new Phrase(formatarSexo(f.getSexo()),textoFonte));
                tabela.addCell(new Phrase(formatarData(f.getNascimento()),textoFonte));
            }
            document.add(tabela);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.desconectar();
        }
    }

    private void adicionarCabecalhoTabela(PdfPTable tabela,String texto,Font fonte) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        tabela.addCell(cell);
    }

    private void adicionarLinhaTabela(PdfPTable tabela,String label,String valor,Font labelFonte,Font textoFonte) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, labelFonte));
        cellLabel.setPadding(8);

        PdfPCell cellValor = new PdfPCell(new Phrase(valor != null ? valor : "",textoFonte));
        cellValor.setPadding(8);
        tabela.addCell(cellLabel);
        tabela.addCell(cellValor);
    }

    private String formatarCpf(String cpf) {

        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return cpf.replaceFirst(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                "$1.$2.$3-$4"
        );
    }

    private String formatarTelefone(String telefone) {

        if (telefone == null) {
            return "";
        }

        telefone = telefone.replaceAll("\\D", "");

        if (telefone.length() == 11) {

            return telefone.replaceFirst(
                    "(\\d{2})(\\d{5})(\\d{4})",
                    "($1) $2-$3"
            );
        }

        if (telefone.length() == 10) {

            return telefone.replaceFirst(
                    "(\\d{2})(\\d{4})(\\d{4})",
                    "($1) $2-$3"
            );
        }

        return telefone;
    }

    private String formatarSexo(String sexo) {

        if (sexo == null) {
            return "";
        }

        return switch (sexo.toUpperCase()) {

            case "M" -> "Masculino";
            case "F" -> "Feminino";
            default -> "Outro";
        };
    }

    private String formatarData(java.util.Date data) {

        if (data == null) {
            return "";
        }

        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(data);
    }
}

