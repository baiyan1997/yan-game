package cn.baiyan.game.module.login;

import java.util.ArrayList;
import java.util.List;

public class AccountProfile {

    PlayerProfile player = new PlayerProfile();

    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayers(PlayerProfile player) {
        this.player = player;
    }

}
