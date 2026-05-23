package pi2.example.back_end.Modelo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pi2.example.back_end.db.Conexao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Usuario {
    private Integer idUsuario;
    private String login;
    private String senha;
    private Boolean ativo;

    private Funcionario funcionario;

    public Usuario() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public boolean incluir(Conexao con) {
        try {
            String sql = """
                INSERT INTO usuario (login,senha,ativo,id_funcionario) VALUES (?, ?, ?, ?)
            """;

            PreparedStatement ps = con.getConnection().prepareStatement(sql);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String senhaCriptografada = encoder.encode(this.senha);

            ps.setString(1, this.login);
            ps.setString(2, senhaCriptografada);
            ps.setBoolean(3, this.ativo);
            ps.setInt(4,this.funcionario.getId());

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorLogin(String login,Conexao con) {
        try {
            String sql = "SELECT * FROM usuario WHERE login = ?";

            PreparedStatement ps = con.getConnection().prepareStatement(sql);
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setLogin(rs.getString("login"));
                u.setSenha(rs.getString("senha"));
                u.setAtivo(rs.getBoolean("ativo"));

                Funcionario f = new Funcionario().buscarporId(rs.getInt("id_funcionario"),con);
                u.setFuncionario(f);
                return u;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}