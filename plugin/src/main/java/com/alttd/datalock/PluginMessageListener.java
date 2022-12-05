package com.alttd.datalock;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;

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
        String subChannel = in.readUTF();
        boolean result = in.readBoolean();
        String data = in.readUTF();
        UUID idempotency = UUID.fromString(in.readUTF());
        IdempotencyData idempotencyData = new IdempotencyData(channel, data, idempotency);
        DataLockLib.getInstance().getLogger().log(Level.INFO, "Received plugin message on [" + subChannel + "] about data: [" + data + "]");
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (subChannel) {
                    case "try-lock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.TRY_LOCK_RESULT, data, result).callEvent();
                    }
                    case "queue-lock-failed" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.QUEUE_LOCK_FAILED, data, result).callEvent();
                    }
                    case "try-unlock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_UNLOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_UNLOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.TRY_UNLOCK_RESULT, data, result).callEvent();
                    }
                    case "locked-queue-lock" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.TRY_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.LOCKED_QUEUE_LOCK, data, result).callEvent();
                    }
                    case "check-lock-result" -> {
                        if (!alreadyReceived.putIdempotencyData(RequestType.CHECK_LOCK, idempotencyData))
                            return;
                        DataLock.getInstance().removeActiveRequest(RequestType.CHECK_LOCK, idempotencyData);
                        new LockResponseEvent(true, channel, ResponseType.CHECK_LOCK_RESULT, data, result).callEvent();
                    }
                }
            }
        }.runTaskAsynchronously(DataLockLib.getInstance());
    }
}
