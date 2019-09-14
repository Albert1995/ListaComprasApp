package br.pucpr.appdev.listacomprasapp.Model;

import java.io.Serializable;
import java.util.List;

public class Categoria implements Serializable {

    String nome,id,idUsuario;
    List<SubCategoria> lista;

    public Categoria(String nome, String id, String idUsuario) {
        this.nome = nome;
        this.id = id;
        this.idUsuario = idUsuario;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

}
