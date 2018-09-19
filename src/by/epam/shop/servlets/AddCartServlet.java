package by.epam.shop.servlets;

import by.epam.shop.models.Cart;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class responcible for add products to cart list
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "AddCartServlet", urlPatterns = "/addCard")
public class AddCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cart> beanList = new ArrayList<>();
        HttpSession session = request.getSession();
        BufferedReader json = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line = null;
        String str = "";
        while ((line = json.readLine()) != null) {
            str += line;
        }
        Gson gson = new Gson();
        Cart cart = gson.fromJson(str, Cart.class);

        if (session.getAttribute("list") == null) {
            beanList.add(cart);
            session.setAttribute("list", beanList);
        } else {
            beanList = (List<Cart>) session.getAttribute("list");
            Boolean same = false;
            for (Cart ca : beanList) {
                if (ca.getId() == cart.getId()) {
                    same = true;
                    ca.setCount(cart.getCount());
                }
            }
            if (!same) {
                beanList.add(cart);
            }
            session.setAttribute("list", beanList);
        }
        response.getWriter().write("true");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "GET requests are not supported");
    }
}
