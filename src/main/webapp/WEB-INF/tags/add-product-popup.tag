<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<!-- Add product to sopping cart popup -->
       <div id="addProductPopup" class="modal fade" tabindex="-1" role="dialog">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title"><custom:i18n key="app.addProduct.header"/></h4>
              </div>
              <div class="modal-body row">
                <div class="col-xs-12 col-sm-6">
                  <div class="thumbnail">
                    <img class="product-image" src="data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=" alt="Product image">
                  </div>
                </div>
                <div class="col-xs-12 col-sm-6">
                  <h4 class="name text-center">Name</h4>
                  <div class="list-group hidden-xs product-spec">
                    <span class="list-group-item"> <small><custom:i18n key="app.addProduct.label.category"/>:</small> <span class="category">?</span></span>
                    <span class="list-group-item"> <small><custom:i18n key="app.addProduct.label.category"/>:</small> <span class="producer">?</span></span>
                  </div>
                  <div class="list-group">
                    <span class="list-group-item"> <small><custom:i18n key="app.addProduct.label.price"/>: </small> <span class="price">0</span></span>
                    <span class="list-group-item"> <small><custom:i18n key="app.addProduct.label.quantity"/>:</small> <input type="number" class="quantity" value="1" min="1" max="10"></span>
                    <span class="list-group-item"> <small><custom:i18n key="app.addProduct.label.total"/>: </small> <span class="total-price">0</span></span>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <a id="addToCart"  class="btn btn-primary"><custom:i18n key="app.addProduct.button.add"/></a>
                <button type="button" class="btn btn-default" data-dismiss="modal"><custom:i18n key="app.addProduct.button.close"/></button>
              </div>
            </div>
          </div>
        </div>
