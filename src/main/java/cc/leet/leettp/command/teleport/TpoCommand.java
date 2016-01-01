package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TpoCommand extends Command {

    private LeetTP plugin;

    public TpoCommand(LeetTP plugin) {
        super("tpo", "Teleports to a player, overriding their status", "/tpo [player]");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.tpo") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(plugin.getMessages().tpNameMissing());
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if(target == null) {
            // Player was not found, let's check if he was tried auto-completed.
            SortedMap<String, Player> players = new TreeMap<>(plugin.getServer().getOnlinePlayers());
            for(Map.Entry<String, Player> entry : players.entrySet()) {
                if(!entry.getValue().getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) continue;
                target = entry.getValue();
                break;
            }

            if(target == null) {
                sender.sendMessage(TextFormat.RED + "Player not found. The player may be offline!");
                return true;
            }
        }

        ((Player) sender).teleport(target.getPosition());
        sender.sendMessage(plugin.getMessages().tpTpaSuccess(target.getName()));

        return true;

    }
}