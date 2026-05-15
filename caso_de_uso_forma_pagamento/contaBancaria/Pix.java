package contaBancaria;

public class Pix implements FormaDePagamento {

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {

        if (origem == null) {
            return false;
        }
        
        return origem.transferir(valor, destino);
    }
    
}
