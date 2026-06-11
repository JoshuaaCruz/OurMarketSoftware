package model.autenticador;

public class CredencialLoginSenha {
    private final String usuario;
    private final String senha;

    public CredencialLoginSenha(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }
}
