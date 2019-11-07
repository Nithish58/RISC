package com6441.team7.risc.api.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * Test class for Risc Command test
 *
 */
public class RiscCommandTest {
	
	/**
	 * Command name
	 */
	String command1;
	
	/**
	 * Method called before eacg test method
	 * @throws Exception and error message
	 */
	@Before
	public void setUp() throws Exception {
		command1 = "validatemap";
	}

	/**
	 * Tests if correct command name is output.
	 * Basically tests the parsing method of the commands.
	 */
	@Test
	public void test_commandParser() {
		assertEquals(RiscCommand.VALIDATE_MAP, RiscCommand.parse(command1));
	}

}
