package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment"}, loadOnStartup = 4)
public class PaymentController extends HttpServlet {
	Util util = new Util();
	OrderDao orderDataStore = OrderDaoMem.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
		WebContext context = new WebContext(req, resp, req.getServletContext());

		if (isNoExistingOrder(req)) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			engine.process("product/error.html", context, resp.getWriter());
			return;
		}

		Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", req)));
		float totalPrice = order.getCart().getLineItemsTotalPrice();
		int itemsNumber = order.getCart().getCartSize();

		context.setVariable("itemsNumber", itemsNumber);
		context.setVariable("totalPrice", totalPrice);
		engine.process("product/payment.html", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//TODO confirm payment
		saveOrderToFile(req, getServletContext());
		resp.sendRedirect("/paymentConfirmation");
	}

	private void saveOrderToFile(HttpServletRequest request, ServletContext context) throws IOException {
		Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", request)));
		String relativeDirectoryPath = "/orders";
		String filename = "order" + order.getId();
		File file = util.prepareFile(relativeDirectoryPath, filename, context);
		util.saveObjectToFile(order, file);
	}

	private boolean isNoExistingOrder(HttpServletRequest request) {
		return util.getCookieValueBy("userId", request) == null;
	}
}
