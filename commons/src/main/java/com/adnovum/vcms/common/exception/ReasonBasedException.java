package com.adnovum.vcms.common.exception;

import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Slf4j
public abstract class ReasonBasedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// generated unique ID to identify the exception
	private final String id;

	private final Reason reason;

	protected ReasonBasedException(String message, Reason reason, Throwable cause) {
		super(message, cause);
		this.reason = reason;
		if (cause instanceof ReasonBasedException) {
			ReasonBasedException businessException = (ReasonBasedException) cause;
			id = businessException.getId();
		}
		else {
			id = generateErrorId();
			log.info("caused by:", cause);
		}
	}

	protected ReasonBasedException(String message, Reason reason) {
		super(message);
		this.reason = reason;
		id = generateErrorId();
	}

	/**
	 * Creates information where this exception was thrown.
	 */
	private static String whereAmI() {
		int maxDepth = 6;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StringBuilder msg = new StringBuilder();
		int depth = Math.min(stack.length, maxDepth);
		for (int currentDepth = 0; currentDepth < depth; currentDepth++) {
			StackTraceElement currentStack = stack[currentDepth];
			String fileName = currentStack.getFileName();

			if (fileName == null) {
				fileName = "Unknown Source";
			}
			else {
				fileName += ":" + currentStack.getLineNumber();
			}

			msg.append(currentStack.getClassName()).append(".").append(currentStack.getMethodName()).append("(").append(fileName)
					.append(") ");
		}

		return msg.toString();
	}

	public Reason getReason() {
		return reason;
	}

	public String getId() {
		return id;
	}

	public int getErrorCode() {
		return reason.getErrorCode();
	}

	/**
	 * Generates a new errorId and logs it (and the place it was created) to the logfile
	 *
	 * @return the errorId
	 */
	private String generateErrorId() {
		String tid = UUID.randomUUID().toString();
		String stackTrace = whereAmI();

		String stringForIdGeneration = toStringForIdGeneration(tid);
		log.info(stringForIdGeneration);
		log.info("at {}", stackTrace);

		return tid;
	}

	@Override
	public String toString() {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("id", id);
		tsb.append("reason", reason);
		tsb.append("message", getMessage());
		return tsb.toString();
	}

	/**
	 * this toString like method lets us pass the tid instead of reading from the member-variable.
	 * At the time this method is called, id has not yet been assigned. As it is only possible to assign id (because
	 * it is declared final) in static{}-block or a constructor we have to use this little workaround.
	 * We can not have generateErrorId assign it
	 */
	private String toStringForIdGeneration(String tid) {
		ToStringBuilder tsb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		tsb.append("id", tid);
		tsb.append("reason", reason);
		tsb.append("message", getMessage());
		return tsb.toString();
	}
}
