package br.com.cybertronyk.minhabibliacatolicav2.vo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by cleiton.dantas on 02/05/2016.
 */
@Entity
public class Anotacoes {

    @Generated
    public  Anotacoes(){
    }

    @Generated
    public  Anotacoes(Long id){
        this.id = id;
    }

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "USERNAME")
    private String versiculos;


    private String comentarios;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
