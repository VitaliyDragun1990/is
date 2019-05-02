$(function() {

    /**
     * This function responsible for initial setup, e.g. add appropriate event handlers
     * to different components, initiating their initial statte and so on.
     */
    var init = function() {
        initBuyBtn();
        $('#addToCart').on('click', addProductToCart);
        $('#addProductPopup .quantity').on('change', calculateItemTotal);
        $('#loadMore').on('click', loadMoreProducts);
        initSearchForm();
        $('#goSearch').on('click', goSearch);
        $('.remove-product').on('click', removeProductFromCart);
    };

    /**
     * This function responsible for handling 'Buy' button on product panel by
     * populating 'Add product to shopping cart' popup with product-specific data
     * and showing this popup to the user
     */
    var showAddProductPopup = function() {
        var productId = $(this).attr('data-id-product');
        var product = $('#product' + productId);
        var price = product.find('.price').text();

        // Populate popup fields
        $('#addProductPopup').attr('data-id-product', productId);
        $('#addProductPopup .product-image').attr('src', product.find('.thumbnail img').attr('src'));
        $('#addProductPopup .name').text(product.find('.name').text());
        $('#addProductPopup .price').text(price);
        $('#addProductPopup .category').text(product.find('.category').text());
        $('#addProductPopup .producer').text(product.find('.producer').text());
        $('#addProductPopup .quantity').val(1);
        $('#addProductPopup .total-price').text(price);

        // Display popup
        $('#addToCartIndicator').addClass('hidden');
        $('#addToCart').removeClass('hidden');
        $('#addProductPopup').modal({
            show: true
        });

    };

    /**
     * This function responsible for binding showing 'add product to shopping cart' modal
     * when user click on 'Buy' button
     */
    var initBuyBtn = function() {
        $('.buy-btn').on('click', showAddProductPopup);
    };

    /**
     * This function responsible for handling 'Add to Cart' popup button by hiding it and
     * displaying 'loading' spinner instead. It also responsible for preparing and sending AJAX
     * request to the server with the data about product added to the user's 'Shopping cart'
     * and processing server's response by updating 'Shopping cart' data and hiding popup.
     */
    var addProductToCart = function() {
        var productId = $('#addProductPopup').attr('data-id-product');
        var quantity = $('#addProductPopup .quantity').val();

        var btn = $('#addToCart');
        convertButtonToLoaderSpinner(btn, 'btn-primary');
        
        $.ajax({
        	url : ctx + '/ajax/json/cart/product/add',
        	method: 'POST',
        	data : {
        		productId : productId,
        		quantity : quantity
        	},
        	success : function(data) {
                $('#currentShoppingCart .cart-total-cost').text(data.totalCost);
                $('#currentShoppingCart .cart-total-count').text(data.totalCount);
                $('#currentShoppingCart').removeClass('hidden');
                
                convertLoaderSpinnerToButton(btn, 'btn-primary', addProductToCart);

                $('#addProductPopup').modal('hide');
			},
			erro : function(data) {
				convertLoaderSpinnerToButton(btn, 'btn-primary', addProductToCart);
				alert("Error while adding product to shopping cart");
			}
        });
    };

    /**
     * This function responsible for handling 'Total' field in the  'Add product to shopping cart popup'
     * reading single item price and multiplying it by quantity value, specified by user.
     */
    var calculateItemTotal = function() {
        var priceString = $('#addProductPopup .price').text();
        var price = parseFloat(priceString.replace('$', ''));
        var quantity = parseInt($('#addProductPopup .quantity').val());

        var min = parseInt($('#addProductPopup .quantity').attr('min'));
        var max = parseInt($('#addProductPopup .quantity').attr('max'));

        if (quantity >= min && quantity <= max) {
            var totalPrice = (price * quantity).toFixed(2);
            $('#addProductPopup .total-price').text('$ ' + totalPrice);
        } else {
            $('#addProductPopup .quantity').val(1);
            $('#addProductPopup .total-price').text(priceString);
        }
    };

    /**
     * This function responsible for toggling 'hidden' classes on 'Load more products' button
     * and corresponding 'Loading' spinner element.
     */
    var loadMoreProducts = function() {
    	var btn = $('#loadMore');
    	convertButtonToLoaderSpinner(btn, 'btn-success');
    	
    	var pathname = location.pathname;
    	var queryString = location.search.substring(1);
    	var currentPage = parseInt($('#productList').attr('data-page-current'));
    	
    	var url = ctx + '/ajax/html/more' + pathname + '?page=' + (currentPage + 1) + '&' + queryString;
        
        $.ajax({
        	url : url,
        	success : function(html) {
        		$('#productList .row').append(html);
        		initBuyBtn();
        		
        		var totalPages = parseInt($('#productList').attr('data-page-total'));
        		currentPage++;
        		$('#productList').attr('data-page-current', currentPage);
        		
        		if (currentPage < totalPages) {
        			convertLoaderSpinnerToButton(btn, 'btn-success', loadMoreProducts);
				} else {
					btn.remove();
				}
			},
			error : function(data) {
				convertLoaderSpinnerToButton(btn, 'btn-success', loadMoreProducts);
				alert('Error');
			}
        });
    };
    
    /**
     * This function responsible for transfroming button into 'loading' spinner 
     * indicator.
     */
    var convertButtonToLoaderSpinner = function(btn, btnClass) {
		btn.removeClass(btnClass);
		btn.removeClass(btn);
		btn.addClass('load-indicator');
		var text = btn.text();
		btn.text('');
		btn.attr('data-btn-text', text);
		btn.off('click');
	};
	
	var convertLoaderSpinnerToButton = function(btn, btnClass, actionClick) {
		btn.removeClass('load-indicator');
		btn.addClass('btn');
		btn.addClass(btnClass);
		btn.text(btn.attr('data-btn-text'));
		btn.removeAttr('data-btn-text');
		btn.click(actionClick);
	};

    /**
     * This function responsible for processing 'Category filters' and 'Product filters'
     * sections in the products.html. Toggling 'All' checkbox should appropriately
     * toggle all relative checkboxes alltogether.
     */
    var initSearchForm = function() {
        $('#allCategories').on('click', function() {
            $('.categories .search-option').prop('checked', $(this).is(':checked'));
        });
        $('.categories .search-option').on('click', function() {
            $('#allCategories').prop('checked', false);
        });

        $('#allProducers').on('click', function() {
            $('.producers .search-option').prop('checked', $(this).is(':checked'));
        });
        $('.producers .search-option').on('click', function() {
            $('#allProducers').prop('checked', false);
        });
    };

    /**
     * This function responsible for handling 'Search product' feature,
     * in such way that if the user checks all checkboxes whether in 'Category filters' or 'Product filters'
     * this function should uncheks all of them in order to they don't appear in the search query and by
     * thus search result would not be dependent on any filters at all. If the user checks only some of the
     * filters, it means they should be present in the search query and this function doesn't be applied
     * for them.
     */
    var goSearch = function() {
        var isAllSelected = function(selector) {
            var unchecked = 0;
            $(selector).each(function(index, value) {
                if (!$(value).is(':checked')) {
                    unchecked++;
                }
            });
            return unchecked === 0;
        };

        if (isAllSelected('.categories .search-option')) {
            $('.categories .search-option').prop('checked', false);
        }
        if (isAllSelected('.producers .search-option')) {
            $('.producers .search-option').prop('checked', false);
        }

        $('.search-form').submit();
    };

    /**
     * This function responsible for showing to user a confirmation popup regarding
     * some matter and invocing specified function if he agrees.
     */
    var confirm = function(msg, okFunction) {
        if (window.confirm(msg)) {
            okFunction();
        }
    };

    /**
     * This function responsible for showing to user a confirmation popup regarding
     * product deleting, and if he agrees, it call delete function.
     */
    var removeProductFromCart = function() {
        var btn = $(this);
        confirm('Are you sure ?', function() {
            executeRemoveProduct(btn);
        });
    };

    /**
     * This function responsible for deleting aproduct from shopping cart via AJAX request
     * and for updating shopping cart page with relevant data.
     */
    var executeRemoveProduct = function(btn) {
        var productId = btn.attr('data-id-product');
        var quantity = btn.attr('data-quantity');

        convertButtonToLoaderSpinner(btn, 'btn-danger');
        
        $.ajax({
        	url : ctx + '/ajax/json/cart/product/remove',
        	method: 'POST',
        	data : {
        		productId : productId,
        		quantity : quantity
        	},
        	success : function(data) {
        		if (data.totalCount == 0) {
                    window.location.href = ctx + 'products';
                } else {
                    var prevQuantity = parseInt($('#product' + productId + ' .quantity').text());
                    var removeQuantity = parseInt(quantity);
                    if (removeQuantity >= prevQuantity) {
                        $('#product' + productId).remove();
                    } else {
                    	convertLoaderSpinnerToButton(btn, 'btn-danger', removeProductFromCart);
                        $('#product' + productId + ' .quantity').text(prevQuantity - removeQuantity);
                        if (prevQuantity - removeQuantity == 1) {
                            $('#product' + productId + ' a.remove-product.all').remove();
                        }
                    }
                }
                refreshCartTotal(data);
			},
			erro : function(data) {
				convertLoaderSpinnerToButton(btn, 'btn-primary', addProductToCart);
				alert("Error while adding product to shopping cart");
			}
        });
    };
    

    /**
     * This function refreshes shopping cart total.
     * TODO: executeRemoveProduct() may do it with data received from server insted !!!!!!
     */
    var refreshCartTotal = function(data) {
    	
    	$('span.cart-total-cost').text(data.totalCost);
    	$('span.cart-total-count').text(data.totalCount);
    	$('td.cart-total-cost').text('$ ' + data.totalCost)
    };

    init();
});