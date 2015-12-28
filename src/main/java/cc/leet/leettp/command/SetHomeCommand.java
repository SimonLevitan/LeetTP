package cc.leet.leettp.command;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Home;
import cc.leet.leettp.util.HomeManager;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;

public class SetHomeCommand extends Command {

    private LeetTP plugin;
    private HomeManager homeManager;

    public SetHomeCommand(LeetTP plugin) {
        super("sethome", "Creates a home", "/home [name]");
        this.plugin = plugin;
        this.homeManager = plugin.getHomeManager();
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {

        if(!sender.hasPermission("leettp.command.sethome") || !(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessages().noPermission());
            return true;
        }

        String name;

        // Default to "home" if nothing is specified.
        if(args.length < 1) {
            name = "home";
        } else {
            name = args[0];
        }

        if(name.isEmpty()) {
            sender.sendMessage(plugin.getMessages().homeNameMissing());
            return true;
        }

        Location location = ((Player) sender).getLocation();

        Home home = new Home(
                name,
                location.getLevel().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        home.setLocation(location);

        if(!homeManager.addHome(sender.getName(), home)) {
            sender.sendMessage(plugin.getMessages().homeExists());
            return true;
        }

        sender.sendMessage(plugin.getMessages().homeSet(name));

        return true;

    }
}