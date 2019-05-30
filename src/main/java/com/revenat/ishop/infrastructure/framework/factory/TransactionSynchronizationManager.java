package com.revenat.ishop.infrastructure.framework.factory;

import java.util.LinkedList;
import java.util.List;

import com.revenat.ishop.infrastructure.framework.exception.FrameworkSystemException;

/**
 * Component responsible for managing collection of
 * {@link TransactionSynchronization} for particular thread of execution.
 * 
 * @author Vitaly Dragun
 *
 */
public final class TransactionSynchronizationManager {
	private static final ThreadLocal<List<TransactionSynchronization>> transactionSynchronizations = new ThreadLocal<>();

	public static void addSynchronization(TransactionSynchronization transactionSynchronization) {
		List<TransactionSynchronization> list = getSynchronizations();
		if (list == null) {
			throw new FrameworkSystemException("transactionSYnchronizations is null. Does your service method have"
					+ " @Transactional(readOnly=false) annotation?");
		}
		list.add(transactionSynchronization);
	}

	static void initSynchronization() {
		transactionSynchronizations.set(new LinkedList<>());
	}

	static List<TransactionSynchronization> getSynchronizations() {
		return transactionSynchronizations.get();
	}

	static void clearSynchronization() {
		transactionSynchronizations.remove();
	}

	private TransactionSynchronizationManager() {
	}
}
