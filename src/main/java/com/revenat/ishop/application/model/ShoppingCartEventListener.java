package com.revenat.ishop.application.model;

import java.io.Serializable;

import com.revenat.ishop.application.dto.ProductDTO;

public interface ShoppingCartEventListener extends Serializable {

	void productWasAdded(ProductDTO product, int quantity);
	
	void productWasRemoved(ProductDTO product, int quantity);
	
	void cartWasCleared();
}
