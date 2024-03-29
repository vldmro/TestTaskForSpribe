package framework.playersApi;

import framework.constants.EndpointConstants;
import framework.constants.FieldsNameConstants;
import framework.models.dto.GetDeletePlayerModel;
import framework.models.dto.PlayerModel;
import framework.utils.ApiUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

public class PlayerControllerApi {

    public static Response createPlayer(PlayerModel player, String editor){
        var requestMap = getRequestMap();
        requestMap.put(FieldsNameConstants.AGE, player.getAge());
        requestMap.put(FieldsNameConstants.GENDER, player.getGender());
        requestMap.put(FieldsNameConstants.LOGIN, player.getLogin());
        requestMap.put(FieldsNameConstants.PASSWORD, player.getPassword());
        requestMap.put(FieldsNameConstants.ROLE, player.getRole());
        requestMap.put(FieldsNameConstants.SCREEN_NAME, player.getScreenName());
        return ApiUtils.methodGetWithAdditionalPath(editor, requestMap, EndpointConstants.CREATE_PLAYER_ENDPOINT);
    }

    public static Response getAllPlayers(){
        return ApiUtils.methodGet(EndpointConstants.GET_ALL_PLAYERS_ENDPOINT);
    }

    public static Response getPlayer(GetDeletePlayerModel getPlayerModel){
        return ApiUtils.methodPost(ContentType.JSON, EndpointConstants.GET_PLAYER_ENDPOINT, getPlayerModel);
    }

    public static Response updatePlayer(String editor, int id, PlayerModel playerModel){
        return ApiUtils.methodPatchWithPathParams(ContentType.JSON, playerModel,
                EndpointConstants.UPDATE_PLAYER_ENDPOINT + editor + "/" + id);
    }

    public static Response deletePlayer(String editor, GetDeletePlayerModel deletePlayerModel){
        return ApiUtils.methodDeleteWithPathParamAndBody(ContentType.JSON, editor,
                deletePlayerModel, EndpointConstants.DELETE_PLAYER_ENDPOINT);
    }

    private static HashMap<String, Object> getRequestMap(){
        return new HashMap<String, Object>();
    }
}
