package com6441.team7.risc.api;

/**
 * a class for the constants in the RISC game
 */
public final class RiscConstants {

    /**
     * private constructor
     */
    private RiscConstants(){}

    //End of Line https://knowledge.ni.com/KnowledgeArticleDetails?id=kA00Z0000019KZDSA2
    /**
     * a static final reference for end of line
     */
    public static final String EOL = "\r\n";

    /**
     * a static final reference of whitespace
     */
    public static final String WHITESPACE = " ";

    /**
     * a static final reference of new line
     */
    public static final String NEWLINE = "\n";

    /**
     * a static final reference of phase view string
     */
    public static final String PHASE_VIEW_STRING="Phase View: ";


    /**
     * a static final reference of domination view string
     */
    public static final String DOMINATION_VIEW_STRING="Domination View: ";


    /**
     * a static final reference of card exchange view string
     */
    public static final String CARD_EXCHANGE_VIEW_STRING="Card Exchange View: ";


    /**
     * a static final reference of maximum number of player
     */
    public static final int MAX_NUM_PLAYERS=9;
    
    /**
     * a static final reference of minimum allowed number of attacking armies in a country
     */
    public static final int MIN_ATTACKING_SOLDIERS=2;

    /**
     * a static final reference for maximum allowed number of dice(s) for attacker to roll 
     */
    public static final int MAX_ATTACKER_DICE_NUM=3;
    
    /**
     * a static final reference for maximum allowed number of dice(s) for defender to roll
     */
    public static final int MAX_DEFENDER_DICE_NUM=2;

    /**
     * a static final reference for ASSIGNMENT
     */
    public static final String ASSIGNMENT = "=";

    /**
     * a static final reference for comma
     */
    public static final String COMMA = ",";

    /**
     * a static final reference string non-exist
     */
    public static final String NON_EXIST = "non_exist";
}
