package by.epam.shop.models;

public class Cart extends BaseModel {

    private String title;
    private int count;
    private int price;

    public Cart(int id, String title, int count, int price) {
        super(id);
        this.title = title;
        this.count = count;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }
}
