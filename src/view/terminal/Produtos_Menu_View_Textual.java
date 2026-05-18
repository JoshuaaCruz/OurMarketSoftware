package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.LivreMercado;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import view.Menu_if;

/**
 *
 */
public class Produtos_Menu_View_Textual implements Menu_if {

    private final LivreMercado model;
    private final Scanner scanner;

    public Produtos_Menu_View_Textual(LivreMercado model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostre() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) {
            System.out.println("\n Erro: Nenhum cliente autenticado.");
            return;
        }
        escolherColecao(cliente);
    }

    private void escolherColecao(Cliente cliente) {
        int opcao = -1;

        while (opcao != 1 && opcao != 2 && opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  ESCOLHA A COLEÇÃO");
            System.out.println("===========================");
            System.out.println("1. Estoque");
            System.out.println("2. Carrinho");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuCRUD(cliente.getEstoque(), "Estoque");
                    break;
                case 2:
                    menuCRUD(cliente.getCarrinho(), "Carrinho");
                    break;
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2 ou 0.");
                    opcao = -1;
                    break;
            }
        }
    }

    private void menuCRUD(ColecaoProdutos colecao, String nomeColecao) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  GERENCIAR " + nomeColecao.toUpperCase());
            System.out.println("===========================");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Ver Produtos");
            System.out.println("3. Atualizar Produto");
            System.out.println("4. Remover Produto");
            System.out.println("5. Ver Nota do Produto e Vendedor");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarProduto(colecao, nomeColecao);
                    break;
                case 2:
                    verProdutos(colecao, nomeColecao);
                    break;
                case 3:
                    atualizarProduto(colecao);
                    break;
                case 4:
                    removerProduto(colecao, nomeColecao);
                    break;
                case 5:
                    verNotaProdutoVendedor(colecao);
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("\nOpção inválida!");
                    break;
            }
        }
    }

    private void adicionarProduto(ColecaoProdutos colecao, String nomeColecao) {
        System.out.println("\n--- ADICIONAR PRODUTO ---");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço base: R$ ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        colecao.adicionarProduto(new Produto(nome, descricao, preco));
        System.out.println("\n Produto '" + nome + "' adicionado ao " + nomeColecao + " com sucesso!");
    }

    private void verProdutos(ColecaoProdutos colecao, String nomeColecao) {
        System.out.println("\n--- PRODUTOS NO " + nomeColecao.toUpperCase() + " ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto encontrado.");
            return;
        }

        System.out.println("\nTotal: " + produtos.size());
        System.out.println("─────────────────────────────────────");
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.println("\n[" + (i + 1) + "] " + p.getNome());
            System.out.println("    Descrição: " + p.getDescricao());
            System.out.println("    Preço: R$ " + String.format("%.2f", p.getPrecoBase()));
            if (p.getCategoria() != null) {
                System.out.println("    Categoria: " + p.getCategoria().getNome());
            }
        }
        System.out.println("─────────────────────────────────────");
    }

    private void atualizarProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível para atualizar.");
            return;
        }

        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) { System.out.println("Cancelado."); return; }
        if (escolha < 1 || escolha > produtos.size()) { System.out.println(" Número inválido."); return; }

        Produto produto = produtos.get(escolha - 1);

        System.out.println("\n1. Nome  2. Descrição  3. Preço  0. Cancelar");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Novo nome: ");
                produto.setNome(scanner.nextLine());
                System.out.println(" Nome atualizado!");
                break;
            case 2:
                System.out.print("Nova descrição: ");
                produto.setDescricao(scanner.nextLine());
                System.out.println(" Descrição atualizada!");
                break;
            case 3:
                System.out.print("Novo preço: R$ ");
                produto.setPrecoBase(scanner.nextDouble());
                scanner.nextLine();
                System.out.println(" Preço atualizado!");
                break;
            case 0:
                System.out.println("Cancelado.");
                break;
            default:
                System.out.println(" Opção inválida.");
        }
    }

    private void removerProduto(ColecaoProdutos colecao, String nomeColecao) {
        System.out.println("\n--- REMOVER PRODUTO ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível.");
            return;
        }

        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) { System.out.println("Cancelado."); return; }
        if (escolha < 1 || escolha > produtos.size()) { System.out.println(" Número inválido."); return; }

        Produto produto = produtos.get(escolha - 1);
        String nome = produto.getNome();
        colecao.removerProduto(produto);
        System.out.println("\n Produto '" + nome + "' removido do " + nomeColecao + " com sucesso!");
    }
}
