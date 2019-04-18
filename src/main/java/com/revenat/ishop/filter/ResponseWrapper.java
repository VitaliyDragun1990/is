package com.revenat.ishop.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {
	private ByteArrayOutputStream output;
	private PrintWriter printWriter;
	private ServletOutputStream customServletOutput;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public void flushBuffer() throws IOException {

		if (output != null) {
			output.flush();
		}

		IOException exc = null;
		try {
			super.flushBuffer();
		} catch (IOException e) {
			exc = e;
		}

		if (exc != null) {
			throw exc;
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (printWriter != null) {
			throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
		}
		if (output == null) {
			output = new ByteArrayOutputStream();
			customServletOutput = new CustomServletOutputStream(output);
		}
		return customServletOutput;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (printWriter == null && output != null) {
			throw new IllegalStateException("OutputStream obtained already - can not get PrintWriter");
		}
		if (printWriter == null) {
			output = new ByteArrayOutputStream();
			printWriter = new PrintWriter(new OutputStreamWriter(output, getResponse().getCharacterEncoding()), true);
		}
		return printWriter;
	}

	@Override
	public void setContentLength(int len) {
		// ignore, since content of minified
		// HTML content does not match content
		// length of normal content.
	}
	
	public String getContentAsString() {
		String content = "";
		
		if (output != null && output.size() != 0) {
			try {
				content = output.toString(getResponse().getCharacterEncoding());
			} catch (UnsupportedEncodingException e) {
				// do nothing
			}
		}
		
		return content;
	}
	
	public void flushContentToUnderlineResponse() throws IOException {
		ServletOutputStream responseOutput = getResponse().getOutputStream();
		responseOutput.write(output.toByteArray());
		getResponse().setContentLength(output.size());
		responseOutput.close();
	}

	private static class CustomServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream output;

		public CustomServletOutputStream(ByteArrayOutputStream output) {
			this.output = output;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
		}

		@Override
		public void close() throws IOException {
			output.close();
		}

		@Override
		public void flush() throws IOException {
			output.flush();
		}

		@Override
		public void write(byte[] b) throws IOException {
			output.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			output.write(b, off, len);
		}

		@Override
		public void write(int b) throws IOException {
			output.write(b);
		}
	}
}
