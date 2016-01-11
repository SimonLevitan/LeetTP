package cc.leet.leettp;

public class SaveTask implements Runnable {

    private LeetTP plugin;

    public SaveTask(LeetTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if(plugin.getHomeManager().dataHasChanged()) plugin.getHomeManager().save();
        if(plugin.getWarpManager().dataHasChanged()) plugin.getWarpManager().save();
    }
}