package com.alttd;

import org.bukkit.plugin.java.JavaPlugin;

public class DataLockLib extends JavaPlugin {

    private static DataLockLib instance;
    private DataLock dataLock;

    protected static DataLockLib getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        dataLock = new DataLock();
    }

    @Override
    public void onDisable() {

    }

}
