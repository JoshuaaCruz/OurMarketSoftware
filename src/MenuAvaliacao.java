import java.util.List;
import java.util.Scanner;

public class MenuAvaliacao {
    private Scanner scanner;
    private Cliente cli;
    private ColecaoProdutos produtos;
    public MenuAvaliacao(Cliente cliente, Scanner scanner) {
        this.cli = cliente;
        produtos = cliente.getCompras();
        this.scanner = scanner;
    }

    public void iniciar() {
        menuAvaliacao();
    }

    private void menuAvaliacao() {
        if (produtos.getQuantidadeProdutos() == 0) {
            System.out.println("O cliente deve ter ao menos um produto para avaliar.");
            return;
        }
        Produto produto = escolherProduto();
        int nota = escolherNota();
        boolean isNotaSubmitted = produto.addNota(cli, nota);
        if (!isNotaSubmitted) {
            System.out.println("A avaliação não foi autorizada!!");
            return;
        }
        Cliente vendedor = produto.getVendedor();
        vendedor.calculaNotaColecao(vendedor.getEstoque());
        System.out.println("A avaliação foi concluída com sucesso!!");
    }

    private Produto escolherProduto() {
        List<Produto> produtos = this.produtos.getProdutos();
        System.out.println("\n===========================");
        System.out.println("  ESCOLHA A O PRODUTO A SER AVALIADO ");
        System.out.println("===========================");
        boolean loop;
        int opcao;
        do {
            for (int i = 1; i <= produtos.size(); i++)
                System.out.printf("%d - %s\n", i, produtos.get(i - 1).getNome());
            System.out.print(": ");
            opcao = Integer.parseInt(scanner.nextLine());
            loop = opcao < 1 || opcao > produtos.size();
        } while (loop);
        return produtos.get(opcao - 1);
    }

    private int escolherNota() {
        System.out.println("\n===========================");
        System.out.println("  ESCOLHA UMA NOTA DE 0 A 5 ");
        System.out.println("===========================");
        System.out.print(": ");
        int nota = Integer.parseInt(scanner.nextLine());
        while (nota < 0 || nota > 5) {
            System.out.print("Escolha uma nota válida (de 0 a 5): ");
            nota = Integer.parseInt(scanner.nextLine());
        }
        return nota;
    }

}
