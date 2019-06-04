package com.revenat.ishop.infrastructure.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;

public class ProducerRepositoryImpl implements SearchProducerRepository {
	
	private EntityManager entityManager;
	
	@Autowired
	public ProducerRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Producer> findByCriteria(ProductCriteria criteria) {
		String nativeQuery = buildNativeQuery(criteria);
		Map<String, Object> params = generateQueryParams(criteria);
		
		Query query = entityManager.createNativeQuery(nativeQuery, Producer.class);
		setQueryParameters(query, params);
		
		return query.getResultList();
	}
	
	private String buildNativeQuery(ProductCriteria criteria) {
		StringBuilder nativeQuery = new StringBuilder("SELECT pr.id, pr.name, CAST(count(prod.id) AS INTEGER) AS product_count ");
		nativeQuery.append("FROM producer pr LEFT OUTER JOIN ");
		nativeQuery.append("(SELECT p.id, p.producer_id FROM product p INNER JOIN category c ON p.category_id = c.id ");
		nativeQuery.append("WHERE (p.name LIKE :query  OR p.description LIKE :query)");
		if (!criteria.getCategoryIds().isEmpty()) {
			nativeQuery.append(" AND c.id IN (:categories) ");
		}
		nativeQuery.append(") prod ON pr.id = prod.producer_id ");
		nativeQuery.append("GROUP BY pr.id, pr.name ");
		nativeQuery.append("ORDER BY pr.name");
		
		return nativeQuery.toString();
	}
	
	private Map<String, Object> generateQueryParams(ProductCriteria criteria) {
		Map<String, Object> params = new HashMap<>();
		params.put("query", "%" + criteria.getQuery() + "%");
		params.put("query", "%" + criteria.getQuery() + "%");
		if (!criteria.getCategoryIds().isEmpty()) {
			params.put("categories", criteria.getCategoryIds());
		}
		return params;
	}
	
	private void setQueryParameters(Query query, Map<String, Object> params) {
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}
}
