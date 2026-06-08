package model.contaBancaria;

public class Boleto implements FormaDePagamento {

    private static final double TAXA = 3.50; // taxa fixa de processamento

    @Override
    public String getNome() {
        return "Boleto";
    }

    @Override
    public String getDescricao(ContaBancaria conta) {
        return String.format("Saldo: R$ %.2f | Taxa fixa: R$ %.2f", conta.getSaldoConta(), TAXA);
    }

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {
        if (origem == null || destino == null || valor <= 0) return false;
        double total = valor + TAXA;
        if (!origem.sacar(total)) return false;
        destino.depositar(valor); //TODO: taxa vai para conta mercado? 
        return true;
    }

    @Override
    public boolean podePagar(ContaBancaria origem, double valor) {
        if (origem == null || valor <= 0) return false;
        double totalComTaxa = valor + TAXA;
        return origem.getSaldoConta() >= totalComTaxa;
    }
}
