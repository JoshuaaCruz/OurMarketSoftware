package model.categoria_produto;

import java.util.ArrayList;
import java.util.List;

public class ColecaoProdutos {
    private List<Produto> produtos; // Supondo que você tenha uma classe Produto

    public ColecaoProdutos() {
        this.produtos = new ArrayList<>();
    }

    // Método para adicionar produto
    public void adicionarProduto(Produto produto) {
        this.produtos.add(produto);
    }

    // Método para remover produto
    public void removerProduto(Produto produto) {
        this.produtos.remove(produto);
    }

    // Método para obter os produtos (CUIDADO: pode retornar a referência!)
    public List<Produto> getProdutos() {
        return produtos;
    }

    // *** MÉTODO CRÍTICO: Para fazer a cópia profunda ***
    public ColecaoProdutos copiar() {
        ColecaoProdutos novaColecao = new ColecaoProdutos();
        for (Produto p : this.produtos) {
            // Se Produto for imutável, pode copiar a referência.
            // Se Produto for mutável, você precisaria de um p.copiar() aqui também.
            novaColecao.adicionarProduto(p); // Adiciona o produto à nova coleção
        }
        return novaColecao;
    }

    @Override
    public String toString() {
        return "ColecaoProdutos{" +
                "produtos=" + produtos +
                '}';
    }
}