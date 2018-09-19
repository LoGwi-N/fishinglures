package by.epam.shop.dao;

public class MysqlAbstractDAOFactory extends AbstractDAOFactory {

    public UserDAO getUserDAO() {

        return new MysqlUserDAO();
    }

    public ProductDAO getProductDAO() {

        return new MysqlProductDAO();
    }

    public OrderDAO getOrderDAO() {

        return new MysqlOrderDAO();
    }
}
