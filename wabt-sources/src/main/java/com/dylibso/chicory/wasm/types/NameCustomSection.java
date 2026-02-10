/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import com.dylibso.chicory.wasm.Encoding;
import com.dylibso.chicory.wasm.types.CustomSection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;

public final class NameCustomSection
extends CustomSection {
    private final Optional<String> moduleName;
    private final List<NameEntry> funcNames;
    private final List<ListEntry<NameEntry>> localNames;
    private final List<ListEntry<NameEntry>> labelNames;
    private final List<NameEntry> tableNames;
    private final List<NameEntry> memoryNames;
    private final List<NameEntry> globalNames;
    private final List<NameEntry> elementNames;
    private final List<NameEntry> dataNames;
    private final List<NameEntry> tagNames;

    private NameCustomSection(Optional<String> moduleName, List<NameEntry> funcNames, List<ListEntry<NameEntry>> localNames, List<ListEntry<NameEntry>> labelNames, List<NameEntry> tableNames, List<NameEntry> memoryNames, List<NameEntry> globalNames, List<NameEntry> elementNames, List<NameEntry> dataNames, List<NameEntry> tagNames) {
        this.moduleName = Objects.requireNonNull(moduleName);
        this.funcNames = List.copyOf((Collection)Objects.requireNonNull(funcNames));
        this.localNames = List.copyOf((Collection)Objects.requireNonNull(localNames));
        this.labelNames = List.copyOf((Collection)Objects.requireNonNull(labelNames));
        this.tableNames = List.copyOf((Collection)Objects.requireNonNull(tableNames));
        this.memoryNames = List.copyOf((Collection)Objects.requireNonNull(memoryNames));
        this.globalNames = List.copyOf((Collection)Objects.requireNonNull(globalNames));
        this.elementNames = List.copyOf((Collection)Objects.requireNonNull(elementNames));
        this.dataNames = List.copyOf((Collection)Objects.requireNonNull(dataNames));
        this.tagNames = List.copyOf((Collection)Objects.requireNonNull(tagNames));
    }

    public static NameCustomSection parse(byte[] bytes) {
        String moduleName = null;
        ArrayList<NameEntry> funcNames = new ArrayList<NameEntry>();
        ArrayList<ListEntry<NameEntry>> localNames = new ArrayList<ListEntry<NameEntry>>();
        ArrayList<ListEntry<NameEntry>> labelNames = new ArrayList<ListEntry<NameEntry>>();
        ArrayList<NameEntry> tableNames = new ArrayList<NameEntry>();
        ArrayList<NameEntry> memoryNames = new ArrayList<NameEntry>();
        ArrayList<NameEntry> globalNames = new ArrayList<NameEntry>();
        ArrayList<NameEntry> elementNames = new ArrayList<NameEntry>();
        ArrayList<NameEntry> dataNames = new ArrayList<NameEntry>();
        ArrayList<NameEntry> tagNames = new ArrayList<NameEntry>();
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        while (buf.hasRemaining()) {
            byte id = buf.get();
            ByteBuffer slice = NameCustomSection.slice(buf, (int)Encoding.readVarUInt32(buf));
            switch (id) {
                case 0: {
                    assert (moduleName == null);
                    moduleName = Encoding.readName(slice);
                    break;
                }
                case 1: {
                    NameCustomSection.oneLevelParse(slice, funcNames);
                    break;
                }
                case 2: {
                    NameCustomSection.twoLevelParse(slice, localNames);
                    break;
                }
                case 3: {
                    NameCustomSection.twoLevelParse(slice, labelNames);
                    break;
                }
                case 5: {
                    NameCustomSection.oneLevelParse(slice, tableNames);
                    break;
                }
                case 6: {
                    NameCustomSection.oneLevelParse(slice, memoryNames);
                    break;
                }
                case 7: {
                    NameCustomSection.oneLevelParse(slice, globalNames);
                    break;
                }
                case 8: {
                    NameCustomSection.oneLevelParse(slice, elementNames);
                    break;
                }
                case 9: {
                    NameCustomSection.oneLevelParse(slice, dataNames);
                    break;
                }
                case 11: {
                    NameCustomSection.oneLevelParse(slice, tagNames);
                    break;
                }
            }
        }
        return new NameCustomSection(Optional.ofNullable(moduleName), funcNames, localNames, labelNames, tableNames, memoryNames, globalNames, elementNames, dataNames, tagNames);
    }

    @Override
    public String name() {
        return "name";
    }

    public Optional<String> moduleName() {
        return this.moduleName;
    }

    public String nameOfFunction(int functionIdx) {
        return NameCustomSection.oneLevelSearch(this.funcNames, functionIdx);
    }

    public int functionNameCount() {
        return this.funcNames.size();
    }

    public String nameOfLocal(int functionIdx, int localIdx) {
        return NameCustomSection.twoLevelSearch(this.localNames, functionIdx, localIdx);
    }

    public String nameOfLabel(int functionIdx, int labelIdx) {
        return NameCustomSection.twoLevelSearch(this.labelNames, functionIdx, labelIdx);
    }

    public String nameOfTable(int tableIdx) {
        return NameCustomSection.oneLevelSearch(this.tableNames, tableIdx);
    }

    public String nameOfMemory(int memoryIdx) {
        return NameCustomSection.oneLevelSearch(this.memoryNames, memoryIdx);
    }

    public String nameOfGlobal(int globalIdx) {
        return NameCustomSection.oneLevelSearch(this.globalNames, globalIdx);
    }

    public String nameOfElement(int elementIdx) {
        return NameCustomSection.oneLevelSearch(this.elementNames, elementIdx);
    }

    public String nameOfData(int dataIdx) {
        return NameCustomSection.oneLevelSearch(this.dataNames, dataIdx);
    }

    public String nameOfTag(int tagIdx) {
        return NameCustomSection.oneLevelSearch(this.tagNames, tagIdx);
    }

    private static void oneLevelParse(ByteBuffer slice, List<NameEntry> list) {
        int cnt = (int)Encoding.readVarUInt32(slice);
        for (int i = 0; i < cnt; ++i) {
            NameCustomSection.oneLevelStore(list, (int)Encoding.readVarUInt32(slice), Encoding.readName(slice));
        }
    }

    private static void twoLevelParse(ByteBuffer slice, List<ListEntry<NameEntry>> list) {
        int listCnt = (int)Encoding.readVarUInt32(slice);
        for (int i = 0; i < listCnt; ++i) {
            int groupIdx = (int)Encoding.readVarUInt32(slice);
            int cnt = (int)Encoding.readVarUInt32(slice);
            for (int j = 0; j < cnt; ++j) {
                NameCustomSection.twoLevelStore(list, groupIdx, (int)Encoding.readVarUInt32(slice), Encoding.readName(slice));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ByteBuffer slice(ByteBuffer buf, int size) {
        int pos = buf.position();
        int lim = buf.limit();
        try {
            buf.limit(pos + size);
            ByteBuffer byteBuffer = buf.slice();
            return byteBuffer;
        }
        finally {
            buf.limit(lim);
            buf.position(pos + size);
        }
    }

    private static String oneLevelSearch(List<NameEntry> list, int searchIdx) {
        int idx = NameCustomSection.binarySearch(list, searchIdx, NameEntry::index);
        return idx < 0 ? null : list.get(idx).name();
    }

    private static String twoLevelSearch(List<ListEntry<NameEntry>> listList, int groupIdx, int subIdx) {
        int fi = NameCustomSection.binarySearch(listList, groupIdx, ListEntry::index);
        if (fi < 0) {
            return null;
        }
        ListEntry<NameEntry> subList = listList.get(fi);
        int li = NameCustomSection.binarySearch(subList, subIdx, NameEntry::index);
        return li < 0 ? null : ((NameEntry)subList.get((int)li)).name;
    }

    private static String oneLevelStore(List<NameEntry> list, int storeIdx, String name) {
        Objects.requireNonNull(name);
        int idx = NameCustomSection.binarySearch(list, storeIdx, NameEntry::index);
        if (idx < 0) {
            list.add(-idx - 1, new NameEntry(storeIdx, name));
            return null;
        }
        return list.set(idx, new NameEntry(storeIdx, name)).name();
    }

    private static String twoLevelStore(List<ListEntry<NameEntry>> listList, int groupIdx, int subIdx, String name) {
        ListEntry<Object> subList;
        Objects.requireNonNull(name);
        int fi = NameCustomSection.binarySearch(listList, groupIdx, ListEntry::index);
        if (fi < 0) {
            subList = new ListEntry(groupIdx);
            listList.add(-fi - 1, subList);
        } else {
            subList = listList.get(fi);
        }
        int li = NameCustomSection.binarySearch(subList, subIdx, NameEntry::index);
        if (li < 0) {
            subList.add(-li - 1, new NameEntry(subIdx, name));
            return null;
        }
        return subList.set(li, new NameEntry(subIdx, name)).name();
    }

    private static <T> int binarySearch(List<T> list, int idx, ToIntFunction<T> indexExtractor) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = low + high >>> 1;
            int cmp = Integer.compare(indexExtractor.applyAsInt(list.get(mid)), idx);
            if (cmp < 0) {
                low = mid + 1;
                continue;
            }
            if (cmp > 0) {
                high = mid - 1;
                continue;
            }
            return mid;
        }
        return -low - 1;
    }

    static final class NameEntry {
        private final int index;
        private final String name;

        NameEntry(int index, String name) {
            this.index = index;
            this.name = name;
        }

        int index() {
            return this.index;
        }

        String name() {
            return this.name;
        }

        public String toString() {
            return "[" + this.index + "] -> " + this.name;
        }
    }

    static final class ListEntry<T>
    extends ArrayList<T> {
        private final int index;

        ListEntry(int index) {
            this.index = index;
        }

        int index() {
            return this.index;
        }

        @Override
        public String toString() {
            return "[" + this.index + "] -> " + super.toString();
        }
    }
}

