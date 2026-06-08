package model.fabrica;

import model.OurMarket;
import model.cliente.Cliente_if;
import view.Cliente_View;
import view.OurMarket_View;
import view.Menu_if;

public interface Fabrica_if {

    public OurMarket_View new_OurMarket_View(OurMarket model);
    public Cliente_View buildClientView(Cliente_if model);

    /**
     * Factory method for creating unified menu views
     * @param menuType Type of menu: "produtos", "categorias", "forma_pagamento"
     * @param model The OurMarket model
     * @return Menu_if implementation for the specified menu type
     */
    public Menu_if new_Menu_View(String menuType, OurMarket model);
}
