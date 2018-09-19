package by.epam.shop.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class User extends BaseModel {

    private String name;
    private String surname;
    private String login;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private String address;
    private int status;
    private String registered;

    public User() {

    }

    public User(int id, String name, String surname, String login, String email, String phone, String password, Role role, String address, int status, String registered) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.address = address;
        this.status = status;
        this.registered = registered;
    }

    //    Registration
    public User(String name, String surname, String login, String email, String phone, String password, String address) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public User(String name, String surname, String login, String email, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public static String getSalt(String str) {
        String key = "25345eklmbd%#%FEF";
        String salt = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            str += key;
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02X", b));
            }
            salt = builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null) {
            User users = (User) object;
            if (users.getLogin().equals(getLogin()) && users.getPassword().equals(getPassword())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return new StringBuilder()
                .append("{\"id\":" + id + ",")
                .append("\"name\":" + "\"" + name + "\",")
                .append("\"surname\":" + "\"" + surname + "\",")
                .append("\"login\":" + "\"" + login + "\",")
                .append("\"email\":" + "\"" + email + "\",")
                .append("\"phone\":" + "\"" + phone + "\",")
                .append("\"password\":" + "\"" + password + "\",")
                .append("\"role\":" + "\"" + role + "\",")
                .append("\"address\":" + "\"" + address + "\",")
                .append("\"status\":" + status + ",")
                .append("\"registered\":" + "\"" + registered + "\"}")
                .toString();
    }

    public enum Role {
        ADMIN, USER
    }
}
