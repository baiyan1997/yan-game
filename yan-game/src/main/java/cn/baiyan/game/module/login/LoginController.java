package cn.baiyan.game.module.login;

import cn.baiyan.GameContext;
import cn.baiyan.game.message.ReqAccountLogin;
import cn.baiyan.hotswap.Person;
import cn.baiyan.hotswap.Student;
import cn.baiyan.message.IdSession;
import cn.baiyan.net.Controller;
import cn.baiyan.net.RequestMapping;
import cn.baiyan.net.SessionManager;

import java.sql.SQLException;

@Controller
public class LoginController {
    @RequestMapping
    public void reqAccountLogin(IdSession session, ReqAccountLogin request) throws SQLException {
        GameContext.loginManager.handleAccountLogin(session, request);
    }

    @RequestMapping
    public void reqStartGoFish(IdSession session, ReqStartGoFish req) throws Exception {
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, req.getUserId());
        SessionManager.INSTANCE.registerNewPlayer(req.getUserId(), session);
        GameContext.mapManager.startGoFish(session, req);
    }

    @RequestMapping
    public void reqStartNextFish(IdSession session, ReqStartNextFish req) throws Exception {
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, req.getUserId());
        SessionManager.INSTANCE.registerNewPlayer(req.getUserId(), session);
        GameContext.mapManager.startNextFish(session, req);
    }

    @RequestMapping
    public void reqBuyItem(IdSession session, ReqBuyItem req) throws Exception {
        //绑定session与玩家id
        String id = req.getId();
        session.setAttribute(IdSession.ID, id);
        SessionManager.INSTANCE.registerNewPlayer(id, session);
        GameContext.shopManager.buy(id, req);
    }

    @RequestMapping
    public void reqSellFish(IdSession session, ReqSellFish req) throws Exception {
        String id = req.getUserId();
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, id);
        SessionManager.INSTANCE.registerNewPlayer(id, session);
        GameContext.shopManager.sell(id, req);
    }

    @RequestMapping
    public void reqUseItem(IdSession session, ReqUseItem req) throws Exception {
        String userId = req.getUserId();
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, userId);
        SessionManager.INSTANCE.registerNewPlayer(userId, session);
        GameContext.itemManager.useItem(userId, req);
    }

    @RequestMapping
    public void reqLoginInfo(IdSession session, ReqLoginInfo req) throws Exception {
        String userId = req.getUserId();
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, userId);
        SessionManager.INSTANCE.registerNewPlayer(userId, session);
        GameContext.loginManager.reqLoginInfo(session, req);
    }

    @RequestMapping
    public void reqChange(IdSession session, ReqChange req) throws Exception {
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, req.getUserId());
        SessionManager.INSTANCE.registerNewPlayer(req.getUserId(), session);
        GameContext.itemManager.reqChange(session, req);
    }

    @RequestMapping
    public void watchAdvertisement(IdSession session, ReqAdvertisement req) throws Exception {
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, req.getUserId());
        SessionManager.INSTANCE.registerNewPlayer(req.getUserId(), session);
        GameContext.mapManager.getItemOrGold(session, req);
    }

    @RequestMapping
    public void upGradeItem(IdSession session, ReqUpGradeItem req) throws Exception {
        //绑定session与玩家id
        session.setAttribute(IdSession.ID, req.getUserId());
        SessionManager.INSTANCE.registerNewPlayer(req.getUserId(), session);
        GameContext.itemManager.upGradeItem(session, req);
    }

}
