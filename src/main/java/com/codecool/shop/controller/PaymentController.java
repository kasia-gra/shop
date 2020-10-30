package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.order.Payment;
import com.google.gson.Gson;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Objects;

@WebServlet(urlPatterns = {"/payment"}, loadOnStartup = 4)
public class PaymentController extends HttpServlet {

	Util util = new Util();
	Gson gson = new Gson();

	private String host;
	private String port;
	private String user;
	private String pass;

	public void init() {
		// reads SMTP server setting from web.xml file
		ServletContext context = getServletContext();
		host = context.getInitParameter("host");
		port = context.getInitParameter("port");
		user = context.getInitParameter("user");
		pass = context.getInitParameter("pass");
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
		WebContext context = new WebContext(req, resp, req.getServletContext());

		if (util.getCookieValueBy("userId", req) == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			engine.process("product/error.html", context, resp.getWriter());
			return;
		}

		OrderDao orderDataStore = OrderDaoMem.getInstance();
		Order order = orderDataStore.getActual(Integer.parseInt(Objects.requireNonNull(
				util.getCookieValueBy("userId", req))));

		float totalPrice = order.getCart().getLineItemsTotalPrice();
		int itemsNumber = order.getCart().getCartSize();
		context.setVariable("itemsNumber", itemsNumber);
		context.setVariable("totalPrice", totalPrice);
		engine.process("product/payment.html", context, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//TODO confirm payment
		OrderDao orderDataStore = OrderDaoMem.getInstance();
		Order order = orderDataStore.getActual(Integer.parseInt(Objects.requireNonNull(util.getCookieValueBy("userId", req))));
		saveOrderToFile(order);
		setPaymentParameters(order, req);
		resp.sendRedirect("/paymentConfirmation");
	}

	private void saveOrderToFile(Order order) throws IOException {
		String jsonOrder = gson.toJson(order);
		String relativeWebPath = "/orders";
		String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
		String filename = "order" + order.getId() + ".json";
		File file = new File(absoluteDiskPath, filename);
		saveFileToDisk(jsonOrder, file);
	}

	private void saveFileToDisk(String jsonOrder, File file) throws IOException {
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(jsonOrder);
		fileWriter.flush();
	}

	private void setPaymentParameters(Order order, HttpServletRequest req) {
		Payment payment = new Payment(req.getParameter("userName"));
		order.setPayment(payment);
	}
}
