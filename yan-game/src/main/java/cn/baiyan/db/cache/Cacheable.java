package cn.baiyan.db.cache;

public abstract class Cacheable {
    /**
     * 当前实体的db状态
     */
    protected DbStatus status = DbStatus.NORMAL;

    /**
     * 返回当前db的状态
     */
    public abstract DbStatus getStatus();

    /**
     * 当前是否为插入状态
     */
    public abstract boolean isInsert();

    /**
     * 当前是否为更新状态
     */
    public abstract boolean isUpdate();

    /**
     * 当前是否为删除状态
     */
    public abstract boolean isDelete();

    /**
     * 切换为插入状态
     */
    public abstract void setInsert();

    /**
     * 切换为更新状态
     */
    public abstract void setUpdate();

    /**
     * 切换为删除状态
     */
    public abstract void setDelete();

    /**
     * 获取持久化对应的sql语句
     * 入库sql语句
     */
    public abstract String getSaveSql();
}
