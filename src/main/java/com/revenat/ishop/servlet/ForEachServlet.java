package com.revenat.ishop.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/for-each")
public class ForEachServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Object obj = null;
		List<Integer> iterable = Arrays.asList(1, 2, 3);
		Iterator<String> iterator = Arrays.asList("Jack", "Anna", "Bill").iterator();
		long[] array = new long[] {1L, 5L, 10L};
		String tokens = "token1,token2,token3";
		Map<String, Integer> map = buildMap();
		Enumeration<String> enumeration = new Vector<String>(Arrays.asList("Jack", "Anna", "Bill")).elements(); 
		
		req.setAttribute("nullObj", obj);
		req.setAttribute("iterable", iterable);
		req.setAttribute("iterator", iterator);
		req.setAttribute("array", array);
		req.setAttribute("tokens", tokens);
		req.setAttribute("emptyString", "    ");
		req.setAttribute("map", map);
		req.setAttribute("enumeration", enumeration);
		req.setAttribute("wrongType", new Object());
		
		req.getRequestDispatcher("/WEB-INF/jsp/for-each-test.jsp").forward(req, resp);
	}
	
	private Map<String,Integer> buildMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put("KeyA", 10);
		map.put("KeyB", 20);
		map.put("KeyC", 30);
		
		return map;
	}

}
