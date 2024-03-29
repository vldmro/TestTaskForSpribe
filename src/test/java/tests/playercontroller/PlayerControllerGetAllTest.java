package tests.playercontroller;

import framework.models.dto.PlayerListModel;
import framework.playersApi.PlayerControllerApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.basetest.BaseTest;
import tests.preconditions.PlayerCreatePrecondition;

@Slf4j
public class PlayerControllerGetAllTest extends BaseTest {

    @Test
    public void getCorrectAllPlayersTest(){
        log.info("Step: Send request to get all players. Assert that status cod is OK and list is not empty.");
        var players = PlayerControllerApi.getAllPlayers();
        Assert.assertEquals(players.getStatusCode(), HttpStatus.SC_OK, "Status codes are not equal.");
        var playerList = players.getBody().as(PlayerListModel.class);
        Assert.assertFalse(playerList.getPlayers().isEmpty(), "List is empty.");
    }
}
