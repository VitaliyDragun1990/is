package com.revenat.ishop.infrastructure.repository;

import com.revenat.ishop.domain.entity.Account;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Insert;
import com.revenat.ishop.infrastructure.framework.annotation.persistence.repository.Select;

/**
 * This interface represents repository responsible for for performing CRUD
 * operations on {@link Account} entity.
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
	@Select("SELECT * FROM account WHERE email = ?")
	Account findByEmail(String email);

	/**
	 * Saves specified {@link Account} object into datastore.
	 * 
	 * @param account {@link Account} instance to save.
	 * @return saved {@link Account} object
	 */
	@Insert
	Account save(Account account);
}
