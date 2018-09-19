package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.UserDAO;
import by.epam.shop.models.User;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class edit user information, and update it in Database
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "EditUserServlet", urlPatterns = "/editUser")
public class EditUserServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
        User userSession = (User) session.getAttribute("user");
        req.setCharacterEncoding("utf-8");

        String login = req.getParameter("login");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        if (login.equals(userSession.getLogin()) || userSession.getRole() == User.Role.ADMIN) {
            User user = new User(name, surname, login, email, phone, address);
            String msg = String.valueOf(userDAO.updateUser(user));
            resp.setContentType("charset=UTF-8");
            resp.getWriter().write(msg);
        } else {
            log.info("Попытка изменить данные другого пользователя. Обратить внимание на пользователя: " + userSession.getLogin());
            userDAO.changeStatusUser(userSession.getId(), 2);
            log.info("Его статус переведен в заблокированные");
            resp.sendRedirect("/logout");
        }
    }

}
