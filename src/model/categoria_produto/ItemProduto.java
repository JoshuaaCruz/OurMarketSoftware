
package model.categoria_produto;

import model.cliente.Cliente;

public class ItemProduto {
    private Produto produto;
    private int quantidadeProduto;
    private Cliente vendedor;
    private double precoBase;
    private double nota;

    public ItemProduto() {
        this.quantidadeProduto = 1;
    }

    public ItemProduto(Produto produto, int quantidadeProduto) {
        this.produto = produto;
        this.quantidadeProduto = Math.max(1, quantidadeProduto);
        if (produto != null) {
            this.vendedor = produto.getVendedor();
            this.precoBase = produto.getPrecoBase();
            this.nota = produto.getNota();
        }
    }

    /**
     * @return the produto
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * @return the quantidadeProduto
     */
    public int getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * @param quantidadeProduto the quantidadeProduto to set
     */
    public void setQuantidadeProduto(int quantidadeProduto) {
        this.quantidadeProduto = Math.max(1, quantidadeProduto);
    }

    public Cliente getVendedor() {
        return vendedor;
    }

    public void setVendedor(Cliente vendedor) {
        this.vendedor = vendedor;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
