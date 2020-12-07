package tdtu.finalproject.model;

public class MyOrderModel {
    private int id;
    private int total;
    private String date;
    private boolean status;
    public MyOrderModel(int id, int total, String date, boolean status) {
        this.id = id;
        this.total = total;
        this.date = date;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
