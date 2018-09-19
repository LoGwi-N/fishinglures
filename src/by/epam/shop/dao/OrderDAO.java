package by.epam.shop.dao;

import by.epam.shop.models.Order;

import java.util.List;

public interface OrderDAO {

    String insertOrder(Order order);
    boolean cancelOrder(int userId, int orderId);
    boolean payOrder(int userId, int orderId);
    List<Order> getAll();
    List<Order> getAllByUser(int ID);
    boolean changeStatusOrder(int id);

}
