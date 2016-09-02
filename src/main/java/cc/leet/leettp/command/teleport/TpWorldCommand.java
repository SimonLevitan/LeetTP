package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpWorldCommand extends Command {

    private LeetTP plugin;

    public TpWorldCommand(LeetTP plugin) {
        super("tpworld", "Teleport to target world", "/tpworld [name]");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.tpworld") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(plugin.getMessages().tpWorldNameMissing());
            return true;
        }

        String world = args[0];

        // Check if level is loaded.
        if(!plugin.getServer().isLevelLoaded(world)) {
            // Check if we should load the world.
            if(!plugin.loadWorlds) {
                sender.sendMessage(plugin.getMessages().worldNotLoaded());
                return true;
            } else {
                // Attempt to load the world.
                if(!plugin.getServer().loadLevel(world)) {
                    sender.sendMessage(plugin.getMessages().worldNotLoaded());
                    return true;
                }
            }
        }

        ((Player) sender).teleport(plugin.getServer().getLevelByName(world).getSpawnLocation().getLocation());

        sender.sendMessage(plugin.getMessages().tpWorldSuccess(world));

        return true;

    }
}