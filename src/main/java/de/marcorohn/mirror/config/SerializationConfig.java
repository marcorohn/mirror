package de.marcorohn.mirror.config;

public class SerializationConfig {

	/**
	 * The output path where the transpiled DTOs will be stored.
	 */
	private String basePath = null;

	/**
	 * Whether the specified base path should be created automatically if it does not exist.
	 */
	private boolean createIfNonExistent;

	public SerializationConfig(String basePath, boolean createIfNonExistent) {
		this.setBasePath(basePath);
		this.createIfNonExistent = createIfNonExistent;
	}

	public void setBasePath(String basePath) {
		if (basePath != null && !basePath.endsWith("/")) {
			this.basePath = basePath + "/";
		} else {
			this.basePath = basePath;
		}
	}

	public String getBasePath() {
		return basePath;
	}

	public boolean isCreateIfNonExistent() {
		return createIfNonExistent;
	}

	public void setCreateIfNonExistent(boolean createIfNonExistent) {
		this.createIfNonExistent = createIfNonExistent;
	}
}
