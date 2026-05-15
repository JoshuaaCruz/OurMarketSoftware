import java.util.Scanner;
import contaBancaria.ContaBancaria;
import contaBancaria.CartaoCredito;
import contaBancaria.Pix;
import contaBancaria.FormaDePagamento;
import java.util.ArrayList;
import java.util.List;


public class main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        List<Cliente> listaClientes = new ArrayList<>();

        Endereco end = null;
        Cliente cli = null;
        ContaBancaria conta = null;

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            if (cli != null) {
                System.out.println("   Cliente Ativo: " + cli.getName());
            }
            System.out.println("===========================");
            System.out.println("1.  Cadastrar Cliente e Conta");
            System.out.println("2.  Ver dados do Cliente");
            System.out.println("3.  Consultar Saldo");
            System.out.println("4.  Depositar");
            System.out.println("5.  Sacar");
            System.out.println("6.  Gerenciar Produtos");
            System.out.println("7.  Gerenciar Categorias");
            System.out.println("8.  Pagar com Pix");
            System.out.println("9.  Pagar com Cartão de Crédito");
            System.out.println("10. Ver Fatura do Cartão");
            System.out.println("11. Pagar Fatura do Cartão");
            System.out.println("12. Trocar Cliente Ativo");
            System.out.println("0.  Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("\n--- 1. DADOS DO CLIENTE ---");
                    cli = new Cliente();
                    System.out.print("Nome completo: ");
                    cli.setNome(scanner.nextLine());
                    System.out.print("CPF: ");
                    cli.setCPF(scanner.nextLine());
                    
                    System.out.println("\n--- 2. ENDEREÇO ---");
                    System.out.print("Estado (UF): ");
                    String uf = scanner.nextLine();
                    System.out.print("Cidade: ");
                    String cidade = scanner.nextLine();
                    System.out.print("Rua: ");
                    String rua = scanner.nextLine();
                    System.out.print("Número: ");
                    int numero = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Complemento (Apto, Casa, etc): ");
                    String comp = scanner.nextLine();
                    end = new Endereco(uf, cidade, rua, numero, comp);
                    cli.setEndereco(end); // vincula endereço ao cliente

                    System.out.println("\n--- 3. DADOS DA CONTA ---");
                    conta = new ContaBancaria();
                    System.out.print("Número da conta: ");
                    conta.setNumero(scanner.nextLine());
                    System.out.print("Defina o limite inicial do cartão de crédito: R$ ");
                    conta.setLimiteFatura(scanner.nextDouble());
                    scanner.nextLine();
                    cli.setContaCliente(conta); // vincula conta ao cliente
                    
                    listaClientes.add(cli); // Salva na lista do sistema

                    System.out.println("\n Cadastro realizado com sucesso!");
                    break;
                    
                case 2:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("\n--- Dados do Cliente ---");
                    System.out.println("Nome: " + cli.getName());
                    System.out.println("CPF: " + cli.getCPF());
                    if (cli.getEndereco() != null) {
                        Endereco e = cli.getEndereco();
                        System.out.println("Endereço: " + e.getLogradouro() + ", " + e.getNumero()
                            + " " + e.getComplemento() + " - " + e.getCidade() + "/" + e.getEstado());
                    }
                    if (cli.getContaCliente() != null) {
                        System.out.println("Conta nº: " + cli.getContaCliente().getNumero());
                    }
                    break;
                    
                case 3:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("\n--- Saldo ---");
                    System.out.println("Saldo atual: R$ " + conta.getSaldoConta());
                    break;
                    
                case 4:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nDigite o valor para depósito: R$ ");
                    double valorDeposito = scanner.nextDouble();
                    scanner.nextLine();
                    conta.depositar(valorDeposito);
                    System.out.println("Depósito de R$ " + valorDeposito + " realizado com sucesso!");
                    break;
                    
                case 5:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nDigite o valor para saque: R$ ");
                    double valorSaque = scanner.nextDouble();
                    scanner.nextLine();
                    boolean sucessoSaque = conta.sacar(valorSaque);
                    if (sucessoSaque) {
                        System.out.println("Saque de R$ " + valorSaque + " realizado com sucesso!");
                    } else {
                        System.out.println("Saldo insuficiente para realizar o saque.");
                    }
                    break;
                    
                case 6:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    MenuProdutos menuProdutos = new MenuProdutos(cli, scanner);
                    menuProdutos.iniciar();
                    break;

                case 7:
                    if (cli == null) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
                        break;
                    }
                    MenuCategoria menuCategoria = new MenuCategoria(cli, scanner);
                    menuCategoria.iniciar();
                    break;

                case 8:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nNúmero da conta de destino: ");
                    String numDestinoPix = scanner.nextLine();
                    
                    ContaBancaria destinoPix = null;
                    for (Cliente c : listaClientes) {
                        if (c.getContaCliente() != null && c.getContaCliente().getNumero().equals(numDestinoPix)) {
                            destinoPix = c.getContaCliente();
                            break;
                        }
                    }
                    if (destinoPix == null) {
                        System.out.println("Erro: Conta de destino não encontrada no sistema!");
                        break;
                    }
                    
                    System.out.print("Valor a transferir via Pix: R$ ");
                    double valorPix = scanner.nextDouble();
                    scanner.nextLine();
                    FormaDePagamento pix = new Pix();
                    boolean sucessoPix = pix.pagar(conta, destinoPix, valorPix);
                    if (sucessoPix) {
                        System.out.println("Pix de R$ " + valorPix + " realizado com sucesso!");
                        System.out.println("Saldo atual: R$ " + conta.getSaldoConta());
                    } else {
                        System.out.println("Falha no Pix. Verifique o saldo (R$ " + conta.getSaldoConta() + ") e o valor informado.");
                    }
                    break;

                case 9:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.print("\nNúmero da conta de destino: ");
                    String numDestinoCartao = scanner.nextLine();
                    
                    ContaBancaria destinoCartao = null;
                    for (Cliente c : listaClientes) {
                        if (c.getContaCliente() != null && c.getContaCliente().getNumero().equals(numDestinoCartao)) {
                            destinoCartao = c.getContaCliente();
                            break;
                        }
                    }
                    if (destinoCartao == null) {
                        System.out.println("Erro: Conta de destino não encontrada no sistema!");
                        break;
                    }
                    
                    System.out.print("Valor a pagar com Cartão de Crédito: R$ ");
                    double valorCartao = scanner.nextDouble();
                    scanner.nextLine();
                    FormaDePagamento cartao = new CartaoCredito();
                    boolean sucessoCartao = cartao.pagar(conta, destinoCartao, valorCartao);
                    if (sucessoCartao) {
                        System.out.println("Pagamento de R$ " + valorCartao + " no cartão realizado com sucesso!");
                        System.out.println("Fatura atual: R$ " + conta.getFatura() + " / Limite: R$ " + conta.getLimiteFatura());
                    } else {
                        System.out.println("Falha no pagamento. Limite disponível: R$ " + (conta.getLimiteFatura() - conta.getFatura()));
                    }
                    break;

                case 10:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("\n--- Fatura do Cartão de Crédito ---");
                    System.out.println("Fatura atual:      R$ " + conta.getFatura());
                    System.out.println("Limite total:      R$ " + conta.getLimiteFatura());
                    System.out.println("Limite disponível: R$ " + (conta.getLimiteFatura() - conta.getFatura()));
                    break;

                case 11:
                    if (conta == null) {
                        System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
                        break;
                    }
                    System.out.println("Fatura atual: R$ " + conta.getFatura());
                    System.out.print("Valor a pagar da fatura: R$ ");
                    double valorFatura = scanner.nextDouble();
                    scanner.nextLine();
                    double faturaAntes = conta.getFatura();
                    conta.pagarFatura(valorFatura);
                    if (conta.getFatura() < faturaAntes) {
                        System.out.println("Pagamento de R$ " + valorFatura + " na fatura realizado com sucesso!");
                        System.out.println("Fatura restante: R$ " + conta.getFatura());
                    } else {
                        System.out.println("Falha: valor inválido ou maior que a fatura atual (R$ " + faturaAntes + ").");
                    }
                    break;

                case 12:
                    if (listaClientes.isEmpty()) {
                        System.out.println("\n Erro: Nenhum cliente cadastrado no sistema.");
                        break;
                    }
                    System.out.println("\n--- Selecionar Cliente Ativo ---");
                    for (int i = 0; i < listaClientes.size(); i++) {
                        System.out.println((i + 1) + ". " + listaClientes.get(i).getName() + " (Conta: " + listaClientes.get(i).getContaCliente().getNumero() + ")");
                    }
                    System.out.print("Escolha o número do cliente: ");
                    int idx = scanner.nextInt() - 1;
                    scanner.nextLine();
                    if (idx >= 0 && idx < listaClientes.size()) {
                        cli = listaClientes.get(idx);
                        conta = cli.getContaCliente();
                        System.out.println("Cliente ativo alterado com sucesso!");
                    } else {
                        System.out.println("Opção inválida.");
                    }
                    break;

                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;
                    
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
        
        scanner.close();
    }
}
