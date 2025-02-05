package net.modificationstation.stationapi.impl.level.chunk;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.modificationstation.stationapi.api.util.collection.IndexedIterable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static it.unimi.dsi.fastutil.Hash.FAST_LOAD_FACTOR;

/**
 * Generally provides better performance over the vanilla {@link BiMapPalette} when calling
 * {@link LithiumHashPalette#index(Object)} through using a faster backing map and reducing pointer chasing.
 */
public class LithiumHashPalette<T> implements Palette<T> {
    private static final int ABSENT_VALUE = -1;

    private final IndexedIterable<T> idList;
    private final PaletteResizeListener<T> resizeHandler;
    private final int indexBits;

    private final Reference2IntMap<T> table;
    private T[] entries;
    private int size = 0;

    public LithiumHashPalette(IndexedIterable<T> idList, PaletteResizeListener<T> resizeHandler, int indexBits, T[] entries, Reference2IntMap<T> table, int size) {
        this.idList = idList;
        this.resizeHandler = resizeHandler;
        this.indexBits = indexBits;
        this.entries = entries;
        this.table = table;
        this.size = size;
    }

    public LithiumHashPalette(IndexedIterable<T> idList, int bits, PaletteResizeListener<T> resizeHandler, List<T> list) {
        this(idList, bits, resizeHandler);

        for (T t : list) {
            this.addEntry(t);
        }
    }

    @SuppressWarnings("unchecked")
    public LithiumHashPalette(IndexedIterable<T> idList, int bits, PaletteResizeListener<T> resizeHandler) {
        this.idList = idList;
        this.indexBits = bits;
        this.resizeHandler = resizeHandler;

        int capacity = 1 << bits;

        this.entries = (T[]) new Object[capacity];
        this.table = new Reference2IntOpenHashMap<>(capacity, FAST_LOAD_FACTOR);
        this.table.defaultReturnValue(ABSENT_VALUE);
    }

    @Override
    public int index(T obj) {
        int id = this.table.getInt(obj);

        if (id == ABSENT_VALUE) {
            id = this.computeEntry(obj);
        }

        return id;
    }

    @Override
    public boolean hasAny(Predicate<T> predicate) {
        for (int i = 0; i < this.size; ++i) {
            if (predicate.test(this.entries[i])) {
                return true;
            }
        }

        return false;
    }

    private int computeEntry(T obj) {
        int id = this.addEntry(obj);

        if (id >= 1 << this.indexBits) {
            if (this.resizeHandler == null) {
                throw new IllegalStateException("Cannot grow");
            } else {
                id = this.resizeHandler.onResize(this.indexBits + 1, obj);
            }
        }

        return id;
    }

    private int addEntry(T obj) {
        int nextId = this.size;

        if (nextId >= this.entries.length) {
            this.resize(this.size);
        }

        this.table.put(obj, nextId);
        this.entries[nextId] = obj;

        this.size++;

        return nextId;
    }

    private void resize(int neededCapacity) {
        this.entries = Arrays.copyOf(this.entries, HashCommon.nextPowerOfTwo(neededCapacity + 1));
    }

    @Override
    public T get(int id) {
        T[] entries = this.entries;

        if (id >= 0 && id < entries.length) {
            return entries[id];
        }

        return null;
    }

    @Override
    public void readPacket(ByteBuffer buf) {
        this.clear();

        int entryCount = buf.getInt();

        for (int i = 0; i < entryCount; ++i) {
            this.addEntry(this.idList.get(buf.getInt()));
        }
    }

    @Override
    public void writePacket(ByteBuffer buf) {
        int size = this.size;
        buf.putInt(size);

        for (int i = 0; i < size; ++i) {
            buf.putInt(this.idList.getRawId(this.get(i)));
        }
    }

    @Override
    public int getPacketSize() {
        return 4 + size * 4;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Palette<T> copy() {
        return new LithiumHashPalette<>(this.idList, this.resizeHandler, this.indexBits, this.entries.clone(), new Reference2IntOpenHashMap<>(this.table), this.size);
    }

    private void clear() {
        Arrays.fill(this.entries, null);
        this.table.clear();
        this.size = 0;
    }

    public List<T> getElements() {
        ImmutableList.Builder<T> builder = new ImmutableList.Builder<>();
        for (T entry : this.entries) {
            if (entry != null) {
                builder.add(entry);
            }
        }
        return builder.build();
    }

    public static <A> Palette<A> create(int bits, IndexedIterable<A> idList, PaletteResizeListener<A> listener, List<A> list) {
        return new LithiumHashPalette<>(idList, bits, listener, list);
    }
}
