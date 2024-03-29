package framework.utils;

import framework.constants.RequiredConstants;
import framework.models.dto.GetDeletePlayerModel;
import framework.models.dto.PlayerListModel;
import framework.playersApi.PlayerControllerApi;

public class CleanUtils {

    public static void cleanData(){
        PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class)
                .getPlayers().stream().filter(p -> p.getScreenName() != null && p.getScreenName().contains("Automation"))
                .forEach(p -> PlayerControllerApi.deletePlayer(RequiredConstants.EDITOR_SUPERVISOR, new GetDeletePlayerModel(p.getId())));
    }

}
