package by.epam.shop.dao;

import by.epam.shop.models.Order;
import by.epam.shop.service.ConfigManager;
import by.epam.shop.service.ConnectionManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class responsible for Orders using MySQL
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

public class MysqlOrderDAO implements OrderDAO {

    final Logger log = Logger.getLogger(this.getClass());

    private final String INSERT_ORDER = ConfigManager.getProperty(ConfigManager.SQL_INSERT_ORDER);
    private final String INSERT_ORDER_LIST = ConfigManager.getProperty(ConfigManager.SQL_INSERT_ORDER_INFO);
    private final String UPDATE_COUNT = ConfigManager.getProperty(ConfigManager.SQL_UPDATE_AMOUNT_PRODUCT);
    private final String SELECT_ORDERS_BY_ID = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ORDERS_BY_ID);
    private final String SELECT_ORDER_LIST_BY_ORDER_ID = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ORDER_LIST_BY_ORDER_ID);
    private final String SELECT_ORDERS = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ORDERS);
    private final String CANCEL_ORDER = ConfigManager.getProperty(ConfigManager.SQL_CANCEL_ORDER);
    private final String PAY_ORDER = ConfigManager.getProperty(ConfigManager.SQL_PAY_ORDER);
    private final String UPDATE_STATUS_ORDER = ConfigManager.getProperty(ConfigManager.SQL_UPDATE_STATUS_ORDER);

    public MysqlOrderDAO() {
    }

    /*
     * This method add new Order to Database
     */
    @Override
    public String insertOrder(Order order) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement updateCount = null;
        PreparedStatement insertOrder = null;
        PreparedStatement insertOrderList = null;
        PreparedStatement setID = null;
        try {
            con.setAutoCommit(false);
            updateCount = con.prepareStatement(UPDATE_COUNT);
            insertOrder = con.prepareStatement(INSERT_ORDER);
            setID = con.prepareStatement("SELECT LAST_INSERT_ID() INTO @last_id_in_orders;");
            insertOrderList = con.prepareStatement(INSERT_ORDER_LIST);
            boolean flag = false;
            for (Order.OrderList ol : order.getOrderLists()) {
                updateCount.setInt(1, ol.getAmount());
                updateCount.setInt(2, ol.getProduct_id());
                updateCount.setInt(3, ol.getAmount());
                if (updateCount.executeUpdate() == 0) {
                    con.rollback();
                    return "error";
                }
                if (!flag) {
                    insertOrder.setInt(1, order.getUserID());
                    insertOrder.setString(2, timeStamp);
                    insertOrder.executeUpdate();
                    setID.executeQuery();
                    flag = true;
                }
                insertOrderList.setInt(1, ol.getProduct_id());
                insertOrderList.setInt(2, ol.getAmount());
                insertOrderList.executeUpdate();
                con.commit();
            }
            return "success";
        } catch (SQLException e) {
            if (con != null) {
                try {
                    log.error("Transaction is being rolled back");
                    con.rollback();
                    return "rollback";
                } catch (SQLException ex) {
                    log.error("con.rollback error; " + ex);
                }
            }
            return "error";
        } finally {
            if (updateCount != null) {
                try {
                    updateCount.close();
                } catch (SQLException e) {
                    log.error("updateCount.close error: " + e);
                }
            }
            if (insertOrder != null) {
                try {
                    insertOrder.close();
                } catch (SQLException e) {
                    log.error("insertOrder.close error: " + e);
                }
            }
            if (insertOrderList != null) {
                try {
                    insertOrderList.close();
                } catch (SQLException e) {
                    log.error("insertOrderList.close error: " + e);
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


    /*
     * This method return list of order by User ID
     */
    @Override
    public List<Order> getAllByUser(int ID) {
        List<Order> order = new ArrayList<>();

        ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
        Connection con = null;
        PreparedStatement selectOrders = null;
        PreparedStatement selectOrderList = null;
        ResultSet resultSelectOrders = null;
        ResultSet resultSelectOrderList = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                selectOrders = con.prepareStatement(SELECT_ORDERS_BY_ID);
                selectOrderList = con.prepareStatement(SELECT_ORDER_LIST_BY_ORDER_ID);
                selectOrders.setInt(1, ID);
                try {
                    resultSelectOrders = selectOrders.executeQuery();
                    while (resultSelectOrders.next()) {
                        int id = resultSelectOrders.getInt(1);
                        int userID = resultSelectOrders.getInt(2);
                        String status = resultSelectOrders.getString(3);
                        String date = resultSelectOrders.getString(4);
                        Order.Status userStatus;
                        switch (status) {
                            case "PROCESSED": {
                                userStatus = Order.Status.PROCESSED;
                                break;
                            }
                            case "PAID": {
                                userStatus = Order.Status.PAID;
                                break;
                            }
                            case "CANCELLED": {
                                userStatus = Order.Status.CANCELLED;
                                break;
                            }
                            default: {
                                userStatus = Order.Status.PROCESSED;
                            }
                        }
                        selectOrderList.setInt(1, id);
                        resultSelectOrderList = selectOrderList.executeQuery();
                        List<Order.OrderList> orderLists = new ArrayList<>();
                        while (resultSelectOrderList.next()) {
                            int orderListID = resultSelectOrderList.getInt(1);
                            int orderListOrdersid = resultSelectOrderList.getInt(2);
                            int orderListProductsid = resultSelectOrderList.getInt(3);
                            int orderListAmount = resultSelectOrderList.getInt(4);
                            orderLists.add(new Order.OrderList(orderListID, orderListOrdersid, productDAO.getById(orderListProductsid), orderListAmount));
                        }
                        order.add(new Order(id, userID, userStatus, date, orderLists));
                    }
                } catch (SQLException e) {
                    log.error("resultSelectOrders error: " + e);
                } finally {
                    if (resultSelectOrders != null) {
                        resultSelectOrders.close();
                    }
                    if (resultSelectOrderList != null) {
                        resultSelectOrderList.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
            } finally {
                if (selectOrders != null) {
                    try {
                        selectOrders.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
                if (selectOrderList != null) {
                    try {
                        selectOrderList.close();
                    } catch (SQLException e) {
                        log.error("close statement error: " + e);
                    }
                }
            }
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
        return order;
    }

    /*
     * This method change status of order to CANCELLED
     */
    @Override
    public boolean cancelOrder(int userId, int orderId) {
        Connection con = null;
        PreparedStatement updateProducts = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                updateProducts = con.prepareStatement(CANCEL_ORDER);
                updateProducts.setInt(1, orderId);
                updateProducts.setInt(2, userId);
                updateProducts.executeUpdate();
                log.info("Order ID: " + orderId + " will be cancelled");
                return true;
            } catch (SQLException e) {
                log.error("updateProducts error; " + e);
                return false;
            } finally {
                if (updateProducts != null) {
                    try {
                        updateProducts.close();
                    } catch (SQLException e) {
                        log.error("updateProducts error: " + e);
                    }
                }
            }
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }

    /*
     * This method change status of order to PAID
     */
    @Override
    public boolean payOrder(int userId, int orderId) {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(PAY_ORDER);
            statement.setInt(1, orderId);
            statement.setInt(2, userId);
            statement.executeUpdate();
            log.info("Order ID: " + orderId + " will be paided");
            return true;
        } catch (SQLException e) {
            log.error("statement; " + e);
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("statement error: " + e);
                }
            }
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }

    /*
     * This method return list of all orders
     */
    @Override
    public List<Order> getAll() {
        List<Order> order = new ArrayList<>();

        ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
        Connection con = null;
        PreparedStatement selectOrders = null;
        PreparedStatement selectOrderList = null;
        ResultSet resultSelectOrders = null;
        ResultSet resultSelectOrderList = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                selectOrders = con.prepareStatement(SELECT_ORDERS);
                selectOrderList = con.prepareStatement(SELECT_ORDER_LIST_BY_ORDER_ID);
                try {
                    resultSelectOrders = selectOrders.executeQuery();
                    while (resultSelectOrders.next()) {
                        int id = resultSelectOrders.getInt("ID");
                        String userName = resultSelectOrders.getString("clientname");
                        String status = resultSelectOrders.getString("status");
                        String date = resultSelectOrders.getString("date");
                        Order.Status userStatus;
                        switch (status) {
                            case "PROCESSED": {
                                userStatus = Order.Status.PROCESSED;
                                break;
                            }
                            case "PAID": {
                                userStatus = Order.Status.PAID;
                                break;
                            }
                            case "CANCELLED": {
                                userStatus = Order.Status.CANCELLED;
                                break;
                            }
                            default: {
                                userStatus = Order.Status.PROCESSED;
                            }
                        }
                        selectOrderList.setInt(1, id);
                        resultSelectOrderList = selectOrderList.executeQuery();
                        List<Order.OrderList> orderLists = new ArrayList<>();
                        while (resultSelectOrderList.next()) {
                            int resultSelectId = resultSelectOrderList.getInt(1);
                            int resultSelectOrdersid = resultSelectOrderList.getInt(2);
                            int resultSelectProductsid = resultSelectOrderList.getInt(3);
                            int resultSelectAmount = resultSelectOrderList.getInt(4);
                            orderLists.add(new Order.OrderList(resultSelectId, resultSelectOrdersid, productDAO.getById(resultSelectProductsid), resultSelectAmount));
                        }
                        order.add(new Order(id, userName, userStatus, date, orderLists));
                    }
                    return order;
                } catch (SQLException e) {
                    log.error("resultSet error: " + e);
                    return order;
                } finally {
                    if (resultSelectOrders != null) {
                        resultSelectOrders.close();
                    }
                    if (resultSelectOrderList != null) {
                        resultSelectOrderList.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
                return order;
            } finally {
                if (selectOrders != null) {
                    try {
                        selectOrders.close();
                    } catch (SQLException e) {
                        log.error("statement close error: " + e);
                    }
                }
                if (selectOrderList != null) {
                    try {
                        selectOrderList.close();
                    } catch (SQLException e) {
                        log.error("statement close error: " + e);
                    }
                }
            }
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }

    /*
     * This method switch status of order to next. It method using in admin panel.
     */
    @Override
    public boolean changeStatusOrder(int id) {

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(UPDATE_STATUS_ORDER);
            statement.setInt(1, id);
            statement.execute();
            log.info("Order ID: " + id + " will be changed status");
            return true;
        } catch (SQLException e) {
            log.error("execute error: " + e);
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("statement close error: " + e);
                }
            }
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
    }
}
