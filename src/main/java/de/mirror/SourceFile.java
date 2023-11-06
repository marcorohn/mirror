package de.mirror;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SourceFile {

	private String location;
	private String contents;

	@Override
	public String toString() {
		return "SourceFile{" + "location='" + location + '\'' + ", contents='" + contents + '\'' + '}';
	}
}
