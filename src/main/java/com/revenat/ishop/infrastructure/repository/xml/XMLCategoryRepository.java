package com.revenat.ishop.infrastructure.repository.xml;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import com.revenat.ishop.domain.entity.Category;
import com.revenat.ishop.domain.entity.Producer;
import com.revenat.ishop.domain.entity.Product;
import com.revenat.ishop.domain.search.criteria.ProductCriteria;
import com.revenat.ishop.infrastructure.exception.PersistenceException;
import com.revenat.ishop.infrastructure.repository.CategoryRepository;
import com.revenat.ishop.infrastructure.repository.ProducerRepository;
import com.revenat.ishop.infrastructure.repository.ProductRepository;

public class XMLCategoryRepository implements CategoryRepository {
	private final ProductRepository productRepo;
	private final ProducerRepository producerRepo;
	private final String fileName;

	public XMLCategoryRepository(ProductRepository productRepo, ProducerRepository producerRepo, String fileName) {
		this.productRepo = productRepo;
		this.producerRepo = producerRepo;
		this.fileName = fileName;
	}

	@Override
	public List<Category> findAll() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Categories.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Categories categories = (Categories) jaxbUnmarshaller.unmarshal(new File(fileName));
			return categories.getCategory().stream().sorted(Comparator.comparing(Category::getName)).collect(toList());
		} catch (JAXBException e) {
			throw new PersistenceException("Can not read categories from file: " + fileName, e);
		}
	}

	@Override
	public List<Category> findByCriteria(ProductCriteria criteria) {
		List<Category> categories = findAll();
		filterByCriteria(categories, criteria);
		return categories;
	}

	private void filterByCriteria(List<Category> categories, ProductCriteria criteria) {
		List<Integer> producerIds = criteria.getProducerIds();
		List<Producer> producers = producerRepo.findAll();
		String query = criteria.getQuery();

		for (Category category : categories) {
			int productCount = getFilteredProductCount(query, producerIds, producers, category);
			category.setProductCount(productCount);
		}

	}

	private int getFilteredProductCount(String query, List<Integer> producerIds, List<Producer> producers, Category category) {
		int allProductsCount = category.getProductCount();
		if (noSpecifiedProducersAndEmptyQuery(query, producerIds)) {
			return allProductsCount;
		} else if (specifiedProducersAndEmptyQuery(query, producerIds)) {
			List<Product> categoryProducts = productRepo.findByCategory(category.getUrl(), 0, allProductsCount);
			return categoryProducts.stream()
					.filter(product -> hasProducer(producerIds, producers, product))
					.collect(toList()).size();
		} else if (noSpecifiedProducersAndSpecifiedQuery(query, producerIds)) {
			List<Product> categoryProducts = productRepo.findByCategory(category.getUrl(), 0, allProductsCount);
			return categoryProducts.stream()
					.filter(p -> containsText(p, query))
					.collect(toList()).size();
		} else {
			List<Product> categoryProducts = productRepo.findByCategory(category.getUrl(), 0, allProductsCount);
			return categoryProducts.stream()
					.filter(p -> hasProducer(producerIds, producers, p) && containsText(p, query))
					.collect(toList()).size();
		}
	}
	
	private static boolean noSpecifiedProducersAndEmptyQuery(String query, List<Integer> producerIds) {
		return producerIds.isEmpty() && query.trim().isEmpty();
	}
	
	private static boolean specifiedProducersAndEmptyQuery(String query, List<Integer> producerIds) {
		return !producerIds.isEmpty() && query.trim().isEmpty();
	}

	private static boolean noSpecifiedProducersAndSpecifiedQuery(String query, List<Integer> producerIds) {
		return producerIds.isEmpty() && !query.trim().isEmpty();
	}
	
	private static boolean hasProducer(List<Integer> producerIds, List<Producer> producers, Product product) {
		return producerIds.contains(getProducerIdByName(product.getProducer(), producers));
	}
	
	private static boolean containsText(Product p, String query) {
		return p.getName().toUpperCase().contains(query.toUpperCase())
				|| p.getDescription().toUpperCase().contains(query.toUpperCase());
	}

	private static Integer getProducerIdByName(String producerName, List<Producer> producers) {
		return producers.stream().filter(producer -> producer.getName().equals(producerName)).findFirst()
				.orElse(new Producer()).getId();
	}

	@XmlRootElement(name = "categories")
	private static class Categories {
		private List<Category> category;

		public List<Category> getCategory() {
			return category;
		}

		@SuppressWarnings("unused")
		public void setCategory(List<Category> category) {
			this.category = category;
		}
	}

}
