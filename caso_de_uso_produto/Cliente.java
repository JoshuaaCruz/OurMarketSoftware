
public class Cliente {
   private String nome;
   private String CPF;
   private Endereco endereco;
   private contaBancaria contaCliente;
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

   public void setNome(String var1) {
      this.nome = var1;
   }

   public void setCPF(String var1) {
      this.CPF = var1;
   }

   public Endereco getEndereco() {
      return this.endereco;
   }

   public contaBancaria getContaCliente() {
      return this.contaCliente;
   }

       public ColecaoProdutos getEstoque() {
        return this.estoque;
    }

    public ColecaoProdutos getCarrinho() {
        return this.carrinho;
    }

}
