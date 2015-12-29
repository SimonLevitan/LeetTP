package cc.leet.leettp.command.warp;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Warp;
import cc.leet.leettp.util.ToolBox;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;

public class SetWarpCommand extends Command {

    private LeetTP plugin;
    private WarpManager warpManager;

    public SetWarpCommand(LeetTP plugin) {
        super("setwarp", "Creates a warp", "/setwarp [name] [-p]");
        this.plugin = plugin;
        this.warpManager = plugin.getWarpManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.setwarp") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(args.length < 1 || args[0].isEmpty()) {
            sender.sendMessage(plugin.getMessages().warpNameMissing());
            return true;
        }

        boolean isPublic = !ToolBox.implode(args, " ").contains("-p");

        if(isPublic && !sender.hasPermission("leettp.warp.public")) isPublic = false;

        if(isPublic && warpManager.getPublicWarps().containsKey(args[0].toLowerCase()) ||
                warpManager.getWarps(sender.getName()) != null &&
                        warpManager.getWarps(sender.getName()).containsKey(args[0].toLowerCase())) {
            sender.sendMessage(plugin.getMessages().warpExists());
            return true;
        }

        Location location = ((Player) sender).getLocation();

        Warp warp = new Warp(
                args[0],
                sender.getName().toLowerCase(),
                location.getLevel().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getZ(),
                isPublic

        );

        warp.setLocation(location);

        if(!warpManager.addWarp(sender.getName(), warp)) {
            sender.sendMessage(plugin.getMessages().warpExists());
            return true;
        }
        if(isPublic) warpManager.addPublic(warp);

        sender.sendMessage(plugin.getMessages().warpSet(warp.getName()));

        return true;
    }
}