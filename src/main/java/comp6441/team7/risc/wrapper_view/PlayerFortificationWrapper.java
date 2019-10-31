package comp6441.team7.risc.wrapper_view;

import com6441.team7.risc.api.model.Country;

public class PlayerFortificationWrapper {
	
	Country fromCountry;
	Country toCountry;
	int numSoldiers;
	
	public PlayerFortificationWrapper(Country from, Country to, int n) {
		this.fromCountry=from;
		this.toCountry=to;
		this.numSoldiers=n;
	}

	public Country getCountryFrom() {
		return fromCountry;
	}
	
	public Country getCountryTo() {
		return toCountry;
	}
	
	public int getNumSoldiers() {
		return numSoldiers;
	}
	
}
