package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import view.Menu_if;

public class Produtos_Menu_View_Textual implements Menu_if {

    private final OurMarket model;
    private final Scanner scanner;

    public Produtos_Menu_View_Textual(OurMarket model) {
        this(model, new Scanner(System.in));
    }

    public Produtos_Menu_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    @Override
    public void mostre() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) {
            System.out.println("\n Erro: Nenhum cliente fornecido.");
            return;
        }

        escolherColecao();
    }

    private void escolherColecao() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  GERENCIAR PRODUTOS");
            System.out.println("===========================");
            System.out.println("1. Meu Estoque");
            System.out.println("2. Meu Carrinho e Checkout");
            System.out.println("3. Minha Lista de Desejos");
            System.out.println("4. Meu Histórico de Compras");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    new Estoque_Menu_View_Textual(model, scanner).iniciar();
                    break;
                case 2:
                    new Carrinho_Checkout_View_Textual(model, scanner).iniciar();
                    break;
                case 3:
                    new ListaDesejos_View_Textual(model, scanner).iniciar();
                    break;
                case 4:
                    new Historico_View_Textual(model, scanner).iniciar();
                    break;
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2, 3, 4 ou 0.");
                    break;
            }
        }
    }

    public static void imprimirFotos(Produto produto) {
        List<String> fotos = produto.getFotos();
        if (fotos.isEmpty()) {
            System.out.println("    Fotos: (Nenhuma)");
            return;
        }
        System.out.println("    Fotos:");
        for (int i = 0; i < fotos.size(); i++) {
            System.out.println("      " + (i + 1) + ". " + fotos.get(i));
        }
    }

    public static void coletarFotos(Scanner scanner, Produto produto) {
        System.out.println("\nFotos do produto");
        System.out.println("Informe caminhos ou URLs. Pressione Enter sem digitar nada para finalizar.");
        while (true) {
            System.out.print("Foto " + (produto.getFotos().size() + 1) + ": ");
            String caminho = scanner.nextLine().trim();
            if (caminho.isEmpty()) {
                return;
            }
            produto.addFoto(caminho);
        }
    }

    public static Produto escolherProdutoDeVendedor(OurMarket model, Scanner scanner, Cliente vendedor) {
        List<Cliente> outrosVendedores = new java.util.ArrayList<>();
        for (Cliente c : model.getClientes()) {
            if (!c.equals(vendedor)) {
                outrosVendedores.add(c);
            }
        }

        if (outrosVendedores.isEmpty()) {
            System.out.println(" Nenhum outro vendedor disponível no momento.");
            return null;
        }

        System.out.println("\nEscolha o vendedor cujos produtos deseja ver:");
        for (int i = 0; i < outrosVendedores.size(); i++) {
            System.out.println((i + 1) + ". " + outrosVendedores.get(i).getName());
        }
        System.out.println("0. Cancelar");
        System.out.print("Escolha: ");

        int vendedorIdx = scanner.nextInt();
        scanner.nextLine();

        if (vendedorIdx == 0) {
            System.out.println("Operação cancelada.");
            return null;
        }

        if (vendedorIdx < 1 || vendedorIdx > outrosVendedores.size()) {
            System.out.println(" Opção inválida.");
            return null;
        }

        Cliente vendedorEscolhido = outrosVendedores.get(vendedorIdx - 1);
        List<Produto> produtos = vendedorEscolhido.getEstoque().getProdutos();

        if (produtos.isEmpty()) {
            System.out.println(" O vendedor '" + vendedorEscolhido.getName() + "' não possui produtos no estoque.");
            return null;
        }

        System.out.println("\nProdutos disponíveis de '" + vendedorEscolhido.getName() + "':");
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            System.out.printf("  %d. %-25s R$ %.2f  (Nota: %.1f)%n",
                    i + 1, p.getNome(), p.getPrecoBase(), p.getNota());
        }

        System.out.print("\nEscolha o número do produto (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return null;
        }

        if (escolha < 1 || escolha > produtos.size()) {
            System.out.println(" Erro: Número inválido.");
            return null;
        }

        return produtos.get(escolha - 1);
    }

    public static void verNotaProdutoVendedor(Scanner scanner, ColecaoProdutos colecao) {
        System.out.println("\n--- VER NOTA ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" A coleção está vazia.");
            return;
        }

        System.out.println("\nEscolha o produto:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nOpção (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha == 0) {
            System.out.println("Operação cancelada.");
            return;
        }

        if (escolha < 1 || escolha > produtos.size()) {
            System.out.println(" Erro: Número inválido.");
            return;
        }

        Produto p = produtos.get(escolha - 1);
        System.out.println("\n--- INFORMAÇÕES DE AVALIAÇÃO ---");
        System.out.println("Produto: " + p.getNome());
        System.out.println("Nota do Produto: " + String.format("%.1f", p.getNota()) + " (" + p.getTotalVotos() + " avaliações)");

        if (p.getVendedor() != null) {
            System.out.println("Vendedor: " + p.getVendedor().getName());
            System.out.println("Nota do Vendedor: " + String.format("%.1f", p.getVendedor().getNota()));
        }
    }

    public static void abrirFotos(Produto p) {
        List<String> fotos = p.getFotos();
        if (fotos.isEmpty()) {
            System.out.println(" Este produto não possui fotos.");
            return;
        }
        System.out.println(" Abrindo " + fotos.size() + " foto(s) de '" + p.getNome() + "' em uma nova janela...");
        for (String caminho : fotos) {
            try {
                view.gui.ImageWeb.generateImageWeb(caminho, p.getNome());
            } catch (Exception e) {
                System.out.println(" Erro ao abrir foto '" + caminho + "': " + e.getMessage());
            }
        }
    }

    public static void gerenciarFotosProduto(Scanner scanner, OurMarket model, Produto produto) {
        System.out.println("\n--- GERENCIAR FOTOS: " + produto.getNome() + " ---");
        while (true) {
            imprimirFotos(produto);
            System.out.println("\nO que deseja fazer?");
            System.out.println("1. Adicionar foto(s)");
            System.out.println("2. Remover uma foto");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    coletarFotos(scanner, produto);
                    model.salvar();
                    break;
                case 2:
                    if (produto.getFotos().isEmpty()) {
                        System.out.println(" Nenhuma foto para remover.");
                        break;
                    }
                    System.out.print("Número da foto a remover (0 para cancelar): ");
                    int num = scanner.nextInt();
                    scanner.nextLine();
                    if (num > 0 && num <= produto.getFotos().size()) {
                        String removida = produto.getFotos().get(num - 1);
                        produto.removeFoto(removida);
                        model.salvar();
                        System.out.println(" Foto removida: " + removida);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println(" Opção inválida.");
            }
        }
    }
}
