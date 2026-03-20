/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.contaBancaria;

/**
 *
 * @author joshu
 */
public class Pix implements FormaDePagamento {

    @Override
    public boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor) {

        return origem.trasferir(valor, destino);
        
    }
    
}
