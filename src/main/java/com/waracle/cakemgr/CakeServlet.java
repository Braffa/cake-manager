package com.waracle.cakemgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "CakeServlet", urlPatterns = { "", "/cakes" })

public class CakeServlet extends HttpServlet {

	@Override
	public void init() throws ServletException {
		super.init();
		System.out.println("init method of Cakes Servlet");
		System.out.println("init started");
		System.out.println("downloading cake json");
		try (InputStream inputStream = new URL(
				"https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json")
						.openStream()) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			StringBuffer buffer = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				line = reader.readLine();
			}

			System.out.println("parsing cake json");
			ObjectMapper mapper = new ObjectMapper();
			List<CakeEntity> list = mapper.readValue(buffer.toString(), new TypeReference<List<CakeEntity>>() {
			});
			Session session = HibernateUtil.getSessionFactory().openSession();
			for (CakeEntity cakeEntity : list) {
				try {
					session.beginTransaction();
					session.persist(cakeEntity);
					session.getTransaction().commit();
				} catch (ConstraintViolationException cve) {
					System.out.println("Cake already in database " + cakeEntity.toString());
					session.getTransaction().rollback();
				}
			}
			session.close();

		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		System.out.println("init finished");
	}

	private boolean validBrowser(String agent) {
		if (agent.contains("Mozilla")) {
			return true;
		}
		if (agent.contains("MSIE")) {
			return true;
		}
		if (agent.contains("IE")) {
			return true;
		}
		if (agent.contains("Safari")) {
			return true;
		}
		return false;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doGet method of Cakes Servlet");
		String url = req.getRequestURL() + "?" + req.getQueryString();
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<CakeEntity> list = session.createCriteria(CakeEntity.class).list();
		session.close();
		if (url.contains("cakes") && !validBrowser(req.getHeader("User-Agent"))) {
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(list);
			resp.getWriter().println(jsonData);
		} else {
			req.setAttribute("cakes", list);
			req.getRequestDispatcher("/WEB-INF/cakes.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("Post method of Cakes Servlet");
		ArrayList<String> messages = new ArrayList<String>();
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String imageURL = req.getParameter("imageurl");
		if (title.equals(null) || title.length() == 0) {
			messages.add("Please enter a title");
		}
		if (description.equals(null) || description.length() == 0) {
			messages.add("Please enter a description");
		}
		if (imageURL.equals(null) || imageURL.length() == 0) {
			messages.add("Please enter an image URL");
		}
		if (messages.size() == 0) {
			CakeEntity cakeEntity = new CakeEntity();
			cakeEntity.setTitle(title);
			cakeEntity.setDesc(description);
			cakeEntity.setImage(imageURL);
			addCake (req, cakeEntity);
		} else {
			req.setAttribute("messages", messages);
		}
		doGet(req, resp);
	}
	private void addCake (HttpServletRequest req, CakeEntity cakeEntity) {
		System.out.println("addCake method of Cakes Servlet");
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			session.beginTransaction();
			session.persist(cakeEntity);
			session.getTransaction().commit();
		} catch (ConstraintViolationException cve) {
			System.out.println("Cake already in database " + cakeEntity.toString());
			req.setAttribute("duplicateCake", "Cake already in database");
			session.getTransaction().rollback();
		}
		session.close();
	}
}
