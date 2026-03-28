package coen6761.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class DataFlowTask3Test extends RobotMotionTestBase {

    @Test
    void testMoveZeroStepsDataFlow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);

        int xBefore = e.state.x;
        int yBefore = e.state.y;

        e.move(0);

        assertEquals(xBefore, e.state.x);
        assertEquals(yBefore, e.state.y);
    }

    @Test
    void testMovePositiveStepsDataFlow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.NORTH;

        e.move(2);

        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
    }

    @Test
    void testMoveWithPenDownDataFlow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.NORTH;

        e.move(2);

        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
    }

    @Test
    void testMoveBoundaryBreakDataFlow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.NORTH;

        e.move(100);

        assertEquals(0, e.state.x);
        assertEquals(4, e.state.y);
    }

    @Test
    void testNegativeMoveDataFlow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);

        int xBefore = e.state.x;
        int yBefore = e.state.y;

        e.move(-5);

        assertEquals(xBefore, e.state.x);
        assertEquals(yBefore, e.state.y);
    }
}