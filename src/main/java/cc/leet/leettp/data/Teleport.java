package cc.leet.leettp.data;

public class Teleport {

    private long timestamp;

    private String sender;
    private String target;
    private TeleportType type;

    public enum TeleportType {
        HERE, TO
    }

    public Teleport(String sender, String target, TeleportType type) {
        this.sender = sender;
        this.target = target;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    public TeleportType getType() {
        return type;
    }

    public void setType(TeleportType type) {
        this.type = type;
    }
}