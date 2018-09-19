package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.OrderDAO;
import by.epam.shop.models.Cart;
import by.epam.shop.models.Order;
import by.epam.shop.models.User;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class do order. It get user of sessian, and make cart
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        OrderDAO orderDAO = AbstractDAOFactory.dbFactory.getOrderDAO();
        User user = (User) session.getAttribute("user");
        List<Order.OrderList> orderLists = new ArrayList<>();
        List<Cart> cartList = (List<Cart>) session.getAttribute("list");
        for (Cart cart : cartList) {
            orderLists.add(new Order.OrderList(cart.getId(), cart.getCount()));
        }
        Order order = new Order(user.getId(), Order.Status.PROCESSED, orderLists);
        String msg = orderDAO.insertOrder(order);
        session.removeAttribute("list");
        log.info("User `" + user.getLogin() + "` made the order");
        response.getWriter().write(msg);
    }

}
