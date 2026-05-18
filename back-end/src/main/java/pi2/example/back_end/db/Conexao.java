package pi2.example.back_end.db;

import java.sql.*;
public class Conexao {

    private Connection connect;
    private String erro;

    private String url;
    private String user;
    private String password;

    public Conexao(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.erro = "";
    }

    public PreparedStatement preparar(String sql) throws SQLException {
        return connect.prepareStatement(sql);
    }

    // preparar COM retorno de ID
    public PreparedStatement prepararComRetorno(String sql) throws SQLException {
        return connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }


    //  ABRIR CONEXÃO
    public boolean conectar() {
        try {
            connect = DriverManager.getConnection(url, user, password);
            return true;
        } catch (SQLException e) {
            erro = "Erro ao conectar: " + e.getMessage();
            return false;
        }
    }

    //  FECHAR CONEXÃO
    public void desconectar() {
        try {
            if (connect != null && !connect.isClosed()) {
                connect.close();
            }
        } catch (SQLException e) {
            erro = "Erro ao fechar conexão: " + e.getMessage();
        }
    }


    // =========================================
    // TRANSAÇÃO
    // =========================================

    public void iniciarTransacao() throws SQLException {
        connect.setAutoCommit(false);
    }

    public void confirmarTransacao() throws SQLException {
        connect.commit();
        connect.setAutoCommit(true);
    }

    public void desfazerTransacao() {
        try {
            connect.rollback();
            connect.setAutoCommit(true);
        } catch (SQLException e) {
            erro = "Erro rollback: " + e.getMessage();
        }
    }


    public String getMensagemErro() {
        return erro;
    }

    public Connection getConnection() {
        return connect;
    }

    //  INSERT / UPDATE / DELETE (COM PreparedStatement)
    public boolean manipular(String sql, Object... params) {
        boolean executou = false;

        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            int result = ps.executeUpdate();
            executou = result > 0;

        } catch (SQLException e) {
            erro = "Erro: " + e.getMessage();
        }

        return executou;
    }

    //  SELECT
    public ResultSet consultar(String sql, Object... params) {
        try {
            PreparedStatement ps = connect.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeQuery();

        } catch (SQLException e) {
            erro = "Erro: " + e.getMessage();
            return null;
        }
    }
    public void beginTransaction() throws SQLException {

        if (connect != null) {

            connect.setAutoCommit(false);
        }
    }

//
//    public void commit() throws SQLException {
//
//        if (connect != null) {
//
//            connect.commit();
//
//            connect.setAutoCommit(true);
//        }
//    }
//
//    public void rollback() {
//
//        try {
//
//            if (connect != null) {
//
//                connect.rollback();
//
//                connect.setAutoCommit(true);
//            }
//
//        } catch (SQLException e) {
//
//            erro = "Erro rollback: " + e.getMessage();
//        }
//    }

    // MAX PK
    public int getMaxPK(String tabela, String chave) {
        String sql = "SELECT MAX(" + chave + ") FROM " + tabela;

        try (PreparedStatement ps = connect.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            erro = "Erro: " + e.getMessage();
        }

        return -1;
    }


}

