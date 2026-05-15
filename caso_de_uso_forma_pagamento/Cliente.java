import contaBancaria.ContaBancaria;

public class Cliente {
   private String nome;
   private String CPF;
   private Endereco endereco;
   private ContaBancaria contaCliente;
   private ColecaoProdutos estoque, carrinho;

   public Cliente() {
      this.estoque = new ColecaoProdutos();
      this.carrinho = new ColecaoProdutos();
   }

   public String getName() {
      return this.nome;
   }

   public String getCPF() {
      return this.CPF;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public void setCPF(String CPF) {
      this.CPF = CPF;
   }

   public Endereco getEndereco() {
      return this.endereco;
   }

   public void setEndereco(Endereco endereco) {
      this.endereco = endereco;
   }

   public ContaBancaria getContaCliente() {
      return this.contaCliente;
   }

   public void setContaCliente(ContaBancaria contaCliente) {
      this.contaCliente = contaCliente;
   }

   public ColecaoProdutos getEstoque() {
      return this.estoque;
   }

   public ColecaoProdutos getCarrinho() {
      return this.carrinho;
   }
}
