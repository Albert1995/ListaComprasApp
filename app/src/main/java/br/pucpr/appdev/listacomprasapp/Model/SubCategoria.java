package br.pucpr.appdev.listacomprasapp.Model;

public class SubCategoria {

    private String categoria;
    private String id;
    private String descricao;

    public SubCategoria() {
    }

    public SubCategoria(String id, String descricao, String categoria) {
        this.id = id;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String idCategoria) {
        this.categoria = idCategoria;
    }
}
