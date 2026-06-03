package view.terminal;

import java.util.Scanner;
import model.LivreMercado;
import model.cliente.Cliente;
import model.cliente.Endereco;
import model.contaBancaria.ContaBancaria;
import view.LivreMercado_View;
import view.Menu_if;

public class LivreMercado_View_Textual implements LivreMercado_View {

    private final LivreMercado model;
    private final Scanner scanner;

    public LivreMercado_View_Textual(LivreMercado model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostre() {
        menuPrincipal();
    }

    private void menuPrincipal() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n===========================");
            System.out.println("       MENU PRINCIPAL      ");
            System.out.println("===========================");
            System.out.println("1. Cadastrar Cliente e Conta");
            System.out.println("2. Ver dados do Cliente");
            System.out.println("3. Consultar Saldo");
            System.out.println("4. Depositar");
            System.out.println("5. Sacar");
            System.out.println("6. Gerenciar Forma de Pagamento");
            System.out.println("7. Ver / Pagar Fatura do Cartão");
            System.out.println("8. Gerenciar Produtos");
            System.out.println("9. Gerenciar Categorias");
            System.out.println("0. Sair");
            System.out.print("Escolha uma operação: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarClienteEConta();
                    break;
                case 2:
                    verDadosCliente();
                    break;
                case 3:
                    consultarSaldo();
                    break;
                case 4:
                    depositar();
                    break;
                case 5:
                    sacar();
                    break;
                case 6:
                    gerenciarFormaPagamento();
                    break;
                case 7:
                    verPagarFaturaCartao();
                    break;
                case 8:
                    gerenciarProdutos();
                    break;
                case 9:
                    gerenciarCategorias();
                    break;
                case 0:
                    System.out.println("\nEncerrando o sistema...");
                    break;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha um número do menu.");
                    break;
            }
        }
    }

    private void cadastrarClienteEConta() {
        System.out.println("\n--- 1. DADOS DO CLIENTE ---");
        Cliente cliente = new Cliente();
        System.out.print("Nome completo: ");
        cliente.setNome(scanner.nextLine());
        System.out.print("CPF: ");
        cliente.setCPF(scanner.nextLine());

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
        String complemento = scanner.nextLine();
        cliente.setEndereco(new Endereco(uf, cidade, rua, numero, complemento));

        System.out.println("\n--- 3. DADOS DA CONTA ---");
        ContaBancaria conta = new ContaBancaria();
        System.out.print("Número da conta: ");
        conta.setNumero(scanner.nextLine());
        System.out.print("Defina o limite inicial do cartão de crédito: R$ ");
        conta.setLimiteFatura(scanner.nextDouble());
        scanner.nextLine();
        cliente.setContaCliente(conta);

        model.adicionarCliente(cliente);
        model.setClienteLogado(cliente);

        System.out.println("\n Cadastro realizado com sucesso!");
    }

    private void verDadosCliente() {
        Cliente cliente = getClienteCadastrado();
        if (cliente == null) {
            return;
        }

        System.out.println("\n--- Dados do Cliente ---");
        System.out.println("Cliente: " + cliente.getName());
        System.out.println("CPF: " + cliente.getCPF());
        if (cliente.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco();
            System.out.println("Endereço: " + endereco.getLogradouro() + ", " + endereco.getNumero()
                + " " + endereco.getComplemento() + " - " + endereco.getCidade() + "/" + endereco.getEstado());
        }
        if (cliente.getContaCliente() != null) {
            System.out.println("Conta nº: " + cliente.getContaCliente().getNumero());
        }
    }

    private void consultarSaldo() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        System.out.println("\n--- Saldo ---");
        System.out.println("Saldo atual: R$ " + conta.getsaldoConta());
    }

    private void depositar() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        System.out.print("\nDigite o valor para depósito: R$ ");
        double valorDeposito = scanner.nextDouble();
        scanner.nextLine();
        conta.depositar(valorDeposito);
        System.out.println("Depósito de R$ " + valorDeposito + " realizado com sucesso!");
    }

    private void sacar() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
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
    }

    private void gerenciarFormaPagamento() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        Menu_if menuFormaPagamento = new MenuFormaPagamento_View_Textual(conta, scanner);
        menuFormaPagamento.mostre();
    }

    private void verPagarFaturaCartao() {
        ContaBancaria conta = getContaCadastrada();
        if (conta == null) {
            return;
        }

        if (!conta.temFormaPagamento("Cartão de Crédito")) {
            System.out.println("\n Cartão de Crédito não está cadastrado. Acesse Gerenciar Forma de Pagamento.");
            return;
        }

        System.out.println("\n--- Fatura do Cartão de Crédito ---");
        System.out.printf("Fatura atual:      R$ %.2f%n", conta.getFatura());
        System.out.printf("Limite total:      R$ %.2f%n", conta.getLimiteFatura());
        System.out.printf("Limite disponível: R$ %.2f%n", conta.getLimiteFatura() - conta.getFatura());

        if (conta.getFatura() <= 0) {
            return;
        }

        System.out.print("\nDeseja pagar a fatura agora? (s/n): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            return;
        }

        System.out.print("Valor a pagar: R$ ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        double faturaAntes = conta.getFatura();
        conta.pagarFatura(valor);
        if (conta.getFatura() < faturaAntes) {
            System.out.printf("Pagamento de R$ %.2f realizado! Fatura restante: R$ %.2f%n", valor, conta.getFatura());
        } else {
            System.out.println("Valor inválido ou maior que a fatura.");
        }
    }

    private void gerenciarProdutos() {
        Cliente cliente = getClienteCadastrado();
        if (cliente == null) {
            return;
        }

        Menu_if menuProdutos = new Produtos_Menu_View_Textual(model, scanner);
        menuProdutos.mostre();
    }

    private void gerenciarCategorias() {
        System.out.println("\n [Acesso Restrito] O menu de categorias é exclusivo para Administradores.");
        System.out.print("Digite a senha de administrador para continuar (ou qualquer outra coisa para cancelar): ");

        String senha = scanner.nextLine();

        if (senha.equals("123")) {
            System.out.println("\n Acesso concedido!");
            Menu_if menuCategorias = new Categoria_Menu_View_Textual(model, scanner);
            menuCategorias.mostre();
        } else {
            System.out.println("\n Senha incorreta. Acesso negado, voltando ao menu principal...");
        }
    }

    private Cliente getClienteCadastrado() {
        Cliente cliente = model.getClienteLogado();
        if (cliente == null) {
            System.out.println("\n Erro: Nenhum cliente cadastrado. Escolha a opção 1 primeiro.");
        }
        return cliente;
    }

    private ContaBancaria getContaCadastrada() {
        Cliente cliente = getClienteCadastrado();
        if (cliente == null) {
            return null;
        }

        ContaBancaria conta = cliente.getContaCliente();
        if (conta == null) {
            System.out.println("\n Erro: Nenhuma conta cadastrada. Escolha a opção 1 primeiro.");
        }
        return conta;
    }
}
