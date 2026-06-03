package model.autenticador;

public class Autenticacao {
    Autenticacao(model.autenticador.Credencial_if credencialUsada) {
        this.credencialUsada = credencialUsada;
    }

    private final Credencial_if credencialUsada;

    Credencial_if getCredencialUsada() {
        return credencialUsada;
    }

}
