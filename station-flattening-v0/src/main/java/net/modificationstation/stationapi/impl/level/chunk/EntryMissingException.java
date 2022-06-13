package net.modificationstation.stationapi.impl.level.chunk;

public class EntryMissingException
extends RuntimeException {
    public EntryMissingException(int index) {
        super("Missing Palette entry for index " + index + ".");
    }
}

