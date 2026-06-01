/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.contaBancaria;

public class CartaoCredito implements FormaDePagamento {

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

}
