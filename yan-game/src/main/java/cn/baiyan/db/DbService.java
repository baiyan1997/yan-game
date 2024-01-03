package cn.baiyan.db;

import cn.baiyan.db.utils.BlockingUniqueQueue;
import cn.baiyan.db.utils.DbUtils;
import cn.baiyan.db.utils.SqlUtils;
import cn.baiyan.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DbService {
    private static Logger logger = LoggerFactory.getLogger(DbService.class);

    private static volatile DbService INSTANCE = new DbService();

    public static DbService getInstance() {
        return INSTANCE;
    }

    private Worker commonWorker = new Worker();

    private final AtomicBoolean run = new AtomicBoolean(true);

    /**
     * 正在执行的db数量，用于抑制并发持久化数量
     */
    private AtomicInteger savingDbCounter = new AtomicInteger();

    private int MAX_DB_COUNTER = 15;

    /**
     * start consumer thread
     */
    public void init() {
        new NamedThreadFactory("db common-service").newThread(commonWorker).start();
    }

    /**
     * 自动插入或者更新数据
     */
    public void insertOrUpdate(BaseEntity entity) {
        if (entity.isSaving()) {
            return;
        }
        entity.setSaving();
        commonWorker.addToQueue(entity);
    }

    /**
     * 仅更新部分字段
     */
    public void saveColumns(BaseEntity entity, String... columns) {
        entity.savingColumns().add(String.join("", columns));
        insertOrUpdate(entity);
    }

    /**
     * 删除数据
     */
    public void delete(BaseEntity entity){
        entity.setDelete();
        commonWorker.addToQueue(entity);
    }


    private class Worker implements Runnable {
        private BlockingQueue<BaseEntity> queue = new BlockingUniqueQueue<>();

        void addToQueue(BaseEntity ent) {
            this.queue.add(ent);
        }

        @Override
        public void run() {
            while (run.get()) {
                int size = queue.size();
                if (size <= 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignore) {

                    }
                }
                for (int i = 0; i < size && savingDbCounter.getAndIncrement() < MAX_DB_COUNTER; i++) {
                    BaseEntity entity = null;
                    try {
                        entity = queue.take();
                        saveToDb(entity);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }

        }

        void shutDown() {
            for (; ; ) {
                if (!queue.isEmpty()) {
                    saveAllBeforeShutDown();
                } else {
                    break;
                }
            }
        }

        void saveAllBeforeShutDown() {
            while (!queue.isEmpty()) {
                Iterator<BaseEntity> it = queue.iterator();
                while (it.hasNext()) {
                    BaseEntity next = it.next();
                    it.remove();
                    saveToDb(next);
                }
            }
        }

        /**
         * 真正数据持久化
         */
        private void saveToDb(BaseEntity entity) {
            entity.tell(() -> {
                try {
                    entity.doBeforeSave();
                    entity.autoSetStatus();
                    if (entity.isDelete()) {
                        String sql = SqlUtils.getDeleteSql(entity);
                        DbUtils.executeUpdate(sql);
                        entity.resetDbStatus();
                    } else if (entity.isUpdate()) {
                        DbUtils.executePreparedUpdate(entity);
                        entity.resetDbStatus();
                    } else if (entity.isInsert()) {
                        DbUtils.executePreparedInsert(entity);
                        entity.resetDbStatus();
                    }
                } catch (Exception e) {
                    logger.error("", e);
                    // 可能是并发报错，重新放入队列
                    insertOrUpdate(entity);

                } finally {
                    savingDbCounter.decrementAndGet();
                }
            });
        }


    }

    public void shutDown() {
        run.getAndSet(false);
        commonWorker.shutDown();
        logger.error("执行全部命令后关闭");
    }
}
