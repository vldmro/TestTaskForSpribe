package tests.dataproviders;

import org.testng.annotations.DataProvider;

public class GenderRoleData {

    @DataProvider(name = "genderRoleData")
    public Object[][] genderRoleData(){
        return new Object[][]{
                {"male", "admin"},
                {"female", "admin"},
                {"male", "user"},
                {"female", "user"}
        };
    }

    @DataProvider(name = "genderWithOnlyUserRoleData")
    public Object[][] genderWithOnlyUserRoleData(){
        return new Object[][]{
                {"male", "user"},
                {"female", "user"}
        };
    }

    @DataProvider(name = "genderWithOnlyAdminRoleData")
    public Object[][] genderWithOnlyAdminRoleData(){
        return new Object[][]{
                {"male", "user"},
                {"female", "user"}
        };
    }
}
