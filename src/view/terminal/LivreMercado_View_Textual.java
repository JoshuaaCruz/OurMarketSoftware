package view.terminal;

import java.util.Scanner;
import model.Fabrica;
import model.LivreMercado;
import model.autenticador.Autenticacao;
import model.autenticador.Credencial_if;
import view.Categoria_Menu_View_if;
import view.Credencial_View;
import view.LivreMercado_View;
import view.Produtos_Menu_View_if;

/**
 *
 * @author joshua.cruz
 */
public class LivreMercado_View_Textual implements LivreMercado_View {

    private final LivreMercado model;
    private final Scanner scanner;

    public LivreMercado_View_Textual(LivreMercado model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostre() {
        System.out.println("LivreMercado executando");

        // Autenticação
        if (model.getAutenticador().getAutenticacao() == null) {
            System.out.println("Voce precisa se autenticar. Insira sua credencial");
            Credencial_View credencialView = Fabrica.GetViewFabricaConcreta().new_Credencial_View(null);
            credencialView.setCredencial();
            if (credencialView.getModel() == null) {
                System.out.println("Sua credencial nao foi criada");
                return;
            } else {
                Credencial_if credencial = credencialView.getModel();
                Autenticacao autenticacao = model.getAutenticador().autentique_se(credencial);
                if (autenticacao == null) {
                    System.out.println("Autenticacao falhou");
                    return;
                } else {
                    System.out.println("Voce se autenticou com sucesso");
                }
            }
        }

        // Menu principal pós-autenticação
        menuPrincipal();
    }

    private void menuPrincipal() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            System.out.println("===========================");
            System.out.println("1. Gerenciar Produtos");
            System.out.println("2. Gerenciar Categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (model.getClienteLogado() == null) {
                        System.out.println("\n Erro: Nenhum cliente autenticado.");
                        break;
                    }
                    Produtos_Menu_View_if menuProdutos =
                            Fabrica.GetViewFabricaConcreta().new_Produtos_Menu_View(model);
                    menuProdutos.mostre();
                    break;

                case 2:
                    Categoria_Menu_View_if menuCategorias =
                            Fabrica.GetViewFabricaConcreta().new_Categoria_Menu_View(model);
                    menuCategorias.mostre();
                    break;

                case 0:
                    System.out.println("\nEncerrando...");
                    break;

                default:
                    System.out.println("\nOpção inválida!");
                    break;
            }
        }
    }
}
