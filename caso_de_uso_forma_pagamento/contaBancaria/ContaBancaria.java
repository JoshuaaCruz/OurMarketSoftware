package contaBancaria;

import java.util.ArrayList;
import java.util.List;

public class ContaBancaria {

    private String numeroConta;
    private double saldoConta;
    private double fatura;
    private double limiteFatura;
    private List<String> formasPagamento;

    public ContaBancaria() {
        this.formasPagamento = new ArrayList<>();
        this.formasPagamento.add("Pix");
        this.formasPagamento.add("Cartão de Crédito");
    }

    public String getNumero() {
        return numeroConta;
    }

    public void setNumero(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldoConta() {
        return saldoConta;
    }

    public void depositar(double valor) {
        if (valor > 0) saldoConta += valor;
    }

    public boolean sacar(double valor) {
        if (saldoConta >= valor && valor > 0) {
            saldoConta -= valor;
            return true;
        }
        return false;
    }

    public boolean transferir(double valor, ContaBancaria contaDestino) {
        if (contaDestino != null && this.sacar(valor)) {
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }

    public double getLimiteFatura() {
        return limiteFatura;
    }

    public void setLimiteFatura(double limiteFatura) {
        this.limiteFatura = limiteFatura;
    }

    public double getFatura() {
        return fatura;
    }

    public void incrementaFatura(double valor) {
        if (valor > 0) fatura += valor;
    }

    public void pagarFatura(double valor) {
        if (valor > 0 && fatura >= valor) fatura -= valor;
    }

    public List<String> getFormasPagamento() {
        return formasPagamento;
    }

    public boolean adicionarFormaPagamento(String forma) {
        if (forma != null && !formasPagamento.contains(forma)) {
            formasPagamento.add(forma);
            return true;
        }
        return false;
    }

    public boolean removerFormaPagamento(String forma) {
        return formasPagamento.remove(forma);
    }

    public boolean temFormaPagamento(String forma) {
        return formasPagamento.contains(forma);
    }
}
