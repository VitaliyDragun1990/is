package com.revenat.ishop.application.service.impl;

import com.revenat.ishop.application.dto.ProductDTO;
import com.revenat.ishop.application.model.ShoppingCart;
import com.revenat.ishop.application.service.ShoppingCartService;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.infrastructure.exception.ResourceNotFoundException;
import com.revenat.ishop.infrastructure.framework.annotation.di.Autowired;
import com.revenat.ishop.infrastructure.framework.annotation.di.Component;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.service.Transactional;
import com.revenat.ishop.infrastructure.repository.ProductRepository;
import com.revenat.ishop.infrastructure.transform.transformer.Transformer;
import com.revenat.ishop.infrastructure.util.Checks;

@Component
public class ShoppingCartServiceImpl implements ShoppingCartService {
	private static final String VALID_PRODUCT_QUANTITY_MSG_CODE = "message.error.validProductQuantity";
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private Transformer transformer;

	public ShoppingCartServiceImpl() {
	}

	public ShoppingCartServiceImpl(ProductRepository productRepository, Transformer transformer) {
		this.productRepository = productRepository;
		this.transformer = transformer;
	}

	@Transactional(readOnly = true)
	@Override
	public void addProductToShoppingCart(int productId, int quantity, ShoppingCart shoppingCart) {
		validateProductQuantity(quantity);
		Product product = productRepository.findById(productId);
		if (product == null) {
			throw new ResourceNotFoundException(
					"Error during adding product to shopping cart: " + "product with id: " + productId + " not found.");
		}
		shoppingCart.addProduct(transformer.transform(product, ProductDTO.class), quantity);
	}

	@Override
	public void removeProductFromShoppingCart(int productId, int quantity, ShoppingCart shoppingCart) {
		validateProductQuantity(quantity);
		shoppingCart.removeProduct(productId, quantity);
	}

	private static void validateProductQuantity(int quantity) {
		Checks.checkParam(quantity > 0 && quantity <= ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART,
				"valid product quantity should be between 1 and %d inclusive.",
				VALID_PRODUCT_QUANTITY_MSG_CODE,
				ShoppingCart.MAX_INSTANCES_OF_ONE_PRODUCT_PER_SHOPPING_CART);
	}
}
