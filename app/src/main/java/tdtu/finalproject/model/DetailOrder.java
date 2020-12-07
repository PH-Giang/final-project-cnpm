package tdtu.finalproject.model;

public class DetailOrder {
    private int detailID;
    private int orderID;
    private int productID;
    private int quantity;
    private int cost;

    public DetailOrder(int detailID, int orderID, int productID, int quantity, int cost) {
        this.detailID = detailID;
        this.orderID = orderID;
        this.productID = productID;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
