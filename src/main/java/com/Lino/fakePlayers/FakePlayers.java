package com.Lino.fakePlayers;

import org.bukkit.plugin.java.JavaPlugin;
import com.Lino.fakePlayers.manager.SimpleFakePlayerManager;
import com.Lino.fakePlayers.manager.ConversationManager;
import com.Lino.fakePlayers.manager.ServerListManager;
import com.Lino.fakePlayers.command.FakePlayerCommand;
import com.Lino.fakePlayers.listener.ServerListListener;
import com.Lino.fakePlayers.listener.PlayerJoinListener;
import com.Lino.fakePlayers.config.MessagesConfig;
import com.Lino.fakePlayers.config.NamesConfig;

public class FakePlayers extends JavaPlugin {
    private static FakePlayers instance;
    private SimpleFakePlayerManager fakePlayerManager;
    private ConversationManager conversationManager;
    private ServerListManager serverListManager;
    private MessagesConfig messagesConfig;
    private NamesConfig namesConfig;

    @Override
    public void onEnable() {
        instance = this;

        // Save default configs
        saveDefaultConfig();

        // Initialize configs
        messagesConfig = new MessagesConfig(this);
        namesConfig = new NamesConfig(this);

        // Load configs
        messagesConfig.loadConfig();
        namesConfig.loadConfig();

        // Initialize managers
        fakePlayerManager = new SimpleFakePlayerManager(this);
        conversationManager = new ConversationManager(this);
        serverListManager = new ServerListManager(this);

        // Register command
        getCommand("fakeplayer").setExecutor(new FakePlayerCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Start conversation system
        conversationManager.startConversations();

        // Auto-spawn if enabled
        if (getConfig().getBoolean("auto-spawn.enabled", false) &&
                getConfig().getBoolean("auto-spawn.on-startup", true)) {
            int count = getConfig().getInt("auto-spawn.count", 10);
            getServer().getScheduler().runTaskLater(this, () -> {
                fakePlayerManager.createRandomFakePlayers(count);
            }, 100L); // 5 seconds after startup
        }

        getLogger().info("FakePlayers v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (conversationManager != null) {
            conversationManager.stopConversations();
        }
        if (fakePlayerManager != null) {
            fakePlayerManager.removeAllFakePlayers();
        }

        getLogger().info("FakePlayers disabled!");
    }

    public static FakePlayers getInstance() {
        return instance;
    }

    public SimpleFakePlayerManager getFakePlayerManager() {
        return fakePlayerManager;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public ServerListManager getServerListManager() {
        return serverListManager;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public NamesConfig getNamesConfig() {
        return namesConfig;
    }

    public void reloadAllConfigs() {
        reloadConfig();
        messagesConfig.loadConfig();
        namesConfig.loadConfig();
    }
}