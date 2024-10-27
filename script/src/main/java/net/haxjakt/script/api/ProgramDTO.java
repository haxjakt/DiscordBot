package net.haxjakt.script.api;

import lombok.Data;

@Data
public class ProgramDTO {
    private String programName;
    private String programText;
    private String programJava;
    private byte[] programByte;
}
