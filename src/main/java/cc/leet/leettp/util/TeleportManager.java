package cc.leet.leettp.util;

import cc.leet.leettp.LeetTP;
import cc.leet.leettp.data.Teleport;

import java.util.HashMap;
import java.util.Map;

public class TeleportManager {

    private LeetTP plugin;
    private long cooldown;
    private Map<String, Map<String, Teleport>> requests;
    private Map<String, Boolean> status;
    private Map<String, Long> cooldowns;

    public TeleportManager(LeetTP plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().get("tp.cooldown", 30) * 1000; // Convert to milliseconds.
        requests = new HashMap<>();
        status = new HashMap<>();
        cooldowns = new HashMap<>();
    }

    /**
     * Returns the requests made FOR the target.
     *
     * @param target Target player
     * @return Map
     */
    public Map<String, Teleport> getRequests(String target) {
        if(!requests.containsKey(target)) return null;
        return requests.get(target);
    }

    public boolean addRequest(String target, String sender, Teleport.TeleportType type) {

        if(!requests.containsKey(sender) || requests.get(target) == null) {
            requests.put(target, new HashMap<>());
        }

        requests.get(target).put(sender, new Teleport(sender, target, type));

        return requests.containsKey(target) && requests.get(target).containsKey(sender);

    }

    public int removeRequest(String target, String sender, Teleport.TeleportType type) {

        Map<String, Teleport> requests = getRequests(target);

        if(requests == null || !requests.containsKey(sender)) {
            return -2;
        }

        if(type != requests.get(sender).getType()) {
            return -1;
        }

        this.requests.get(target).remove(sender);

        return this.requests.get(target).containsKey(sender) ? 0 : 1;

    }

    public Long getCooldownTime() {
        return cooldown;
    }

    public Long getCooldown(String player) {
        if(!cooldowns.containsKey(player)) return null;
        return cooldowns.get(player);
    }

    public void setCooldown(String player, long time) {
        cooldowns.put(player, time);
    }

    public boolean getStatus(String player) {
        if(!status.containsKey(player)) status.put(player, true);
        return status.get(player);
    }

    public void setStatus(String player, boolean status) {
        this.status.put(player, status);
    }

}