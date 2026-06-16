package model.categoria_produto;

import java.util.ArrayList;
import java.util.List;

public class ColecaoProdutos {
    private List<ItemProduto> itens;

    public ColecaoProdutos() {
        this.itens = new ArrayList<>();
    }

    /**
     * Adiciona um Produto à coleção especificando a quantidade.
     */
    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return;
        
        for (ItemProduto item : itens) {
            if (item.getProduto().equals(produto)) {
                item.setQuantidadeProduto(item.getQuantidadeProduto() + quantidade);
                return;
            }
        }
        
        itens.add(new ItemProduto(produto, quantidade));
    }


    /**
     * Remove uma quantidade específica do Produto da coleção.
     */
    public void removerProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return;
        
        for (int i = 0; i < itens.size(); i++) {
            ItemProduto item = itens.get(i);
            if (item.getProduto().equals(produto)) {
                int novaQtd = item.getQuantidadeProduto() - quantidade;
                if (novaQtd <= 0) {
                    itens.remove(i);
                } else {
                    item.setQuantidadeProduto(novaQtd);
                }
                return;
            }
        }
    }

    public void limparColecao() {
        itens.clear();
    }

    /**
     * Consulta a quantidade atual de um produto nesta coleção.
     * Ver se tem estoque suficiente
     */
    public int getQuantidade(Produto produto) {
        if (produto == null) return 0;
        for (ItemProduto item : itens) {
            if (item.getProduto().equals(produto)) {
                return item.getQuantidadeProduto();
            }
        }
        return 0;
    }


    public List<ItemProduto> getItens() {
        return itens;
    }

    /**
     * Método auxiliar para manter compatibilidade com código que precisa
     * apenas da lista de produtos únicos (ignorando quantidades).
     */
    public List<Produto> getProdutos() {
        List<Produto> produtosUnicos = new ArrayList<>();
        for (ItemProduto item : itens) {
            produtosUnicos.add(item.getProduto());
        }
        return produtosUnicos;
    }

    @Override
    public String toString() {
        return "ColecaoProdutos{" +
                "itens=" + itens +
                '}';
    }
}