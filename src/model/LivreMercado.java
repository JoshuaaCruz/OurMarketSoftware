/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package model;

import java.util.ArrayList;
import model.autenticador.Autenticador_if;
import model.categoria_produto.Categoria;
import model.categoria_produto.Categoria_if;
import model.categoria_produto.Produto;
import model.cliente.Cliente;
import model.contaBancaria.ContaBancaria;

/**
 *
 * @author joshua.cruz
 */
public class LivreMercado {
    
    private ArrayList<Cliente> clientes;
    private ContaBancaria contaMercado;
    
    private final Autenticador_if autenticador;
    private final Categoria_if categoriaRaiz;
    

    public LivreMercado() {
        initOrLoad();
    }
    public Autenticador_if getAutenticador() {
        return autenticador;
    }

    private void initOrLoad() {
        // inicializa categorias
        Categoria g1, g2;
        categoriaRaiz.addCategoria(new Categoria("Veiculos"));
        g1 = new Categoria("Tecnologia");
        categoriaRaiz.addCategoria(g1);
        categoriaRaiz.addCategoria(new Categoria("Casa e Mooveis"));
        g1.addCategoria(new Categoria("Informatica"));
        
        Categoria games = new Categoria("Games");
        g1.addCategoria(games);
        
        games.addProduto(new Produto("GTA", "SO MUCH FUN", 100));
        games.addProduto(new Produto("RDR2", "SO MUCH COWBOYS", 1000));

        
        //g1.addCategoria(new Categoria("Games"));
        
        
        g2 = new Categoria("Celulares e Telefones");
        g1.addCategoria(g2);
//        g2.addCategoria(new Categoria("Peccas para Celular"));
                

        Categoria pecasCelular = new Categoria("Peças para Celular");
        Produto randP = new Produto("Carregador", "Carrega Iphone", 50);
        Produto randP2 = new Produto("capinha", "Carrega Iphone", 5000);
        
        pecasCelular.addProduto(randP);
        pecasCelular.addProduto(randP2);
        g2.addCategoria(pecasCelular);
        
   
    }
    
    /**
     * @return the categoriaRai
     */
    public Categoria_if getCategoriaRaiz() {
        return categoriaRaiz;
    }

    {
        categoriaRaiz = new Categoria("Geral");
        autenticador = Fabrica.new_Autenticador();
    }


}
