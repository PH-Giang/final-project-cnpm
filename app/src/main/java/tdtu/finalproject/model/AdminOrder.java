package tdtu.finalproject.model;

public class AdminOrder {
    private int id;
    private String customer;
    private String phone;
    private String address;
    private int total;
    private String date;
    private int status;

    public AdminOrder(int id, String customer, String phone, String address, int total, String date, int status) {
        this.id = id;
        this.customer = customer;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
