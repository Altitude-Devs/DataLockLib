import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        if (!activeChannels.contains(channel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String data = in.readUTF();
        boolean result = in.readBoolean();
        UUID idempotency = UUID.fromString(in.readUTF());
        new BukkitRunnable() {
            @Override
            public void run() {
                switch (in.readUTF()) {
                    case "try-lock-result" -> new LockResponseEvent(true, channel, ResponseType.TRY_LOCK_RESULT, data, result);
                    case "queue-lock-failed" -> new LockResponseEvent(true, channel, ResponseType.QUEUE_LOCK_FAILED, data, result);
                    case "try-unlock-result" -> new LockResponseEvent(true, channel, ResponseType.TRY_UNLOCK_RESULT, data, result);
                    case "locked-queue-lock" -> new LockResponseEvent(true, channel, ResponseType.LOCKED_QUEUE_LOCK, data, result);
                    case "check-lock-result" -> new LockResponseEvent(true, channel, ResponseType.CHECK_LOCK_RESULT, data, result);
                }
            }
        }.runTaskAsynchronously(DataLockLib.getInstance());
    }
}
