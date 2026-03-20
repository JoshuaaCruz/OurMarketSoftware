/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.autenticador;

/**
 *
 * @author joshua
 */
public class AutenticadorDefault implements Autenticador_if {
    
    @Override
    public Autenticacao autentique_se(Credencial_if credencial) {
        // verifica se a credencial eh vaalida
        boolean ehValida = true;
        if (ehValida) {
            autenticacao = new Autenticacao(credencial);
        } else {
            autenticacao = null;
        }
        return autenticacao;
    }

    @Override
    public void clearAutenticacao() {
        autenticacao = null;
    }

    @Override
    public Autenticacao getAutenticacao() {
        return autenticacao;
    }
    
    private Autenticacao autenticacao;
}
