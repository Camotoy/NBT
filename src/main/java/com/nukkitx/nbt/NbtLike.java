package com.nukkitx.nbt;

import java.io.IOException;

public interface NbtLike {

    /**
     * Not to be called manually.
     */
    default void serializeToNbt(NbtWriter writer) throws IOException {
        serializeToNbt(writer, NbtUtils.MAX_DEPTH);
    }

    /**
     * Not to be called manually.
     */
    void serializeToNbt(NbtWriter writer, int depth) throws IOException;
}
