package cc.leet.leettp.command.warp;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Warp;
import cc.leet.leettp.util.ToolBox;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class WarpCommand extends Command {

    private LeetTP plugin;
    private WarpManager warpManager;
    private HashMap<String, Long> cooldown;

    public WarpCommand(LeetTP plugin) {
        super("warp", "Teleports to a warp", "/warp [name] [-p]");
        this.plugin = plugin;
        this.warpManager = plugin.getWarpManager();
        this.cooldown = new HashMap<>();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.warp") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(cooldown.containsKey(sender.getName())) {
            long time = System.currentTimeMillis() - cooldown.get(sender.getName());
            if(time < warpManager.getCooldown()) {
                sender.sendMessage(plugin.getMessages().cooldownWait((int)(warpManager.getCooldown() - time) / 1000));
                return true;
            }
        }

        if(args.length < 1 || args[0].isEmpty() || args[0].equalsIgnoreCase("-p")) {
            sender.sendMessage(plugin.getMessages().warpNameMissing());
            return true;
        }

        boolean isPublic = !ToolBox.implode(args, " ").contains("-p");
        boolean privateExists = false;

        Warp warp = null;

        if(isPublic) {
            Map<String, Warp> warps = warpManager.getPublicWarps();
            if(warps == null || !warps.containsKey(args[0].toLowerCase())) {

                warps = warpManager.getWarps(sender.getName());

                if(warps == null || !warps.containsKey(args[0].toLowerCase())) {
                    sender.sendMessage(plugin.getMessages().warpNotExists());
                    return true;
                }

                warp = warps.get(args[0].toLowerCase());

                isPublic = false;

            } else {
                warp = warps.get(args[0].toLowerCase());
            }
            // Let player know that a private warp exists.
            if(!warp.getOwner().equalsIgnoreCase(sender.getName()) &&
                    warpManager.getWarps(sender.getName()).containsKey(args[0].toLowerCase())) {
                privateExists = true;
            }
        }

        if(warp == null) {

            warp = warpManager.getWarp(sender.getName(), args[0]);

            if(warp == null) {
                sender.sendMessage(plugin.getMessages().warpNotExists());
                return true;
            }

            if(isPublic) privateExists = true;

        }

        if(privateExists) sender.sendMessage(plugin.getMessages().warpPrivateExists());

        // Check if level is loaded.
        if(!plugin.getServer().isLevelLoaded(warp.getWorld())) {
            sender.sendMessage(plugin.getMessages().worldNotLoaded());
            return true;
        }

        ((Player) sender).teleport(warp.getLocation());
        sender.sendMessage(plugin.getMessages().warpTeleported(warp.getName()));

        // Attach cooldown for last used /warp.
        cooldown.put(sender.getName(), System.currentTimeMillis());

        return true;

    }
}