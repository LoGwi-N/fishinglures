package by.epam.shop.dao;

public abstract class AbstractDAOFactory {

    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";

    private static AbstractDAOFactory getDAOFactory(String whichFactory) {
        switch (whichFactory) {
            case MYSQL: {
                return new MysqlAbstractDAOFactory();
            }
            default: {
                return null;
            }
        }
    }

    public abstract UserDAO getUserDAO();
    public abstract ProductDAO getProductDAO();
    public abstract OrderDAO getOrderDAO();


    /**
     * This object returns us DAO with the desired database
     * If We want to change the database enough to make it in this line
     */
    public static AbstractDAOFactory dbFactory =
            AbstractDAOFactory.getDAOFactory(AbstractDAOFactory.MYSQL);

}
