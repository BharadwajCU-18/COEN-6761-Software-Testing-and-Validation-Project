package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RobotMotionTest {

    @Test
    void testTurnRight() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
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
    void testBoundaryStop() {
        RobotMotion.Engine e = new RobotMotion.Engine(2);
        e.move(10);
        assertEquals(1, e.state.y);
    }
}



