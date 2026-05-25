package pi2.example.back_end.Control;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.http.ResponseEntity;
import pi2.example.back_end.Modelo.Beneficiario;
import pi2.example.back_end.Modelo.Erro;
import pi2.example.back_end.db.Banco;
import pi2.example.back_end.db.Conexao;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BeneficiarioControl {
    public BeneficiarioControl() {}

    // -------------------- INCLUIR --------------------
    public ResponseEntity<?> incluir(Beneficiario b)
    {
        if (b.getNome() != null && !b.getNome().isEmpty()) {

            if (b.getNascimento() != null) {

                if (b.getIdade() != null && b.getIdade() > 0) {

                    if (b.getRg() != null && !b.getRg().isEmpty()) {

                        if (b.getCpf() != null && !b.getCpf().isEmpty()) {

                            if (b.getNis() != null && !b.getNis().isEmpty()) {

                                if (b.getRenda() != null && b.getRenda() >= 0) {

                                    if (b.getEndereco() != null && !b.getEndereco().isEmpty()) {

                                        if (b.getBairro() != null && !b.getBairro().isEmpty()) {

                                            if (b.getTipoResidencia() != null && !b.getTipoResidencia().isEmpty()) {

                                                if (b.getTelefone() != null && !b.getTelefone().isEmpty()) {

                                                    if (b.getCelular() != null && !b.getCelular().isEmpty()) {

                                                        if (b.getCelularRecado() != null && !b.getCelularRecado().isEmpty()) {

                                                            if (b.getAlergias() != null && !b.getAlergias().isEmpty()) {

                                                                if (b.getTratamentos() != null && !b.getTratamentos().isEmpty()) {


                                                                    if (b.getParticipacao() != null && !b.getParticipacao().isEmpty()) {

                                                                        if (b.getSituacao() != null && !b.getSituacao().isEmpty()) {

                                                                            Conexao db = Banco.getConexao();
                                                                            try {
                                                                                if (!db.conectar()) {
                                                                                    throw new Exception("Erro ao conectar: " + db.getMensagemErro());
                                                                                }

                                                                                Beneficiario resultado = b.incluir(db);
                                                                                return ResponseEntity.ok(resultado);

                                                                            } catch (SQLException e) {
                                                                                return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

                                                                            } catch (Exception e) {
                                                                                return ResponseEntity.badRequest().body(new Erro("Erro geral"));

                                                                            } finally {
                                                                                db.desconectar();
                                                                            }

                                                                        } else return ResponseEntity.badRequest().body(new Erro("Situação é obrigatória"));
                                                                    } else return ResponseEntity.badRequest().body(new Erro("Participação é obrigatória"));
                                                                } else return ResponseEntity.badRequest().body(new Erro("Tratamentos são obrigatórios"));
                                                            } else return ResponseEntity.badRequest().body(new Erro("Alergias são obrigatórias"));
                                                        } else return ResponseEntity.badRequest().body(new Erro("Celular de recado é obrigatório"));
                                                    } else return ResponseEntity.badRequest().body(new Erro("Celular é obrigatório"));
                                                } else return ResponseEntity.badRequest().body(new Erro("Telefone é obrigatório"));
                                            } else return ResponseEntity.badRequest().body(new Erro("Tipo de residência é obrigatório"));
                                        } else return ResponseEntity.badRequest().body(new Erro("Bairro é obrigatório"));
                                    } else return ResponseEntity.badRequest().body(new Erro("Endereço é obrigatório"));
                                } else return ResponseEntity.badRequest().body(new Erro("Renda é obrigatória"));
                            } else return ResponseEntity.badRequest().body(new Erro("NIS é obrigatório"));
                        } else return ResponseEntity.badRequest().body(new Erro("CPF é obrigatório"));
                    } else return ResponseEntity.badRequest().body(new Erro("RG é obrigatório"));
                } else return ResponseEntity.badRequest().body(new Erro("Idade é obrigatória"));
            } else return ResponseEntity.badRequest().body(new Erro("Nascimento é obrigatório"));
        } else return ResponseEntity.badRequest().body(new Erro("Nome é obrigatório"));
    }

    // -------------------- BUSCAR TODOS --------------------
    public ResponseEntity<?> getAll() {

        Conexao db = Banco.getConexao();

        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario b = new Beneficiario();
            List<Beneficiario> lista = b.getAll(db);

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Erro("Erro ao buscar beneficiários"));
        } finally {
            db.desconectar();
        }
    }


    // -------------------- BUSCAR POR ID --------------------
    public ResponseEntity<?> getById(Integer id)
    {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));
        }

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario b = new Beneficiario();
            Beneficiario resultado = b.buscarPorId(id, db);

            if (resultado != null)
                return ResponseEntity.ok(resultado);
            else
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR NOME --------------------
    public ResponseEntity<?> buscaPorNome(String nome)
    {
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorNome(nome != null ? nome : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR CPF --------------------
    public ResponseEntity<?> buscaPorCpf(String cpf)
    {
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorCpf(cpf != null ? cpf : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- BUSCAR POR NIS --------------------
    public ResponseEntity<?> buscaPorNis(String nis)
    {
        List<Beneficiario> lista;
        Beneficiario b = new Beneficiario();

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            lista = b.buscarPorNis(nis != null ? nis : "", db);

            if (lista != null && !lista.isEmpty())
                return ResponseEntity.ok(lista);
            else
                return ResponseEntity.badRequest().body(new Erro("Nenhum beneficiário encontrado"));

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- UPDATE --------------------
    public ResponseEntity<?> update(Beneficiario b)
    {
        if (b.getId() == null || b.getId() <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID obrigatório"));

        if (b.getNome() == null || b.getNome().isEmpty())
            return ResponseEntity.badRequest().body(new Erro("Nome obrigatório"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario existente = b.buscarPorId(b.getId(), db);

            if (existente != null) {
                Beneficiario atualizado = b.alterar(db);

                if (atualizado != null)
                    return ResponseEntity.ok(atualizado);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao atualizar"));
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
            }

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    // -------------------- DELETE --------------------
    public ResponseEntity<?> delete(Integer id)
    {
        if (id == null || id <= 0)
            return ResponseEntity.badRequest().body(new Erro("ID inválido"));

        Conexao db = Banco.getConexao();
        try {
            if (!db.conectar()) {
                throw new Exception("Erro ao conectar: " + db.getMensagemErro());
            }

            Beneficiario b = new Beneficiario(id);
            Beneficiario existente = b.buscarPorId(id, db);

            if (existente != null) {
                if (b.apagar(db))
                    return ResponseEntity.ok(true);
                else
                    return ResponseEntity.badRequest().body(new Erro("Erro ao excluir"));
            }
            else {
                return ResponseEntity.badRequest().body(new Erro("Beneficiário não encontrado"));
            }

        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(new Erro("Erro com banco"));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro geral"));

        } finally {
            db.desconectar();
        }
    }

    public byte[] gerarPdf(Integer id) {

        Conexao db = Banco.getConexao();

        try {

            if (!db.conectar()) {
                throw new Exception("Erro conectar");
            }

            Beneficiario beneficiario =
                    new Beneficiario().buscarPorId(id, db);

            if (beneficiario == null) {
                return null;
            }

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            Document document =
                    new Document(
                            PageSize.A4,
                            40,
                            40,
                            50,
                            50
                    );

            PdfWriter.getInstance(document, out);

            document.open();

            Font tituloFonte =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            22,
                            Font.BOLD
                    );

            Font subtituloFonte =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            14,
                            Font.BOLD
                    );

            Font labelFonte =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            12,
                            Font.BOLD
                    );

            Font textoFonte =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            12
                    );

            Paragraph titulo =
                    new Paragraph(
                            "FICHA DO BENEFICIÁRIO",
                            tituloFonte
                    );

            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(25);

            document.add(titulo);

            LineSeparator linha = new LineSeparator();

            linha.setPercentage(100);

            document.add(linha);

            document.add(new Paragraph(" "));

            Paragraph dados =
                    new Paragraph(
                            "Dados do Beneficiário",
                            subtituloFonte
                    );

            dados.setSpacingAfter(15);

            document.add(dados);

            PdfPTable tabela =
                    new PdfPTable(2);

            tabela.setWidthPercentage(100);

            tabela.setWidths(
                    new float[]{2f, 4f}
            );

            adicionarLinhaTabela(
                    tabela,
                    "Nome",
                    beneficiario.getNome(),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "CPF",
                    formatarCpf(
                            beneficiario.getCpf()
                    ),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "RG",
                    beneficiario.getRg(),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "Telefone",
                    formatarTelefone(
                            beneficiario.getTelefone()
                    ),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "Endereço",
                    beneficiario.getEndereco(),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "Nascimento",
                    formatarData(
                            beneficiario.getNascimento()
                    ),
                    labelFonte,
                    textoFonte
            );

            adicionarLinhaTabela(
                    tabela,
                    "Situação",
                    beneficiario.getSituacao(),
                    labelFonte,
                    textoFonte
            );

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

    // ================= RELATÓRIO =================

    public byte[] gerarRelatorioBeneficiarios(List<Beneficiario> lista) {

        try {

            if (lista == null || lista.isEmpty()) {
                return null;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(
                    PageSize.A4.rotate(),
                    40,
                    40,
                    50,
                    50
            );

            PdfWriter.getInstance(document, out);

            document.open();

            Font tituloFonte = new Font(
                    Font.FontFamily.HELVETICA,
                    20,
                    Font.BOLD
            );

            Font cabecalhoFonte = new Font(
                    Font.FontFamily.HELVETICA,
                    12,
                    Font.BOLD
            );

            Font textoFonte = new Font(
                    Font.FontFamily.HELVETICA,
                    11
            );

            Paragraph titulo = new Paragraph(
                    "RELATÓRIO DE BENEFICIÁRIOS",
                    tituloFonte
            );

            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(15);

            document.add(titulo);

            PdfPTable tabela = new PdfPTable(6);

            tabela.setWidthPercentage(100);

            tabela.setWidths(new float[]{
                    4f,
                    3f,
                    2f,
                    3f,
                    3f,
                    3f
            });

            adicionarCabecalhoTabela(tabela, "Nome", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "CPF", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Telefone", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Situação", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Nascimento", cabecalhoFonte);
            adicionarCabecalhoTabela(tabela, "Bairro", cabecalhoFonte);

            for (Beneficiario b : lista) {
                tabela.addCell(new Phrase(b.getNome(), textoFonte));
                tabela.addCell(new Phrase(formatarCpf(b.getCpf()), textoFonte));
                tabela.addCell(new Phrase(formatarTelefone(b.getTelefone()), textoFonte));
                tabela.addCell(new Phrase(b.getSituacao(), textoFonte));
                tabela.addCell(new Phrase(formatarData(b.getNascimento()), textoFonte));
                tabela.addCell(new Phrase(b.getBairro(), textoFonte));
            }

            document.add(tabela);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // ================= AUX =================

    private void adicionarCabecalhoTabela(
            PdfPTable tabela,
            String texto,
            Font fonte
    ) {

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(texto, fonte)
                );

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        cell.setPadding(8);

        tabela.addCell(cell);
    }

    private void adicionarLinhaTabela(
            PdfPTable tabela,
            String label,
            String valor,
            Font labelFonte,
            Font textoFonte
    ) {

        PdfPCell cellLabel =
                new PdfPCell(
                        new Phrase(label, labelFonte)
                );

        cellLabel.setPadding(8);

        PdfPCell cellValor =
                new PdfPCell(
                        new Phrase(
                                valor != null
                                        ? valor
                                        : "",
                                textoFonte
                        )
                );

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

    private String formatarTelefone(
            String telefone
    ) {

        if (telefone == null) {
            return "";
        }

        telefone =
                telefone.replaceAll("\\D", "");

        if (telefone.length() == 11) {

            return telefone.replaceFirst(
                    "(\\d{2})(\\d{5})(\\d{4})",
                    "($1) $2-$3"
            );
        }

        return telefone;
    }

    private String formatarData(String data) {

        if (data == null || data.isEmpty()) {
            return "";
        }

        try {

            if (data.contains("T")) {
                data = data.split("T")[0];
            }

            String[] partes = data.split("-");

            if (partes.length == 3) {

                return partes[2]
                        + "/"
                        + partes[1]
                        + "/"
                        + partes[0];
            }

            return data;

        } catch (Exception e) {

            return data;
        }
    }

}