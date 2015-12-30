package cc.leet.leettp.command.spawn;

import cc.leet.leettp.LeetTP;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class SetSpawnCommand extends Command {

    private LeetTP plugin;

    public SetSpawnCommand(LeetTP plugin) {
        super("setspawn", "Sets the spawn location in the current world", "/setspawn");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.setspawn") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        ((Player) sender).getLevel().setSpawnLocation(((Player) sender).getPosition());

        sender.sendMessage(plugin.getMessages().spawnSet());

        return true;
    }
}