package comp6441.team7.risc.wrapper_view;

import com6441.team7.risc.api.model.Player;

public class PlayerChangeWrapper {
	
	private Player currentPlayer;
	
	public PlayerChangeWrapper(Player player) {
		this.currentPlayer=player;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	

}
