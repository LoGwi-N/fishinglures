package by.epam.shop.models;

import java.util.List;

public class Order extends BaseModel {

    private int userId;
    private Status status;
    private String date;
    private List<OrderList> orderLists;
    private String clientName;

    public Order(int userId, Status status, List<OrderList> orderLists) {
        this.userId = userId;
        this.status = status;
        this.orderLists = orderLists;
    }

    public Order(int id, int userId, Status status, String date, List<OrderList> orderLists) {
        super(id);
        this.userId = userId;
        this.status = status;
        this.date = date;
        this.orderLists = orderLists;
    }

    public Order(int id, String username, Status status, String date, List<OrderList> orderLists) {
        super(id);
        this.clientName = username;
        this.status = status;
        this.date = date;
        this.orderLists = orderLists;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + "\"" + id + "\"," +
                "\"userId\":" + "\"" + userId + "\"," +
                "\"username\":" + "\"" + clientName + "\"," +
                "\"status\":" + "\"" + status + "\"," +
                "\"products\":"  + orderLists + "," +
                "\"date\":" + "\"" + date + "\"" +
                "}";
    }

    public int getUserID() {
        return userId;
    }

    public void setUserID(int userId) {
        this.userId = userId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderList> getOrderLists() {
        return orderLists;
    }

    public void setProductOfOrderList(List<OrderList> orderLists) {
        this.orderLists = orderLists;
    }

    public enum Status {
        PROCESSED, PAID, CANCELLED
    }

    public static class OrderList {

        private int id;
        private int orderId;
        private int productId;
        private Product product;
        private int amount;

        public OrderList(int productId, int amount) {
            this.productId = productId;
            this.amount = amount;
        }

        public OrderList(int id, int orderId, Product product, int amount) {
            this.id = id;
            this.orderId = orderId;
            this.product = product;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getProduct_id() {
            return productId;
        }

        public void setProduct_id(int productId) {
            this.productId = productId;
        }

        @Override
        public String toString() {
            return "{\"id\":\"" + id + "\"," +
                    "\"orderId\":\"" + orderId + "\"," +
                    "\"product\":" + product + "," +
                    "\"amount\":\"" + amount + "\"}";
        }
    }

}
