package cc.leet.leettp;

import cc.leet.leettp.command.DelHomeCommand;
import cc.leet.leettp.command.HomeCommand;
import cc.leet.leettp.command.HomesCommand;
import cc.leet.leettp.command.SetHomeCommand;
import cc.leet.leettp.util.HomeManager;
import cc.leet.leettp.util.Messages;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.plugin.PluginBase;

import java.nio.file.Files;

public class LeetTP extends PluginBase {

    private static LeetTP plugin;
    private int version;

    private Messages messages;
    private HomeManager homeManager;
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        plugin = this;

        if(!Files.exists(getDataFolder().toPath())) {
            if(!getDataFolder().mkdir()) getLogger().critical("Failed to create data folder!");
        }

        saveDefaultConfig();
        reloadSettings();

        messages = new Messages(plugin);
        homeManager = new HomeManager(plugin);
        warpManager = new WarpManager(plugin);

        // Register commands.
        getServer().getCommandMap().register("home", new HomeCommand(plugin));
        getServer().getCommandMap().register("homes", new HomesCommand(plugin));
        getServer().getCommandMap().register("sethome", new SetHomeCommand(plugin));
        getServer().getCommandMap().register("delhome", new DelHomeCommand(plugin));

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
        this.version = getConfig().get("version", 1);
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
}