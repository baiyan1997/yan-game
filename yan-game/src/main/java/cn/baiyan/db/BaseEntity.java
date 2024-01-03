package cn.baiyan.db;

import cn.baiyan.actor.Actor;
import cn.baiyan.db.cache.AbstractCacheable;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BaseEntity extends AbstractCacheable implements
        Serializable, Actor {

    /**
     * 实体的主键最好定义为包装类型，防止属性getter/setter方法类型不匹配
     */
    public abstract String getId();

    /**
     * init hook
     */
    public void doAfterInit() {
    }

    /**
     * save hook
     */
    public void doBeforeSave() {
    }

    private int sqlStatus;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getId().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        if (getId() != other.getId()) {
            return false;
        }
        return true;
    }

}
