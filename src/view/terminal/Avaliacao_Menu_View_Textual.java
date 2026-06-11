package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class Avaliacao_Menu_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public Avaliacao_Menu_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) {
            System.out.println(" Erro: Nenhum cliente logado.");
            return;
        }

        List<ItemProduto> historico = cliente.getPedidosComprados();
        if (historico.isEmpty()) {
            System.out.println(" Você ainda não realizou nenhuma compra para avaliar.");
            return;
        }

        menuAvaliacao(cliente, historico);
    }

    private void menuAvaliacao(Cliente cliente, List<ItemProduto> historico) {
        System.out.println("\n===========================");
        System.out.println("   AVALIAR PRODUTO COMPRADO");
        System.out.println("===========================");

        // apenas produtos não avaliados
        List<Produto> disponiveis = new java.util.ArrayList<>();
        for (ItemProduto item : historico) {
            Produto p = item.getProduto();
            if (p != null && !cliente.jaAvaliou(p)) {
                disponiveis.add(p);
            }
        }

        if (disponiveis.isEmpty()) {
            System.out.println(" Você já avaliou todos os produtos comprados.");
            return;
        }

        System.out.println("\nEscolha o produto a avaliar:");
        for (int i = 0; i < disponiveis.size(); i++) {
            Produto p = disponiveis.get(i);
            String vendedor = (p.getVendedor() != null) ? " (Vendedor: " + p.getVendedor().getName() + ")" : "";
            System.out.println("[" + (i + 1) + "] " + p.getNome() + vendedor);
        }
        System.out.println("[0] Cancelar");

        int escolha;
        while (true) {
            System.out.print("Escolha: ");
            escolha = scanner.nextInt();
            scanner.nextLine();
            if (escolha == 0) {
                System.out.println("Operação cancelada.");
                return;
            }
            if (escolha >= 1 && escolha <= disponiveis.size()) break;
            System.out.println("Número inválido. Tente novamente.");
        }

        Produto produtoEscolhido = disponiveis.get(escolha - 1);

        double nota = escolherNota();

        String resultado = model.avaliarProduto(cliente, produtoEscolhido, nota);

        if ("Sucesso".equals(resultado)) {
            System.out.printf(" Avaliação registrada! '%s' agora tem nota média %.1f (%d voto(s)).%n",
                    produtoEscolhido.getNome(),
                    produtoEscolhido.getNota(),
                    produtoEscolhido.getTotalVotos());
        } else {
            System.out.println(" " + resultado);
        }
    }

    private double escolherNota() {
        System.out.println("\n===========================");
        System.out.println("  ESCOLHA UMA NOTA DE 0 A 5");
        System.out.println("===========================");
        double nota;
        while (true) {
            System.out.print("Nota: ");
            nota = scanner.nextDouble();
            scanner.nextLine();
            if (nota >= 0 && nota <= 5) break;
            System.out.println("Nota inválida. Digite um valor entre 0 e 5.");
        }
        return nota;
    }
}
