package com.alttd.datalock;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    private final DataLock dataLock;
    private final Idempotency alreadyReceived;
    PluginMessageListener() {
        this.dataLock = DataLock.getInstance();
        this.alreadyReceived = new Idempotency();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        if (!dataLock.isActiveChannel(channel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String data = in.readUTF();
        boolean result = in.readBoolean();
        UUID idempotency = UUID.fromString(in.readUTF());
        IdempotencyData idempotencyData = new IdempotencyData(channel, data, idempotency);
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (in.readUTF()) {
                    case "try-lock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.TRY_LOCK_RESULT, data, result);
                    }
                    case "queue-lock-failed" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.QUEUE_LOCK_FAILED, data, result);
                    }
                    case "try-unlock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_UNLOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_UNLOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.TRY_UNLOCK_RESULT, data, result);
                    }
                    case "locked-queue-lock" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.LOCKED_QUEUE_LOCK, data, result);
                    }
                    case "check-lock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.CHECK_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.CHECK_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.CHECK_LOCK_RESULT, data, result);
                    }
                }
            }
        }.runTaskAsynchronously(DataLockLib.getInstance());
    }
}
