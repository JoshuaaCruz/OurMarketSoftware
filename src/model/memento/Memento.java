package model.memento;

import model.categoria_produto.ColecaoProdutos;
// Certifique-se de que o Memento só expõe o estado de forma controlada

public class Memento {

    // Final para garantir que o estado salvo é imutável após a criação do Memento
    private final ColecaoProdutos carrinhoSalvo;

    // O Memento DEVE receber uma CÓPIA do estado, não a referência original.
    // Isso garante que o estado salvo não seja alterado externamente.
    public Memento(ColecaoProdutos newCarrinho) {
        // Assume que ColecaoProdutos.copiar() faz uma deep copy
        this.carrinhoSalvo = newCarrinho.copiar();
    }

    // O Caretaker precisa acessar o estado salvo, mas não deve modificá-lo.
    // Retornar uma cópia do estado é mais seguro, ou um tipo imutável.
    // Se ColecaoProdutos é mutável, retornar sua referência ainda pode ser um problema.
    // Uma opção é retornar uma cópia (se houver um getProdutos().copiar() ou algo assim)
    // Ou retornar um objeto imutável que represente o carrinho.
    public ColecaoProdutos getCarrinhoSalvo() {
        // Se ColecaoProdutos for mutável, você pode retornar uma cópia aqui também
        // para evitar modificações externas:
        return this.carrinhoSalvo.copiar(); // Garante que quem recebe não altera o Memento
    }
}