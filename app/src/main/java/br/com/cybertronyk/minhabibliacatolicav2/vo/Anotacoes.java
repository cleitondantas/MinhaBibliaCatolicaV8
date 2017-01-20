package br.com.cybertronyk.minhabibliacatolicav2.vo;

/**
 * Created by cleiton.dantas on 02/05/2016.
 */
public class Anotacoes {

    private int id;
    private String versiculos;
    private String comentarios;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getVersiculos() {
        return versiculos;
    }

    public void setVersiculos(String versiculos) {
        this.versiculos = versiculos;
    }
}
