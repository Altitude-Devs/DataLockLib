package com.alttd.datalock;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LockResponseEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final String channel;
    private final ResponseType responseType;
    private final String data;
    private final boolean result;

    protected LockResponseEvent(boolean isAsync, String channel, ResponseType responseType, String data, boolean result) {
        super(isAsync);
        this.channel = channel;
        this.responseType = responseType;
        this.data = data;
        this.result = result;
    }

    public String getChannel() {
        return channel;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getData() {
        return data;
    }

    public boolean getResult() {
        return result;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
