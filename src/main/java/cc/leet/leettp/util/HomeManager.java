package cc.leet.leettp.util;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Home;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.*;

public class HomeManager {

    private LeetTP plugin;

    private Map<String, Map<String, Home>> homes;

    private Config file;

    private double cooldown;

    public boolean bedSetHome;

    public HomeManager(LeetTP plugin) {
        this.plugin = plugin;
        homes = new HashMap<>();
        cooldown = plugin.getConfig().getNested("home.cooldown", 5) * 1000; // Convert to milliseconds.
        bedSetHome = plugin.getConfig().getNested("home.set-by-bed", true);
        load();
    }

    public void load() {
        if(!homesExists()) plugin.saveResource("homes.yml");
        file = new Config(plugin.getDataFolder().getAbsolutePath() + "/homes.yml", Config.YAML);

        // Level 1 - All players
        LinkedHashMap all = (LinkedHashMap) file.getAll();

        // Level 2 - Name of home
        for(Object o : all.entrySet()) {
            Map.Entry lvl2 = (Map.Entry) o;

            if(!this.homes.containsKey(lvl2.getKey().toString())) {
                this.homes.put(lvl2.getKey().toString(), new HashMap<>());
            }

            Map<String, Home> homes = new HashMap<>();
            HashMap entryValue = (HashMap) lvl2.getValue();

            // Level 3 - Details of home
            for(Object ob : entryValue.entrySet()) {
                Map.Entry lvl3 = (Map.Entry) ob;
                LinkedHashMap homeData = (LinkedHashMap) lvl3.getValue();
                Home home = new Home(
                        lvl3.getKey().toString(),
                        homeData.get("world").toString(),
                        (double) homeData.get("x"),
                        (double) homeData.get("y"),
                        (double) homeData.get("z"),
                        (double) homeData.get("yaw"),
                        (double) homeData.get("pitch")
                );
                Level level = plugin.getServer().getLevelByName(home.getWorld());
                if(level == null) continue;
                home.setLocation(new Location(home.getX(), home.getY(), home.getZ(), home.getYaw(), home.getPitch(), level));
                homes.put(lvl3.getKey().toString().toLowerCase(), home);
            }
            this.homes.put(lvl2.getKey().toString(), homes);
        }
    }

    /**
     * Saves homes to disk.
     */
    public void save() {

        // Level 1 - All players
        for(Map.Entry<String, Map<String, Home>> lvl1 : homes.entrySet()) {
            // Level 2 - Home data.
            for(Map.Entry<String, Home> lvl2 : lvl1.getValue().entrySet()) {

                Home home = lvl2.getValue();

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".name", home.getName().toLowerCase());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".world", home.getWorld());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".x", home.getX());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".y", home.getY());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".z", home.getZ());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".yaw", home.getYaw());

                file.setNested(lvl1.getKey().toLowerCase() + "." +
                        lvl2.getKey().toLowerCase() + ".pitch", home.getPitch());

            }
        }

    }

    /**
     * Attempts to get the specified home
     * from the specified player.
     *
     * @param player Target player
     * @param homeName Target home
     * @return Home
     */
    public Home getHome(String player, String homeName) {
        player = player.toLowerCase();
        homeName = homeName.toLowerCase();
        // Check if the player has homes and that the specified home exists.
        if(!homes.containsKey(player) || !homes.get(player).containsKey(homeName)) return null;
        return homes.get(player).get(homeName);
    }

    /**
     * Attempts to get all homes
     * by the target player.
     *
     * @param player Targer player
     * @return Home
     */
    public Map<String, Home> getHomes(String player) {
        player = player.toLowerCase();
        if(!homes.containsKey(player)) return null;
        return homes.get(player);
    }

    /**
     * Deletes the target player's home.
     *
     * @param player Target player
     * @param home Target home
     * @return boolean
     */
    public boolean deleteHome(String player, String home) {
        player = player.toLowerCase();
        home = home.toLowerCase();
        if(!homes.containsKey(player) || !homes.get(player).containsKey(home)) return false;
        homes.get(player).remove(home);
        return homes.get(player).containsKey(home);
    }

    /**
     * Adds a home to the target player.
     *
     * @param player Target player
     * @param home Home data
     * @return boolean
     */
    public boolean addHome(String player, Home home) {
        player = player.toLowerCase();
        if(!homes.containsKey(player)) homes.put(player, new HashMap<>());
        if(homes.get(player).containsKey(home.getName())) return false;
        homes.get(player).put(home.getName().toLowerCase(), home);
        return homes.get(player).containsKey(home.getName().toLowerCase());
    }

    public double getCooldown() {
        return cooldown;
    }

    private boolean homesExists() {
        return new File(plugin.getDataFolder(), "homes.yml").exists();
    }

}