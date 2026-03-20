
import model.Fabrica;
import model.LivreMercado;
import model.fabrica.Fabrica_Grafica;
import model.fabrica.Fabrica_Textual;
import view.LivreMercado_View;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Main {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            for (String arg: args) {
                if (arg.equals("--gui")) {
                    Fabrica.SetFabricaConcreta(new Fabrica_Grafica());
                } else if (arg.equals("--terminal")) {
                    Fabrica.SetFabricaConcreta(new Fabrica_Textual());
                }
            }
        }
        LivreMercado mercado = new LivreMercado();
        LivreMercado_View frontEnd = Fabrica.GetViewFabricaConcreta().new_LivreMercado_View(mercado);
        frontEnd.mostre();
    }   
}
