
import model.Fabrica;
import model.OurMarket;
import model.fabrica.Fabrica_Grafica;
import model.fabrica.Fabrica_Textual;
import view.OurMarket_View;


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
        OurMarket mercado = new OurMarket();
        OurMarket_View frontEnd = Fabrica.GetViewFabricaConcreta().new_OurMarket_View(mercado);
        frontEnd.mostre();
    }   
}
