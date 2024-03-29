package tests.playercontroller;

import framework.constants.RequiredConstants;
import framework.models.dto.GetDeletePlayerModel;
import framework.models.dto.PlayerListModel;
import framework.models.dto.PlayerModel;
import framework.playersApi.PlayerControllerApi;
import framework.utils.DataUtils;
import framework.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.basetest.BaseTest;
import tests.dataproviders.GenderRoleData;
import tests.preconditions.PlayerCreatePrecondition;

@Slf4j
public class PlayerControllerCreateTest extends BaseTest {
    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createValidPlayerBySupervisorTest(String gender, String role){

        log.info("Step 1: Create model");
        var player = DataUtils.getNewPlayerData(gender, role);

        log.info("Step 2: Send a request to create a user. Assert that status code is OK and the right player was created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseRequestModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertNotNull(playerResponseRequestModel.getLogin(), "The login from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getRole(), "The role from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getPassword(), "The password from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getGender(), "The gender from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getScreenName(), "The screenName from the response is null.");

        log.info("Step 3: Send a request to get a user. Assert that status code is OK and the right player was got");
        var playerResponseModel = playerResponse.getBody().as(PlayerModel.class);
        var playerGet = PlayerControllerApi.getPlayer(new GetDeletePlayerModel(playerResponseModel.getId()));
        var playerGetModel = playerGet.getBody().as(PlayerModel.class);
        Assert.assertEquals(player.getLogin(), playerGetModel.getLogin(), "Logins are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createValidPlayerByAdminTest(String gender, String role){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(gender);

        log.info("Step 1: Create model");
        var player = DataUtils.getNewPlayerData(gender, role);

        log.info("Step 2: Send a request to create a user. Assert that status code is OK and the right player was created");
        var playerResponse = PlayerControllerApi.createPlayer(player, playerAdmin.getLogin());
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerResponseRequestModel = playerResponse.getBody().as(PlayerModel.class);
        Assert.assertNotNull(playerResponseRequestModel.getLogin(), "The login from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getRole(), "The role from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getPassword(), "The password from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getGender(), "The gender from the response is null.");
        Assert.assertNotNull(playerResponseRequestModel.getScreenName(), "The screenName from the response is null.");

        log.info("Step 3: Send a request to get a user. Assert that status code is OK and the right player was got");
        var playerGet = PlayerControllerApi.getPlayer(new GetDeletePlayerModel(playerResponseRequestModel.getId()));
        var playerGetModel = playerGet.getBody().as(PlayerModel.class);
        Assert.assertEquals(player.getLogin(), playerGetModel.getLogin(), "Logins are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerByUserTest(String gender, String role){
        log.info("Precondition: Create user with role user");
        var playerUser = PlayerCreatePrecondition.createUserPlayer(gender);

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(gender, role);
        var playersListBeforeCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is FORBIDDEN and the player was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, playerUser.getLogin());
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_FORBIDDEN, "Status codes are not equal.");
        var playersListAfterCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertTrue(playersListBeforeCreate.getPlayers().size() == playersListAfterCreate.getPlayers().size(),
                "Lists are not equal.");
        Assert.assertFalse(playersListAfterCreate.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are equal");
    }

    @Test
    public void createInvalidPlayerBySupervisorTest(){

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(RequiredConstants.INVALID_GENDER, RequiredConstants.INVALID_ROLE);
        var playersListBeforeCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and the player was not created.");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
        var playersListAfterCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertTrue(playersListBeforeCreate.getPlayers().size() == playersListAfterCreate.getPlayers().size(),
                "List are not equal.");
        Assert.assertFalse(playersListAfterCreate.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are equal.");
    }

    @Test
    public void createInvalidPlayerByAdminTest(){
        log.info("Precondition: Create admin user");
        var playerAdmin = PlayerCreatePrecondition.createAdminPlayer(RequiredConstants.VALID_GENDER_MALE);

        log.info("Step 1: Create model and Send a request to get all players");
        var player = DataUtils.getNewPlayerData(RequiredConstants.INVALID_GENDER, RequiredConstants.INVALID_ROLE);
        var playersListBeforeCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and the player was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, playerAdmin.getLogin());
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
        var playersListAfterCreate = PlayerControllerApi.getAllPlayers().getBody().as(PlayerListModel.class);
        Assert.assertTrue(playersListBeforeCreate.getPlayers().size() == playersListAfterCreate.getPlayers().size(), "List are not equal.");
        Assert.assertFalse(playersListAfterCreate.getPlayers().stream().anyMatch(p -> p.getLogin() != null && p.getLogin().equals(player.getLogin())),
                "Logins are equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerWithLessThenRequiredPasswordLengthTest(String gender, String role){

        log.info("Step 1: Create model with password less then required");
        var player = DataUtils.getNewPlayerData(gender, role);
        player.setPassword(StringUtils.getRandomAlphanumericStringWithGap(1, 6));

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerWithMoreThenRequiredPasswordLengthTest(String gender, String role){

        log.info("Step 1: Create model with password more then required");
        var player = DataUtils.getNewPlayerData(gender, role);
        player.setPassword(StringUtils.getRandomAlphanumericStringWithGap(RequiredConstants.INVALID_MIN_PASSWORD_COUNT,
                RequiredConstants.INVALID_MAX_PASSWORD_COUNT));

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerWithLessThenRequiredAgeTest(String gender, String role){

        log.info("Step 1: Create model with age less then required");
        var player = DataUtils.getNewPlayerData(gender, role);
        player.setAge(RequiredConstants.INVALID_AGE_YOUNG);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerWithMoreThenRequiredAgeTest(String gender, String role){

        log.info("Step 1: Create model");
        var player = DataUtils.getNewPlayerData(gender, role);
        player.setAge(RequiredConstants.INVALID_AGE_OLD);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayersSameLoginTest(String gender, String role){

        log.info("Step 1: Create model for first player");
        var firstPlayer = DataUtils.getNewPlayerData(gender, role);

        log.info("Step 2: Send a request to create a user. Assert that status code is OK and the right player was created");
        var firstPlayerResponse = PlayerControllerApi.createPlayer(firstPlayer, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(firstPlayerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var firstPlayerResponseModel = firstPlayerResponse.getBody().as(PlayerModel.class);

        log.info("Step 3: Create model for second player with same login");
        var secondPlayer = DataUtils.getNewPlayerData(gender, role);
        secondPlayer.setLogin(firstPlayerResponseModel.getLogin());

        log.info("Step 4: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var secondPlayerResponse = PlayerControllerApi.createPlayer(secondPlayer, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(secondPlayerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayersSameScreenNameTest(String gender, String role){

        log.info("Step 1: Create model");
        var firstPlayer = DataUtils.getNewPlayerData(gender, role);

        log.info("Step 2: Send a request to create a user. Assert that status code is OK and the right player was created");
        var firstPlayerResponse = PlayerControllerApi.createPlayer(firstPlayer, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(firstPlayerResponse.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var firstPlayerResponseModel = firstPlayerResponse.getBody().as(PlayerModel.class);

        log.info("Step 3: Create model for second player with same screen name");
        var secondPlayer = DataUtils.getNewPlayerData(gender, role);
        secondPlayer.setScreenName(firstPlayerResponseModel.getScreenName());

        log.info("Step 4: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var secondPlayerResponse = PlayerControllerApi.createPlayer(secondPlayer, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(secondPlayerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }

    @Test(dataProvider = "genderRoleData", dataProviderClass = GenderRoleData.class)
    public void createPlayerWithInvalidGenderTest(String gender, String role){

        log.info("Step 1: Create model");
        var player = DataUtils.getNewPlayerData(gender, role);
        player.setGender(RequiredConstants.INVALID_GENDER);

        log.info("Step 2: Send a request to create a user. Assert that status code is BAD REQUEST and user was not created");
        var playerResponse = PlayerControllerApi.createPlayer(player, RequiredConstants.EDITOR_SUPERVISOR);
        Assert.assertEquals(playerResponse.getStatusCode(), HttpStatus.SC_BAD_REQUEST, "Status codes are not equal.");
    }
}
