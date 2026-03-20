/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.terminal;

import java.util.Scanner;
import model.autenticador.CredencialLoginSenha;
import model.autenticador.Credencial_if;
import view.Credencial_View;

/**
 *
 * @author joshua
 */
public class CredencialLoginSenha_View_Textual implements Credencial_View {
    public CredencialLoginSenha_View_Textual(Credencial_if model) {
        this.model = model;
    }
    @Override
    public Credencial_if getModel() {
        return model;
    }

    @Override
    public void setCredencial() {
        Scanner s = new Scanner(System.in);
        System.out.print("Digite seu login: ");
        String login = s.nextLine();
        System.out.print("Digite sua senha: ");
        String senha = s.nextLine();
        model = new CredencialLoginSenha(login, senha);
    }

    private Credencial_if model;
}
