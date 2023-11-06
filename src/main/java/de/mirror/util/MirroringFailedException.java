package de.mirror.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MirroringFailedException extends Exception {

	public Exception originalException;
}
