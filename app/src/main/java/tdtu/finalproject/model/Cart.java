package tdtu.finalproject.model;

public class Cart {
    private int id;
    private String image;
    private String name;
    private int price;
    private int quantity;
    private String size;
    private int retail;
    public Cart(int id, String image, String name, int price, int quantity, String size, int retail) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.retail = retail;
    }

    public int getRetail() {
        return retail;
    }

    public void setRetail(int retail) {
        this.retail = retail;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
