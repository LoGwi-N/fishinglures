package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.OrderDAO;
import by.epam.shop.models.Order;
import by.epam.shop.models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * This class send information about orders of user
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "ShowOrdersByIdServlet", urlPatterns = "/showUserOrders")
public class ShowOrdersByIdServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int userID = user.getId();

        OrderDAO orderDAO = AbstractDAOFactory.dbFactory.getOrderDAO();
        List<Order> orderList = orderDAO.getAllByUser(userID);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(String.valueOf(orderList));
    }
}
