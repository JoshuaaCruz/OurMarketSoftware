
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.terminal;

import model.Fabrica;
import model.LivreMercado;
import model.autenticador.Autenticacao;
import model.autenticador.Credencial_if;
import view.Credencial_View;
import view.LivreMercado_View;

/**
 *
 * @author joshua.cruz
 */
public class LivreMercado_View_Textual implements LivreMercado_View {

    public LivreMercado_View_Textual(LivreMercado model) {
        this.model = model;
    }

    @Override
    public void mostre() {
        System.out.println("LivreMercado executando");
        if (model.getAutenticador().getAutenticacao() == null) {
            System.out.println("Voce precisa se autenticar. Insira sua credencial");
            Credencial_View credencialView = Fabrica.GetViewFabricaConcreta().new_Credencial_View(null);
            credencialView.setCredencial();
            if (credencialView.getModel() == null) {
                System.out.println("Sua credencial nao foi criada");
            } else {
                Credencial_if credencial = credencialView.getModel();
                Autenticacao autenticacao = model.getAutenticador().autentique_se(credencial);
                if (autenticacao==null) {
                    System.out.println("Autenticacao falhou");
                } else {
                    System.out.println("Voce se autenticou com sucesso");
                }
            }
        }
    }
    
    private final LivreMercado model;
    
}
