package cc.leet.leettp.command;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Home;
import cc.leet.leettp.util.HomeManager;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

import java.util.Map;

public class HomesCommand extends Command {

    private LeetTP plugin;
    private HomeManager homeManager;

    public HomesCommand(LeetTP plugin) {
        super("homes", "List your homes", "/homes");
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.homes")) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        Map<String, Home> homes = homeManager.getHomes(sender.getName());
        StringBuilder message = new StringBuilder();
        boolean color = true;

        if(homes == null) {
            message.append(TextFormat.GRAY + "You have no homes.");
        } else {
            for(Map.Entry<String, Home> entry : homes.entrySet()) {
                message.append((color ? TextFormat.WHITE : TextFormat.GRAY)).append(entry.getKey()).append(", ");
                color = !color;
            }
            sender.sendMessage(TextFormat.GRAY + "Your homes:");
        }

        sender.sendMessage(message.toString().substring(0, message.toString().length() - 2));

        return true;

    }
}