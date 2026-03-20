/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.contaBancaria;

/**
 *
 * @author joshu
 */
public interface FormaDePagamento {
    boolean pagar(ContaBancaria origem, ContaBancaria destino, double valor);
}
