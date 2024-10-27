package net.haxjakt.script.jcompiler;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class BytecodeFile extends SimpleJavaFileObject {
    private ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    public BytecodeFile(String className, Kind kind) {
        super(URI.create("bytes:///" + className.replace('.', '/') + kind.extension), kind);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteCode;
    }

    public byte[] getByteCode() {
        return byteCode.toByteArray();
    }
}
