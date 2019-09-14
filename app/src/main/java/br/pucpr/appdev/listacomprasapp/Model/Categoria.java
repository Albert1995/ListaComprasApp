package br.pucpr.appdev.listacomprasapp.Model;

public class Categoria {

    String nome,id;


    public Categoria(String nome, String id) {
        this.nome = nome;
        this.id = id;
    }

    public Categoria() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
