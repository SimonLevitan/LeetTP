package cc.leet.leettp.util;

import cc.leet.leettp.LeetTP;
import cn.nukkit.utils.TextFormat;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Messages {

    private static HashMap<String, String> colors;

    private String no_permission;

    private String cooldown_wait;

    private String home_exists;
    private String home_not_exists;
    private String home_not_deleted;
    private String home_name_missing;
    private String home_set;
    private String home_deleted;
    private String home_teleported;

    private String warp_set;
    private String warp_sign_created;
    private String warp_name_missing;
    private String warp_exists;
    private String warp_not_exists;
    private String warp_deleted;
    private String warp_not_deleted;
    private String warp_private_exists;
    private String warp_teleported;

    private String world_not_loaded;

    private String tp_name_missing;
    private String tp_request_exists;
    private String tp_not_allowed;
    private String tp_request_sent;
    private String tp_to_received;
    private String tp_here_received;
    private String tp_status_changed;
    private String tp_no_request;
    private String tp_not_exists;
    private String tp_not_online;
    private String tp_tpa_success;
    private String tp_tpahere_success;
    private String tp_tpa_target_success;
    private String tp_tpahere_target_success;
    private String tp_target_rejected;
    private String tp_rejected;
    private String tpworld_name_missing;
    private String tpworld_not_loaded;
    private String tpworld_success;

    private String back_empty;
    private String back_teleported;

    private String spawn_set;
    private String spawn_teleported;

    public Messages(LeetTP plugin) {
        findColors();

        no_permission = plugin.getConfig().getNested("messages.error.no-permission", "%red%You don't have permission to do that.");
        cooldown_wait = plugin.getConfig().getNested("messages.error.cooldown-wait", "%red%You need to wait {0} second(s) before doing that.");

        home_exists = plugin.getConfig().getNested("messages.error.home-exists", "%red%A home with that name already exists.");
        home_not_exists = plugin.getConfig().getNested("messages.error.home-not-exists", "%red%You don't have a home by that name.");
        home_not_deleted = plugin.getConfig().getNested("messages.error.home-not-deleted", "%red%Failed to delete that home.");
        home_name_missing = plugin.getConfig().getNested("messages.error.home-name-missing", "%red%You have to specify a name for your home.");
        home_set = plugin.getConfig().getNested("messages.success.home-set", "%green%Home set! Use /home %aqua%{0} %green%to return to it.");
        home_deleted = plugin.getConfig().getNested("messages.success.home-deleted", "%green%Home successfully deleted!");
        home_teleported = plugin.getConfig().getNested("messages.success.home-teleported", "%green%Welcome back to {0}");

        warp_set = plugin.getConfig().getNested("messages.success.warp-set", "%green%Warp set! Use /warp %aqua%{0} %green%to warp to it.");
        warp_sign_created = plugin.getConfig().getNested("messages.success.warp-sign-created", "%green%Warp sign created, click to teleport to %aqua%{0}%green%.");
        warp_name_missing = plugin.getConfig().getNested("messages.error.warp-name-missing", "%red%Warp name is missing.");
        warp_exists = plugin.getConfig().getNested("messages.error.warp-exists", "%red%A warp with that name already exists.");
        warp_not_exists = plugin.getConfig().getNested("messages.error.warp-not-exists", "%red%Found no warp with that name.");
        warp_deleted = plugin.getConfig().getNested("messages.success.warp-deleted", "%green%Warp successfully deleted!");
        warp_not_deleted = plugin.getConfig().getNested("messages.error.warp-not-deleted", "%red%Failed to delete that warp.");
        warp_private_exists = plugin.getConfig().getNested("messages.notify.warp-private-exists", "%yellow%A private warp with the same name exists, add '-p' to warp to it.");
        warp_teleported = plugin.getConfig().getNested("messages.success.warp-teleported", "%green%Warped to {0}");

        world_not_loaded = plugin.getConfig().getNested("messages.error.world-not-loaded", "%red%Target world NOT loaded!");

        tp_name_missing = plugin.getConfig().getNested("messages.error.tp-name-missing", "%red%You need to specify a target player.");
        tp_request_exists = plugin.getConfig().getNested("messages.error.tp-request-exists", "%red%Your target already has a teleportation request from you.");
        tp_not_allowed = plugin.getConfig().getNested("messages.error.tp-not-allowed", "%red%Your target does not allow teleportation requests.");
        tp_request_sent = plugin.getConfig().getNested("messages.error.tp-request-sent", "%green%Sent teleportation request to {0}.");
        tp_to_received = plugin.getConfig().getNested("messages.notify.tp-to-received", "%yellow%{0} has asked to teleport to you, type %aqua%/tpaccept {1}");
        tp_here_received = plugin.getConfig().getNested("messages.notify.tp-here-received", "%yellow%You were asked to teleport to {0}, type %aqua%/tpaccept {1}");
        tp_status_changed = plugin.getConfig().getNested("messages.success.tp-status-changed", "%green%You {0} allow teleportation requests.");
        tp_no_request = plugin.getConfig().getNested("messages.error.tp-no-request", "%red%You do not have any teleportation requests.");
        tp_not_exists = plugin.getConfig().getNested("messages.error.tp-not-exists", "%red%You have no requests by that player.");
        tp_not_online = plugin.getConfig().getNested("messages.error.tp-not-online", "%red%You can only accept requests from online players.");
        tp_tpa_success = plugin.getConfig().getNested("messages.success.tp-tpa-success", "%green%You teleported to {0}.");
        tp_tpahere_success = plugin.getConfig().getNested("messages.success.tp-tpahere-success", "%green%You teleported {0} to you.");
        tp_tpa_target_success = plugin.getConfig().getNested("messages.success.tp-tpa-target-success", "%green%{0} was teleported to you.");
        tp_tpahere_target_success = plugin.getConfig().getNested("messages.success.tp-tpahere-target-success", "%green%You were teleported to {0}.");
        tp_target_rejected = plugin.getConfig().getNested("messages.success.tp-target-rejected", "%green%Teleportation request has been rejected.");
        tp_rejected = plugin.getConfig().getNested("messages.error.tp-rejected", "%red%Your teleportation request made to {0} has been rejected.");
        tpworld_name_missing = plugin.getConfig().getNested("messages.error.tpworld-name-missing", "%red%You need to specify a target world.");
        tpworld_not_loaded = plugin.getConfig().getNested("messages.error.tpworld-not-loaded", "%red%Your target world is not loaded.");
        tpworld_success = plugin.getConfig().getNested("messages.success.tpworld-success", "%green%You teleported to {0}");
        back_empty = plugin.getConfig().getNested("messages.error.back-empty", "%red%You have to die before using /back.");
        back_teleported = plugin.getConfig().getNested("messages.success.back-teleported", "%green%You returned to your last death location.");

        spawn_set = plugin.getConfig().getNested("messages.success.spawn-set", "%green%Spawn location set.");
        spawn_teleported = plugin.getConfig().getNested("messages.success.spawn-teleported", "%green%Teleported to spawn.");

    }

    private String parse(String message, Object... params) {
        return MessageFormat.format(parseColors(message), params);
    }

    private String parseColors(String msg) {
        String message = msg;
        for (Map.Entry<String, String> color : colors.entrySet()) {
            String colorKey = "%" + color.getKey().toLowerCase() + "%";
            if (message.contains(colorKey)) {
                message = message.replaceAll(colorKey, color.getValue());
            }
        }
        return message;
    }

    public static void findColors() {
        colors = new HashMap<>();
        Field[] declaredFields = TextFormat.class.getDeclaredFields();
        for(Field field : declaredFields) {
            if(java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    if(field.getName().equalsIgnoreCase("ESCAPE")) continue;
                    colors.put(field.getName(), (String) TextFormat.class.getDeclaredField(field.getName()).get(String.class));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String noPermission() {
        return parse(no_permission);
    }

    public String cooldownWait(int seconds) {
        return parse(cooldown_wait, seconds);
    }

    public String homeExists() {
        return parse(home_exists);
    }

    public String homeNotExists() {
        return parse(home_not_exists);
    }

    public String homeNotDeleted() {
        return parse(home_not_deleted);
    }

    public String homeNameMissing() {
        return parse(home_name_missing);
    }

    public String homeSet(String name) {
        return parse(home_set, name);
    }

    public String homeDeleted() {
        return parse(home_deleted);
    }

    public String homeTeleported(String name) {
        return parse(home_teleported, name);
    }

    public String warpSet(String name) {
        return parse(warp_set, name);
    }

    public String warpSignCreated(String name) {
        return parse(warp_sign_created, name);
    }

    public String warpNameMissing() {
        return parse(warp_name_missing);
    }

    public String warpExists() {
        return parse(warp_exists);
    }

    public String warpNotExists() {
        return parse(warp_not_exists);
    }

    public String warpDeleted() {
        return parse(warp_deleted);
    }

    public String warpNotDeleted() {
        return parse(warp_not_deleted);
    }

    public String warpPrivateExists() {
        return parse(warp_private_exists);
    }

    public String warpTeleported(String name) {
        return parse(warp_teleported, name);
    }

    public String worldNotLoaded() {
        return parse(world_not_loaded);
    }

    public String tpNameMissing() {
        return parse(tp_name_missing);
    }

    public String tpRequestExists() {
        return parse(tp_request_exists);
    }

    public String tpNotAllowed() {
        return parse(tp_not_allowed);
    }

    public String tpRequestSent(String player) {
        return parse(tp_request_sent, player);
    }

    public String tpToReceived(String sender) {
        return parse(tp_to_received, sender, sender);
    }

    public String tpHereReceived(String sender) {
        return parse(tp_here_received, sender);
    }

    public String tpStatusChanged(String status) {
        return parse(tp_status_changed, status);
    }

    public String tpNoRequest() {
        return parse(tp_no_request);
    }

    public String tpNotExists() {
        return parse(tp_not_exists);
    }

    public String tpNotOnline() {
        return parse(tp_not_online);
    }

    public String tpTpaSuccess(String player) {
        return parse(tp_tpa_success, player);
    }

    public String tpTpahereSuccess(String player) {
        return parse(tp_tpahere_success, player);
    }

    public String tpTpaTargetSuccess(String player) {
        return parse(tp_tpa_target_success, player);
    }

    public String tpTpahereTargetSuccess(String player) {
        return parse(tp_tpahere_target_success, player);
    }

    public String tpTargetRejected() {
        return parse(tp_target_rejected);
    }

    public String tpRejected(String player) {
        return parse(tp_rejected, player);
    }

    public String tpWorldNameMissing() {
        return parse(tpworld_name_missing);
    }

    public String tpWorldNotLoaded() {
        return parse(tpworld_not_loaded);
    }

    public String tpWorldSuccess(String world) {
        return parse(tpworld_success, world);
    }

    public String backEmpty() {
        return parse(back_empty);
    }

    public String backTeleported() {
        return parse(back_teleported);
    }

    public String spawnSet() {
        return parse(spawn_set);
    }

    public String spawnTeleported() {
        return parse(spawn_teleported);
    }

}