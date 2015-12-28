package cc.leet.leettp.command;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Home;
import cc.leet.leettp.util.HomeManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.HashMap;

public class HomeCommand extends Command {

    private LeetTP plugin;
    private HashMap<String, Long> cooldown;
    private HomeManager homeManager;

    public HomeCommand(LeetTP plugin) {
        super("home", "Teleport to home", "/home [name]");
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
        cooldown = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.home") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(cooldown.containsKey(sender.getName())) {
            long time = System.currentTimeMillis();
            if(time < cooldown.get(sender.getName())) {
                sender.sendMessage(plugin.getMessages().cooldownWait((int)(cooldown.get(sender.getName()) - time) / 1000));
                return true;
            }
        }

        Home home;
        // Default to "home" if nothing is specified.
        if(args.length < 1 && homeManager.getHome(sender.getName(), "home") != null) {
            home = homeManager.getHome(sender.getName(), "home");
        } else {
            if(homeManager.getHome(sender.getName(), args[0]) == null) {
                sender.sendMessage(plugin.getMessages().homeNotExists());
                return true;
            }
            home = homeManager.getHome(sender.getName(), args[0]);
        }

        if(home == null) return false;

        // Check if level is loaded.
        if(!plugin.getServer().isLevelLoaded(home.getWorld())) {
            sender.sendMessage(plugin.getMessages().worldNotLoaded());
            return true;
        }

        ((Player) sender).teleport(home.getLocation());

        sender.sendMessage(plugin.getMessages().homeTeleported(home.getName()));

        // Attach cooldown for last used /home.
        cooldown.put(sender.getName(), System.currentTimeMillis());

        return true;

    }
}