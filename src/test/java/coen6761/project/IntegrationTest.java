package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests verifying multi-step command sequences
 * and the history replay functionality.
 */
class IntegrationTest extends RobotMotionTestBase {

    @Test
    void testSquarePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        e.state.facing = e.state.facing.right();
        e.move(3);
        e.state.facing = e.state.facing.right();
        e.move(3);
        e.state.facing = e.state.facing.right();
        e.move(3);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testLShapePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(2);
        assertEquals(2, e.state.x);
        assertEquals(4, e.state.y);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][4]);
        assertEquals(1, e.floor.grid[2][4]);
    }

    @Test
    void testHistoryReplayHReplaysCommandsFromStart() {
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);
        assertEquals(2, getEngine().state.x);
        assertEquals(3, getEngine().state.y);
        String out = captureStdout(() -> invokeCmd("H", true));
        assertEquals(2, getEngine().state.x);
        assertEquals(3, getEngine().state.y);
        assertTrue(out.contains(">Replaying:"));
        assertTrue(getHistory().contains("H"));
    }
}
