package net.haxjakt.script.engine;

import javax.tools.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomFilemanager extends ForwardingJavaFileManager<StandardJavaFileManager> {
    private final Map<String, BytecodeFile> classFiles = new HashMap<>();

    protected CustomFilemanager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        BytecodeFile classFile = new BytecodeFile(className, kind);
        classFiles.put(className, classFile);
        return classFile;
    }

    public byte[] getByteCode(String className) {
        return classFiles.get(className).getByteCode();
    }
}
