package cc.leet.leettp.util;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Warp;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class WarpManager {

    private LeetTP plugin;

    private Map<String, Map<String, Warp>> warps;
    private Map<String, Map<String, Warp>> publicWarps;

    private Config file;

    private long cooldown;

    public WarpManager(LeetTP plugin) {
        this.plugin = plugin;
        warps = new HashMap<>();
        publicWarps = new HashMap<>();
        cooldown = plugin.getConfig().getNested("warp.cooldown", 5) * 1000; // Convert to milliseconds.
        load();
    }

    public void load() {
        if(!warpsExist()) plugin.saveResource("warps.yml");
        file = new Config(plugin.getDataFolder().getAbsolutePath() + "/warps.yml", Config.YAML);

        // Level 1 - All players
        LinkedHashMap all = (LinkedHashMap) file.getAll();

        // Level 2 - Name of warp
        for(Object o : all.entrySet()) {
            Map.Entry lvl2 = (Map.Entry) o;

            if(!this.warps.containsKey(lvl2.getKey().toString())) {
                this.warps.put(lvl2.getKey().toString(), new HashMap<>());
            }

            Map<String, Warp> warps = new HashMap<>();
            HashMap entryValue = (HashMap) lvl2.getValue();

            // Level 3 - Details of warp
            for(Object ob : entryValue.entrySet()) {
                Map.Entry lvl3 = (Map.Entry) ob;
                LinkedHashMap homeData = (LinkedHashMap) lvl3.getValue();
                Warp warp = new Warp(
                        lvl3.getKey().toString(),
                        homeData.get("owner").toString(),
                        homeData.get("world").toString(),
                        (double) homeData.get("x"),
                        (double) homeData.get("y"),
                        (double) homeData.get("z"),
                        (double) homeData.get("yaw"),
                        (double) homeData.get("pitch"),
                        (boolean) homeData.get("isPublic")
                );
                Level level = plugin.getServer().getLevelByName(warp.getWorld());
                if(level == null) continue;
                warp.setLocation(new Location(warp.getX(), warp.getY(), warp.getZ(), warp.getYaw(), warp.getPitch(), level));

                if(warp.isPublic()) addPublic(warp);

                warps.put(lvl3.getKey().toString().toLowerCase(), warp);
            }
            this.warps.put(lvl2.getKey().toString(), warps);
        }
    }

    private boolean warpsExist() {
        return new File(plugin.getDataFolder(), "warps.yml").exists();
    }

    /**
     * Adds a warp to the public map.
     *
     * @param warp Warp data
     * @return boolean
     */
    public boolean addPublic(Warp warp) {
        if(!publicWarps.containsKey(warp.getName().toLowerCase())) {
            publicWarps.put(warp.getName().toLowerCase(), new HashMap<>());
        }
        publicWarps.get(warp.getName().toLowerCase()).put(warp.getOwner(), warp);
        return publicWarps.get(warp.getName().toLowerCase()).containsKey(warp.getOwner());
    }

    /**
     * Removes a warp from the public map.
     *
     * @param warp Warp data
     * @return boolean
     */
    public boolean removePublic(Warp warp) {
        return removePublic(warp.getName(), warp.getOwner());
    }

    /**
     * Removes a warp from the public map.
     *
     * @param warp Name of warp
     * @param player Name of player
     * @return boolean
     */
    public boolean removePublic(String warp, String player) {
        warp = warp.toLowerCase();
        player = player.toLowerCase();
        if(!publicWarps.containsKey(warp) || !publicWarps.get(warp).containsKey(player)) return false;
        publicWarps.get(warp).remove(player);
        if(publicWarps.get(warp).size() == 0) {
            publicWarps.remove(warp);
            return true;
        }
        return !publicWarps.get(warp).containsKey(player);
    }

    /**
     * Gets all public warps.
     *
     * @return Map
     */
    public Map<String, Map<String, Warp>> getPublicWarps() {
        return publicWarps;
    }

    /**
     * Gets a specific warp from a player.
     *
     * @param player Owner
     * @param warp Target warp
     * @return Warp
     */
    public Warp getWarp(String player, String warp) {
        player = player.toLowerCase();
        warp = warp.toLowerCase();

        if(!warps.containsKey(player) || !warps.get(player).containsKey(warp)) return null;

        return warps.get(player).get(warp);
    }

    /**
     * Gets all warps by target player.
     *
     * @param player Target
     * @return Map
     */
    public Map<String, Warp> getWarps(String player) {
        player = player.toLowerCase();
        if(!warps.containsKey(player)) return null;
        return warps.get(player);
    }

    /**
     * Deletes a warp.
     *
     * @param player Player name
     * @param warp Warp name
     * @return boolean
     */
    public boolean deleteWarp(String player, String warp) {
        player = player.toLowerCase();
        warp = warp.toLowerCase();

        if(!warps.containsKey(player) || !warps.get(player).containsKey(warp)) return false;

        if(warps.get(player).get(warp).isPublic()) removePublic(warp, player);

        warps.get(player).remove(warp);
        file.removeNested(player + "." + warp);

        if(warps.get(player).size() == 0) {
            warps.remove(player);
            file.remove(player);
            return true;
        }

        return !warps.get(player).containsKey(warp);
    }

    /**
     * Adds a warp.
     *
     * @param player Owner
     * @param warp Warp data
     * @return boolean
     */
    public boolean addWarp(String player, Warp warp) {
        player = player.toLowerCase();
        if(!warps.containsKey(player)) warps.put(player, new HashMap<>());
        if(warps.get(player).containsKey(warp.getName().toLowerCase())) return false;
        warps.get(player).put(warp.getName().toLowerCase(), warp);
        if(warp.isPublic()) addPublic(warp);
        return warps.get(player).containsKey(warp.getName().toLowerCase());
    }

    /**
     * Returns the cooldown for using /warp.
     *
     * @return long
     */
    public long getCooldown() {
        return cooldown;
    }

    public void save() {

        // Level 1 - All players
        for(Map.Entry<String, Map<String, Warp>> lvl1 : warps.entrySet()) {
            // Level 2 - Warp data.
            for(Map.Entry<String, Warp> lvl2 : lvl1.getValue().entrySet()) {

                Warp warp = lvl2.getValue();

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".name", warp.getName().toLowerCase());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".owner", warp.getOwner().toLowerCase());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".world", warp.getWorld());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".x", warp.getX());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".y", warp.getY());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".z", warp.getZ());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".yaw", warp.getYaw());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".pitch", warp.getPitch());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".isPublic", warp.isPublic());

            }
        }

        file.save();
    }

}