package model.categoria_produto;

public class ItemProduto {
    //contém quantidades de um produto...
    private Produto produto;
    private int quantidadeProduto;

    public ItemProduto(Produto produto, int quantidadeProduto) {
        this.produto = produto;
        this.quantidadeProduto = quantidadeProduto;
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

    /**
     * @param quantidadeProduto the quantidadeProduto to set
     */
    public void setQuantidadeProduto(int quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }
    
    
}
