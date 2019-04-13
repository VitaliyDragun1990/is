package com.revenat.ishop.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/image/*", initParams = {
		@WebInitParam(name="ROOT_DIR", value="e:\\ishop-layout\\media\\")
})
public class ShowPictureServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String rootDir;
	
	@Override
	public void init() throws ServletException {
		rootDir = getServletConfig().getInitParameter("ROOT_DIR");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/jpg");
		String[] parts = req.getRequestURI().split("/");
		String fileName = parts[parts.length - 1];
		File image = new File(rootDir + fileName);
		if (image.exists()) {
			try (InputStream in = new BufferedInputStream(new FileInputStream(image));
					OutputStream out = new BufferedOutputStream(resp.getOutputStream())) {
				int data = -1;
				while ((data = in.read()) != -1) {
					out.write(data);
				}
				out.flush();
			}
		} else {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
