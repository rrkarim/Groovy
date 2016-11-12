package Classes;

/**
 * Created by YoAtom on 11/6/2016.
 */

public class Country {
    private String name;
    private int id;
    public Country(String name, int id) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}
