package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.LivreMercado;
import model.categoria_produto.ColecaoProdutos;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import view.Menu_if;


public class Produtos_Menu_View_Textual implements Menu_if {

    private final LivreMercado model;
    private final Scanner scanner;


    public Produtos_Menu_View_Textual(LivreMercado model) {
        this(model, new Scanner(System.in));
    }

    public Produtos_Menu_View_Textual(LivreMercado model, Scanner scanner) {
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

        escolherColecao(cliente);
    }

    private void escolherColecao(Cliente cliente) {
        int opcao = -1;

        while (opcao != 1 && opcao != 2 && opcao != 3 && opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("  ESCOLHA A COLEÇÃO");
            System.out.println("===========================");
            System.out.println("1. Estoque");
            System.out.println("2. Carrinho");
            System.out.println("3. Lista de Desejos");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuCRUD(cliente.getEstoque(), "Estoque", cliente);
                    break;
                case 2:
                    menuCRUD(cliente.getCarrinho(), "Carrinho", cliente);
                    break;
                case 3:
                    menuListaDesejos(cliente);
                    break;
                case 0:
                    System.out.println("\nVoltando...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha 1, 2, 3 ou 0.");
                    opcao = -1;
                    break;
            }
        }
    }

    private void menuCRUD(ColecaoProdutos colecao, String nomeColecao, Cliente cliente) {
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
            System.out.println("6. Gerenciar Lista de Desejos");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (colecao == model.getClienteLogado().getCarrinho()) {
                        adicionarProdutoAoCarrinho(cliente);
                    } else {
                        adicionarProdutoEstoque(colecao);
                    }
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
                case 6:
                    menuListaDesejos(cliente);
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

    private void menuListaDesejos(Cliente cliente) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("    LISTA DE DESEJOS");
            System.out.println("===========================");
            System.out.println("1. Ver Lista de Desejos");
            System.out.println("2. Adicionar Produto à Lista de Desejos");
            System.out.println("3. Remover Produto da Lista de Desejos");
            System.out.println("4. Limpar Lista de Desejos");
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
        }

        System.out.println("-------------------------------------------------");
    }

    
    private Produto escolherProdutoDeVendedor(Cliente vendedor) {
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

    private void adicionarProdutoListaDesejos(Cliente cliente) {
        System.out.println("\n--- ADICIONAR À LISTA DE DESEJOS ---");

        Produto produto = escolherProdutoDeVendedor(cliente);
        if (produto == null) return;

        if (cliente.getListaDesejos().contains(produto)) {
            System.out.println(" O produto '" + produto.getNome() + "' já está na sua lista de desejos.");
        } else {
            cliente.addProdutoListaDesejo(produto);
            System.out.println("\n Produto '" + produto.getNome() + "' adicionado à Lista de Desejos com sucesso!");
        }
    }

    private void adicionarProdutoAoCarrinho(Cliente cliente) {
        System.out.println("\n--- ADICIONAR AO CARRINHO ---");

        Produto produto = escolherProdutoDeVendedor(cliente);
        if (produto == null) return;

        cliente.getCarrinho().adicionarProduto(produto);
        System.out.println("\n Produto '" + produto.getNome() + "' adicionado ao Carrinho com sucesso!");
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
            System.out.println("\n Lista de desejos limpa com sucesso!");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    private void adicionarProdutoEstoque(ColecaoProdutos colecao) {
        System.out.println("\n--- ADICIONAR PRODUTO ---");

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço base: R$ ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Nota do produto (0-5): ");
        double nota = scanner.nextDouble();
        scanner.nextLine();

        Produto novoProduto = new Produto(nome, descricao, preco);
        novoProduto.setNota(nota);
        novoProduto.setVendedor(model.getClienteLogado());
        colecao.adicionarProduto(novoProduto);

        System.out.println("\n Produto '" + nome + "' adicionado ao seu Estoque com sucesso!");
    }

    private void verProdutos(ColecaoProdutos colecao, String nomeColecao) {
        System.out.println("\n--- PRODUTOS NO " + nomeColecao.toUpperCase() + " ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto encontrado no " + nomeColecao + ".");
            return;
        }

        System.out.println("\nTotal de produtos: " + produtos.size());
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
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
        }

        System.out.println("-------------------------------------------------");
    }

    private void atualizarProduto(ColecaoProdutos colecao) {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível para atualizar.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto a atualizar (0 para cancelar): ");
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

        Produto produto = produtos.get(escolha - 1);

        System.out.println("\nO que deseja atualizar?");
        System.out.println("1. Nome");
        System.out.println("2. Descrição");
        System.out.println("3. Preço");
        System.out.println("4. Nota");
        System.out.println("0. Cancelar");
        System.out.print("Escolha: ");

        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Novo nome: ");
                String novoNome = scanner.nextLine();
                produto.setNome(novoNome);
                System.out.println(" Nome atualizado com sucesso!");
                break;
            case 2:
                System.out.print("Nova descrição: ");
                String novaDesc = scanner.nextLine();
                produto.setDescricao(novaDesc);
                System.out.println(" Descrição atualizada com sucesso!");
                break;
            case 3:
                System.out.print("Novo preço: R$ ");
                double novoPreco = scanner.nextDouble();
                scanner.nextLine();
                produto.setPrecoBase(novoPreco);
                System.out.println(" Preço atualizado com sucesso!");
                break;
            case 4:
                System.out.print("Nova nota (0-5): ");
                double novaNota = scanner.nextDouble();
                scanner.nextLine();
                produto.setNota(novaNota);
                System.out.println(" Nota atualizada com sucesso!");
                break;
            case 0:
                System.out.println("Operação cancelada.");
                break;
            default:
                System.out.println(" Opção inválida.");
                break;
        }
    }

    private void removerProduto(ColecaoProdutos colecao, String nomeColecao) {
        System.out.println("\n--- REMOVER PRODUTO ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível para remover.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto a remover (0 para cancelar): ");
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

        Produto produto = produtos.get(escolha - 1);
        String nomeProduto = produto.getNome();

        colecao.removerProduto(produto);

        System.out.println("\n Produto '" + nomeProduto + "' removido do " + nomeColecao + " com sucesso!");

        if (colecao == model.getClienteLogado().getCarrinho()) {
            System.out.println("Deseja apagar o produto do sistema (retirar do estoque do vendedor)? (S/N)");
            String resposta = scanner.nextLine().trim().toUpperCase();

            if (resposta.equals("S")) {
                Cliente vendedor = produto.getVendedor();

                if (vendedor != null) {
                    vendedor.getEstoque().removerProduto(produto);
                    System.out.println(" Produto completamente apagado do sistema (removido do estoque do vendedor '" + vendedor.getName() + "').");
                } else {
                    System.out.println(" Aviso: Não foi possível apagar do sistema pois o produto não possui um vendedor associado.");
                }
            } else {
                System.out.println(" O produto foi removido apenas do seu Carrinho.");
            }
        }
    }

    private void verNotaProdutoVendedor(ColecaoProdutos colecao) {
        System.out.println("\n--- VER NOTA DO PRODUTO E VENDEDOR ---");

        List<Produto> produtos = colecao.getProdutos();
        if (produtos.isEmpty()) {
            System.out.println(" Nenhum produto disponível.");
            return;
        }

        System.out.println("\nProdutos disponíveis:");
        for (int i = 0; i < produtos.size(); i++) {
            System.out.println((i + 1) + ". " + produtos.get(i).getNome());
        }

        System.out.print("\nEscolha o número do produto (0 para cancelar): ");
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

        Produto produto = produtos.get(escolha - 1);

        System.out.println("\n--- NOTA DO PRODUTO ---");
        System.out.println("Produto: " + produto.getNome());
        System.out.println("Nota: " + String.format("%.1f", produto.getNota()));

        Cliente vendedor = produto.getVendedor();
        if (vendedor == null) {
            System.out.println("\nProduto não possui vendedor associado.");
            return;
        }

        System.out.println("\n--- INFORMAÇÕES DO VENDEDOR ---");
        System.out.println("Nome: " + vendedor.getName());
        System.out.println("CPF: " + vendedor.getCPF());

        if (vendedor.getQuantidadeProdutosVendidos() < 5) {
            System.out.println("Aviso: Vendedor tem poucos (<5) produtos vendidos. Nota pode ser instável.");
        }
        System.out.println("Nota do Vendedor: " + String.format("%.1f", vendedor.getNota()));
        System.out.println("Quantidade de produtos vendidos: " + vendedor.getQuantidadeProdutosVendidos());
    }
}