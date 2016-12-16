package Classes;

import java.util.Date;

/**
 * Created by YoAtom on 12/14/2016.
 */

public class Author {
    private int id, fol_cont;
    private String name;
    private String surname;
    private String email;

    public int getId() {
        return id;
    }

    public int getFol_cont() {
        return fol_cont;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getImage_small() {
        return image_small;
    }

    public Date getDate() {
        return date;
    }

    private String image;
    private String image_small;
    private Date date;

    public void setId(int id) {
        this.id = id;
    }

    public void setFol_cont(int fol_cont) {
        this.fol_cont = fol_cont;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Author(int id, String name, String surname, String image_small, Date date) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.image_small = image_small;
        this.date = date;
    }
}
