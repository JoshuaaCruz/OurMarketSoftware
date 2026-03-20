/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.autenticador.AutenticadorDefault;
import model.autenticador.Autenticador_if;
import model.fabrica.Fabrica_Grafica;
import model.fabrica.Fabrica_if;

/**
 *
 * @author joshua.cruz
 */
public abstract class Fabrica implements Fabrica_if{
 
    /**
     * @return the fabricaConcreta
     */
    public static Fabrica GetViewFabricaConcreta() {
        return fabricaConcreta;
    }

    /**
     * @param aFabricaConcreta the fabricaConcreta to set
     */
    public static void SetFabricaConcreta(Fabrica aFabricaConcreta) {
        fabricaConcreta = aFabricaConcreta;
    }

    static Autenticador_if new_Autenticador() {
        return new AutenticadorDefault();
    }
    static {
        /// Configuracao Geral do frontend do aplicativo
        //fabricaConcreta = new Fabrica_Textual();
        fabricaConcreta = new Fabrica_Grafica();
    }
    private static Fabrica fabricaConcreta;
}
