package cn.baiyan.game.map;

import cn.baiyan.actor.MailBox;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.thread.ThreadCenter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "itement")
public class ItemEnt extends BaseEntity {
    private static final long serialVersionUID = 7943475247135948498L;

    @Column(name = "id")
    private String id;

    @Column(name = "itemId")
    private int itemId;

    @Column(name = "count")
    private int count;

    @Column(name = "itemStatus")
    private int itemStatus;

    @Column(name = "useCount")
    private int useCount;

    @Column(name = "level")
    private int level;

    public void setId(String userId) {
        this.id = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public MailBox mailBox() {
        return ThreadCenter.createBusinessMailBox("item");
    }

    @Override
    public String getId() {
        return id;
    }
}
