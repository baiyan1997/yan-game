package cn.baiyan.game;

public interface Modules {
    // ------------------底层功能支持模块（从0到100）-----------------

    short BASE = 1;

    short GM = 2;

    short NOTICE = 3;

    // ------------------业务功能模块（101开始）---------------------

    /**
     * 登录
     */
    short LOGIN = 101;

    /**
     * 注册
     */
    short REGISTER = 102;

    /**
     * 购买物品
     */
    short BUY = 103;
    /**
     * 卖鱼
     */
    short SELL = 104;
    /**
     * 技能
     */
    short FISH = 105;
    /**
     * 使用道具
     */
    short USE = 106;
    /**
     * 钓鱼第二次
     */
    short NEXT_FISH = 107;
    /**
     * 登录信息
     */
    short LOGIN_INFO = 108;

    /**
     * 兑换道具
     */
    short CHANGE = 109;

    /**
     * 看广告
     */
    short WATCH = 110;

    /**
     * 道具升级
     */
    short ITEM_UP_GRADE = 111;

    // ------------------跨服业务功能模块（300开始）---------------------
    /**
     * 跨服基础
     */
    short CROSS = 300;
    /**
     * 跨服玩法
     */
    short CROSS_BUSINESS = 301;
}
