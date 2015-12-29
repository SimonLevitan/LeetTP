package cc.leet.leettp.command.warp;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Warp;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Map;

public class WarpsCommand extends Command {

    private LeetTP plugin;
    private WarpManager warpManager;

    public WarpsCommand(LeetTP plugin) {
        super("warps", "List your own and public warps", "/warps");
        this.plugin = plugin;
        this.warpManager = plugin.getWarpManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.warps")) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        Map<String, Warp> warps = warpManager.getWarps(sender.getName());

        StringBuilder own = new StringBuilder();
        boolean color = true;

        if(warps == null || warps.size() == 0) {
            own.append(TextFormat.GRAY).append("You have no warps.  ");
        } else {
            for(Map.Entry<String, Warp> entry : warps.entrySet()) {
                own.append(color ? TextFormat.WHITE : TextFormat.GRAY).append(entry.getKey()).append(", ");
                color = !color;
            }
        }

        StringBuilder publicWarps = new StringBuilder();
        color = true;

        for(Map.Entry<String, Warp> entry : warpManager.getPublicWarps().entrySet()) {
            if(entry.getValue().getOwner().equalsIgnoreCase(sender.getName())) continue;
            publicWarps.append(color ? TextFormat.WHITE : TextFormat.GRAY).append(entry.getKey()).append(", ");
            color = !color;
        }

        if(publicWarps.toString().length() > 0) {
            sender.sendMessage(TextFormat.YELLOW + "Public warps:");
            sender.sendMessage(publicWarps.toString().substring(0, publicWarps.toString().length() - 2));
        }

        if(!own.toString().equalsIgnoreCase("You have no warps.  ")) sender.sendMessage(TextFormat.YELLOW + "Your warps:");
        sender.sendMessage(own.toString().substring(0, own.toString().length() - 2));

        return true;

    }
}