package com.alttd.datalock;

public enum ResponseType {
    TRY_LOCK_RESULT,
    QUEUE_LOCK_FAILED,
    TRY_UNLOCK_RESULT,
    LOCKED_QUEUE_LOCK,
    CHECK_LOCK_RESULT
}
