package by.epam.shop.dao;

import by.epam.shop.models.Product;
import by.epam.shop.service.ConfigManager;
import by.epam.shop.service.ConnectionManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class responsible for Products using MySQL
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

public class MysqlProductDAO implements ProductDAO {

    final Logger log = Logger.getLogger(this.getClass());

    private final String SELECT_COMMAND = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ALL_PR);
    private final String SELECT_COMMAND_ACTIVE = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ACT_PR);
    private final String INSERT_COMMAND = ConfigManager.getProperty(ConfigManager.SQL_INSERT_PRODUCT);
    private final String SELECT_PR_BY_ID = ConfigManager.getProperty(ConfigManager.SQL_SELECT__PR_BY_ID);
    private final String UPDATE_PRODUCT = ConfigManager.getProperty(ConfigManager.SQL_UPDATE_PRODUCT);
    private final String DELETE_PRODUCT = ConfigManager.getProperty(ConfigManager.SQL_DEL_PRODUCT_BY_ID);
    private final String CHANGE_STATUS = ConfigManager.getProperty(ConfigManager.SQL_PRODUCT_CHANGE_STATUS);

    public MysqlProductDAO() {
    }

    @Override
    public boolean insertProduct(Product product) {
        Connection con = null;
        PreparedStatement insertProduct = null;

        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                insertProduct = con.prepareStatement(INSERT_COMMAND);
                try {
                    insertProduct.setString(1, product.getTitle());
                    insertProduct.setString(2, product.getDesc());
                    insertProduct.setInt(3, product.getPrice());
                    insertProduct.setInt(4, product.getAmount());
                    insertProduct.executeUpdate();
                    return true;
                } finally {
                    if (insertProduct != null) {
                        insertProduct.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
                return false;
            } finally {
                if (insertProduct != null) {
                    try {
                        insertProduct.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Connection null: " + e);
            return false;
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }


    @Override
    public boolean commandProduct(int id, String command) {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            con.setAutoCommit(false);
            switch (command) {
                case "delete": {
                    /**
                     * This action danger for DB, it possible when you testing project or just start it, in other case you can get problems with Database
                     */
                    statement = con.prepareStatement(DELETE_PRODUCT);
                    break;
                }
                case "status": {
                    statement = con.prepareStatement(CHANGE_STATUS);
                    break;
                }
            }
            statement.setInt(1, id);
            if (statement.executeUpdate() == 0) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    log.error("Transaction is being rolled back");
                    con.rollback();
                    return false;
                } catch (SQLException ex) {
                    log.error("con.rollback error; " + ex);
                }
            }
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("findProducts.close error: " + e);
                }
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("con.setAutoCommit error: " + e);
            }
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }

    @Override
    public Product getById(int id) {
        Product product = null;
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                statement = con.prepareStatement(SELECT_PR_BY_ID);
                statement.setInt(1, id);
                try {
                    resultSet = statement.executeQuery();
                    int pr_id = 0;
                    String title = "";
                    String desc = "";
                    int price = 0;
                    int amount = 0;
                    int status = 0;
                    while (resultSet.next()) {
                        pr_id = resultSet.getInt(1);
                        title = resultSet.getString(2);
                        desc = resultSet.getString(3);
                        price = resultSet.getInt(4);
                        amount = resultSet.getInt(5);
                        status = resultSet.getInt(6);
                    }
                    product = new Product(pr_id, title, desc, price, amount, status);
                } catch (SQLException e) {
                    log.error("resultSet error: " + e);
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Connection null: " + e);
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
        return product;
    }

    @Override
    public boolean updateProduct(Product product) {

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            con.setAutoCommit(false);
            statement = con.prepareStatement(UPDATE_PRODUCT);
            statement.setString(1, product.getTitle());
            statement.setString(2, product.getDesc());
            statement.setInt(3, product.getPrice());
            statement.setInt(4, product.getAmount());
            statement.setInt(5, product.getStatus());
            statement.setInt(6, product.getId());
            if (statement.executeUpdate() == 0) {
                con.rollback();
                return false;
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    log.error("Transaction is being rolled back");
                    con.rollback();
                    return false;
                } catch (SQLException ex) {
                    log.error("con.rollback error; " + ex);
                }
            }
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("findProducts.close error: " + e);
                }
            }
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("con.setAutoCommit error: " + e);
            }
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }


    }

    @Override
    public List<Product> getAll() {
        List<Product> product = new LinkedList<>();
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                statement = con.createStatement();
                try {
                    resultSet = statement.executeQuery(SELECT_COMMAND);
                    while (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        String title = resultSet.getString(2);
                        String desc = resultSet.getString(3);
                        int price = resultSet.getInt(4);
                        int amount = resultSet.getInt(5);
                        int status = resultSet.getInt(6);
                        product.add(new Product(id, title, desc, price, amount, status));
                    }
                } catch (SQLException e) {
                    log.error("resultSet error: " + e);
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Connection null: " + e);
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }

        return product;
    }

    @Override
    public List<Product> getActive() {
        List<Product> product = new LinkedList<>();
        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                statement = con.createStatement();
                try {
                    resultSet = statement.executeQuery(SELECT_COMMAND_ACTIVE);
                    while (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        String title = resultSet.getString(2);
                        String desc = resultSet.getString(3);
                        int price = resultSet.getInt(4);
                        int amount = resultSet.getInt(5);
                        int status = resultSet.getInt(6);
                        product.add(new Product(id, title, desc, price, amount, status));
                    }
                } catch (SQLException e) {
                    log.error("resultSet error: " + e);
                } finally {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Connection null: " + e);
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }

        return product;
    }
}
