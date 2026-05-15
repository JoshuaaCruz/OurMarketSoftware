package contaBancaria;

public class CartaoCredito implements FormaDePagamento {

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {
        
        if (origem == null || destino == null || valor <= 0) {
            return false;
        }
        
        if ((origem.getFatura() + valor) > origem.getLimiteFatura()) {
            return false;
        } else {
        
            origem.incrementaFatura(valor);
            destino.depositar(valor);
            return true;
        }
        
    }
    
}
