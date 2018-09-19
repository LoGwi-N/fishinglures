package by.epam.shop.servlets;

import by.epam.shop.models.Cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * This class delete product of cart list
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "DelProductOfCartServlet", urlPatterns = "/delProductOfCartServlet")
public class DelProductOfCartServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        BufferedReader json = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String line;
        String str = "";
        while ((line = json.readLine()) != null) {
            str += line;
        }

        int id = Integer.parseInt(str);
        List<Cart> cartList;
        Cart forDel = null;
        cartList = (List<Cart>) session.getAttribute("list");
        for (Cart cart : cartList) {
            if (cart.getId() == id) {
                forDel = cart;
            }
        }
        cartList.remove(forDel);
        session.setAttribute("list", cartList);

        response.getWriter().write("true");
    }


}
