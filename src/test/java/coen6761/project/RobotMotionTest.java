package coen6761.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RobotMotionTest {

    // DIRECTION OR TURNING

    @Test
    void testInitialDirectionIsNorth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    @Test
    void testTurnRight() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
    }

    @Test
    void testTurnLeft() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.left();
        assertEquals(RobotMotion.Direction.WEST, e.state.facing);
    }

    @Test
    void testFullRotationRight() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.right().right().right().right();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    @Test
    void testFullRotationLeft() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.left().left().left().left();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    @Test
    void testTurnRightThenLeft() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.right().left();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    @Test
    void testAllDirectionsClockwise() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.SOUTH, e.state.facing);
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.WEST, e.state.facing);
    }

    // INITIAL STATE

    @Test
    void testInitialPosition() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testInitialPenUp() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertFalse(e.state.penDown);
    }

    @Test
    void testInitialFloorBlank() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }

    // PEN UP OR PEN DOWN

    @Test
    void testMoveWithPenUp() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = false;
        e.move(3);
        assertEquals(3, e.state.y);
        assertEquals(0, e.floor.grid[0][0]);
        assertEquals(0, e.floor.grid[0][1]);
        assertEquals(0, e.floor.grid[0][2]);
    }

    @Test
    void testMoveWithPenDown() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        assertEquals(3, e.state.y);
        assertEquals(1, e.floor.grid[0][0]);
    }

    @Test
    void testPenDownMarksAllCells() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        // Should mark cells (0,0), (0,1), (0,2), (0,3)
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(1, e.floor.grid[0][3]);
    }

    @Test
    void testPenToggle() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(2);
        e.state.penDown = false;
        e.move(2);
        // First 3 cells marked, last 2 not
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(0, e.floor.grid[0][3]);
        assertEquals(0, e.floor.grid[0][4]);
    }

    // MOVEMENT IN ALL DIRECTIONS

    @Test
    void testMoveNorth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(3);
        assertEquals(0, e.state.x);
        assertEquals(3, e.state.y);
    }

    @Test
    void testMoveEast() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(3);
        assertEquals(3, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testMoveSouth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(4); // go north to (0,4)
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(2);
        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
    }

    @Test
    void testMoveWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(4); // go east to (4,0)
        e.state.facing = RobotMotion.Direction.WEST;
        e.move(2);
        assertEquals(2, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testBoundaryStopNorth() {
        RobotMotion.Engine e = new RobotMotion.Engine(2);
        e.move(10);
        assertEquals(1, e.state.y);
    }

    @Test
    void testBoundaryStopEast() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(100);
        assertEquals(2, e.state.x);
    }

    @Test
    void testBoundaryStopSouth() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(5);
        assertEquals(0, e.state.y); // already at bottom
    }

    @Test
    void testBoundaryStopWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.facing = RobotMotion.Direction.WEST;
        e.move(5);
        assertEquals(0, e.state.x); // already at left edge
    }

    @Test
    void testMoveZeroSteps() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(0);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testMoveExactlyToEdge() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(4); // grid is 0-4, so 4 steps from 0 reaches edge
        assertEquals(4, e.state.y);
    }

    // COMPLEX PATHS OR INTEGRATION

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
        // Should return to origin
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
    }

    @Test
    void testLShapePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4); // north
        e.state.facing = e.state.facing.right();
        e.move(2); // east
        assertEquals(2, e.state.x);
        assertEquals(4, e.state.y);
        // Verify L-shape drawn
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][4]);
        assertEquals(1, e.floor.grid[2][4]);
    }

    @Test
    void testOverlappingPathDoesNotDoubleCount() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(3);
        e.state.facing = RobotMotion.Direction.NORTH;
        e.move(3);
        // Cell should still be 1, not 2 or 3
        assertEquals(1, e.floor.grid[0][1]);
    }

    // GRID SIZE EDGE CASES

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

    // NEGATIVE OR INVALID INPUT (if applicable)

    @Test
    void testNegativeMoveSteps() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        // Depending on implementation, should either throw or do nothing
        int Before = e.state.y;
        try {
            e.move(-1);
        } catch (Exception ex) {
            // acceptable
        }
        assertTrue(e.state.y >= 0);
    }

    // PRINT OR DISPLAY (smoke test)

    @Test
    void testFloorPrintDoesNotThrow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        assertDoesNotThrow(() -> e.floor.print());
    }
}