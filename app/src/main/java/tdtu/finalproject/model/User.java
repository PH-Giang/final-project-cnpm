package tdtu.finalproject.model;

public class User {
    private String username = "";
    private String password = "";
    private String phone ="";
    private int id = -1;
    private String name = "";
    private String pass2 = "";
    private String address = "";
    public User(String username, String password, String phone, int id, String name, String pass2, String address) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.id = id;
        this.name = name;
        this.pass2 = pass2;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public User(){

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
