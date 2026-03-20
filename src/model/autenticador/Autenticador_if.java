/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model.autenticador;

/**
 *
 * @author joshua
 */
public interface Autenticador_if {
    Autenticacao autentique_se(Credencial_if credencial);
    Autenticacao getAutenticacao();
    void clearAutenticacao();
}
