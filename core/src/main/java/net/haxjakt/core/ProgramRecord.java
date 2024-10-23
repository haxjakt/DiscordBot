package net.haxjakt.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "example-test")
public class ProgramRecord {
    @Id
    private String id;

    private String scriptName;

    private String javaSourceCode;

    private String javaByteCodeBase64;

    private String authorId;

    private String guildId;
}
