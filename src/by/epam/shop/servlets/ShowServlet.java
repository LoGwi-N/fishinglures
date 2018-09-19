package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.OrderDAO;
import by.epam.shop.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class send information about of list models
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet("/showModels")
public class ShowServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String model = req.getParameter("model");
        String msg = null;
        switch (model) {
            case "users": {
                UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
                msg = String.valueOf(userDAO.getAll());
                break;
            }
            case "orders": {
                OrderDAO orderDAO = AbstractDAOFactory.dbFactory.getOrderDAO();
                msg = String.valueOf(orderDAO.getAll());
                break;
            }
        }
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(String.valueOf(msg));
    }
}
