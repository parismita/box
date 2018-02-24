package sagarmusa.box2;
public class Product {
    public String id;
    public String name;
    public String d1;
    public String d2;
    public String d3;

    public Product(String id, String name,String d1, String d2, String d3) {
        this.id = id;
        this.name = name;
        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getD1() {
        return d1;
    }

    public String getD2() {
        return d2;
    }

    public String getD3() {
        return d3;
    }
}