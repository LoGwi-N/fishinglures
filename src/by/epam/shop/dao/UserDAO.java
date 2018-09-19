package by.epam.shop.dao;

import by.epam.shop.models.User;
import java.util.List;

public interface UserDAO {

    String insertUser(User user);
    boolean updateUser(User user);
    boolean changeStatusUser(int id, int status);
    List<User> getAll();
    User checkUser(String login, String password);

}
