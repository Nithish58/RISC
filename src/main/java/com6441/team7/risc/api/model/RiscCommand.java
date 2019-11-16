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
    EXCHANGE_CARD("exchangecards"),
    FORTIFY("fortify"),
    ATTACK("attack"),
    UNKNOWN("unknown"),
    
    SHOW_CARDS("showcards"),
    
    //ATTACK Commands
    DEFEND("defend"),
    ATTACKMOVE("attackmove"),


	EXIT_MAPEDIT("exitmapedit"),
	SHOW_PLAYER("showplayer"),
	SHOW_PLAYER_ALL_COUNTRIES("showplayerallcountries"),
	SHOW_PLAYER_COUNTRIES("showplayercountries"),
	SHOW_ALL_PLAYERS("showallplayers"),
	SHOW_FILE("showfile"),
	EXIT("exit")	
	;

    /**
     * the command name
     */
    private String name;

    /**
     * Game command
     * @param name command
     */
    RiscCommand(String name) {
        this.name = name;
    }

    /**
     * get name of command
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Return normalized commands
     * @param command strings of commands
     * @return arrays of command
     */
    public static RiscCommand parse(String command) {
        String normalizedCommand = StringUtils.deleteWhitespace(command).toLowerCase(Locale.CANADA);
        return Arrays.stream(values())
                .filter(c -> c.getName().equals(normalizedCommand))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
