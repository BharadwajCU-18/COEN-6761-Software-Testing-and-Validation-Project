package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Floor class, focusing on initialization and
 * coordinate boundary verification.
 */
class FloorTest {

    @Test
    void testInitialFloorBlank() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }

    @Test
    void testFloorPrintDoesNotThrow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        assertDoesNotThrow(() -> e.floor.print());
    }

    @Test
    void testMinimumGridSize() {
        RobotMotion.Engine e = new RobotMotion.Engine(1);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        e.move(5);
        assertEquals(0, e.state.y); // can't move on 1x1 grid
    }

    @Test
    void testLargeGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(100);
        e.move(99);
        assertEquals(99, e.state.y);
    }

    @Test
    void testInvalidInitializeSizeRejected() {
        assertThrows(NegativeArraySizeException.class, () -> new RobotMotion.Engine(-5));
        RobotMotion.Engine e0 = new RobotMotion.Engine(0);
        assertFalse(e0.floor.inBounds(0, 0));
    }
}
