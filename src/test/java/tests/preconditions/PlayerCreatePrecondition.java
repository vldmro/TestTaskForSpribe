package tests.preconditions;

import framework.constants.RequiredConstants;
import framework.models.dto.PlayerModel;
import framework.playersApi.PlayerControllerApi;
import framework.utils.DataUtils;

public class PlayerCreatePrecondition {

    public static PlayerModel createAdminPlayer(String gender){
        var playerAdmin = DataUtils.getNewPlayerData(gender, RequiredConstants.EDITOR_ADMIN);
        return PlayerControllerApi.createPlayer(playerAdmin, RequiredConstants.EDITOR_SUPERVISOR).getBody().as(PlayerModel.class);
    }

    public static PlayerModel createUserPlayer(String gender){
        var playerUser = DataUtils.getNewPlayerData(gender, RequiredConstants.EDITOR_USER);
        return PlayerControllerApi.createPlayer(playerUser, RequiredConstants.EDITOR_SUPERVISOR).getBody().as(PlayerModel.class);
    }

}
