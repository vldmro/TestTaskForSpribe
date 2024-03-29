package tests.playercontroller;

import framework.constants.RequiredConstants;
import framework.models.dto.GetDeletePlayerModel;
import framework.models.dto.PlayerListModel;
import framework.models.dto.PlayerModel;
import framework.playersApi.PlayerControllerApi;
import framework.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.basetest.BaseTest;
import tests.dataproviders.GenderRoleData;
import tests.preconditions.PlayerCreatePrecondition;

@Slf4j
public class PlayerControllerGetTest extends BaseTest {

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void getCorrectPlayerBySupervisorTest(String gender, String role){

        log.info("Step 1: Create model and send a request to create a player.");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");

        log.info("Step 2: Send request to get player by id. Assert that status cod is OK and got the right player.");
        var playerGet = PlayerControllerApi.getPlayer(new GetDeletePlayerModel(playerResponseModel.getId()));
        Assert.assertEquals(playerGet.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        Assert.assertEquals(player.getLogin(), playerResponseModel.getLogin(), "Logins are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void getCorrectPlayerByAdminTest(String gender, String role){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(RequiredConstants.VALID_GENDER_MALE);

        log.info("Step 1: Create model and send a request to create a player.");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, playerAdmin.getLogin());
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");

        log.info("Step 2: Send request to get player by id. Assert that status cod is OK and got the right player.");
        var getPlayerModel = new GetDeletePlayerModel();
        getPlayerModel.setPlayerId(playerResponseModel.getId());
        var playerGet = PlayerControllerApi.getPlayer(getPlayerModel);
        Assert.assertEquals(playerGet.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        Assert.assertEquals(player.getLogin(), playerResponseModel.getLogin(), "Logins are not equal.");
    }

    @Test
    public void getPlayerWithInvalidIdTest(){
        log.info("Step 1: Send request to get player by id. Assert that status cod is SC_NOT_FOUND.");
        var player = PlayerControllerApi.getPlayer(new GetDeletePlayerModel(RequiredConstants.INVALID_ID));
        Assert.assertEquals(player.getStatusCode(), HttpStatus.SC_NOT_FOUND,"Status codes are not equal.");
        var allPlayers = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertTrue(allPlayers.getPlayers().stream().noneMatch(p -> p.getId() != RequiredConstants.INVALID_ID),"Id equals INVALID_ID.");
    }
}
