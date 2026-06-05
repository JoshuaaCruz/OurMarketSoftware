package view.terminal;

import java.util.List;
import java.util.Scanner;
import model.cliente.Cliente;
import model.cliente.Endereco;
import view.Menu_if;

public class Endereco_Menu_View_Textual implements Menu_if {

    private final Cliente cliente;
    private final Scanner scanner;

    public Endereco_Menu_View_Textual(Cliente cliente, Scanner scanner) {
        this.cliente = cliente;
        this.scanner = scanner;
    }

    @Override
    public void mostre() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("    GERENCIAR ENDEREÇOS");
            System.out.println("===========================");
            System.out.println("1. Listar endereços");
            System.out.println("2. Adicionar endereço");
            System.out.println("3. Editar endereço");
            System.out.println("4. Remover endereço");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: listar(); 
                break;
                case 2: adicionar(); 
                break;
                case 3: editar(); 
                break;
                case 4: remover(); 
                break;
                case 0: System.out.println("Voltando..."); 
                break;
                default: System.out.println("Opção inválida."); 
                break;
            }
        }
    }


    private void listar() {
        List<Endereco> enderecos = cliente.getEnderecos();
        System.out.println("\n--- Seus Endereços ---");
        if (enderecos.isEmpty()) {
            System.out.println("Nenhum endereço cadastrado.");
            return;
        }
        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + enderecos.get(i));
        }
    }

    private void adicionar() {
        System.out.println("\n--- Adicionar Endereço ---");
        Endereco e = coletarCampos();
        cliente.addEndereco(e);
        System.out.println("\n Endereço adicionado com sucesso!");
    }

    private void editar() {
        Endereco e = escolherEndereco("editar");
        if (e == null) return;

        System.out.println("\n--- Editar Endereço ---");
        System.out.println("Pressione Enter para manter o valor atual.\n");

        System.out.print("Estado [" + e.getEstado() + "]: ");
        String v = scanner.nextLine();
        if (!v.isBlank()) e.setEstado(v);

        System.out.print("Cidade [" + e.getCidade() + "]: ");
        v = scanner.nextLine();
        if (!v.isBlank()) e.setCidade(v);

        System.out.print("Rua [" + e.getRua() + "]: ");
        v = scanner.nextLine();
        if (!v.isBlank()) e.setRua(v);

        System.out.print("Número [" + e.getNumero() + "]: ");
        v = scanner.nextLine();
        if (!v.isBlank()) {
            try {
                e.setNumero(Integer.parseInt(v));
            } catch (NumberFormatException ex) {
                System.out.println("Número inválido, valor anterior mantido.");
            }
        }

        System.out.print("Complemento [" + e.getComplemento() + "]: ");
        v = scanner.nextLine();
        if (!v.isBlank()) e.setComplemento(v);

        String apelidoAtual = (e.getApelido() != null && !e.getApelido().isBlank())
                ? e.getApelido() : "(sem apelido)";
        System.out.print("Apelido [" + apelidoAtual + "] (- para remover): ");
        v = scanner.nextLine();
        if (v.equals("-")) {
            e.setApelido(null);
        } else if (!v.isBlank()) {
            e.setApelido(v);
        }

        System.out.println("\n Endereço atualizado com sucesso!");
    }

    private void remover() {
        if (cliente.getEnderecos().size() <= 1) {
            System.out.println("\n Não é possível remover o único endereço cadastrado.");
            return;
        }

        Endereco e = escolherEndereco("remover");
        if (e == null) return;

        System.out.print("Confirma remoção de \"" + e + "\"? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            cliente.removeEndereco(e);
            System.out.println("\n Endereço removido com sucesso!");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    //  Helpers 
    Endereco coletarCampos() {
        System.out.print("Estado (UF): ");
        String estado = scanner.nextLine();

        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();

        System.out.print("Rua: ");
        String rua = scanner.nextLine();

        System.out.print("Número: ");
        int numero = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Complemento (Apto, Casa, etc — Enter para pular): ");
        String complemento = scanner.nextLine();

        System.out.print("Apelido (ex: Casa, Trabalho — Enter para pular): ");
        String apelido = scanner.nextLine();

        Endereco e = new Endereco(estado, cidade, rua, numero, complemento);
        if (!apelido.isBlank()) e.setApelido(apelido);
        return e;
    }

    private Endereco escolherEndereco(String acao) {
        List<Endereco> enderecos = cliente.getEnderecos();
        if (enderecos.isEmpty()) {
            System.out.println("Nenhum endereço cadastrado.");
            return null;
        }
        listar();
        System.out.print("Escolha o número para " + acao + " (0 para cancelar): ");
        int indice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (indice < 0) { 
            System.out.println("Cancelado."); return null; 
        }

        if (indice >= enderecos.size()) { 
            System.out.println("Opção inválida."); 
            return null; 
        }
        return enderecos.get(indice);
    }
}