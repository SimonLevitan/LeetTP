package cc.leet.leettp;

import cc.leet.leettp.util.Messages;
import cn.nukkit.plugin.PluginBase;

import java.nio.file.Files;

public class LeetTP extends PluginBase {

    private static LeetTP plugin;
    private int version;

    private Messages messages;

    @Override
    public void onEnable() {
        plugin = this;

        if(!Files.exists(getDataFolder().toPath())) {
            if(!getDataFolder().mkdir()) getLogger().critical("Failed to create data folder!");
        }
        saveDefaultConfig();
        reloadSettings();

        messages = new Messages(plugin);
    }

    @Override
    public void onDisable() {

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
}