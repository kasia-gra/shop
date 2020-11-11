package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.mem.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.order.Payment;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(urlPatterns = {"/payment"}, loadOnStartup = 4)
public class PaymentController extends HttpServlet {
	private final Util util = new Util();
	private final OrderDao orderDataStore = OrderDaoMem.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
		WebContext context = new WebContext(req, resp, req.getServletContext());

		if (!util.isExistingOrder(req)) {
			util.showErrorPage(resp, engine, context);
			return;
		}

		showPaymentPage(req, resp, engine, context);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//TODO confirm payment
		Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
		saveOrderToFile(order, getServletContext());
		setPaymentParameters(order, req);
		resp.sendRedirect("/paymentConfirmation");
	}

	private void setContextParameters(HttpServletRequest req, WebContext context) {
		Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
		float totalPrice = order.getCart().getLineItemsTotalPrice();
		int itemsNumber = order.getCart().getCartSize();

		context.setVariable("itemsNumber", itemsNumber);
		context.setVariable("totalPrice", totalPrice);
	}

	private void showPaymentPage(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context) throws IOException {
		setContextParameters(req, context);
		engine.process("product/payment.html", context, resp.getWriter());
	}

	private void saveOrderToFile(Order order, ServletContext context) throws IOException {
		String relativeDirectoryPath = "/orders";
		String filename = "order" + order.getId();
		File file = util.prepareFile(relativeDirectoryPath, filename, context);
		util.saveObjectToFile(order, file);
	}
	private void setPaymentParameters(Order order, HttpServletRequest req) {
		Payment payment = new Payment(req.getParameter("userName"));
		order.setPayment(payment);
	}
}
