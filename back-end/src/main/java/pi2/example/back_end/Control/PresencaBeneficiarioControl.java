package pi2.example.back_end.Control;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.DAO.DAOPresencaBeneficiario;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.Modelo.PresencaBeneficiario;
import pi2.example.back_end.Modelo.RelatorioFaltaBeneficiario;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PresencaBeneficiarioControl {
    public PresencaBeneficiarioControl() {
    }

    public ResponseEntity<?> buscaPorIdAgendamento(Integer idAgendamento) {
        if (idAgendamento == null || idAgendamento <= 0) {
            return ResponseEntity.badRequest().body(new Erro("Agendamento invalido"));
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(db);
            dao.criarTabelaSeNaoExistir();

            PresencaBeneficiario presenca = new PresencaBeneficiario();
            List<PresencaBeneficiario> lista = presenca.buscarPorAgendamento(db, idAgendamento);

            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar presencas"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> salvar(List<PresencaBeneficiario> presencas) {
        if (presencas == null || presencas.isEmpty()) {
            return ResponseEntity.badRequest().body(new Erro("Lista de presencas invalida"));
        }

        Integer idAgendamento = presencas.get(0).getIdAgendamento();

        if (idAgendamento == null || idAgendamento <= 0) {
            return ResponseEntity.badRequest().body(new Erro("Agendamento invalido"));
        }

        for (PresencaBeneficiario p : presencas) {
            if (p.getIdAgendamento() == null
                    || !p.getIdAgendamento().equals(idAgendamento)
                    || p.getIdBeneficiario() == null
                    || p.getIdBeneficiario() <= 0) {
                return ResponseEntity.badRequest().body(new Erro("Dados de presenca invalidos"));
            }
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(db);
            dao.criarTabelaSeNaoExistir();

            PresencaBeneficiario presenca = new PresencaBeneficiario();
            presenca.apagarPorAgendamento(db, idAgendamento);

            List<PresencaBeneficiario> resultado = new ArrayList<>();

            for (PresencaBeneficiario p : presencas) {
                PresencaBeneficiario salvo = p.incluir(db);
                if (salvo == null) {
                    return ResponseEntity.badRequest().body(new Erro("Erro ao salvar presenca"));
                }
                resultado.add(salvo);
            }

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));
        } finally {
            db.desconectar();
        }
    }

    public ResponseEntity<?> buscarFaltasPorBeneficiario(Integer idBeneficiario) {
        if (idBeneficiario == null || idBeneficiario <= 0) {
            return ResponseEntity.badRequest().body(new Erro("Beneficiario invalido"));
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(db);
            dao.criarTabelaSeNaoExistir();

            List<RelatorioFaltaBeneficiario> lista = dao.buscarRelatorioPorBeneficiario(idBeneficiario);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar relatorio de faltas"));
        } finally {
            db.desconectar();
        }
    }

    public byte[] gerarPdfFaltas(Integer idBeneficiario) {
        if (idBeneficiario == null || idBeneficiario <= 0) {
            return null;
        }

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar");
            }

            DAOPresencaBeneficiario dao = new DAOPresencaBeneficiario(db);
            dao.criarTabelaSeNaoExistir();

            List<RelatorioFaltaBeneficiario> registros = dao.buscarRelatorioPorBeneficiario(idBeneficiario);
            long totalFaltas = registros.stream()
                    .filter(r -> !Boolean.TRUE.equals(r.getPresente()))
                    .count();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate(), 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            Font tituloFonte = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font cabecalhoFonte = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font textoFonte = new Font(Font.FontFamily.HELVETICA, 11);

            Paragraph titulo = new Paragraph("RELATORIO DE FALTAS", tituloFonte);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);
            document.add(titulo);

            if (!registros.isEmpty()) {
                RelatorioFaltaBeneficiario primeira = registros.get(0);
                document.add(new Paragraph("Beneficiario: " + primeira.getBeneficiario(), textoFonte));
                document.add(new Paragraph("CPF: " + primeira.getCpf(), textoFonte));
            } else {
                document.add(new Paragraph("Beneficiario ID: " + idBeneficiario, textoFonte));
            }

            document.add(new Paragraph("Total de registros: " + registros.size(), textoFonte));
            document.add(new Paragraph("Total de faltas: " + totalFaltas, textoFonte));
            document.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{4f, 3f, 3f, 3f, 2f});

            adicionarCabecalhoTabela(tabela, "Atividade", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Funcionario", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Inicio", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Fim", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Status", cabecalhoFonte);

            for (RelatorioFaltaBeneficiario falta : registros) {
                tabela.addCell(new Phrase(valor(falta.getAtividade()), textoFonte));
                tabela.addCell(new Phrase(valor(falta.getFuncionario()), textoFonte));
                tabela.addCell(new Phrase(formatarData(falta.getDataInicio()), textoFonte));
                tabela.addCell(new Phrase(formatarData(falta.getDataFim()), textoFonte));
                tabela.addCell(new Phrase(Boolean.TRUE.equals(falta.getPresente()) ? "Presente" : "Falta", textoFonte));
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

    private void adicionarCabecalhoTabela(PdfPTable tabela, String texto, Font fonte) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, fonte));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        tabela.addCell(cell);
    }

    private String valor(String valor) {
        return valor != null ? valor : "";
    }

    private String formatarData(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }

        try {
            DateTimeFormatter entrada = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            DateTimeFormatter saida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return LocalDateTime.parse(valor, entrada).format(saida);
        } catch (Exception e) {
            return valor;
        }
    }
}
