package net.haxjakt.bot.dynamic;

public class CustomClassLoader extends ClassLoader {
    public Class<?> defineClass(String className, byte[] classData) {
        return defineClass(className, classData, 0, classData.length);
    }
}
