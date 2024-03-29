package framework.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDeletePlayerModel {
    private int playerId;

    public GetDeletePlayerModel(){
    }

    public GetDeletePlayerModel(int playerId){
        this.playerId = playerId;
    }
}
