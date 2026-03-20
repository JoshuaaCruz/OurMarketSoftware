/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.fabrica;

import model.Fabrica;
import model.LivreMercado;
import model.autenticador.Autenticador_if;
import model.autenticador.Credencial_if;
import model.cliente.Cliente;
import model.cliente.Cliente_if;
import view.Autenticador_View;
import view.Cliente_View;
import view.Credencial_View;
import view.LivreMercado_View;
import view.gui.Autenticador_View_Grafico;
import view.gui.CredencialLoginSenha_View_Grafico;
import view.gui.LivreMercado_View_Grafico;
import view.gui.buildClientViewGrafico;

/**
 *
 * @author joshua.cruz
 */
public class Fabrica_Grafica extends Fabrica {
    @Override
    public Autenticador_View new_Autenticador_View(Autenticador_if model) {
        return new Autenticador_View_Grafico(model);
    }

    public Credencial_View new_Credencial_View(java.awt.Frame parent, boolean modal, Credencial_if model) {
        return new CredencialLoginSenha_View_Grafico(parent, modal, model);
    }

    @Override
    public Credencial_View new_Credencial_View(Credencial_if model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public LivreMercado_View new_LivreMercado_View(LivreMercado model) {
        return new LivreMercado_View_Grafico(model);
    }


    
     public buildClientViewGrafico buildClientView(java.awt.Frame parent, boolean modal, Cliente model) {
        return new buildClientViewGrafico(parent, modal, model);
    }

    @Override
    public Cliente_View buildClientView(Cliente_if model) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Cliente_View new_Cliente_View(java.awt.Frame parent, boolean modal, Cliente model) {
        return new buildClientViewGrafico(parent, modal, model);
    }

}
