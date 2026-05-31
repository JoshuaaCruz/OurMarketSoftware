import java.util.ArrayList;
import java.util.List;

public class ColecaoProdutos {
    private List<Produto> produtos;

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

    // Método para obter os produtos (ver se não retorna referência direta para evitar mutabilidade externa)
    public List<Produto> getProdutos() {
        return produtos;
    }

    // *** MÉTODO CRÍTICO: Para fazer a cópia profunda ***
    public ColecaoProdutos copiar() {
        ColecaoProdutos novaColecao = new ColecaoProdutos();
        for (Produto p : this.produtos) {
            novaColecao.adicionarProduto(p); // Adiciona o produto à nova coleção
        }
        return novaColecao;
    }

    public int getQuantidadeProdutos() {
        return this.produtos.size();
    }

    @Override
    public String toString() {
        return "ColecaoProdutos{" +
                "produtos=" + produtos +
                '}';
    }
}