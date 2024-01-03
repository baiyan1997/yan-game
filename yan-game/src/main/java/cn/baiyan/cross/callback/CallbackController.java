package cn.baiyan.cross.callback;


import cn.baiyan.cross.CCSession;
import cn.baiyan.cross.CrossController;
import cn.baiyan.cross.SCSession;
import cn.baiyan.message.Message;
import cn.baiyan.net.RequestMapping;

@CrossController
public class CallbackController {

    public CallbackController() {
        // 初始化
        CallBackService.getInstance();
    }

    @RequestMapping
    public void onReqCallBack(SCSession session, G2FCallBack req) {
        int cmdType = req.getCommand();
        CallbackHandler handler = CallbackHandler.queryHandler(cmdType);
        if (handler != null) {
            req.deserialize();
            handler.onRequest(session, req);
        }
    }

    @RequestMapping
    public void onRespCallBack(CCSession session, F2GCallBack response) {
        try {
            Message callback = response.getMessage();
            CallBackService.getInstance().fillCallBack(response.getIndex(), callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}