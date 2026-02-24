package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the core Engine logic.
 * Focuses on coordinate calculations, pen marking logic,
 * boundary stopping, and path rendering.
 */
class EngineTest {

    @Test
    void testInitialState() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertFalse(e.state.penDown);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    @Test
    void testMoveWithPenUp() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(3);
        assertEquals(3, e.state.y);
        // Ensure no marking happens
        for (int y = 0; y <= 3; y++) {
            assertEquals(0, e.floor.grid[0][y]);
        }
    }

    @Test
    void testMoveWithPenDown() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        assertEquals(3, e.state.y);
        // Verify path is marked
        for (int y = 0; y <= 3; y++) {
            assertEquals(1, e.floor.grid[0][y], "Cell (0," + y + ") should be marked");
        }
        assertEquals(0, e.floor.grid[0][4]);
    }

    @Test
    void testMovementDirections() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);

        // Move North
        e.move(2);
        assertEquals(2, e.state.y);

        // Move East
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(2);
        assertEquals(2, e.state.x);

        // Move South
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(1);
        assertEquals(1, e.state.y);

        // Move West
        e.state.facing = RobotMotion.Direction.WEST;
        e.move(1);
        assertEquals(1, e.state.x);
    }

    @Test
    void testEdgeCases() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);

        // Move beyond edge
        e.move(10);
        assertEquals(4, e.state.y);

        // Move zero steps
        int prevX = e.state.x;
        int prevY = e.state.y;
        e.move(0);
        assertEquals(prevX, e.state.x);
        assertEquals(prevY, e.state.y);

        // Negative steps (should do nothing)
        e.move(-5);
        assertEquals(prevX, e.state.x);
        assertEquals(prevY, e.state.y);
    }

    @Test
    void testPenToggleAndOverlapping() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(2); // (0,0) to (0,2)

        e.state.penDown = false;
        e.move(2); // (0,2) to (0,4)

        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(0, e.floor.grid[0][3]);

        // Overlapping path
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(2); // (0,4) back to (0,2)
        assertEquals(1, e.floor.grid[0][3]);
    }

    @Test
    void testPrintOutput() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(3);

        String out = e.floor.print();
        assertTrue(out.contains(" 0"));
        assertTrue(out.contains(" 9"));
        assertTrue(out.contains(" 4 |"));
        assertTrue(out.contains("*"));

        long starCount = out.chars().filter(ch -> ch == '*').count();
        assertEquals(8, starCount); // (0,0->4) = 5 stars + (1->3,4) = 3 stars = 8
    }

    @Test
    void testEngineReset() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);

        // Simulating the 'I' command effect (new instance)
        e = new RobotMotion.Engine(10);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertFalse(e.state.penDown);
        assertEquals(10, e.floor.n);
    }
}
