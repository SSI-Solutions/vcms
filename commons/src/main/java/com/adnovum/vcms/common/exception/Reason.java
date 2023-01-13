package com.adnovum.vcms.common.exception;

import java.io.Serializable;

public interface Reason extends Serializable {

	/**
	 * returns the reason (like "res.error.impl") which is describes which entry in a message-file to display the user.
	 * Like this we can display a nice message to the user depending on the problem. For example "the lastname you entered was
	 * too long"
	 *
	 * @return the reason.
	 */
	String getReason();

	/**
	 * describes the type of error that occurred. This code is used to decide how to handle the error, how the flow is supposed
	 * to work.
	 * For example
	 * errorCode="1" means "backend down" and the UI only can fail.
	 * errorCode="100" means "some business problem" and can be handled
	 * errorCode="112" could mean "there was a problem on field x" and be handled differently.
	 *
	 * @return the error-code
	 */
	int getErrorCode();
}
