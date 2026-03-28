package coen6761.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class QATask3Test extends RobotMotionTestBase {

    @Test
    void testInitializeShouldClearHistory() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);

        assertFalse(getHistory().isEmpty(), "History should contain commands before reinitialization");

        invokeCmd("I 5", true);

        assertEquals(0, getHistory().size(), "History should be cleared after initialization");
    }

    @Test
    void testQuitShouldNotBeStoredInHistory() {
        invokeCmd("D", true);
        invokeCmd("Q", true);

        assertFalse(getHistory().contains("Q"), "Q command should not be stored in history");
    }

    @Test
    void testReplayShouldNotBeStoredInHistory() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);
        invokeCmd("H", true);

        assertFalse(getHistory().contains("H"), "H command should not be stored in history");
    }

    @Test
    void testInvalidCommandShouldNotChangeState() {
        int xBefore = getEngine().state.x;
        int yBefore = getEngine().state.y;
        boolean penBefore = getEngine().state.penDown;
        RobotMotion.Direction facingBefore = getEngine().state.facing;

        invokeCmd("Z", true);

        assertEquals(xBefore, getEngine().state.x);
        assertEquals(yBefore, getEngine().state.y);
        assertEquals(penBefore, getEngine().state.penDown);
        assertEquals(facingBefore, getEngine().state.facing);
    }

    @Test
    void testReplayRestoresSameFinalState() {
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);

        int finalX = getEngine().state.x;
        int finalY = getEngine().state.y;
        RobotMotion.Direction finalFacing = getEngine().state.facing;

        invokeCmd("H", true);

        assertEquals(finalX, getEngine().state.x);
        assertEquals(finalY, getEngine().state.y);
        assertEquals(finalFacing, getEngine().state.facing);
    }

    @Test
    void testInBoundsConditionCombinations() {
        RobotMotion.Floor floor = new RobotMotion.Floor(5);

        assertTrue(floor.inBounds(0, 0));
        assertFalse(floor.inBounds(-1, 0));
        assertFalse(floor.inBounds(5, 0));
        assertFalse(floor.inBounds(0, -1));
        assertFalse(floor.inBounds(0, 5));
    }

    @Test
    void testMoveStopsAtBoundaryWithoutCrossing() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.NORTH;

        e.move(100);

        assertEquals(0, e.state.x);
        assertEquals(4, e.state.y);
    }

    @Test
    void testMoveWithNegativeStepsDoesNothing() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);

        int xBefore = e.state.x;
        int yBefore = e.state.y;

        e.move(-3);

        assertEquals(xBefore, e.state.x);
        assertEquals(yBefore, e.state.y);
    }

    @Test
    void testReplayPrintsCommands() {
        invokeCmd("D", true);
        invokeCmd("M 1", true);

        String out = captureStdout(() -> invokeCmd("H", true));

        assertTrue(out.contains(">Replaying:"), "Replay should print replayed commands");
    }
}