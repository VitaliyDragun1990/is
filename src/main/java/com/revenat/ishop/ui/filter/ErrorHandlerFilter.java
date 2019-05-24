package com.revenat.ishop.ui.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.json.JSONObject;

import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.exception.base.ApplicationException;
import com.revenat.ishop.infrastructure.exception.flow.FlowException;
import com.revenat.ishop.infrastructure.exception.flow.InvalidParameterException;
import com.revenat.ishop.infrastructure.exception.security.AccessDeniedException;
import com.revenat.ishop.ui.config.Constants.Attribute;
import com.revenat.ishop.ui.config.Constants.Page;
import com.revenat.ishop.ui.util.RoutingUtils;
import com.revenat.ishop.ui.util.UrlUtils;

/**
 * This filter represents top-level error handler, responsible for intercepting
 * all exceptions that may happen in the application and appropriately handle
 * them.
 * 
 * @author Vitaly Dragun
 *
 */
public class ErrorHandlerFilter extends AbstractFilter {
	private static final String INTERNAL_ERROR = "Internal error";

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, new ExceptionThrowerHttpServletResponse(response));
		} catch (Exception e) {
			processException(e, request, response);
			sendResponse(request, response, e);
		}
	}

	private void processException(Exception e, HttpServletRequest req, HttpServletResponse resp) {
		String requestUri = req.getRequestURI();
		int statusCode = getStausCode(e);
		if (statusCode != HttpServletResponse.SC_BAD_REQUEST) {
			LOGGER.error("Request {} failed: {}", requestUri, e.getMessage(), e);
		} else {
			LOGGER.warn("Bad request {}: {}", requestUri, e.getMessage(), e);
		}
		req.setAttribute(Attribute.STATUS_CODE, statusCode);
		resp.setStatus(statusCode);
	}

	private void sendResponse(HttpServletRequest req, HttpServletResponse resp, Exception e)
			throws IOException, ServletException {
		String requestUri = req.getRequestURI();
		if (UrlUtils.isAjaxJsonUrl(requestUri)) {
			String json = getJsonResponse(e);
			RoutingUtils.sendJSON(json, resp);
		} else if (UrlUtils.isAjaxHtmlUrl(requestUri)) {
			RoutingUtils.sendHtmlFragment(INTERNAL_ERROR, resp);
		} else {
			RoutingUtils.forwardToPage(Page.ERROR, req, resp);
		}
	}

	private int getStausCode(Exception e) {
		if (e instanceof ApplicationException) {
			return ((ApplicationException) e).getCode();
		} else {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	private String getJsonResponse(Exception e) {
		JSONObject json = new JSONObject();
		json.put("message", getMessage(getStausCode(e), e.getMessage()));
		return json.toString();
	}

	private String getMessage(int statusCode, String badRequestMessage) {
		switch (statusCode) {
		case 400:
			return badRequestMessage;
		case 401:
			return "You must be authorized to view this resource";
		case 403:
			return "You don't have permissions to view this resource";
		case 404:
			return "Resource not found";
		default:
			return INTERNAL_ERROR;
		}
	}

	/**
	 * This custom {@link HttpServletResponseWrapper} implementation throws
	 * exception in case of invalid request (method not implemented, page not exist,
	 * etc) instead of default behaviour (returning container-specific response
	 * page). This custom wrapper is needed in order to let our
	 * {@link ErrorHandlerFilter} filter handle all kind of exceptions application
	 * can thorw.
	 * 
	 * @author Vitaly Dragun
	 *
	 */
	private static class ExceptionThrowerHttpServletResponse extends HttpServletResponseWrapper {

		public ExceptionThrowerHttpServletResponse(HttpServletResponse response) {
			super(response);
		}

		@Override
		public void sendError(int sc) throws IOException {
			sendError(sc, INTERNAL_ERROR);
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			switch (sc) {
			case 400:
				throw new InvalidParameterException(msg);
			case 403:
				throw new AccessDeniedException(msg);
			case 404:
				throw new ResourceNotFoundException(msg);
			default:
				throw new FlowException(msg, sc);
			}
		}
	}
}
