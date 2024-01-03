package cn.baiyan.cross.callback;


import cn.baiyan.message.Message;

public interface RequestCallback {

    /**
     * 请求方接受回调消息的业务处理
     *
     * @param callBack
     */
    void onSuccess(Message callBack);

    void onError(Throwable error);

}
