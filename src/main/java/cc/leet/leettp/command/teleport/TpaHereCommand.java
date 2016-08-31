package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Teleport;
import cc.leet.leettp.util.TeleportManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class TpaHereCommand extends Command {

    private LeetTP plugin;
    private TeleportManager teleportManager;

    public TpaHereCommand(LeetTP plugin) {
        super("tpahere", "Asks a player to teleport to you", "/tpahere [player]", new String[]{"tphere"});
        this.plugin = plugin;
        teleportManager = plugin.getTeleportManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.tpahere") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(teleportManager.getCooldown(sender.getName()) != null && !sender.isOp()) {
            long time = System.currentTimeMillis() - teleportManager.getCooldown(sender.getName());
            if(time < teleportManager.getCooldownTime()) {
                sender.sendMessage(plugin.getMessages().cooldownWait((int)(teleportManager.getCooldownTime() - time) / 1000));
                return true;
            }
        }

        if(args.length < 1) {
            sender.sendMessage(plugin.getMessages().tpNameMissing());
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if(target == null) {
            // Player was not found, let's check if he was tried auto-completed.
            SortedMap<UUID, Player> players = new TreeMap<>(plugin.getServer().getOnlinePlayers());
            for(Map.Entry<UUID, Player> entry : players.entrySet()) {
                if(!entry.getValue().getName().substring(0, args[0].length()).equalsIgnoreCase(args[0])) continue;
                target = entry.getValue();
                break;
            }

            if(target == null) {
                sender.sendMessage(TextFormat.RED + "Player was not found, the player may be offline.");
                return true;
            }
        }

        Map<String, Teleport> requests = teleportManager.getRequests(target.getName());

        if(requests != null) {

            for(Map.Entry<String, Teleport> entry : requests.entrySet()) {

                if(sender.getName().equalsIgnoreCase(entry.getKey())) {
                    sender.sendMessage(plugin.getMessages().tpRequestExists());
                    return true;
                }

            }

        }

        if(!teleportManager.addRequest(target.getName(), sender.getName(), Teleport.TeleportType.HERE)) {
            plugin.getLogger().alert("Failed to create a tpahere request to " + target.getName() + " from " + sender.getName());
            return true;
        }

        sender.sendMessage(plugin.getMessages().tpRequestSent(target.getName()));
        target.sendMessage(plugin.getMessages().tpHereReceived(sender.getName()));

        teleportManager.setCooldown(sender.getName(), System.currentTimeMillis());

        return true;

    }
}