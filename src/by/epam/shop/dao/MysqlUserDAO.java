package by.epam.shop.dao;

import by.epam.shop.models.User;
import by.epam.shop.service.ConfigManager;
import by.epam.shop.service.ConnectionManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static by.epam.shop.models.User.Role.ADMIN;
import static by.epam.shop.models.User.Role.USER;

/**
 * This  class responsible for Users using MySQL
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

public class MysqlUserDAO implements UserDAO {

    final Logger log = Logger.getLogger(this.getClass());

    private final String SELECT_COMMAND = ConfigManager.getProperty(ConfigManager.SQL_SELECT_ALL_USER);
    private final String INSERT_COMMAND = ConfigManager.getProperty(ConfigManager.SQL_INSERT_USER);
    private final String SELECT = ConfigManager.getProperty(ConfigManager.SQL_LOGIN);
    private final String UPDATE_USER = ConfigManager.getProperty(ConfigManager.SQL_UPDATE_USER);
    private final String UPDATE_STATUS_USER = ConfigManager.getProperty(ConfigManager.SQL_UPDATE_STATUS_USER);

    public MysqlUserDAO() {
    }

    @Override
    public String insertUser(User user) {


        UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
        List<User> userList = userDAO.getAll();

        for (User u : userList) {
            if (user.getLogin().equals(u.getLogin())) {
                return "login";
            } else if (user.getEmail().equals(u.getEmail())) {
                return "email";
            }
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                statement = con.prepareStatement(INSERT_COMMAND);
                try {
                    statement.setString(1, user.getName());
                    statement.setString(2, user.getSurname());
                    statement.setString(3, user.getLogin());
                    statement.setString(4, user.getEmail());
                    statement.setString(5, user.getPhone());
                    statement.setString(6, user.getPassword());
                    statement.setString(7, User.getSalt(user.getLogin()));
                    statement.setString(8, user.getAddress());
                    statement.setString(9, timeStamp);
                    statement.executeUpdate();
                    return "success";
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
                return "error";
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
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
    }

    @Override
    public boolean updateUser(User user) {
        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            con.setAutoCommit(false);
            statement = con.prepareStatement(UPDATE_USER);
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getLogin());
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
    public List<User> getAll() {
        List<User> user = new LinkedList<>();
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
                        int id = resultSet.getInt("ID");
                        String name = resultSet.getString("name");
                        String surname = resultSet.getString("surname");
                        String login = resultSet.getString("login");
                        String email = resultSet.getString("email");
                        String phone = resultSet.getString("phone");
                        String roles = resultSet.getString("role");
                        String address = resultSet.getString("address");
                        int status = resultSet.getInt("status");
                        String registered = resultSet.getString("registered");
                        User.Role role;
                        switch (roles) {
                            case "admin": {
                                role = User.Role.ADMIN;
                                break;
                            }
                            case "user": {
                                role = User.Role.USER;
                                break;
                            }
                            default: {
                                role = User.Role.USER;
                                break;
                            }
                        }
                        user.add(new User(id, name, surname, login, email, phone, null, role, address, status, registered));
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
        } finally {
            if (con != null) {
                ConnectionManager.getInstance().releaseConnection(con);
            }
        }
        return user;
    }

    @Override
    public User checkUser(String login, String password) {
        User user = new User();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = ConnectionManager.getInstance().getConnection();
            try {
                statement = con.prepareStatement(SELECT);
                try {
                    statement.setString(1, login);
                    statement.setString(2, User.getSalt(login));
                    statement.setString(3, password);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        user.setLogin(login);
                        user.setPassword(password);
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setSurname(resultSet.getString("surname"));
                        user.setAddress(resultSet.getString("address"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPhone(resultSet.getString("phone"));
                        user.setStatus(resultSet.getInt("status"));
                        user.setRegistered(resultSet.getString("registered"));
                        String role = resultSet.getString("role");
                        switch (role) {
                            case "admin": {
                                user.setRole(ADMIN);
                                break;
                            }
                            case "user": {
                                user.setRole(USER);
                                break;
                            }
                            default: {
                                user.setRole(USER);
                                break;
                            }
                        }
                        return user;
                    } else return new User();
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            } catch (SQLException e) {
                log.error("statement error: " + e);
                return new User();
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
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
    }

    @Override
    public boolean changeStatusUser(int id, int status) {

        Connection con = ConnectionManager.getInstance().getConnection();
        PreparedStatement statement = null;
        try {
            con.setAutoCommit(false);
            statement = con.prepareStatement(UPDATE_STATUS_USER);
            statement.setInt(1, status);
            statement.setInt(2, id);
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
}
