package model.memento;

import model.categoria_produto.ColecaoProdutos;

public class Originador {
    private ColecaoProdutos carrinho;

    public Originador(ColecaoProdutos carrinhoInicial) {
        this.carrinho = carrinhoInicial;
    }

    public ColecaoProdutos getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(ColecaoProdutos carrinho) {
        // Se você está definindo o carrinho para o Originator,
        // pode ser uma boa prática clonar também para evitar dependências externas.
        this.carrinho = carrinho.copiar(); // Opcional, dependendo do uso
    }

    // Cria um Memento, passando uma CÓPIA do estado atual
    public Memento salvarEstado() {
        return new Memento(this.carrinho); // O Memento se encarrega da cópia profunda
    }

    // Restaura o estado a partir de um Memento
    public void restaurarEstado(Memento memento) {
        // O Originator recebe uma CÓPIA do estado do Memento
        this.carrinho = memento.getCarrinhoSalvo();
    }

}