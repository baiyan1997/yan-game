package cn.baiyan.game.map;

import cn.baiyan.actor.MailBox;
import cn.baiyan.db.BaseEntity;
import cn.baiyan.thread.ThreadCenter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "exchangent")
public class Exchange extends BaseEntity {

    private static final long serialVersionUID = -1601963013529938867L;

    @Column
    private String id;

    @Column
    private String content;


    @Override
    public MailBox mailBox() {
        return ThreadCenter.createBusinessMailBox("change");
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
