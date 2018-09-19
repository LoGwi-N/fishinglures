package by.epam.shop.dao;

import by.epam.shop.models.Product;

import java.util.List;

public interface ProductDAO {

    boolean insertProduct(Product product);
    boolean commandProduct(int id, String command);
    Product getById(int id);
    boolean updateProduct(Product product);
    List<Product> getAll();
    List<Product> getActive();

}
