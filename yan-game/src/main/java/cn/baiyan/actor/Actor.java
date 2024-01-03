package cn.baiyan.actor;

public interface Actor {

    /**
     * 绑定邮箱
     */
    MailBox mailBox();

    default void tell(Runnable message){
        MailBox mailBox = mailBox();
        mailBox.receive(message);
    }
}
