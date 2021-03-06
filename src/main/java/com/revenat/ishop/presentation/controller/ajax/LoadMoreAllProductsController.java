package com.revenat.ishop.presentation.controller.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.service.ProductService;
import com.revenat.ishop.presentation.config.Constants;
import com.revenat.ishop.presentation.config.Constants.Attribute;
import com.revenat.ishop.presentation.config.Constants.Fragment;
import com.revenat.ishop.presentation.controller.AbstractProductController;
import com.revenat.ishop.presentation.util.RoutingUtils;

public class LoadMoreAllProductsController extends AbstractProductController {
	private static final long serialVersionUID = 1L;
	
	public LoadMoreAllProductsController(ProductService productService) {
		super(productService);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int requestedPage = getPage(req);
		List<ProductDTO> page = productService.findProducts(requestedPage, Constants.MAX_PRODUCTS_PER_HTML_PAGE);

		req.setAttribute(Attribute.PRODUCTS, page);

		RoutingUtils.forwardToFragment(Fragment.PRODUCT_LIST, req, resp);
	}
}
