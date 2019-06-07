package com.revenat.ishop.presentation.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.json.JSONObject;

import com.revenat.ishop.application.exception.ResourceNotFoundException;
import com.revenat.ishop.application.exception.security.AccessDeniedException;
import com.revenat.ishop.application.exception.security.AuthenticationException;
import com.revenat.ishop.application.service.I18nService;
import com.revenat.ishop.domain.exception.FlowException;
import com.revenat.ishop.domain.exception.flow.InvalidParameterException;
import com.revenat.ishop.domain.exception.flow.ValidationException;
import com.revenat.ishop.presentation.config.Constants.Attribute;
import com.revenat.ishop.presentation.config.Constants.Page;
import com.revenat.ishop.presentation.util.RoutingUtils;
import com.revenat.ishop.presentation.util.UrlUtils;

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
	
	private final I18nService i18nService;
	
	public ErrorHandlerFilter(I18nService i18nService) {
		this.i18nService = i18nService;
	}

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
		logException(e, requestUri, statusCode);
		
		if (statusCode == HttpServletResponse.SC_BAD_REQUEST || statusCode == HttpServletResponse.SC_NOT_FOUND) {
			req.setAttribute(Attribute.ERROR_MESSAGE, requestUri);
		}
		
		req.setAttribute(Attribute.STATUS_CODE, statusCode);
		resp.setStatus(statusCode);
	}

	private void logException(Exception e, String requestUri, int statusCode) {
		if (statusCode != HttpServletResponse.SC_BAD_REQUEST) {
			LOGGER.error("Request {} failed: {}", requestUri, e.getMessage(), e);
		} else {
			LOGGER.warn("Bad request {}: {}", requestUri, e.getMessage(), e);
		}
	}

	private void sendResponse(HttpServletRequest req, HttpServletResponse resp, Exception e)
			throws IOException, ServletException {
		String requestUri = req.getRequestURI();
		if (UrlUtils.isAjaxJsonUrl(requestUri)) {
			String json = getJsonResponse(e, req);
			RoutingUtils.sendJSON(json, resp);
		} else if (UrlUtils.isAjaxHtmlUrl(requestUri)) {
			String msg = i18nService.getMessage("message.error.500", req.getLocale());
			RoutingUtils.sendHtmlFragment(msg, resp);
		} else {
			RoutingUtils.forwardToPage(Page.ERROR, req, resp);
		}
	}

	private int getStausCode(Exception e) {
		if (e instanceof InvalidParameterException || e instanceof ValidationException) {
			return HttpServletResponse.SC_BAD_REQUEST;
		} else if (e instanceof AccessDeniedException) {
			return HttpServletResponse.SC_FORBIDDEN;
		} else if (e instanceof AuthenticationException) {
			return HttpServletResponse.SC_UNAUTHORIZED;
		} else if (e instanceof ResourceNotFoundException) {
			return HttpServletResponse.SC_NOT_FOUND;
		}
		else {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
		}
	}

	private String getJsonResponse(Exception e, HttpServletRequest req) {
		JSONObject json = new JSONObject();
		json.put("message", getMessage(req, e));
		return json.toString();
	}

	private String getMessage(HttpServletRequest req, Exception e) {
		int statusCode = getStausCode(e);
		switch (statusCode) {
		case 400:
			FlowException exc = (FlowException) e;
			return i18nService.getMessage(exc.getMessageCode(), req.getLocale(), exc.getArgs());
		case 401:
			return i18nService.getMessage("message.error.401", req.getLocale());
		case 403:
			return i18nService.getMessage("message.error.403", req.getLocale());
		case 404:
			return i18nService.getMessage("message.error.404", req.getLocale(), req.getRequestURI());
		default:
			return i18nService.getMessage("message.error.500", req.getLocale());
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
				throw new InvalidParameterException(msg, "message.error.400", msg);
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
