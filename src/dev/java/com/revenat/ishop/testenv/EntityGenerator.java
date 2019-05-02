package com.revenat.ishop.testenv;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.RandomStringUtils;

import com.revenat.ishop.entity.Category;
import com.revenat.ishop.entity.Producer;
import com.revenat.ishop.entity.Product;

public class EntityGenerator {
	private static final int MAX_MODEL_NUMBER = 10000;
	private static final int MIN_MODEL_NUMBER = 10;
	private static final int MAX_PRICE = 2200;
	private static final int MIN_PRICE = 250;
	private static final String[] CATEGORIES = { "Tablet", "Smartphone", "E-book", "Notebook", "Smartwatch",
			"MP3-player", "TV" };
	private static final String[] PRODUCERS = { "Apple", "Sony", "Samsung", "Dell", "Philips", "Panasonic", "Toshiba",
			"Acer", "Huawei", "Xiaomi", "LG", "Sharp" };
	private static final Map<String, String[]> CATEGORY_IMAGE = new HashMap<>();
	private static final Map<String, String> CATEGORY_DESCRIPTION = new HashMap<>();
	private static final List<Category> ALL_CATEGORIES = new ArrayList<>();
	private static final List<Producer> ALL_PRODUCERS = new ArrayList<>();

	static {
		ALL_CATEGORIES.addAll(generateCategories());
		ALL_PRODUCERS.addAll(generateProducers());

		CATEGORY_IMAGE.put("Tablet", new String[]{"/media/tablet01.jpg", "/media/tablet02.jpg", "/media/tablet03.jpg"});
		CATEGORY_IMAGE.put("Smartphone", new String[] {"/media/smartphone01.jpg","/media/smartphone02.jpg","/media/smartphone03.jpg"});
		CATEGORY_IMAGE.put("E-book", new String[] {"/media/e-book01.jpg","/media/e-book02.jpg","/media/e-book03.jpg"});
		CATEGORY_IMAGE.put("Notebook", new String[] {"/media/notebook01.jpg","/media/notebook02.jpg","/media/notebook03.jpg"});
		CATEGORY_IMAGE.put("Smartwatch", new String[] {"/media/smartwatch01.jpg", "/media/smartwatch02.jpg","/media/smartwatch03.jpg"});
		CATEGORY_IMAGE.put("MP3-player", new String[] {"/media/mp3-player01.jpg","/media/mp3-player02.jpg","/media/mp3-player03.jpg",});
		CATEGORY_IMAGE.put("TV", new String[] {"/media/tv01.jpg","/media/tv02.jpg","/media/tv03.jpg"});
		
		CATEGORY_DESCRIPTION.put("Tablet", "9.7‑inch Retina display / RAM 8 GB / Capacity " + 
				                           "64 GB / Black / Weight 450 g / Bluetooth / 5G / miniUSB / GPS / WiFi");
		CATEGORY_DESCRIPTION.put("Smartphone", "Display diagonal 6.0\" / " + 
											   "Camera: 40.2Mp / RAM: 8 Gb / Orange / 3500 mA/h / Weight 220 g / " + 
				 							   "WiFi / Bluetooth / Dictophone / USB type-C");
		CATEGORY_DESCRIPTION.put("E-book", "Display 6\"/ Matrix " + 
										   "type: E lnk Pearl / Resolution: 1600x1200 / Memory 6 GB / " + 
				                           "Weight 190 g");
		CATEGORY_DESCRIPTION.put("Notebook", "Display 14\" / RAM 32 GB / SSD " + 
                							 "500 GB / Black / Weight 1250 g / Bluetooth / 4G / USB TypeC / WiFi");
		CATEGORY_DESCRIPTION.put("Smartwatch", "Display diagonal 2.0\" / " + 
				   							   "Camera: 10.2Mp / RAM: 2 Gb / Blue / 1100 mA/h / Weight 58 g / " + 
					   						   "WiFi / Bluetooth / Dictophone / miniUSB");
		CATEGORY_DESCRIPTION.put("MP3-player", "Display diagonal 2.0\" / " + 
				            				   "Camera: 6.2Mp / RAM: 5 Gb / Capacity 32 Gb /  Blue / 1100 mA/h / Weight 112 g / " + 
										       "WiFi / Bluetooth / Dictophone / miniUSB");
		CATEGORY_DESCRIPTION.put("TV", "Display 56\" / RAM 8 GB / Resolution: 4096 × 3072 / Capacity" + 
									   "1 TB / Black / Weight 2300 g / Bluetooth / 4G / USB / GPS / HDMI x 5");
	}

	public static List<Category> getAllCategories() {
		return Collections.unmodifiableList(ALL_CATEGORIES);
	}

	public static List<Producer> getAllProducers() {
		return Collections.unmodifiableList(ALL_PRODUCERS);
	}
	
	public static List<Product> generateProducts(int quantity) {
		List<Product> products = new ArrayList<>();
		
		for (int i = 0; i < quantity; i++) {
			products.add(generateProduct());
		}
		
		return products;
	}

	private static Product generateProduct() {
		Category category = getRandomCategory();
		Producer producer = getRandomProducer();
		String name = generateProductName(producer.getName());
		String description = getStubDescriptionFor(category.getName());
		String imageLink = getStubImageLinkFor(category.getName());
		BigDecimal price = getRandomPrice();
		return new Product(name, description, imageLink, price, category.getName(), producer.getName());
	}

	private static BigDecimal getRandomPrice() {
		double rendomD = ThreadLocalRandom.current().nextDouble(MIN_PRICE, MAX_PRICE);
		return round(rendomD, 2);
	}
	
	private static BigDecimal round(double value, int places) {
	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd;
	}

	private static String getStubImageLinkFor(String name) {
		return CATEGORY_IMAGE.get(name)[ThreadLocalRandom.current().nextInt(0, 3)];
	}

	private static Producer getRandomProducer() {
		int idx = ThreadLocalRandom.current().nextInt(0, ALL_PRODUCERS.size());
		return ALL_PRODUCERS.get(idx);
	}

	private static String getStubDescriptionFor(String category) {
		return CATEGORY_DESCRIPTION.get(category);
	}

	private static Category getRandomCategory() {
		int idx = ThreadLocalRandom.current().nextInt(0, ALL_CATEGORIES.size());
		return ALL_CATEGORIES.get(idx);
	}

	private static String generateProductName(String producer) {
		int prefixLength = ThreadLocalRandom.current().nextInt(2, 4);
		String prefix = RandomStringUtils.random(prefixLength, true, false).toUpperCase();
		int modelNumber = ThreadLocalRandom.current().nextInt(MIN_MODEL_NUMBER, MAX_MODEL_NUMBER);
		
		return String.format("%s %s%d", producer, prefix, modelNumber);
	}

	private static Collection<? extends Category> generateCategories() {
		List<Category> categories = new ArrayList<>();

		for (int i = 1; i <= CATEGORIES.length; i++) {
			String name = CATEGORIES[i - 1];
			String url = "/" + name.toLowerCase();
			categories.add(new Category(i, name, url));
		}

		return categories;
	}

	private static Collection<? extends Producer> generateProducers() {
		List<Producer> producers = new ArrayList<>();

		for (int i = 1; i <= PRODUCERS.length; i++) {
			producers.add(new Producer(i, PRODUCERS[i - 1]));
		}

		return producers;
	}

	private EntityGenerator() {
	}
}
