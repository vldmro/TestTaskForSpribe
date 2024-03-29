package framework.utils;

import framework.models.dto.PlayerModel;

import static framework.constants.RequiredConstants.*;

public class DataUtils {

    public static PlayerModel getNewPlayerData(String gender, String role){
        return new PlayerModel(RandomUtils.randomValidAgeString(), gender,
                "Automation" + StringUtils.getRandomAlphanumericString(LOGIN_COUNT),
                StringUtils.getRandomAlphanumericStringWithGap(MIN_PASSWORD_COUNT, MAX_PASSWORD_COUNT),
                role, "Automation" + StringUtils.getRandomAlphabeticString(LOGIN_COUNT));
    }

}
