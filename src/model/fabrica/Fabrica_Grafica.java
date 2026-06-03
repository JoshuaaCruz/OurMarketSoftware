/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.fabrica;

import model.Fabrica;
import model.OurMarket;
import model.autenticador.Autenticador_if;
import model.autenticador.Credencial_if;
import model.cliente.Cliente;
import model.cliente.Cliente_if;
import view.Autenticador_View;
import view.Cliente_View;
import view.Credencial_View;
import view.OurMarket_View;
import view.Menu_if;
import view.gui.Autenticador_View_Grafico;
import view.gui.CredencialLoginSenha_View_Grafico;
import view.gui.OurMarket_View_Grafico;
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
    public OurMarket_View new_OurMarket_View(OurMarket model) {
        return new OurMarket_View_Grafico(model);
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

    @Override
    public Menu_if new_Menu_View(String menuType, OurMarket model) {
        // GUI menu implementations not yet available
        throw new UnsupportedOperationException("GUI menus not yet implemented for menuType: " + menuType);
    }
}
