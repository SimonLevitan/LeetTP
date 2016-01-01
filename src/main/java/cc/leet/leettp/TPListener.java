package cc.leet.leettp;

import cc.leet.leettp.data.Home;
import cc.leet.leettp.data.Warp;
import cc.leet.leettp.util.HomeManager;
import cc.leet.leettp.util.WarpManager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerBedEnterEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.tile.Sign;
import cn.nukkit.tile.Tile;

import java.util.Map;

public class TPListener implements Listener {

    private LeetTP plugin;
    private WarpManager warpManager;
    private HomeManager homeManager;

    public TPListener(LeetTP plugin) {
        this.plugin = plugin;
        warpManager = plugin.getWarpManager();
        homeManager = plugin.getHomeManager();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSleep(PlayerBedEnterEvent event) {

        if(event.isCancelled()) return;

        if(!homeManager.bedSetHome) return;

        plugin.getServer().dispatchCommand(event.getPlayer(), "sethome bed");

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Map<String, Home> homes = homeManager.getHomes(event.getPlayer().getName());

        if(!homes.containsKey("bed")) return;
        event.getPlayer().getLocation();

        event.setRespawnPosition(homes.get("bed").getLocation());

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (!event.getEntity().hasPermission("leettp.command.back")) return;

        plugin.deaths.put(event.getEntity().getName(), event.getEntity().getLocation());

    }

    /*@EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {

        System.out.println(0);

        if(event.getBlock().getId() != 63 && event.getBlock().getId() != 68) return;

        System.out.println(1);

        Tile tile = event.getBlock().getLevel().getTile(new Vector3(
                event.getBlock().getX(),
                event.getBlock().getY(),
                event.getBlock().getZ()
        ));

        // Double check that tile is a sign.
        if(!(tile instanceof Sign)) {
            plugin.getLogger().error("Tile was not a instance of sign at X: " +
                    event.getBlock().getX() + " Y: " +
                    event.getBlock().getY() + " Z: " +
                    event.getBlock().getZ()
            );
            return;
        }

        Sign sign = (Sign) tile;

        System.out.println(2);
        System.out.println(sign.getText()[0]);

        // Only process further if the sign is actually containing text on line 2 and it is [WARP]!
        if(!sign.getText()[0].equalsIgnoreCase("[WARP]")) return;

        System.out.println(3);

        Map<String, Warp> warps = warpManager.getPublicWarps();

        if(!warps.containsKey(((Sign) tile).getText()[1].toLowerCase())) {
            event.getPlayer().sendMessage(plugin.getMessages().warpNotExists());
            return;
        }

        System.out.println(4);

        Warp warp = warps.get(sign.getText()[1].toLowerCase());

        event.getPlayer().teleport(warp.getLocation());

        event.getPlayer().sendMessage(plugin.getMessages().warpTeleported(warp.getName()));

    }*/

   /* @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent event) {

        if(!event.getPlayer().hasPermission("leettp.warp.public")) return;

        if(event.getLines().length < 2 || !event.getLine(1).equalsIgnoreCase("[WARP]")) return;

        Map<String, Warp> warps = warpManager.getPublicWarps();

        if(warps.containsKey(event.getLine(1).toLowerCase())) {
            event.getPlayer().sendMessage(plugin.getMessages().warpExists());
            return;
        }

        Warp warp = new Warp(
                event.getLine(1),
                event.getPlayer().getName(),
                event.getPlayer().getLevel().getName(),
                event.getPlayer().getX(),
                event.getPlayer().getY(),
                event.getPlayer().getZ(),
                event.getPlayer().getYaw(),
                event.getPlayer().getPitch(),
                true
        );

        if(!warpManager.addPublic(warp)) {
            event.getPlayer().sendMessage(plugin.getMessages().warpExists());
            return;
        }

        event.getPlayer().sendMessage(plugin.getMessages().warpSet(warp.getName()));

    } */

}