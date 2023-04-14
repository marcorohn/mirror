package de.marcorohn.mirror.util;

public class MirroringFailedException extends Exception {

	public Exception originalException;

	public MirroringFailedException(Exception originalException) {
		this.originalException = originalException;
	}

	public Exception getOriginalException() {
		return originalException;
	}

	public void setOriginalException(Exception originalException) {
		this.originalException = originalException;
	}
}
