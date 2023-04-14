package de.marcorohn.mirror;

public class SourceFile {

	private String location;
	private String contents;

	public SourceFile(String location, String contents) {
		this.location = location;
		this.contents = contents;
	}

	@Override
	public String toString() {
		return "SourceFile{" + "location='" + location + '\'' + ", contents='" + contents + '\'' + '}';
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
}
