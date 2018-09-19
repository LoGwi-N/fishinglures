package by.epam.shop.models;

public class Product extends BaseModel {

    private String title;
    private String desc;
    private int price;
    private int amount;
    private int status;

    public Product(int id, String title, String desc, int price, int amount, int status) {
        super(id);
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.amount = amount;
        this.status = status;
    }

    public Product(String title, String desc, int price, int amount) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + "\"" + id + "\"," +
                "\"title\":" + "\"" + title + "\"," +
                "\"desc\":" + "\"" + desc + "\"," +
                "\"price\":" + "\"" + price + "\"," +
                "\"amount\":" + "\"" + amount + "\"," +
                "\"status\":" + "\"" + status + "\"" +
                "}";
    }
}
