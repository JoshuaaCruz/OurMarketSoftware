/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.fabrica;

import model.Fabrica;
import model.LivreMercado;
import model.autenticador.Autenticador_if;
import model.autenticador.Credencial_if;
import model.cliente.Cliente_if;
import view.Autenticador_View;
import view.Cliente_View;
import view.LivreMercado_View;
import view.terminal.Autenticador_View_Textual;
import view.Credencial_View;
import view.terminal.CredencialLoginSenha_View_Textual;
import view.terminal.LivreMercado_View_Textual;

/**
 *
 * @author joshua.cruz
 */
public class Fabrica_Textual extends Fabrica {
    @Override
    public Autenticador_View new_Autenticador_View(Autenticador_if model) {
        return new Autenticador_View_Textual(model);
    }

    @Override
    public LivreMercado_View new_LivreMercado_View(LivreMercado model) {
        return new LivreMercado_View_Textual(model);
    }

    @Override
    public Credencial_View new_Credencial_View(Credencial_if model) {
        return new CredencialLoginSenha_View_Textual(model);
    }

    @Override
    public Cliente_View buildClientView(Cliente_if model) {
//AINDA NÃO IMPLEMENTADO
return null;
    }

}
