package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = {"/payment"}, loadOnStartup = 3)
public class PaymentController extends HttpServlet {

	Util util = new Util();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
		WebContext context = new WebContext(req, resp, req.getServletContext());
		float totalPrice;

		if (util.getCookieValueBy("userId", req) != null) {
			OrderDao orderDataStore = OrderDaoMem.getInstance();
			Order order = orderDataStore.getActual(Integer.parseInt(Objects.requireNonNull(
					util.getCookieValueBy("userId", req))));
			totalPrice = order.getCart().getLineItemsTotalPrice();
		} else { totalPrice = 0; }

		context.setVariable("totalPrice", totalPrice);
		engine.process("product/payment.html", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
}
