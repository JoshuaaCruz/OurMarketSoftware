package model.fabrica;

import model.LivreMercado;
import model.autenticador.Autenticador_if;
import model.autenticador.Credencial_if;
import model.cliente.Cliente_if;
import view.Autenticador_View;
import view.Categoria_Menu_View_if;
import view.Cliente_View;
import view.Credencial_View;
import view.LivreMercado_View;
import view.Produtos_Menu_View_if;

/**
 *
 * @author joshua.cruz
 */
public interface Fabrica_if {

    public LivreMercado_View new_LivreMercado_View(LivreMercado model);
    public Autenticador_View new_Autenticador_View(Autenticador_if model);
    public Credencial_View new_Credencial_View(Credencial_if model);
    public Cliente_View buildClientView(Cliente_if model);

    // Novos menus
    public Categoria_Menu_View_if new_Categoria_Menu_View(LivreMercado model);
    public Produtos_Menu_View_if new_Produtos_Menu_View(LivreMercado model);
}
