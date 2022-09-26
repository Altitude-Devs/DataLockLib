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
    }

    @Override
    public void onDisable() {

    }

}
