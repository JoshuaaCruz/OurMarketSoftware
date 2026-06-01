package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.contaBancaria.ContaBancaria;
import view.Menu_if;

public class MenuFormaPagamento_View_Textual implements Menu_if {

    private static final String[] FORMAS_DISPONIVEIS = {"Pix", "Cartão de Crédito"};

    private final ContaBancaria conta;
    private final Scanner scanner;

    public MenuFormaPagamento_View_Textual(ContaBancaria conta, Scanner scanner) {
        this.conta = conta;
        this.scanner = scanner;
    }

    @Override
    public void mostre() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println(" GERENCIAR FORMA DE PAGAMENTO");
            System.out.println("===========================");
            System.out.println("1. Listar formas de pagamento");
            System.out.println("2. Cadastrar forma de pagamento");
            System.out.println("3. Editar forma de pagamento");
            System.out.println("4. Excluir forma de pagamento");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listar();
                    break;
                case 2:
                    cadastrar();
                    break;
                case 3:
                    editar();
                    break;
                case 4:
                    excluir();
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }
    }

    private void listar() {
        System.out.println("\n--- Formas de Pagamento Cadastradas ---");
        List<String> formas = conta.getFormasPagamento();

        if (formas.isEmpty()) {
            System.out.println("Nenhuma forma de pagamento cadastrada.");
            return;
        }

        for (int i = 0; i < formas.size(); i++) {
            String forma = formas.get(i);
            System.out.print("  " + (i + 1) + ". " + forma);

            if (forma.equals("Cartão de Crédito")) {
                System.out.printf("  (Limite: R$ %.2f | Fatura atual: R$ %.2f | Disponível: R$ %.2f)",
                    conta.getLimiteFatura(),
                    conta.getFatura(),
                    conta.getLimiteFatura() - conta.getFatura());
            } else if (forma.equals("Pix")) {
                System.out.printf("  (Saldo: R$ %.2f)", conta.getSaldoConta());
            }

            System.out.println();
        }
    }

    private void cadastrar() {
        System.out.println("\n--- Cadastrar Forma de Pagamento ---");
        System.out.println("Formas disponíveis:");

        for (int i = 0; i < FORMAS_DISPONIVEIS.length; i++) {
            String status = conta.temFormaPagamento(FORMAS_DISPONIVEIS[i]) ? " [já cadastrada]" : "";
            System.out.println("  " + (i + 1) + ". " + FORMAS_DISPONIVEIS[i] + status);
        }

        System.out.println("  0. Cancelar");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        if (opcao == 0) {
            System.out.println("Cancelado.");
            return;
        }

        if (opcao < 1 || opcao > FORMAS_DISPONIVEIS.length) {
            System.out.println("Opção inválida.");
            return;
        }

        String forma = FORMAS_DISPONIVEIS[opcao - 1];
        if (conta.temFormaPagamento(forma)) {
            System.out.println("Esta forma de pagamento já está cadastrada.");
            return;
        }

        if (forma.equals("Cartão de Crédito")) {
            System.out.print("Defina o limite do cartão: R$ ");
            double limite = scanner.nextDouble();
            scanner.nextLine();
            conta.setLimiteFatura(limite);
        }

        conta.adicionarFormaPagamento(forma);
        System.out.println(forma + " cadastrado com sucesso!");
    }

    private void editar() {
        System.out.println("\n--- Editar Forma de Pagamento ---");
        List<String> formas = conta.getFormasPagamento();

        if (formas.isEmpty()) {
            System.out.println("Nenhuma forma cadastrada.");
            return;
        }

        for (int i = 0; i < formas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + formas.get(i));
        }

        System.out.print("Escolha o número para editar (0 para cancelar): ");
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indice < 0) {
            System.out.println("Cancelado.");
            return;
        }

        if (indice >= formas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        String forma = formas.get(indice);
        if (forma.equals("Cartão de Crédito")) {
            editarCartaoCredito();
        } else if (forma.equals("Pix")) {
            System.out.println("O Pix não possui configurações editáveis.");
        }
    }

    private void editarCartaoCredito() {
        System.out.printf("Limite atual: R$ %.2f%n", conta.getLimiteFatura());
        System.out.print("Novo limite: R$ ");
        double novoLimite = scanner.nextDouble();
        scanner.nextLine();

        if (novoLimite < conta.getFatura()) {
            System.out.printf("Atenção: novo limite (R$ %.2f) é menor que a fatura atual (R$ %.2f). Operação cancelada.%n",
                novoLimite,
                conta.getFatura());
            return;
        }

        conta.setLimiteFatura(novoLimite);
        System.out.println("Limite atualizado com sucesso!");
    }

    private void excluir() {
        System.out.println("\n--- Excluir Forma de Pagamento ---");
        List<String> formas = conta.getFormasPagamento();

        if (formas.isEmpty()) {
            System.out.println("Nenhuma forma cadastrada.");
            return;
        }

        for (int i = 0; i < formas.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + formas.get(i));
        }

        System.out.print("Escolha o número para excluir (0 para cancelar): ");
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indice < 0) {
            System.out.println("Cancelado.");
            return;
        }

        if (indice >= formas.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        String forma = formas.get(indice);
        System.out.print("Confirma exclusão de '" + forma + "'? (s/n): ");
        String confirmacao = scanner.nextLine().trim().toLowerCase();

        if (confirmacao.equals("s")) {
            conta.removerFormaPagamento(forma);
            System.out.println(forma + " removido com sucesso!");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }
}
