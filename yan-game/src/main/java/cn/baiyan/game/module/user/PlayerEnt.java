package cn.baiyan.game.module.user;

import cn.baiyan.actor.MailBox;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.thread.ThreadCenter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 玩家实体
 */
@Entity(name = "playerent")
public class PlayerEnt extends BaseEntity {
    private static final long serialVersionUID = -8975923698284971075L;

    @Id
    @Column(name = "id")
    private String id;

    @Column()
    private long lastLoginTime;

    @Column(name = "energy")
    private int energy;

    @Column(name = "maxWeight")
    private int maxWeight;

    @Column(name = "fishTime")
    private int fishTime;

    @Column(name = "rodTime")
    private int rodTime;

    @Column(name = "fishMan")
    private int fishMan;

    public PlayerEnt() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void doAfterInit() {
    }

    @Override
    public void doBeforeSave() {
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getFishTime() {
        return fishTime;
    }

    public void setFishTime(int fishTime) {
        this.fishTime = fishTime;
    }

    public int getRodTime() {
        return rodTime;
    }

    public void setRodTime(int rodTime) {
        this.rodTime = rodTime;
    }

    public int getFishMan() {
        return fishMan;
    }

    public void setFishMan(int fishMan) {
        this.fishMan = fishMan;
    }

    @Override
    public MailBox mailBox() {
        return ThreadCenter.createBusinessMailBox("player");
    }

}
