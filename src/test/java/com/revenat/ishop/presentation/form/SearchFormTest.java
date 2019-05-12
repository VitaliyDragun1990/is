package com.revenat.ishop.presentation.form;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.revenat.ishop.presentation.form.SearchForm;

public class SearchFormTest {

	private SearchForm form;

	@Test
	public void shouldAllowToCreateSearchFormUsingOnlyQuery() throws Exception {
		String query = "abs";

		form = new SearchForm(query, null, null);

		assertThat(form.getQuery(), equalTo(query));
		assertThat(form.getCategories(), empty());
		assertThat(form.getProducers(), empty());
	}

	@Test
	public void shouldReturnEmptyStringIfQueryWasNull() throws Exception {
		form = new SearchForm(null, null, null);

		assertThat(form.getQuery(), is(""));
	}

	@Test
	public void shouldAllowToSpecifyCategories() throws Exception {
		String query = "abs";
		String[] categories = new String[] {"1", "2"};

		form = new SearchForm(query, categories, null);

		assertThat(form.getCategories(), contains(1, 2));
	}
	
	@Test
	public void shouldAllowToSpecifyProducers() throws Exception {
		String query = "abs";
		String[] producers = new String[] {"1", "2"};

		form = new SearchForm(query, null, producers);

		assertThat(form.getProducers(), contains(1, 2));
	}
	
	@Test
	public void shouldSilentlyIgnoreInvalidProducers() throws Exception {
		String query = "abs";
		String[] producers = new String[] {"1", "2","dd"};

		form = new SearchForm(query, null, producers);

		assertThat(form.getProducers(), hasSize(2));
		assertThat(form.getProducers(), contains(1, 2));
		
	}
	
	@Test
	public void shouldSilentlyIgnoreInvalidCategories() throws Exception {
		String query = "abs";
		String[] categories = new String[] {"1", "2", "mm"};

		form = new SearchForm(query, categories, null);

		assertThat(form.getCategories(), hasSize(2));
		assertThat(form.getCategories(), contains(1, 2));
	}
	
	@Test
	public void shouldAllowToFindIfSearchFormIsEmpty() throws Exception {
		form = new SearchForm("abs", new String[] {"1", "2"}, new String[] {"1", "2"});
		assertFalse(form.isEmpty());
		
		form = new SearchForm(null, null, null);
		assertTrue(form.isEmpty());
	}
}
