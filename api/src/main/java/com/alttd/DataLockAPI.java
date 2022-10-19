package com.alttd;

import org.jetbrains.annotations.ApiStatus;

public interface DataLockAPI {

    static DataLockAPI get() {
        return Provider.instance;
    }

    final class Provider {
        private static DataLockAPI instance = null;

        @ApiStatus.Internal
        static void register(DataLockAPI instance) {
            if (Provider.instance != null)
                throw new UnsupportedOperationException("Cannot redefine singleton");

            Provider.instance = instance;
        }
    }

    void tryLock(String channel, String data);

    void tryUnlock(String channel, String data);

    void registerChannel(String channel);

    boolean isActiveChannel(String channel);

}
