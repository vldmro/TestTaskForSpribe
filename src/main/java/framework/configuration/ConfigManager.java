package framework.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import framework.constants.RequiredConstants;
import framework.models.ConfigModel;
import framework.utils.FileUtils;
import framework.utils.JsonUtils;

public class ConfigManager {
    private static ConfigModel configValue = null;

    private ConfigManager(){}

    public static ConfigModel getConfigValue(){
        if (configValue == null){
            configValue = JsonUtils.deserializeJsonFromFile(FileUtils.getAbsolutePathToResourceFile(RequiredConstants.API_SETTINGS_FILE),
                    new TypeReference<ConfigModel>() {
                    });
            return configValue;
        }
        return configValue;
    }
}
