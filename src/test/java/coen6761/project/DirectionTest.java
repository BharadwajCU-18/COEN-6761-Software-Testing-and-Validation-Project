package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Direction enum logic, including 90-degree turns and rotations.
 */
class DirectionTest {

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
}
