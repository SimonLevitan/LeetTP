package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Teleport;
import cc.leet.leettp.util.TeleportManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class TpAcceptCommand extends Command {

    private LeetTP plugin;
    private TeleportManager teleportManager;

    public TpAcceptCommand(LeetTP plugin) {
        super("tpaccept", "Accepts a teleportation request", "/tpaccept [name]");
        this.plugin = plugin;
        teleportManager = plugin.getTeleportManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.tpdeny") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        Map<String, Teleport> requests = teleportManager.getRequests(sender.getName());

        if(requests == null || requests.size() == 0) {
            sender.sendMessage(plugin.getMessages().tpNoRequest());
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(plugin.getMessages().tpNameMissing());
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        Teleport teleport;

        if(target == null) {
            // Player was not found, let's check if he was tried auto-completed.
            SortedMap<UUID, Player> players = new TreeMap<>(plugin.getServer().getOnlinePlayers());
            for(Map.Entry<UUID, Player> entry : players.entrySet()) {
                if(!entry.getValue().getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) continue;
                target = entry.getValue();
                break;
            }

            if(target == null) {
                sender.sendMessage(plugin.getMessages().tpNoRequest());
                return true;
            }

        }

        if(!requests.containsKey(target.getName())) {
            sender.sendMessage(plugin.getMessages().tpNotExists());
            return true;
        }

        teleport = requests.get(target.getName());

        Player senderPlayer = plugin.getServer().getPlayer(teleport.getSender());

        if(senderPlayer == null) {
            sender.sendMessage(plugin.getMessages().tpNotOnline());
            return true;
        }

        if(teleport.getType() == Teleport.TeleportType.TO) {

            senderPlayer.teleport(((Player) sender).getPosition());

            senderPlayer.sendMessage(plugin.getMessages().tpTpaSuccess(sender.getName()));
            sender.sendMessage(plugin.getMessages().tpTpaTargetSuccess(senderPlayer.getName()));

        } else {

            ((Player) sender).teleport(senderPlayer.getPosition());

            sender.sendMessage(plugin.getMessages().tpTpahereSuccess(senderPlayer.getName()));
            senderPlayer.sendMessage(plugin.getMessages().tpTpahereTargetSuccess(senderPlayer.getName()));

        }

        switch(teleportManager.removeRequest(sender.getName(), teleport.getSender(), teleport.getType())) {

            // No request by that player, shouldn't be possible to reach.
            case -2:
                break;

            // Provided data did not match the request.
            case -1:
                plugin.getLogger().alert("Data for teleportation did not match! Dumping data.");
                plugin.getLogger().alert("Target: " + teleport.getTarget());
                plugin.getLogger().alert("Sender: " + teleport.getSender());
                plugin.getLogger().alert("Type: " + teleport.getType().toString());
                break;

            // No error, but entry still exists.
            case 0:
                plugin.getLogger().alert("Failed to remove request from TeleportManager, dumping data.");
                plugin.getLogger().alert("Target: " + teleport.getTarget());
                plugin.getLogger().alert("Sender: " + teleport.getSender());
                plugin.getLogger().alert("Type: " + teleport.getType().toString());
                break;

        }

        return true;

    }

}