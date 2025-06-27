package com.Lino.fakePlayers.command;

import com.Lino.fakePlayers.FakePlayers;
import com.Lino.fakePlayers.entity.SimpleFakePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.stream.Collectors;

public class FakePlayerCommand implements CommandExecutor, TabCompleter {
    private final FakePlayers plugin;

    public FakePlayerCommand(FakePlayers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("fakeplayers.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                handleAdd(sender, args);
                break;
            case "remove":
                handleRemove(sender, args);
                break;
            case "list":
                handleList(sender);
                break;
            case "clear":
                handleClear(sender);
                break;
            case "random":
                handleRandom(sender, args);
                break;
            case "setcount":
                handleSetCount(sender, args);
                break;
            case "chat":
                handleChat(sender, args);
                break;
            case "reload":
                handleReload(sender);
                break;
            default:
                sendHelp(sender);
        }

        return true;
    }

    private void handleAdd(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /fakeplayer add <name>");
            return;
        }

        String name = args[1];

        if (name.length() > 16) {
            sender.sendMessage("§cName must be 16 characters or less!");
            return;
        }

        SimpleFakePlayer fakePlayer = plugin.getFakePlayerManager().createFakePlayer(name);

        if (fakePlayer != null) {
            sender.sendMessage("§aFake player " + name + " has been added!");
        } else {
            sender.sendMessage("§cFake player " + name + " already exists or name is invalid!");
        }
    }

    private void handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /fakeplayer remove <name>");
            return;
        }

        String name = args[1];
        plugin.getFakePlayerManager().removeFakePlayer(name);
        sender.sendMessage("§aFake player " + name + " has been removed!");
    }

    private void handleList(CommandSender sender) {
        Collection<SimpleFakePlayer> fakePlayers = plugin.getFakePlayerManager().getAllFakePlayers();

        if (fakePlayers.isEmpty()) {
            sender.sendMessage("§cNo fake players online!");
            return;
        }

        sender.sendMessage("§aFake players online: §f" + fakePlayers.size());
        sender.sendMessage("§aMax allowed: §f" + plugin.getConfig().getInt("fake-players.max-count", 100));

        String names = fakePlayers.stream()
                .map(SimpleFakePlayer::getName)
                .sorted()
                .collect(Collectors.joining(", "));
        sender.sendMessage("§7" + names);
    }

    private void handleClear(CommandSender sender) {
        int count = plugin.getFakePlayerManager().getFakePlayerCount();
        plugin.getFakePlayerManager().removeAllFakePlayers();
        sender.sendMessage("§aRemoved " + count + " fake players!");
    }

    private void handleRandom(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /fakeplayer random <count>");
            return;
        }

        try {
            int count = Integer.parseInt(args[1]);
            int maxCount = plugin.getConfig().getInt("fake-players.max-count", 100);
            int currentCount = plugin.getFakePlayerManager().getFakePlayerCount();

            if (count < 1) {
                sender.sendMessage("§cCount must be at least 1!");
                return;
            }

            if (currentCount + count > maxCount) {
                sender.sendMessage("§cThis would exceed the maximum of " + maxCount + " fake players!");
                sender.sendMessage("§cCurrent: " + currentCount + ", Requested: " + count);
                return;
            }

            plugin.getFakePlayerManager().createRandomFakePlayers(count);
            sender.sendMessage("§aCreating " + count + " random fake players!");
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number!");
        }
    }

    private void handleSetCount(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /fakeplayer setcount <count>");
            return;
        }

        try {
            int count = Integer.parseInt(args[1]);
            if (count < 0 || count > 1000) {
                sender.sendMessage("§cCount must be between 0 and 1000!");
                return;
            }

            plugin.getServerListManager().setAdditionalPlayers(count);
            plugin.getConfig().set("server-list.additional-players", count);
            plugin.saveConfig();

            sender.sendMessage("§aServer list will show " + count + " additional players!");
        } catch (NumberFormatException e) {
            sender.sendMessage("§cInvalid number!");
        }
    }

    private void handleChat(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cUsage: /fakeplayer chat <name> <message>");
            return;
        }

        String name = args[1];
        SimpleFakePlayer fakePlayer = plugin.getFakePlayerManager().getFakePlayer(name);

        if (fakePlayer == null) {
            sender.sendMessage("§cFake player " + name + " not found!");
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        fakePlayer.sendMessage(message);
        sender.sendMessage("§aMessage sent as " + name + "!");
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadAllConfigs();
        sender.sendMessage("§aAll configurations reloaded!");
        sender.sendMessage("§7Note: Some changes may require a restart to take full effect.");
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6FakePlayers Commands:");
        sender.sendMessage("§e/fakeplayer add <name> §7- Add a fake player");
        sender.sendMessage("§e/fakeplayer remove <name> §7- Remove a fake player");
        sender.sendMessage("§e/fakeplayer list §7- List all fake players");
        sender.sendMessage("§e/fakeplayer clear §7- Remove all fake players");
        sender.sendMessage("§e/fakeplayer random <count> §7- Add random fake players");
        sender.sendMessage("§e/fakeplayer setcount <count> §7- Set additional server list count");
        sender.sendMessage("§e/fakeplayer chat <name> <message> §7- Send chat as fake player");
        sender.sendMessage("§e/fakeplayer reload §7- Reload all configuration files");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("fakeplayers.admin")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Arrays.asList("add", "remove", "list", "clear", "random", "setcount", "chat", "reload")
                    .stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("chat"))) {
            return plugin.getFakePlayerManager().getAllFakePlayers()
                    .stream()
                    .map(SimpleFakePlayer::getName)
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}