package cn.baiyan.game.module.login;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

@ProtobufClass
public class PlayerLoginVo {

    private long id;
    private String name;
    private long fighting;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFighting() {
        return fighting;
    }

    public void setFighting(long fighting) {
        this.fighting = fighting;
    }

    @Override
    public String toString() {
        return "PlayerLoginVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fighting=" + fighting +
                '}';
    }
}
