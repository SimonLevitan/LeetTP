package cc.leet.leettp.command.teleport;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.util.TeleportManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class TpToggleCommand extends Command {

    private LeetTP plugin;
    private TeleportManager teleportManager;

    public TpToggleCommand(LeetTP plugin) {
        super("tptoggle", "Toggles your teleport status", "/tptoggle");
        this.plugin = plugin;
        teleportManager = plugin.getTeleportManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.tptoggle") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        boolean status = !teleportManager.getStatus(sender.getName());

        teleportManager.setStatus(sender.getName(), status);

        sender.sendMessage(status ? plugin.getMessages().tpStatusChanged("now") : plugin.getMessages().tpStatusChanged("no longer"));

        return true;
    }
}