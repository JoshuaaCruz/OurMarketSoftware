package model.contaBancaria;

public class Pix implements FormaDePagamento {

    @Override
    public String getNome() {
        return "Pix";
    }

    @Override
    public String getDescricao(ContaBancaria conta) {
        return String.format("Saldo: R$ %.2f", conta.getSaldoConta());
    }

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {
        if (origem == null || destino == null || valor <= 0) return false;
        if (!origem.sacar(valor)) return false;
        destino.depositar(valor);
        return true;
    }

    @Override
    public boolean podePagar(ContaBancaria origem, double valor) {
        if (origem == null || valor <= 0) return false;
        return origem.getSaldoConta() >= valor;
    }
    
}
