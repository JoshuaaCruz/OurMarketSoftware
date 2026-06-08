/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.fabrica;

import model.Fabrica;
import model.OurMarket;
import model.cliente.Cliente_if;
import view.Cliente_View;
import view.OurMarket_View;
import view.Menu_if;
import view.terminal.Categoria_Menu_View_Textual;
import view.terminal.OurMarket_View_Textual;
import view.terminal.MenuFormaPagamento_View_Textual;
import view.terminal.Produtos_Menu_View_Textual;

public class Fabrica_Textual extends Fabrica {

    @Override
    public OurMarket_View new_OurMarket_View(OurMarket model) {
        return new OurMarket_View_Textual(model);
    }

    @Override
    public Cliente_View buildClientView(Cliente_if model) {
        // AINDA NÃO IMPLEMENTADO
        return null;
    }

    @Override
    public Menu_if new_Menu_View(String menuType, OurMarket model) {
        switch (menuType.toLowerCase()) {
            case "produtos":
                return new Produtos_Menu_View_Textual(model);
            case "categorias":
                return new Categoria_Menu_View_Textual(model);
            case "forma_pagamento":
            case "forma pagamento":
                if (model.getClienteLogado() == null || model.getClienteLogado().getContaCliente() == null) {
                    throw new IllegalStateException("Nenhum cliente com conta cadastrado.");
                }
                return new MenuFormaPagamento_View_Textual(model.getClienteLogado().getContaCliente(), new java.util.Scanner(System.in));
            default:
                throw new IllegalArgumentException("Unknown menu type: " + menuType);
        }
    }
}
