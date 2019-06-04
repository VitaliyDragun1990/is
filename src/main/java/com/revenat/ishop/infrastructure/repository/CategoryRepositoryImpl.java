package com.revenat.ishop.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public class CategoryRepositoryImpl implements SearchCategoryRepository {
	
	private EntityManager entityManager;

	@Autowired
	public CategoryRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Category> findByCriteria(ProductCriteria criteria) {
		String nativeQuery = buildNativeQuery(criteria);
		Map<String, Object> params = generateQueryParams(criteria);
		
		Query query = entityManager.createNativeQuery(nativeQuery, Category.class);
		setQueryParameters(query, params);
		
		return query.getResultList();
	}
	
	private String buildNativeQuery(ProductCriteria criteria) {
		StringBuilder nativeQuery = new StringBuilder("SELECT c.id, c.name, c.url, CAST(count(prod.id) AS INTEGER) AS product_count ");
		nativeQuery.append("FROM category c LEFT OUTER JOIN ");
		nativeQuery.append("(SELECT p.id, p.category_id FROM product p INNER JOIN producer pr ON p.producer_id = pr.id ");
		nativeQuery.append("WHERE (p.name LIKE :query OR p.description LIKE :query)");
		if (!criteria.getProducerIds().isEmpty()) {
			nativeQuery.append(" AND pr.id IN (:producers) ");
		}
		nativeQuery.append(") prod ON c.id = prod.category_id ");
		nativeQuery.append("GROUP BY c.id, c.name, c.url ");
		nativeQuery.append("ORDER BY c.name");
		
		return nativeQuery.toString();
	}
	
	private Map<String, Object> generateQueryParams(ProductCriteria criteria) {
		Map<String, Object> params = new HashMap<>();
		params.put("query", "%" + criteria.getQuery() + "%");
		params.put("query", "%" + criteria.getQuery() + "%");
		if (!criteria.getProducerIds().isEmpty()) {
			params.put("producers", criteria.getProducerIds());
		}
		return params;
	}


	private void setQueryParameters(Query query, Map<String, Object> params) {
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}

}
