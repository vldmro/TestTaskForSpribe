package tests.playercontroller;

import framework.constants.RequiredConstants;
import framework.models.dto.PlayerListModel;
import framework.models.dto.PlayerModel;
import framework.utils.DataUtils;
import framework.playersApi.PlayerControllerApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.basetest.BaseTest;
import tests.dataproviders.GenderRoleData;
import tests.preconditions.PlayerCreatePrecondition;

@Slf4j
public class PlayerControllerUpdateTest extends BaseTest {

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void updatePlayerBySupervisorTest(String gender, String role){

        log.info("Step 1: Create model and Send a request to create player");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        var playerResponseRequestModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");

        log.info("Step 1: Create update model and Send a request to update player. Assert that status cod is OK and player was updated");
        var playerUpdateModel = DataUtils.getNewPlayerData(gender, role);
        var playerUpdate = PlayerControllerApi.updatePlayer(RequiredConstants.EDITOR_SUPERVISOR,
                playerResponseRequestModel.getId(),playerUpdateModel);
        Assert.assertEquals(playerUpdate.getStatusCode(), HttpStatus.SC_OK);
        var playerUpdateRequestModel = playerUpdate.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerUpdateModel.getLogin(), playerUpdateRequestModel.getLogin(), "Logins are not equal.");
        Assert.assertEquals(playerUpdateModel.getGender(), playerUpdateRequestModel.getGender(), "Genders are not equal.");
        Assert.assertEquals(playerUpdateModel.getAge(), playerUpdateRequestModel.getAge(), "Ages are not equal.");
        Assert.assertEquals(playerUpdateModel.getScreenName(), playerUpdateRequestModel.getScreenName(), "Screen names are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void updatePlayerByAdminTest(String gender, String role){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(RequiredConstants.VALID_GENDER_MALE);

        log.info("Step 1: Create model and Send a request to create player");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        var playerResponseRequestModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");

        log.info("Step 1: Create update model and Send a request to update player. Assert that status cod is OK and player was updated");
        var playerUpdateModel = DataUtils.getNewPlayerData(gender, role);
        var playerUpdate = PlayerControllerApi.updatePlayer(playerAdmin.getLogin(),
                playerResponseRequestModel.getId(),playerUpdateModel);
        Assert.assertEquals(playerUpdate.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerUpdateRequestModel = playerUpdate.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerUpdateModel.getLogin(), playerUpdateRequestModel.getLogin(), "Logins are not equal.");
        Assert.assertEquals(playerUpdateModel.getGender(), playerUpdateRequestModel.getGender(), "Genders are not equal.");
        Assert.assertEquals(playerUpdateModel.getAge(), playerUpdateRequestModel.getAge(), "Ages are not equal.");
        Assert.assertEquals(playerUpdateModel.getScreenName(), playerUpdateRequestModel.getScreenName(), "Screen names are not equal.");
    }

    @Test(dataProvider = "genderWithOnlyUserRoleData", dataProviderClass = GenderRoleData.class)
    public void updatePlayerUserBySameUserAndRoleTest(String gender, String role){
        log.info("Precondition: Create user with role user");
        var playerUser = PlayerCreatePrecondition.createUserPlayer(gender);

        log.info("Step 1: Create update model and Send a request to update player. Assert that status cod is OK and player was updated");
        var playerUpdateModel = DataUtils.getNewPlayerData(gender, role);
        var playerUpdate = PlayerControllerApi.updatePlayer(playerUser.getLogin(),
                playerUser.getId(),playerUpdateModel);
        Assert.assertEquals(playerUpdate.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerUpdateRequestModel = playerUpdate.getBody().as(PlayerModel.class);
        Assert.assertEquals(playerUpdateModel.getLogin(), playerUpdateRequestModel.getLogin(), "Logins are not equal.");
        Assert.assertEquals(playerUpdateModel.getGender(), playerUpdateRequestModel.getGender(), "Genders are not equal.");
        Assert.assertEquals(playerUpdateModel.getAge(), playerUpdateRequestModel.getAge(), "Ages are not equal.");
        Assert.assertEquals(playerUpdateModel.getScreenName(), playerUpdateRequestModel.getScreenName(), "Screen names are not equal.");
    }

    @Test(dataProvider = "genderWithOnlyAdminRoleData", dataProviderClass = GenderRoleData.class)
    public void updatePlayerAdminByUserTest(String gender, String role){
        log.info("Precondition: Create user with role user");
        var playerUser = PlayerCreatePrecondition.createUserPlayer(gender);

        log.info("Step 1: Create update model and Send a request to update player. Assert that status cod is OK and player was updated");
        var playerUpdateModel = DataUtils.getNewPlayerData(gender, role);
        var playerUpdate = PlayerControllerApi.updatePlayer(playerUser.getLogin(),
                playerUser.getId(), playerUpdateModel);
        Assert.assertEquals(playerUpdate.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void updatePlayerWithInvalidIdTest(String gender, String role){
        log.info("Step: Create model and Send a request to update player. Assert that status cod is FORBIDDEN and player was not updated");
        var playerUpdateModel = DataUtils.getNewPlayerData(gender, role);
        var playerUpdate = PlayerControllerApi.updatePlayer(RequiredConstants.EDITOR_SUPERVISOR,
                RequiredConstants.INVALID_ID, playerUpdateModel);
        Assert.assertEquals(playerUpdate.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status codes are not equal.");
        var allPlayers = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertTrue(allPlayers.getPlayers().stream().noneMatch(p -> p.getId() != RequiredConstants.INVALID_ID), "Id equal INVALID_ID.");
    }
}
