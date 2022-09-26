public interface DataLockInterface {

    void tryLock(String channel, String data);

    void tryUnlock(String channel, String data);

    void registerChannel(String channel);

    boolean isActiveChannel(String channel);

}
