package model.contaBancaria;

public class CartaoCredito implements FormaDePagamento {

    @Override
    public String getNome() {
        return "Cartão de Crédito";
    }

    @Override
    public String getDescricao(ContaBancaria conta) {
        return String.format("Limite: R$ %.2f | Fatura: R$ %.2f | Disponível: R$ %.2f",
                conta.getLimiteFatura(), conta.getFatura(),
                conta.getLimiteFatura() - conta.getFatura());
    }

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {
        if (origem == null || destino == null || valor <= 0) {
            return false;
        }

        if (origem.getFatura() + valor > origem.getLimiteFatura()) {
            return false;
        }

        origem.incrementaFatura(valor);
        destino.depositar(valor);
        return true;
    }

    @Override
    public boolean podePagar(ContaBancaria origem, double valor) {
        if (origem == null || valor <= 0) return false;
        return (origem.getFatura() + valor <= origem.getLimiteFatura());
    }

}
