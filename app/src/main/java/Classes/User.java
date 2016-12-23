package Classes;

import java.util.Date;

/**
 * Created by YoAtom on 12/21/2016.
 */

public class User {
    private int id, folCont;
    private String name, surname;
    private String email, image, image_small;
    private String date; // fix

    public User() {}
    public User(int id, String name, String surname, String image, int folCont) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.image = image;
        this.folCont = folCont;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFol_cont() {
        return folCont;
    }

    public void setFol_cont(int fol_cont) {
        this.folCont = fol_cont;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_small() {
        return image_small;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
