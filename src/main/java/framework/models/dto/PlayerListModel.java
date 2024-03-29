package framework.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayerListModel {
    private List<PlayerModel> players;
}
