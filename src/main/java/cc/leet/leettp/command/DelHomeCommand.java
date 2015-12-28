package cc.leet.leettp.command;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.util.HomeManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class DelHomeCommand extends Command {

    private LeetTP plugin;
    private HomeManager homeManager;

    public DelHomeCommand(LeetTP plugin) {
        super("delhome", "Deletes a home", "/delhome [name]");
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.delhome") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        String name;

        // Default to "home" if nothing is specified.
        if(args.length < 1) {
            name = "home";
        } else {
            name = args[0];
        }

        if(name.isEmpty()) {
            sender.sendMessage(plugin.getMessages().homeNameMissing());
            return true;
        }

        if(homeManager.getHome(sender.getName(), name) == null) {
            sender.sendMessage(plugin.getMessages().homeNotExists());
            return true;
        }

        if(!homeManager.deleteHome(sender.getName(), name)) {
            sender.sendMessage(plugin.getMessages().homeNotDeleted());
        } else {
            sender.sendMessage(plugin.getMessages().homeDeleted());
        }

        return true;

    }

}