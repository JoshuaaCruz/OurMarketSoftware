/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.autenticador;

/**
 *
 * @author joshua
 */
public class Autenticacao {
    Autenticacao(model.autenticador.Credencial_if credencialUsada) {
        this.credencialUsada = credencialUsada;
    }

    private final Credencial_if credencialUsada;

    Credencial_if getCredencialUsada() {
        return credencialUsada;
    }

}
