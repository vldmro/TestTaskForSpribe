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
public class PlayerControllerDeleteTest extends BaseTest {

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void deletePlayerBySupervisorTest(String gender, String role){

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is NO_CONTENT and the player was deleted");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        var deletePlayer = PlayerControllerApi.deletePlayer(RequiredConstants.EDITOR_SUPERVISOR,
                new GetDeletePlayerModel(playerResponseModel.getId()));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertNotEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are equal.");
        Assert.assertFalse(playersAfterDelete.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void deletePlayerByAdminTest(String gender, String role){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(RequiredConstants.VALID_GENDER_MALE);

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, playerAdmin.getLogin());
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is NO_CONTENT and the player was deleted");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        var deletePlayer = PlayerControllerApi.deletePlayer(playerAdmin.getLogin(),
                new GetDeletePlayerModel(playerResponseModel.getId()));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertNotEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are equal.");
        Assert.assertFalse(playersAfterDelete.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void deletePlayerUserByAnotherUserTest(String gender, String role){
        log.info("Precondition: Create user with role user");
        var playerUser = PlayerCreatePrecondition.createUserPlayer(gender);

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and the player was not deleted");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        var deletePlayer = PlayerControllerApi.deletePlayer(playerUser.getLogin(),
                new GetDeletePlayerModel(playerResponseModel.getId()));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertNotEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are equal");
        Assert.assertTrue(playersAfterDelete.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void deletePlayerUserBySameUserTest(String gender, String role){

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and the player was deleted");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        var deletePlayer = PlayerControllerApi.deletePlayer(playerResponseModel.getLogin(),
                new GetDeletePlayerModel(playerResponseModel.getId()));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertNotEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are equal.");
        Assert.assertTrue(playersAfterDelete.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are not equal.");
    }

    @Test
    public void deleteInvalidPlayerBySupervisorTest(){
        log.info("Step 1: Send a request to get all players.");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is FORBIDDEN");
        var deletePlayer = PlayerControllerApi.deletePlayer(RequiredConstants.EDITOR_SUPERVISOR, new GetDeletePlayerModel(RequiredConstants.INVALID_ID));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are not equal.");
    }

    @Test
    public void deleteInvalidPlayerByAdminTest(){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(RequiredConstants.VALID_GENDER_MALE);

        log.info("Step 1: Send a request to get all players.");
        var playersBeforeDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status cod is FORBIDDEN");
        var deletePlayer = PlayerControllerApi.deletePlayer(playerAdmin.getLogin(), new GetDeletePlayerModel(RequiredConstants.INVALID_ID));
        Assert.assertEquals(deletePlayer.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status codes are not equal.");
        var playersAfterDelete = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertEquals(playersBeforeDelete.getPlayers().size(), playersAfterDelete.getPlayers().size(), "Lists are not equal");
    }
}
