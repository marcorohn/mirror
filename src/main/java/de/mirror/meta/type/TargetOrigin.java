package de.mirror.meta.type;

public enum TargetOrigin {
	/**
	 * Builtin primitives
	 */
	BUILTIN,
	/**
	 * The type is provided via an external package in the target project or another directory.
	 */
	EXTERNAL,

	/**
	 * The type is another mirrored type, that will be provided by the mirror api.
	 */
	INCLUDED,
}
