package model.contaBancaria;

import java.util.ArrayList;
import java.util.List;

public class ContaBancaria {

    private String numeroConta;
    private double saldoConta;
    private double fatura;
    private double limiteFatura;

    // Strategy Pattern — available payment methods for this account
    private List<FormaDePagamento> formasPagamento;

    // estrategia de pagamento selecionada usada por pagar()
    private FormaDePagamento formaDePagamentoSelec;

    public ContaBancaria() {
        this.formasPagamento = new ArrayList<>();
        this.formasPagamento.add(new Pix());
        this.formasPagamento.add(new CartaoCredito()); // TODO: Verificar se deveria ser adicionado automaticamente as formas de pgmento na inicializacao
    }

    public String getNumero(){
        return numeroConta;
    }

    public void setNumero(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    
    public double getSaldoConta() {
        return saldoConta;
    }
    
  /**
     * @return the limiteFatura
     */
    public double getLimiteFatura() {
        return limiteFatura;
    }

    /**
     * @param limiteFatura the limiteFatura to set
     */
    public void setLimiteFatura(double limiteFatura) {
        this.limiteFatura = limiteFatura;
    }

    public double getFatura() {
        return fatura;
    }

    public void incrementaFatura(double valor) {
        fatura += valor;
    }

    public void pagarFatura(double valor) {
        if (valor > 0 && fatura >= valor) {
            fatura -= valor;
        }
    }

    public void depositar(double valor) {
        if (valor > 0) {
            saldoConta += valor;
        }
    }

    public boolean sacar(double valor) {
        if (valor > 0 && saldoConta >= valor) {
            saldoConta -= valor;
            return true;
        }
        return false;
    }

    public boolean transferir(double valor, ContaBancaria contaDestino) {
        if (contaDestino == null) return false;
        if (this.sacar(valor)) {
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }

    // retorna todas as formas de pagamento disponíveis
    public List<FormaDePagamento> getFormasPagamento() {
        return formasPagamento;
    }

    // retorna a forma de pagamento selecionada
    public FormaDePagamento getFormaDePagamentoSelec() {
        return formaDePagamentoSelec;
    }

    /**
     * Selects a payment strategy that must already be registered on this account.
     * @return true if the strategy was found and selected, false otherwise
     */
    public boolean selecionarFormaPagamento(FormaDePagamento forma) {
        for (FormaDePagamento f : formasPagamento) {
            if (f.getNome().equals(forma.getNome())) {
                this.formaDePagamentoSelec = f;
                return true;
            }
        }
        return false;
    }

    public boolean pagar(ContaBancaria destino, double valor) {
        if (formaDePagamentoSelec == null) return false;
        return formaDePagamentoSelec.pagar(this, destino, valor);
    }

    public boolean adicionarFormaPagamento(FormaDePagamento forma) {
        if (forma != null && !temFormaPagamento(forma.getNome())) {
            formasPagamento.add(forma);
            return true;
        }
        return false;
    }

    public boolean removerFormaPagamento(FormaDePagamento forma) {
        return formasPagamento.remove(forma);
    }

    public boolean temFormaPagamento(String nome) {
        for (FormaDePagamento f : formasPagamento) {
            if (f.getNome().equals(nome)) return true;
        }
        return false;
    }
}
