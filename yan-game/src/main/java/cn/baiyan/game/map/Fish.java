package cn.baiyan.game.map;

import cn.baiyan.actor.MailBox;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.thread.ThreadCenter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "fishent")
public class Fish extends BaseEntity {
    private static final long serialVersionUID = -6875017851868782197L;

    @Column
    private String id;

    @Column
    private Integer fishId;

    @Column
    private int weight;

    @Column
    private int fishStatus;

    public void setUserId(String userId) {
        this.id = userId;
    }

    public Integer getFishId() {
        return fishId;
    }

    public void setFishId(Integer fishId) {
        this.fishId = fishId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getFishStatus() {
        return fishStatus;
    }

    public void setFishStatus(int fishStatus) {
        this.fishStatus = fishStatus;
    }

    @Override
    public MailBox mailBox() {
        return ThreadCenter.createBusinessMailBox("fish");
    }


    @Override
    public String getId() {
        return id;
    }
}
