package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.OurMarket;
import model.categoria_produto.ItemProduto;
import model.categoria_produto.Produto;
import model.cliente.Cliente;

public class Historico_View_Textual {

    private final OurMarket model;
    private final Scanner scanner;

    public Historico_View_Textual(OurMarket model, Scanner scanner) {
        this.model = model;
        this.scanner = scanner;
    }

    public void iniciar() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) return;
        verHistoricoCompras(cliente);
    }

    private void verHistoricoCompras(Cliente cliente) {
        System.out.println("\n--- HISTORICO DE COMPRAS ---");

        List<ItemProduto> historico = cliente.getPedidosComprados();
        if (historico.isEmpty()) {
            System.out.println(" Voce ainda não realizou nenhuma compra.");
            return;
        }

        System.out.println("\nTotal de itens comprados: " + historico.size());
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < historico.size(); i++) {
            ItemProduto item = historico.get(i);
            Produto p = item.getProduto();
            int qtd = item.getQuantidadeProduto();
            double subtotal = p.getPrecoBase() * qtd;

            System.out.println("\n[" + (i + 1) + "] " + p.getNome());
            System.out.println("    Descricao: " + p.getDescricao());
            System.out.println("    Preco unitario: R$ " + String.format("%.2f", p.getPrecoBase()));
            System.out.println("    Quantidade: " + qtd);
            System.out.println("    Subtotal:   R$ " + String.format("%.2f", subtotal));
            System.out.println("    Vendedor: " + p.getVendedor().getName());
            System.out.println("    Nota do produto: " + String.format("%.1f", p.getNota())
                    + " (" + p.getTotalVotos() + " voto(s))"
                    + (cliente.jaAvaliou(p) ? " [JA AVALIADO POR VOCE]" : ""));
            Produtos_Menu_View_Textual.imprimirFotos(p);
        }

        System.out.println("\n-------------------------------------------------");

        System.out.print("Deseja ver as fotos de algum produto? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            abrirFotosProduto(historico);
        }

        System.out.print("Deseja avaliar algum produto? (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            new Avaliacao_Menu_View_Textual(model, scanner).iniciar();
        }
    }

    private void abrirFotosProduto(List<ItemProduto> historico) {
        System.out.println("\nEscolha o produto:");
        for (int i = 0; i < historico.size(); i++) {
            System.out.println((i + 1) + ". " + historico.get(i).getProduto().getNome());
        }

        System.out.print("\nOpção (0 para cancelar): ");
        int escolha = scanner.nextInt();
        scanner.nextLine();

        if (escolha >= 1 && escolha <= historico.size()) {
            Produtos_Menu_View_Textual.abrirFotos(historico.get(escolha - 1).getProduto());
        } else if (escolha != 0) {
            System.out.println(" Erro: Número inválido.");
        }
    }
}
