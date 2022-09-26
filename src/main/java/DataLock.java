import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class DataLock implements DataLockInterface {

    private static DataLock instance = null;

    public static DataLock getInstance() {
        if (instance == null)
            instance = new DataLock();
        return instance;
    }

    private DataLock() {}

    private final HashMap<UUID, IdempotencyData> activeQueries = new HashMap<>();

    protected void putQuery(IdempotencyData idempotencyData) {
        activeQueries.put(idempotencyData.idempotencyToken(), idempotencyData);
    }

    protected IdempotencyData getQuery(UUID idempotencyToken) {
        return activeQueries.getOrDefault(idempotencyToken, null);
    }

    protected IdempotencyData removeQuery(UUID idempotencyToken) {
        return activeQueries.remove(idempotencyToken);
    }

    private final HashSet<String> activeChannels = new HashSet<>();

    public void registerChannel(String channel) {

    }

    @Override
    public void tryLock(String channel, String data) {
        IdempotencyData idempotencyData = new IdempotencyData(channel, data, UUID.randomUUID());

    }

    @Override
    public void tryUnlock(String channel, String data) {

    }
}
