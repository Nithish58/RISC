package com6441.team7.risc.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;

/**
 * different commands the game accepts
 */
public enum RiscCommand {
    EDIT_MAP("editmap"),
    EDIT_COUNTRY("editcountry"),
    EDIT_NEIGHBOR("editneighbor"),
    SHOW_MAP("showmap"),
    VALIDATE_MAP("validatemap"),
    EDIT_CONTINENT("editcontinent"),
    SAVE_MAP("savemap"),
    LOAD_MAP("loadmap"),
    GAME_PLAYER("gameplayer"),
    POPULATE_COUNTRY("populatecountries"),
    PLACE_ARMY("placearmy"),
    PLACE_ALL("placeall"),
    REINFORCE("reinforce"),
    FORTIFY("fortify"),
    UNKNOWN("unknown"),
	
	//Added By keshav
	EXIT_MAPEDIT("exitmapedit"),
	SHOW_PLAYER("showplayer"),
	SHOW_ALL_PLAYERS("showallplayers")
	
	
	;
	
    private String name;


    RiscCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RiscCommand parse(String command) {
        String normalizedCommand = StringUtils.deleteWhitespace(command).toLowerCase(Locale.CANADA);
        return Arrays.stream(values())
                .filter(c -> c.getName().equals(normalizedCommand))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
