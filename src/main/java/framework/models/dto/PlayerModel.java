package framework.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerModel {
    private int age;
    private String gender;
    private int id;
    private String login;
    private String password;
    private String role;
    private String screenName;

    public PlayerModel(){}

    public PlayerModel(int age, String gender, String login, String password, String role, String screenName){
        this.age = age;
        this.gender = gender;
        this.login = login;
        this.password = password;
        this.role = role;
        this.screenName = screenName;
    }

}
