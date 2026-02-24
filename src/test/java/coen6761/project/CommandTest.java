package coen6761.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for individual command parsing and execution logic in RobotMotion.
 * Verifies that each command character (D, U, R, L, M, P, C, I, H, Q)
 * correctly translates to the intended engine state or output.
 */
class CommandTest extends RobotMotionTestBase {

    @ParameterizedTest(name = "Command {0} correctly updates state")
    @CsvSource({
            "D, penDown, true",
            "U, penDown, false",
            "R, facing, EAST",
            "L, facing, WEST"
    })
    void testStateCommands(String cmd, String property, String expectedValue) {
        invokeCmd(cmd, true);
        RobotMotion.Engine e = getEngine();
        if ("penDown".equals(property)) {
            assertEquals(Boolean.parseBoolean(expectedValue), e.state.penDown);
        } else if ("facing".equals(property)) {
            assertEquals(RobotMotion.Direction.valueOf(expectedValue), e.state.facing);
        }
    }

    @Test
    void testCommandMMovesRobot() {
        invokeCmd("M 3", true);
        assertEquals(3, getEngine().state.y);
    }

    @Test
    void testCommandPPrintsFloor() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);
        String out = captureStdout(() -> invokeCmd("P", true));
        assertTrue(out.contains("*"));
    }

    @Test
    void testCommandCPrintsStatusLine() {
        invokeCmd("D", true);
        String out = captureStdout(() -> invokeCmd("C", true));
        assertTrue(out.contains("Position:"));
        assertTrue(out.contains("Pen: down"));
    }

    @Test
    void testCommandIReinitializes() {
        invokeCmd("I 15", true);
        assertEquals(15, getEngine().floor.n);
    }

    @Test
    void testHistoryRecording() {
        invokeCmd("I 5", true);
        invokeCmd("D", true);
        assertEquals(2, getHistory().size());
        assertEquals("I 5", getHistory().get(0));
    }

    @Test
    void testHistoryNoRecord() {
        invokeCmd("D", false);
        assertEquals(0, getHistory().size());
    }

    @Test
    void testComplexMovementSequence() {
        invokeCmd("I 10", true);
        invokeCmd("D", true);
        invokeCmd("M 4", true);
        invokeCmd("R", true);
        invokeCmd("M 3", true);

        RobotMotion.Engine e = getEngine();
        assertEquals(3, e.state.x);
        assertEquals(4, e.state.y);
    }

    @Test
    void testCommandCaseInsensitivityAndWhitespace() {
        invokeCmd(" i 5 ", true);
        invokeCmd(" d ", true);
        invokeCmd(" m 2 ", true);

        RobotMotion.Engine e = getEngine();
        assertEquals(5, e.floor.n);
        assertTrue(e.state.penDown);
        assertEquals(2, e.state.y);
    }

    @Test
    void testInvalidCommand() {
        String out = captureStdout(() -> invokeCmd("Z", true));
        assertTrue(out.toLowerCase().contains("invalid command"));
    }

    @Test
    void testInvalidMoveArgument() {
        String out = captureStdout(() -> invokeCmd("M abc", true));
        assertTrue(out.toLowerCase().contains("error:"));
    }

    @Test
    void testEmptyCommand() {
        int initialSize = getHistory().size();
        invokeCmd("  ", true);
        assertEquals(initialSize, getHistory().size());
    }
}
