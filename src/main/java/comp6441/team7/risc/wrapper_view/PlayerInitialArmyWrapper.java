package comp6441.team7.risc.wrapper_view;

import com6441.team7.risc.api.model.Player;

public class PlayerInitialArmyWrapper {
	
	private Player player;
	
	public PlayerInitialArmyWrapper(Player p) {
		this.player=p;
	}
	
	public Player getPlayer() {
		return player;
	}

}
