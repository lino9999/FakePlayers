package com.Lino.fakePlayers;

import org.bukkit.plugin.java.JavaPlugin;
import com.Lino.fakePlayers.manager.FakePlayerManager;
import com.Lino.fakePlayers.manager.ConversationManager;
import com.Lino.fakePlayers.manager.ServerListManager;
import com.Lino.fakePlayers.command.FakePlayerCommand;
import com.Lino.fakePlayers.listener.ServerListListener;
import com.Lino.fakePlayers.listener.PlayerJoinListener;

public class FakePlayers extends JavaPlugin {
    private static FakePlayers instance;
    private FakePlayerManager fakePlayerManager;
    private ConversationManager conversationManager;
    private ServerListManager serverListManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        fakePlayerManager = new FakePlayerManager(this);
        conversationManager = new ConversationManager(this);
        serverListManager = new ServerListManager(this);

        getCommand("fakeplayer").setExecutor(new FakePlayerCommand(this));

        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        conversationManager.startConversations();
    }

    @Override
    public void onDisable() {
        if (conversationManager != null) {
            conversationManager.stopConversations();
        }
        if (fakePlayerManager != null) {
            fakePlayerManager.removeAllFakePlayers();
        }
    }

    public static FakePlayers getInstance() {
        return instance;
    }

    public FakePlayerManager getFakePlayerManager() {
        return fakePlayerManager;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public ServerListManager getServerListManager() {
        return serverListManager;
    }
}