package com.revenat.ishop.repository;

import com.revenat.ishop.entity.Account;

/**
 * This interface represents repository for managing CRUD operations with
 * {@link Account} entity.
 * 
 * @author Vitaly Dragun
 *
 */
public interface AccountRepository {

	/**
	 * Returns {@link Account} entity with specified {@code email} property.
	 * 
	 * @param email email of the account to look for
	 * @return {@link Account} entity with given email or null if no account with
	 *         such email.
	 */
	Account getByEmail(String email);

	/**
	 * Saves specified {@link Account} object into datastore. Generates and sets
	 * unique identifier at specified instance.
	 * 
	 * @param account {@link Account} instance to save.
	 */
	void save(Account account);
}
