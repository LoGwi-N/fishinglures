package by.epam.shop.servlets;

import by.epam.shop.models.Cart;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * This class responcible for cart list
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "CartServlet", urlPatterns = "/cart")
public class CartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Cart> cartList;
        HttpSession session = request.getSession();
        cartList = (List<Cart>) session.getAttribute("list");

        Gson gson = new Gson();
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (cartList != null) {
            String str = gson.toJson(cartList);
            response.getWriter().write(str);
        } else
            response.getWriter().write("null");
    }

}
