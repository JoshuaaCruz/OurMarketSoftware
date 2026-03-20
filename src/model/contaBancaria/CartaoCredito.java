/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.contaBancaria;

/**
 *
 * @author joshu
 */
public class CartaoCredito implements FormaDePagamento {

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {
        // aumenta valor à fatura do cliente (não saca da conta direta dele), transfere valor para o destino e pronto
        //vamos precisar de outra variavel na conta bancaria
        
        
        //aqui podemos tirar a verificação
        if(origem.getFatura() > origem.getLimiteFatura()){
            return false;
        } else{
        
        origem.incrementaFatura(valor);
        destino.depositar(valor);
        return true;
        }
        
    }
    
}
