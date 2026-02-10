/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

final class WasiRights {
    public static final int FD_DATASYNC = WasiRights.bit(0);
    public static final int FD_READ = WasiRights.bit(1);
    public static final int FD_SEEK = WasiRights.bit(2);
    public static final int FD_FDSTAT_SET_FLAGS = WasiRights.bit(3);
    public static final int FD_SYNC = WasiRights.bit(4);
    public static final int FD_TELL = WasiRights.bit(5);
    public static final int FD_WRITE = WasiRights.bit(6);
    public static final int FD_ADVISE = WasiRights.bit(7);
    public static final int FD_ALLOCATE = WasiRights.bit(8);
    public static final int PATH_CREATE_DIRECTORY = WasiRights.bit(9);
    public static final int PATH_CREATE_FILE = WasiRights.bit(10);
    public static final int PATH_LINK_SOURCE = WasiRights.bit(11);
    public static final int PATH_LINK_TARGET = WasiRights.bit(12);
    public static final int PATH_OPEN = WasiRights.bit(13);
    public static final int FD_READDIR = WasiRights.bit(14);
    public static final int PATH_READLINK = WasiRights.bit(15);
    public static final int PATH_RENAME_SOURCE = WasiRights.bit(16);
    public static final int PATH_RENAME_TARGET = WasiRights.bit(17);
    public static final int PATH_FILESTAT_GET = WasiRights.bit(18);
    public static final int PATH_FILESTAT_SET_SIZE = WasiRights.bit(19);
    public static final int PATH_FILESTAT_SET_TIMES = WasiRights.bit(20);
    public static final int FD_FILESTAT_GET = WasiRights.bit(21);
    public static final int FD_FILESTAT_SET_SIZE = WasiRights.bit(22);
    public static final int FD_FILESTAT_SET_TIMES = WasiRights.bit(23);
    public static final int PATH_SYMLINK = WasiRights.bit(24);
    public static final int PATH_REMOVE_DIRECTORY = WasiRights.bit(25);
    public static final int PATH_UNLINK_FILE = WasiRights.bit(26);
    public static final int POLL_FD_READWRITE = WasiRights.bit(27);
    public static final int SOCK_SHUTDOWN = WasiRights.bit(28);
    public static final int SOCK_ACCEPT = WasiRights.bit(29);
    public static final int FILE_RIGHTS_BASE = FD_DATASYNC | FD_READ | FD_SEEK | FD_FDSTAT_SET_FLAGS | FD_SYNC | FD_TELL | FD_WRITE | FD_ADVISE | FD_ALLOCATE | FD_FILESTAT_GET | FD_FILESTAT_SET_SIZE | FD_FILESTAT_SET_TIMES | POLL_FD_READWRITE;
    public static final int DIRECTORY_RIGHTS_BASE = FD_DATASYNC | FD_FDSTAT_SET_FLAGS | FD_SYNC | PATH_CREATE_DIRECTORY | PATH_CREATE_FILE | PATH_LINK_SOURCE | PATH_LINK_TARGET | PATH_OPEN | FD_READDIR | PATH_READLINK | PATH_RENAME_SOURCE | PATH_RENAME_TARGET | PATH_FILESTAT_GET | PATH_FILESTAT_SET_SIZE | PATH_FILESTAT_SET_TIMES | FD_FILESTAT_GET | FD_FILESTAT_SET_TIMES | PATH_SYMLINK | PATH_REMOVE_DIRECTORY | PATH_UNLINK_FILE;

    private WasiRights() {
    }

    private static int bit(int n) {
        return 1 << n;
    }
}

