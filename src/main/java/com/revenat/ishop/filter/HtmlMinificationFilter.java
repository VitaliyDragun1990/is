package com.revenat.ishop.filter;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This filter responsible for minifying response body in case of type
 * 'text/html' by removing all \n \r \t symbols from it.
 * 
 * @author Vitaly Dragun
 *
 */
public class HtmlMinificationFilter extends AbstractFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HtmlMinificationResponseWrapper responseWrapper = new HtmlMinificationResponseWrapper(response);

		chain.doFilter(request, responseWrapper);

		responseWrapper.processResponse();
	}

	/**
	 * Simple implementation of the {@link HttpServletResponseWrapper} aimed to
	 * store input into separate buffers to allow to retrieve it later and provide
	 * and provide minification process on it in case of HTML content type.
	 * 
	 * @author Vitaly Dragun
	 *
	 */
	static class HtmlMinificationResponseWrapper extends HttpServletResponseWrapper {
		private static final String FIND_ALL_SPACES_BETWEEN_TAGS = "(?<=\\s)\\s+(?![^<>]*</pre>)";
		private static final String FIND_ALL_NEW_LINES_BETWEEN_TAGS = "\\r?\\n(?![^<]*</pre>)";

		private Pattern regex;
		private CharArrayWriter charBuffer;
		private ByteArrayOutputStream byteBuffer;
		private PrintWriter printWriter;

		public HtmlMinificationResponseWrapper(HttpServletResponse response) {
			super(response);
			initPattern();
		}

		private void initPattern() {
			StringBuilder pattern = new StringBuilder();
			pattern.append(FIND_ALL_SPACES_BETWEEN_TAGS).append('|').append(FIND_ALL_NEW_LINES_BETWEEN_TAGS);
			regex = Pattern.compile(pattern.toString(), Pattern.MULTILINE);
		}

		private String contentAsString() throws UnsupportedEncodingException {
			if (byteBuffer != null) {
				return byteBuffer.toString(getResponse().getCharacterEncoding());
			} else if (charBuffer != null) {
				return charBuffer.toString();
			} else {
				return "";
			}
		}

		/**
		 * Processes response body content in such was that if content type is HTML,
		 * than such HTML will be minified and written to underlying
		 * {@link ServletResponse} object. If content type is not the HTML, then such
		 * content will be written to underlying response as is without any modification.
		 * 
		 * @throws IOException
		 */
		public void processResponse() throws IOException {
			ServletResponse wrappedResponse = getResponse();

			if (isHtmlContentType(wrappedResponse.getContentType())) {
				writeMinifiedHtml(wrappedResponse);
			} else {
				writeOriginalContent(wrappedResponse);
			}
		}

		private void writeMinifiedHtml(ServletResponse wrappedResponse) throws IOException {
			String html = contentAsString();
			html = regex.matcher(html).replaceAll("");
			int contentLength = html.getBytes("UTF-8").length;
			wrappedResponse.setContentLength(contentLength);
			PrintWriter out = wrappedResponse.getWriter();
			out.write(html);
			out.close();
		}

		private void writeOriginalContent(ServletResponse wrappedResponse) throws IOException {
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

		private boolean isHtmlContentType(String contentType) {
			return contentType != null && contentType.startsWith("text/html");
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
