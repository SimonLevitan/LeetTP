package cc.leet.leettp.command.warp;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Warp;
import cc.leet.leettp.util.ToolBox;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class DelWarpCommand extends Command {

    private LeetTP plugin;
    private WarpManager warpManager;

    public DelWarpCommand(LeetTP plugin) {
        super("delwarp", "Deletes a warp", "/delwarp ");
        this.plugin = plugin;
        this.warpManager = plugin.getWarpManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.delwarp")) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(args.length < 1 || args[0].isEmpty()) {
            sender.sendMessage(plugin.getMessages().warpNameMissing());
            return true;
        }

        boolean isPublic = !ToolBox.implode(args, " ").contains("-p");
        Warp warp = null;

        if(isPublic && warpManager.getPublicWarps().containsKey(args[0].toLowerCase())) {
            // Check if player owns this public warp or not.
            warp = warpManager.getPublicWarps().get(args[0].toLowerCase());
            if(warp == null) {
                sender.sendMessage(TextFormat.RED + "Could not find a public warp with that name.");
                return true;
            }
            // Check if user has permission to delete public warps which is not his/hers.
            if(!warp.getOwner().equalsIgnoreCase(sender.getName()) && !sender.hasPermission("leettp.warp.delall")) {
                sender.sendMessage(plugin.getMessages().noPermission());
                return true;
            }
        }

        if(warp == null) {
            warp = warpManager.getWarp(sender.getName(), args[0]);
            if(warp == null) {
                sender.sendMessage(plugin.getMessages().warpNotExists());
                return true;
            }
        }

        if(!warpManager.deleteWarp(warp.getOwner(), warp.getName())) {
            sender.sendMessage(plugin.getMessages().warpNotDeleted());
            return true;
        }

        sender.sendMessage(plugin.getMessages().warpDeleted());

        return true;

    }
}