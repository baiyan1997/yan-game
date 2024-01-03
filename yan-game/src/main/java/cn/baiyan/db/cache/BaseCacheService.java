package cn.baiyan.db.cache;

import cn.baiyan.db.BaseEntity;
import cn.baiyan.db.DbService;

import java.util.concurrent.Callable;

public abstract class BaseCacheService<K, V extends BaseEntity> implements Persistable<K, V> {
    private final AbstractCacheContainer<K, V> container;

    public BaseCacheService() {
        this(CacheOptions.defaultCacheOptions());
    }

    private BaseCacheService(CacheOptions p) {
        container = new DefaultCacheContainer<>(this, p);
    }

    /**
     * 通过key获取对象
     */
    public V get(K key) {
        return container.get(key);
    }

    public AbstractCacheContainer<K, V> getContainer() {
        return container;
    }

    public final V getOrCreate(K k, Callable<V> cacheable) {
        return container.getOrCreate(k, cacheable);
    }

    /**
     * 手动移除缓存
     */
    public void remove(K key) {
        container.remove(key);
    }

    /**
     * 手动加入缓存
     */
    public void put(K key, V v) {
        this.container.put(key, v);
    }

    public void save(V v) {
        DbService.getInstance().insertOrUpdate(v);
    }

}
