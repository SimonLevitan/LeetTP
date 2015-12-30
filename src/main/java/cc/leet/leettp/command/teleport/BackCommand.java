package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class BackCommand extends Command {

    private LeetTP plugin;

    public BackCommand(LeetTP plugin) {
        super("back", "Teleports to you last died", "/back");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.back") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(!plugin.deaths.containsKey(sender.getName())) {
            sender.sendMessage(plugin.getMessages().backEmpty());
            return true;
        }

        ((Player) sender).teleport(plugin.deaths.get(sender.getName()));
        plugin.deaths.remove(sender.getName());

        sender.sendMessage(plugin.getMessages().backTeleported());

        return true;
    }
}