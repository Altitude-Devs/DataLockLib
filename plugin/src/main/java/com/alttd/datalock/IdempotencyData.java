package com.alttd.datalock;

import java.util.UUID;

record IdempotencyData(String channel, String data, UUID idempotencyToken) {
    @Override
    public String toString() {
        return "Channel: [" + channel + "] Data: [" + data + "] Idempotency Token: [" + idempotencyToken + "]";
    }

    @Override
    public String channel() {
        return channel;
    }

    @Override
    public String data() {
        return data;
    }

    @Override
    public UUID idempotencyToken() {
        return idempotencyToken;
    }
}