package contaBancaria;

public interface FormaDePagamento {
    boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor);
}
