import org.bukkit.plugin.java.JavaPlugin;

public class DataLockLib extends JavaPlugin {

    public static DataLockLib instance;

    protected static DataLockLib getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        registerEvents();
    }

    @Override
    public void onDisable() {

    }

    private void registerEvents() {
//        getServer().getPluginManager().registerEvents(new TalkToQuest(), this);
//        getServer().getMessenger().registerOutgoingPluginChannel(this, "aquest:player-data");
//        getServer().getMessenger().registerIncomingPluginChannel(this, "aquest:player-data", new PluginMessageListener());
    }

}
