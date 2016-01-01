package cc.leet.leettp;

import cc.leet.leettp.command.home.DelHomeCommand;
import cc.leet.leettp.command.home.HomeCommand;
import cc.leet.leettp.command.home.HomesCommand;
import cc.leet.leettp.command.home.SetHomeCommand;
import cc.leet.leettp.command.spawn.SetSpawnCommand;
import cc.leet.leettp.command.spawn.SpawnCommand;
import cc.leet.leettp.command.teleport.*;
import cc.leet.leettp.command.warp.DelWarpCommand;
import cc.leet.leettp.command.warp.SetWarpCommand;
import cc.leet.leettp.command.warp.WarpCommand;
import cc.leet.leettp.command.warp.WarpsCommand;
import cc.leet.leettp.util.HomeManager;
import cc.leet.leettp.util.Messages;
import cc.leet.leettp.util.TeleportManager;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LeetTP extends PluginBase {

    private static LeetTP plugin;
    //private int version;

    private Messages messages;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private TeleportManager teleportManager;

    public Map<String, Position> deaths;

    @Override
    public void onEnable() {
        plugin = this;
        deaths = new HashMap<>();

        if(!Files.exists(getDataFolder().toPath())) {
            if(!getDataFolder().mkdir()) getLogger().critical("Failed to create data folder!");
        }

        saveDefaultConfig();
        reloadSettings();

        messages = new Messages(plugin);
        homeManager = new HomeManager(plugin);
        warpManager = new WarpManager(plugin);
        teleportManager = new TeleportManager(plugin);

        // Register commands.
        getServer().getCommandMap().register("home", new HomeCommand(plugin));
        getServer().getCommandMap().register("homes", new HomesCommand(plugin));
        getServer().getCommandMap().register("sethome", new SetHomeCommand(plugin));
        getServer().getCommandMap().register("delhome", new DelHomeCommand(plugin));

        getServer().getCommandMap().register("warps", new WarpsCommand(plugin));
        getServer().getCommandMap().register("warp", new WarpCommand(plugin));
        getServer().getCommandMap().register("setwarp", new SetWarpCommand(plugin));
        getServer().getCommandMap().register("delwarp", new DelWarpCommand(plugin));

        getServer().getCommandMap().register("spawn", new SpawnCommand(plugin));
        getServer().getCommandMap().register("setspawn", new SetSpawnCommand(plugin));

        getServer().getCommandMap().register("back", new BackCommand(plugin));
        getServer().getCommandMap().register("tptoggle", new TpToggleCommand(plugin));
        getServer().getCommandMap().register("tpa", new TpaCommand(plugin));
        getServer().getCommandMap().register("tpahere", new TpaHereCommand(plugin));
        getServer().getCommandMap().register("tpdeny", new TpDenyCommand(plugin));
        getServer().getCommandMap().register("tpaccept", new TpAcceptCommand(plugin));
        getServer().getCommandMap().register("tpo", new TpoCommand(plugin));
        getServer().getCommandMap().register("tpohere", new TpoHereCommand(plugin));

    }

    @Override
    public void onDisable() {
        homeManager.save();
        warpManager.save();
    }

    /**
     * Reloads the configuration and redeclare
     * variable values.
     */
    public void reloadSettings() {
        reloadConfig();
        //this.version = getConfig().get("version", 1);
    }

    /**
     * Gets the plugin instance.
     *
     * @return LeetTP
     */
    public static LeetTP getPlugin() {
        return plugin;
    }

    public Messages getMessages() {
        return messages;
    }

    /**
     * Gets the home manager.
     *
     * @return HomeManager
     */
    public HomeManager getHomeManager() {
        return homeManager;
    }

    /**
     * Gets the warp manager.
     *
     * @return WarpManager
     */
    public WarpManager getWarpManager() {
        return warpManager;
    }

    /**
     * Gets the teleport manager.
     *
     * @return TeleportManager
     */
    public TeleportManager getTeleportManager() {
        return teleportManager;
    }
}