<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

	<c:forEach var="p" items="${products}">
	<!-- PRODUCT DATA -->
		<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 col-xlg-2">
			<div id="product${p.id}" class="panel panel-default product">
				<div class="panel-body">
					<div class="thumbnail">
						<img src='<c:url value="${p.imageLink}"/>' alt="${p.name}">
						<div class="desc">
							<div class="cell">
								<p>
									<span class="title"><custom:i18n key="app.product.title"/></span> ${p.description}
								</p>
							</div>
						</div>
					</div>
					<h4 class="name">${p.name}</h4>
					<div class="code text-muted"><custom:i18n key="app.label.code"/>: ${p.id}</div>
					<div class="price">$ ${p.price}</div>
					<a class="btn btn-primary pull-right buy-btn" data-id-product="${p.id}"><custom:i18n key="app.button.buy"/></a>
					<div class="list-group">
						<span class="list-group-item"> <small><custom:i18n key="app.label.category"/>:</small> <span class="category">${p.category}</span></span>
						<span class="list-group-item"> <small><custom:i18n key="app.label.producer"/>:</small> <span class="producer">${p.producer}</span></span>
					</div>
				</div>
			</div>
		</div>
	<!-- /PRODUCT DATA -->
	</c:forEach>	