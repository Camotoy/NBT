package com.nukkitx.nbt;

import java.io.Closeable;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

import static com.nukkitx.nbt.NbtType.byClass;
import static com.nukkitx.nbt.NbtUtils.MAX_DEPTH;

public class NBTOutputStream implements NbtWriter, Closeable {
    private final DataOutput output;
    private boolean closed = false;

    public NBTOutputStream(DataOutput output) {
        this.output = Objects.requireNonNull(output, "output");
    }

    public void writeTag(Object tag) throws IOException {
        this.writeTag(tag, MAX_DEPTH);
    }

    public void writeTag(Object tag, int maxDepth) throws IOException {
        Objects.requireNonNull(tag, "tag");
        if (closed) {
            throw new IllegalStateException("closed");
        }

        NbtType<?> type = byClass(tag.getClass());

        writeTag("", type, tag, maxDepth);
    }

    @Override
    public void writeTag(String name, NbtType<?> type, Object value, int maxDepth) throws IOException {
        Objects.requireNonNull(value, "value");
        if (closed) {
            throw new IllegalStateException("closed");
        }

        writeTypeAndName(type, name);

        this.serialize(value, type, maxDepth);
    }

    @Override
    public void writeValue(String name, Object value, int maxDepth) throws IOException {
        Objects.requireNonNull(value, "tag");
        if (closed) {
            throw new IllegalStateException("closed");
        }

        NbtType<?> type = byClass(value.getClass());
        writeTypeAndName(type, name);
        this.serialize(value, type, maxDepth);
    }

    public void writeValue(Object tag) throws IOException {
        this.writeValue(tag, MAX_DEPTH);
    }

    public void writeValue(Object tag, int maxDepth) throws IOException {
        Objects.requireNonNull(tag, "tag");
        if (closed) {
            throw new IllegalStateException("closed");
        }

        NbtType<?> type = byClass(tag.getClass());
        this.serialize(tag, type, maxDepth);
    }

    @Override
    public void writeByte(String name, byte b) throws IOException {
        writeTypeAndName(NbtType.BYTE, name);
        output.writeByte(b);
    }

    @Override
    public void writeShort(String name, short s) throws IOException {
        writeTypeAndName(NbtType.SHORT, name);
        output.writeShort(s);
    }

    @Override
    public void writeInt(String name, int i) throws IOException {
        writeTypeAndName(NbtType.INT, name);
        output.writeInt(i);
    }

    @Override
    public void writeLong(String name, long l) throws IOException {
        writeTypeAndName(NbtType.LONG, name);
        output.writeLong(l);
    }

    @Override
    public void writeFloat(String name, float f) throws IOException {
        writeTypeAndName(NbtType.FLOAT, name);
        output.writeFloat(f);
    }

    @Override
    public void writeDouble(String name, double d) throws IOException {
        writeTypeAndName(NbtType.DOUBLE, name);
        output.writeDouble(d);
    }

    private void writeTypeAndName(NbtType<?> type, String name) throws IOException {
        output.writeByte(type.getId());
        output.writeUTF(Objects.requireNonNull(name));
    }

    private void serialize(Object tag, NbtType<?> type, int maxDepth) throws IOException {
        if (maxDepth < 0) {
            throw new IllegalArgumentException("Reached depth limit");
        }

        switch (type.getEnum()) {
            case END:
                break;
            case BYTE:
                Byte byteVal = (Byte) tag;
                output.writeByte(byteVal);
                break;
            case SHORT:
                Short shortVal = (Short) tag;
                output.writeShort(shortVal);
                break;
            case INT:
                Integer intVal = (Integer) tag;
                output.writeInt(intVal);
                break;
            case LONG:
                Long longVal = (Long) tag;
                output.writeLong(longVal);
                break;
            case FLOAT:
                Float floatVal = (Float) tag;
                output.writeFloat(floatVal);
                break;
            case DOUBLE:
                Double doubleVal = (Double) tag;
                output.writeDouble(doubleVal);
                break;
            case BYTE_ARRAY:
                byte[] byteArray = (byte[]) tag;
                output.writeInt(byteArray.length);
                output.write(byteArray);
                break;
            case STRING:
                String string = (String) tag;
                output.writeUTF(string);
                break;
            case LIST:
                NbtList<?> list = (NbtList<?>) tag;
                NbtType<?> listType = list.getType();
                output.writeByte(listType.getId());
                output.writeInt(list.size());
                for (Object entry : list) {
                    this.serialize(entry, listType, maxDepth - 1);
                }
                break;
            case COMPOUND:
                NbtLike map = (NbtLike) tag;
                map.serializeToNbt(this);

                output.writeByte(0); // End tag
                break;
            case INT_ARRAY:
                int[] intArray = (int[]) tag;
                output.writeInt(intArray.length);
                for (int val : intArray) {
                    output.writeInt(val);
                }
                break;
            case LONG_ARRAY:
                long[] longArray = (long[]) tag;
                output.writeInt(longArray.length);
                for (long val : longArray) {
                    output.writeLong(val);
                }
                break;
        }
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        closed = true;
        if (output instanceof Closeable) {
            ((Closeable) output).close();
        }
    }
}
