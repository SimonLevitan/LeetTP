package cc.leet.leettp.command.spawn;

import cc.leet.leettp.LeetTP;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

import java.util.HashMap;

public class SpawnCommand extends Command {

    private LeetTP plugin;
    private HashMap<String, Long> cooldown;
    private long cdTime;

    public SpawnCommand(LeetTP plugin) {
        super("spawn", "Teleports to spawn", "/spawn");
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
        this.cdTime = plugin.getConfig().getNested("spawn.cooldown", 5) * 1000;
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.spawn") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        if(cooldown.containsKey(sender.getName())) {
            long time = System.currentTimeMillis() - cooldown.get(sender.getName());
            if(time < cdTime) {
                sender.sendMessage(plugin.getMessages().cooldownWait((int)(cdTime - time) / 1000));
                return true;
            }
        }

        ((Player) sender).teleport(((Player) sender).getLocation().getLevel().getSpawnLocation());

        sender.sendMessage(plugin.getMessages().spawnTeleported());

        return true;
    }
}