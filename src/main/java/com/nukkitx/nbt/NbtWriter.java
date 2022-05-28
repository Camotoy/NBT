package com.nukkitx.nbt;

import java.io.IOException;

import static com.nukkitx.nbt.NbtUtils.MAX_DEPTH;

public interface NbtWriter {

    default void writeTag(Object value) throws IOException {
        writeTag(value, MAX_DEPTH);
    }

    void writeTag(Object value, int maxDepth) throws IOException;

    default void writeTag(String name, NbtType<?> type, Object value) throws IOException {
        writeTag(name, type, value, MAX_DEPTH);
    };

    void writeTag(String name, NbtType<?> type, Object value, int maxDepth) throws IOException;

    void writeByte(String name, byte b) throws IOException;

    void writeShort(String name, short s) throws IOException;

    void writeInt(String name, int i) throws IOException;

    void writeLong(String name, long l) throws IOException;

    void writeFloat(String name, float f) throws IOException;

    void writeDouble(String name, double d) throws IOException;

    default void writeValue(String name, Object value) throws IOException {
        writeValue(name, value, MAX_DEPTH);
    }

    void writeValue(String name, Object value, int maxDepth) throws IOException;
}
