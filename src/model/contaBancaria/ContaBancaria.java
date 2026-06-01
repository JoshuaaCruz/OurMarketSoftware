/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.contaBancaria;

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

    public String getNumero(){
        return numeroConta;
    }

    public void setNumero(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getsaldoConta(){
        return saldoConta;
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

    /**
     * @return the fatura
     */


    public double getFatura() {
        return fatura;
    }
    
    public void incrementaFatura(double valor){
        fatura += valor;
    }
    
    
    
    public void depositar(double valor){
        if(valor > 0){
            saldoConta += valor;
        }
    }

    public boolean sacar(double valor){
        if(valor > 0 && saldoConta >= valor){
            saldoConta -= valor;
            return true;
        }
        return false;
    }
    
    public boolean trasferir(double valor, ContaBancaria contaDestino){
        if (contaDestino == null) {
            return false;
        }

        if(this.sacar(valor)){
            contaDestino.depositar(valor);
            return true;
        }
        return false;
    }

    public boolean transferir(double valor, ContaBancaria contaDestino) {
        return trasferir(valor, contaDestino);
    }

    public List<String> getFormasPagamento() {
        return formasPagamento;
    }

    public boolean adicionarFormaPagamento(String forma) {
        if (forma != null && !forma.isBlank() && !formasPagamento.contains(forma)) {
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

    public void pagarFatura(double valor) {
        if (valor > 0 && fatura >= valor) {
            fatura -= valor;
        }
    }
}
