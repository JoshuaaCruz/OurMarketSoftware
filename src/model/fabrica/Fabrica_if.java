package model.fabrica;

import model.LivreMercado;
import model.autenticador.Autenticador_if;
import model.autenticador.Credencial_if;
import model.cliente.Cliente_if;
import view.Autenticador_View;
import view.Cliente_View;
import view.Credencial_View;
import view.LivreMercado_View;
import view.Menu_if;

/**
 *
 * @author joshua.cruz
 */
public interface Fabrica_if {

    public LivreMercado_View new_LivreMercado_View(LivreMercado model);
    public Autenticador_View new_Autenticador_View(Autenticador_if model);
    public Credencial_View new_Credencial_View(Credencial_if model);
    public Cliente_View buildClientView(Cliente_if model);

    /**
     * Factory method for creating unified menu views
     * @param menuType Type of menu: "produtos", "categorias"
     * @param model The LivreMercado model
     * @return Menu_if implementation for the specified menu type
     */
    public Menu_if new_Menu_View(String menuType, LivreMercado model);
}
