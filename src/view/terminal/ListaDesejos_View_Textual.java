package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class ListaDesejos_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public ListaDesejos_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) return;
        
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("    LISTA DE DESEJOS");
            System.out.println("===========================");
            System.out.println("1. Ver Lista de Desejos");
            System.out.println("2. Adicionar Produto à Lista de Desejos");
            System.out.println("3. Remover Produto da Lista de Desejos");
            System.out.println("4. Limpar Lista de Desejos");
            System.out.println("5. Ver Fotos de um Produto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    verListaDesejos(cliente);
                    break;
                case 2:
                    adicionarProdutoListaDesejos(cliente);
                    break;
                case 3:
                    removerProdutoListaDesejos(cliente);
                    break;
                case 4:
                    limparListaDesejos(cliente);
                    break;
                case 5:
                    abrirFotosProduto(cliente);
                    break;
                case 0:
                    System.out.println("\nVoltando ao menu anterior...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
    }

    private void verListaDesejos(Cliente cliente) {
        System.out.println("\n--- LISTA DE DESEJOS ---");

        List<Produto> listaDesejos = cliente.getListaDesejos();
        if (listaDesejos == null || listaDesejos.isEmpty()) {
            System.out.println(" Sua lista de desejos está vazia.");
            return;
        }

        System.out.println("\nTotal de produtos: " + listaDesejos.size());
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < listaDesejos.size(); i++) {
            Produto p = listaDesejos.get(i);
            System.out.println("\n[" + (i + 1) + "] " + p.getNome());
            System.out.println("    Descrição: " + p.getDescricao());
            System.out.println("    Preço: R$ " + String.format("%.2f", p.getPrecoBase()));
            if (p.getCategoria() != null) {
                System.out.println("    Categoria: " + p.getCategoria().getNome());
            }
            if (p.getVendedor() != null) {
                System.out.println("    Vendedor: " + p.getVendedor().getName());
            }
            System.out.println("    Nota: " + String.format("%.2f", p.getNota()));
            Produtos_Menu_View_Textual.imprimirFotos(p);
        }

        System.out.println("-------------------------------------------------");
    }

    private void adicionarProdutoListaDesejos(Cliente cliente) {
        System.out.println("\n--- ADICIONAR À LISTA DE DESEJOS ---");

        Produto produto = Produtos_Menu_View_Textual.escolherProdutoDeVendedor(model, scanner, cliente);
        if (produto == null) return;

        if (cliente.getListaDesejos().contains(produto)) {
            System.out.println(" O produto '" + produto.getNome() + "' já está na sua lista de desejos.");
        } else {
            cliente.addProdutoListaDesejo(produto);
            model.salvar();
            System.out.println("\n Produto '" + produto.getNome() + "' adicionado à Lista de Desejos com sucesso!");
        }
    }

    private void removerProdutoListaDesejos(Cliente cliente) {
        System.out.println("\n--- REMOVER DA LISTA DE DESEJOS ---");

        List<Produto> listaDesejos = cliente.getListaDesejos();
        if (listaDesejos == null || listaDesejos.isEmpty()) {
            System.out.println(" Sua lista de desejos está vazia.");
            return;
        }

        System.out.println("\nProdutos na lista de desejos:");
        for (int i = 0; i < listaDesejos.size(); i++) {
            System.out.println((i + 1) + ". " + listaDesejos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto a remover (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (escolha < 1 || escolha > listaDesejos.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        Produto produto = listaDesejos.get(escolha - 1);
        cliente.removeProdutoListaDesejo(produto);
        model.salvar();

        System.out.println("\n Produto '" + produto.getNome() + "' removido da Lista de Desejos com sucesso!");
    }

    private void limparListaDesejos(Cliente cliente) {
        System.out.println("\n--- LIMPAR LISTA DE DESEJOS ---");

        List<Produto> listaDesejos = cliente.getListaDesejos();
        if (listaDesejos == null || listaDesejos.isEmpty()) {
            System.out.println(" Sua lista de desejos já está vazia.");
            return;
        }

        System.out.println("Tem certeza que deseja limpar toda a lista de desejos? (" + listaDesejos.size() + " produto(s)) (S/N)");
        String confirmacao = scanner.nextLine().trim().toUpperCase();

        if (confirmacao.equals("S")) {
            cliente.deleteListaDesejos();
            model.salvar();
            System.out.println("\n Lista de desejos limpa com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void abrirFotosProduto(Cliente cliente) {
        System.out.println("\n--- VER FOTOS ---");

        List<Produto> produtos = cliente.getListaDesejos();
        if (produtos == null || produtos.isEmpty()) {
            System.out.println(" Sua lista de desejos está vazia.");
            return;
        }

        System.out.println("\nEscolha o produto:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nOpção (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha >= 1 && escolha <= produtos.size()) {
            Produtos_Menu_View_Textual.abrirFotos(produtos.get(escolha - 1));
        } else if (escolha != 0) {
            System.out.println(" Erro: Número inválido.");
        }
    }
}
