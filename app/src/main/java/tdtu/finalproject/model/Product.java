package tdtu.finalproject.model;

public class Product {
    private int id;
    private int id_cate;
    private String name;
    private String description;
    private int price;
    private String image;


    public Product(int id, int id_cate, String name, String description, int price, String image) {
        this.id = id;
        this.id_cate = id_cate;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;

    }

    public Product(String name, int price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cate() {
        return id_cate;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
