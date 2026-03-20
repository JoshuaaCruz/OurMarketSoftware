/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.memento;

import java.util.ArrayList;

/**
 *
 * @author joshua.cruz
 */
public class Caretaker {


    private ArrayList<Memento> historicoCarrinho;
    private int pointer2CarrNow;

    public Caretaker() {
        this.historicoCarrinho = new ArrayList<>();
        this.pointer2CarrNow = -1; // -1 indica que não há nenhum estado salvo ainda
    }

    public Memento desfazer() {
        if (pointer2CarrNow > 0) { // Pode ir para trás se não estiver no primeiro estado
            pointer2CarrNow--;
            System.out.println("Desfeito. Ponteiro agora em " + pointer2CarrNow);
            return historicoCarrinho.get(pointer2CarrNow);
        } else {
            System.out.println("Não é possível desfazer mais (já no primeiro estado).");
            return historicoCarrinho.get(0); // Retorna o primeiro estado se não puder desfazer mais
        }
    }

    public Memento desfazer(int qtdDesfeitas) {
        if (qtdDesfeitas <= 0) {
            System.out.println("Quantidade para desfazer deve ser positiva.");
            return historicoCarrinho.get(pointer2CarrNow); // Retorna o estado atual
        }

        int novoPointer = pointer2CarrNow - qtdDesfeitas;
        if (novoPointer < 0) { // Se tentar ir além do início
            novoPointer = 0;
            System.out.println("Não é possível desfazer " + qtdDesfeitas + " ações. Desfazendo até o início.");
        } else {
            System.out.println("Desfazendo " + qtdDesfeitas + " ações.");
        }
        pointer2CarrNow = novoPointer;
        return historicoCarrinho.get(pointer2CarrNow);
    }

    public Memento refazer() {
        if (pointer2CarrNow < historicoCarrinho.size() - 1) { // Pode ir para frente se não estiver no último estado
            pointer2CarrNow++;
            System.out.println("Refeito. Ponteiro agora em " + pointer2CarrNow);
            return historicoCarrinho.get(pointer2CarrNow);
        } else {
            System.out.println("Não é possível refazer mais (já no último estado).");
            return historicoCarrinho.get(historicoCarrinho.size() - 1); // Retorna o último estado se não puder refazer mais
        }
    }

    public Memento refazer(int qtdRefeitas) {
        if (qtdRefeitas <= 0) {
            System.out.println("Quantidade para refazer deve ser positiva.");
            return historicoCarrinho.get(pointer2CarrNow); // Retorna o estado atual
        }

        int novoPointer = pointer2CarrNow + qtdRefeitas;
        if (novoPointer >= historicoCarrinho.size()) { // Se tentar ir além do final
            novoPointer = historicoCarrinho.size() - 1;
            System.out.println("Não é possível refazer " + qtdRefeitas + " ações. Refazendo até o final.");
        } else {
            System.out.println("Refazendo " + qtdRefeitas + " ações.");
        }
        pointer2CarrNow = novoPointer;
        return historicoCarrinho.get(pointer2CarrNow);
    }



    public void advanceState(Memento novoCarrinho) {
        // Se o ponteiro não está no final do histórico, significa que o usuário desfez algumas ações.
        // Ao adicionar um novo estado, todos os estados "refazíveis" à frente do ponteiro atual devem ser descartados.
        if (pointer2CarrNow < historicoCarrinho.size() - 1) {
            // Remove os elementos a partir do ponteiro atual + 1 até o final
            historicoCarrinho.subList(pointer2CarrNow + 1, historicoCarrinho.size()).clear();
        }

        historicoCarrinho.add(novoCarrinho);
        pointer2CarrNow = historicoCarrinho.size() - 1; // Aponta para o último estado adicionado
        System.out.println("Estado avançado. Novo histórico de " + historicoCarrinho.size() + " estados. Ponteiro em " + pointer2CarrNow);
    }
    
//    public Memento returnMemento(int pointer){
//        //torna o carrinho como o estado anterior
//
//
//        return historicoCarrinho.get(pointer);
//    }
    
    
    
    
}
