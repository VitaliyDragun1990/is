package com.revenat.ishop.infrastructure.framework.factory;

/**
 * Represents arbitrary actionw hich should be done after transaction has been
 * commited successfully.
 * 
 * @author Vitaly Dragun
 *
 */
@FunctionalInterface
public interface TransactionSynchronization {
	void afterCommit();
}
