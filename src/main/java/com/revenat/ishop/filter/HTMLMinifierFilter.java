package com.revenat.ishop.filter;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This filter responsible for minifying response body in case of type
 * 'text/html' efectivelly removing all \n \r \t symbols from it.
 * 
 * @author Vitaly Dragun
 *
 */
public class HTMLMinifierFilter extends AbstractFilter {
	private static final String FIND_ALL_SPACES_BETWEEN_TAGS = "(?<=\\s)\\s+(?![^<>]*</pre>)";
	private static final String FIND_ALL_NEW_LINES_BETWEEN_TAGS = "\\r?\\n(?![^<]*</pre>)";

	private Pattern regex;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		StringBuilder pattern = new StringBuilder();
		pattern.append(FIND_ALL_SPACES_BETWEEN_TAGS).append('|').append(FIND_ALL_NEW_LINES_BETWEEN_TAGS);
		regex = Pattern.compile(pattern.toString(), Pattern.MULTILINE);
	}

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		SimpleResponseWrapper responseWrapper = new SimpleResponseWrapper(response);

		chain.doFilter(request, responseWrapper);
		if (isHtmlContentType(response.getContentType())) {
			minifyHtmlContent(response, responseWrapper);
		} else {
			writeOriginalContent(responseWrapper);
		}
	}
	
	private boolean isHtmlContentType(String contentType) {
		return contentType != null && contentType.startsWith("text/html");
	}

	private void minifyHtmlContent(HttpServletResponse response, SimpleResponseWrapper responseWrapper)
			throws IOException {
		String html = responseWrapper.contentAsString();
		html = regex.matcher(html).replaceAll("");
		int contentLength = html.getBytes("UTF-8").length;
		response.setContentLength(contentLength);
		PrintWriter out = response.getWriter();
		out.write(html);
		out.close();
	}
	
	private void writeOriginalContent(SimpleResponseWrapper responseWrapper) throws IOException {
		responseWrapper.flushContentToWrappedResponse();
	}

	/**
	 * Simple implementation of the {@link HttpServletResponseWrapper} aimed to
	 * store input into separate buffers to allow
	 * to retrieve it and provide some sort of processing on it content.
	 * 
	 * @author Vitaly Dragun
	 *
	 */
	static class SimpleResponseWrapper extends HttpServletResponseWrapper {
		private CharArrayWriter charBuffer;
		private ByteArrayOutputStream byteBuffer;
		private PrintWriter printWriter;

		public SimpleResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		public String contentAsString() throws UnsupportedEncodingException {
			if (byteBuffer != null) {
				return byteBuffer.toString(getResponse().getCharacterEncoding());
			} else if (charBuffer != null) {
				return charBuffer.toString();
			} else {
				return "";
			}
		}
		
		public void flushContentToWrappedResponse() throws IOException {
			ServletResponse wrappedResponse = getResponse();
			if (byteBuffer != null) {
				wrappedResponse.getOutputStream().write(byteBuffer.toByteArray());
				wrappedResponse.setContentLength(byteBuffer.size());
				wrappedResponse.getOutputStream().close();
			} else if (charBuffer != null) {
				wrappedResponse.getWriter().write(charBuffer.toCharArray());
				wrappedResponse.setContentLength(charBuffer.size());
				wrappedResponse.getWriter().close();
			} else {
				wrappedResponse.setContentLength(0);
			}
		}

		@Override
		public PrintWriter getWriter() throws UnsupportedEncodingException {
			if (printWriter == null && byteBuffer != null) {
				throw new IllegalStateException("OutputStream obtained already - can not get PrintWriter");
			}
			if (printWriter == null) {
				charBuffer = new CharArrayWriter();
				printWriter = new PrintWriter(charBuffer);
			}
			return printWriter;
		}

		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (printWriter != null) {
				throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
			}
			if (byteBuffer == null) {
				byteBuffer = new ByteArrayOutputStream();
			}
			return new ServletOutputStream() {

				@Override
				public void write(int b) throws IOException {
					byteBuffer.write(b);
				}

				@Override
				public void setWriteListener(WriteListener writeListener) {
					// this implementation doesn't support addition of write listeners
				}

				@Override
				public boolean isReady() {
					return false;
				}
			};
		}
	}

}
