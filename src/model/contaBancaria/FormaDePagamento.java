package model.contaBancaria;


public interface FormaDePagamento {
    boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor);
    boolean podePagar(ContaBancaria origem, double valor);
    String getNome();
    String getDescricao(ContaBancaria conta);
}
