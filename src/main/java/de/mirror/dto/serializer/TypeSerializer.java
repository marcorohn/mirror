package de.mirror.dto.serializer;

import de.marcorohn.mirror.config.SerializationConfig;
import de.marcorohn.mirror.meta.member.MetaField;
import de.marcorohn.mirror.meta.type.MetaType;

public interface TypeSerializer {
	TypeSerializer setSerializationConfig(SerializationConfig config);
	TypeSerializer setTargetType(MetaType targetType);
	TypeSerializer addField(MetaField metaMember);

	String serialize();
	String getFilePath();
	String getFileName(boolean withEnding);
}
