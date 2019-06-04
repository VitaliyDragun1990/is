package com.revenat.ishop.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public class ProductRepositoryImpl implements SearchProductRepository {
	private EntityManager entityManager;

	@Autowired
	public ProductRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Product> findByCriteria(ProductCriteria criteria, Pageable pageable) {
		StringBuilder hql = new StringBuilder("FROM Product p WHERE (p.name LIKE :query OR p.description LIKE :query)");
		Map<String, Object> params = populateSearchParams(criteria, hql);
		
		TypedQuery<Product> query = entityManager.createQuery(hql.toString(), Product.class);
		setQueryParameters(query, params);
		query.setMaxResults(pageable.getPageSize());
		query.setFirstResult(pageable.getOffset());
		
		return query.getResultList();
	}
	
	@Override
	public long countByCriteria(ProductCriteria criteria) {
		StringBuilder hql =
				new StringBuilder("SELECT count(*) FROM Product p WHERE (p.name LIKE :query or p.description LIKE :query)");
		Map<String, Object> params = populateSearchParams(criteria, hql);
		
		TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
		setQueryParameters(query, params);
		
		return query.getSingleResult();
	}

	private void setQueryParameters(Query query, Map<String, Object> params) {
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, Object> populateSearchParams(ProductCriteria criteria, StringBuilder hql) {
		Map<String, Object> params = new HashMap<>();
		params.put("query", "%" + criteria.getQuery() + "%");
		if (!criteria.getCategoryIds().isEmpty()) {
			hql.append(" AND p.category.id IN (:categories) ");
			params.put("categories", criteria.getCategoryIds());
		}
		if (!criteria.getProducerIds().isEmpty()) {
			hql.append(" AND p.producer.id IN (:producers) ");
			params.put("producers", criteria.getProducerIds());
		}
		return params;
	}
}
