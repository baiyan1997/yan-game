package cn.baiyan.game.map;

import cn.baiyan.actor.MailBox;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.thread.ThreadCenter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "mapent")
public class MapEnt extends BaseEntity {
    private static final long serialVersionUID = -9073294952107531017L;

    @Column
    private String id;

    @Column
    private int mapId;

    @Column
    private String evenGetFish;


    @Override
    public MailBox mailBox() {
        return ThreadCenter.createBusinessMailBox("map");
    }


    @Override
    public String getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getEvenGetFish() {
        return evenGetFish;
    }

    public void setEvenGetFish(String evenGetFish) {
        this.evenGetFish = evenGetFish;
    }
}
