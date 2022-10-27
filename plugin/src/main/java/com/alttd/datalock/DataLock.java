package com.alttd.datalock;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class DataLock implements DataLockAPI {

    private static DataLock instance = null;

    public static DataLock getInstance() {
        if (instance == null)
            new DataLock();
        return instance;
    }

    private final PluginMessageListener pluginMessageListener;
    private final DataLockLib plugin;
    private final Idempotency activeRequests;
    private DataLock() {
        instance = this;
        DataLockAPI.Provider.register(this);
        pluginMessageListener = new PluginMessageListener();
        plugin = DataLockLib.getInstance();
        activeRequests = new Idempotency();
        new RepeatRequest().runTaskTimerAsynchronously(plugin, 15 * 20, 15 * 20);
        //Run repeat request task every 15 seconds
    }

    private void sendPluginMessage(RequestType requestType, IdempotencyData idempotencyData) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(requestType.subChannel);
        out.writeUTF(idempotencyData.data());
        plugin.getServer().sendPluginMessage(plugin, idempotencyData.channel(), out.toByteArray());
    }

    private final HashSet<String> activeChannels = new HashSet<>();

    protected boolean removeActiveRequest(RequestType requestType, IdempotencyData idempotencyData) {
        return activeRequests.removeIdempotencyData(requestType, idempotencyData);
    }

    @Override
    public synchronized void registerChannel(String channel) {
        activeChannels.add(channel);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, pluginMessageListener);
    }

    @Override
    public synchronized boolean isActiveChannel(String channel) {
        return activeChannels.contains(channel);
    }

    @Override
    public void tryLock(String channel, String data) {
        IdempotencyData idempotencyData = new IdempotencyData(channel, data, UUID.randomUUID());
        activeRequests.putIdempotencyData(RequestType.TRY_LOCK, idempotencyData);
        sendPluginMessage(RequestType.TRY_LOCK, idempotencyData);
    }

    @Override
    public void tryUnlock(String channel, String data) {
        IdempotencyData idempotencyData = new IdempotencyData(channel, data, UUID.randomUUID());
        activeRequests.putIdempotencyData(RequestType.TRY_UNLOCK, idempotencyData);
        sendPluginMessage(RequestType.TRY_UNLOCK, idempotencyData);
    }

    private class RepeatRequest extends BukkitRunnable {
        @Override
        public void run() {
            for (RequestType requestType : RequestType.values()) {
                for (IdempotencyData next : activeRequests.getIdempotencyData(requestType)) {
                    sendPluginMessage(requestType, next);
                }
            }
        }
    }
}
