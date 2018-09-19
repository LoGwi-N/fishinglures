package by.epam.shop.models;

public class BaseModel {

    protected int id;

    public BaseModel(int id) {
        this.id = id;
    }

    public BaseModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
