package cn.baiyan.cross;

import org.apache.mina.core.session.AttributeKey;

public class MinaSessionProperties {

    /**
     * 洪水检查记录
     */
    AttributeKey FLOOD = new AttributeKey(MinaSessionProperties.class, "FLOOD");

    /**
     * 业务session
     */
    AttributeKey UserSession = new AttributeKey(MinaSessionProperties.class, "GameSession");


}
