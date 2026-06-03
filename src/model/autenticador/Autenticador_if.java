package model.autenticador;

public interface Autenticador_if {
    Autenticacao autentique_se(Credencial_if credencial);
    Autenticacao getAutenticacao();
    void clearAutenticacao();
}
