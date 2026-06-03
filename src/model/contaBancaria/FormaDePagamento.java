package model.contaBancaria;


public interface FormaDePagamento {
    boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor);
    String getNome();
    String getDescricao(ContaBancaria conta);
}
