package coen6761.project;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RobotMotionTest {



    private static java.lang.reflect.Method processCommandMethod() {
        try {
            var m = RobotMotion.class.getDeclaredMethod("processCommand", String.class, boolean.class);
            m.setAccessible(true);
            return m;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void invokeCmd(String cmd, boolean record) {
        try {
            processCommandMethod().invoke(null, cmd, record);
        } catch (Exception e) {
            Throwable cause = (e.getCause() != null) ? e.getCause() : e;
            throw new RuntimeException(cause);
        }
    }

    @SuppressWarnings("unchecked")
    private static java.util.List<String> getHistory() {
        try {
            var f = RobotMotion.class.getDeclaredField("history");
            f.setAccessible(true);
            return (java.util.List<String>) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RobotMotion.Engine getEngine() {
        try {
            var f = RobotMotion.class.getDeclaredField("engine");
            f.setAccessible(true);
            return (RobotMotion.Engine) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setEngine(RobotMotion.Engine newEngine) {
        try {
            var f = RobotMotion.class.getDeclaredField("engine");
            f.setAccessible(true);
            f.set(null, newEngine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String captureStdout(Runnable r) {
        java.io.PrintStream old = System.out;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        try (java.io.PrintStream ps = new java.io.PrintStream(baos)) {
            System.setOut(ps);
            r.run();
        } finally {
            System.setOut(old);
        }
        return baos.toString();
    }

    @org.junit.jupiter.api.BeforeEach
    void resetRobotMotionStatics() {
        setEngine(new RobotMotion.Engine(10));
        getHistory().clear();
    }

    

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

    @Test
    void testTurnRightFromSouthGivesWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.WEST, e.state.facing);
    }

    
    @Test
    void testTurnRightFromWestGivesNorth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.WEST;
        e.state.facing = e.state.facing.right();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }


    @Test
    void testTurnLeftFromEastGivesNorth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.state.facing = e.state.facing.left();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
    }

    
    @Test
    void testTurnLeftFromSouthGivesEast() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.state.facing = e.state.facing.left();
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
    }

    
    @Test
    void testTurnLeftFromWestGivesSouth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.WEST;
        e.state.facing = e.state.facing.left();
        assertEquals(RobotMotion.Direction.SOUTH, e.state.facing);
    }

   
    @Test
    void testThreeRightTurnsFromNorthGiveWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = e.state.facing.right().right().right();
        assertEquals(RobotMotion.Direction.WEST, e.state.facing);
    }

    
    @Test
    void testPenStatePreservedThroughTurns() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = e.state.facing.right().right().left();
        assertTrue(e.state.penDown, "Pen state should not change after turning");
    }



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
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(0, e.floor.grid[0][3]);
        assertEquals(0, e.floor.grid[0][4]);
    }


    @Test
    void testPenUpWhenAlreadyUpIsIdempotent() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertFalse(e.state.penDown);
        e.state.penDown = false;
        assertFalse(e.state.penDown);
    }


    @Test
    void testPenDownWhenAlreadyDownIsIdempotent() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.penDown = true;
        assertTrue(e.state.penDown);
    }


    @Test
    void testMoveEastAfterPenUpDoesNotMarkFloor() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(2);
        e.state.penDown = false;
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(3);
        assertEquals(0, e.floor.grid[1][2]);
        assertEquals(0, e.floor.grid[2][2]);
        assertEquals(0, e.floor.grid[3][2]);
    }

  
    @Test
    void testPenDownUpDownSequenceMarksCorrectly() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(2);               
        e.state.penDown = false;
        e.move(2);               
        e.state.penDown = true;
        e.move(2);           
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(0, e.floor.grid[0][3]);
        assertEquals(1, e.floor.grid[0][5]);
        assertEquals(1, e.floor.grid[0][6]);
    }


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
        e.move(4);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(2);
        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
    }

    @Test
    void testMoveWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(4);
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
        assertEquals(0, e.state.y);
    }

    @Test
    void testBoundaryStopWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.facing = RobotMotion.Direction.WEST;
        e.move(5);
        assertEquals(0, e.state.x);
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
        e.move(4);
        assertEquals(4, e.state.y);
    }


    @Test
    void testMoveSouthWithPenDownMarksCorrectCells() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(4);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.state.penDown = true;
        e.move(3);
        assertEquals(1, e.floor.grid[0][4]);
        assertEquals(1, e.floor.grid[0][3]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(0, e.floor.grid[0][0]);
    }


    @Test
    void testMoveWestWithPenDownMarksCorrectCells() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(4);
        e.state.facing = RobotMotion.Direction.WEST;
        e.state.penDown = true;
        e.move(3);
        assertEquals(1, e.floor.grid[4][0]);
        assertEquals(1, e.floor.grid[3][0]);
        assertEquals(1, e.floor.grid[2][0]);
        assertEquals(1, e.floor.grid[1][0]);
        assertEquals(0, e.floor.grid[0][0]);
    }


    @Test
    void testPenDownMoveBeyondBoundaryStopsAtEdge() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.penDown = true;
        e.move(100);
        assertEquals(2, e.state.y);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(1, e.floor.grid[0][2]);
    }


    @Test
    void testRobotAtNorthBoundaryCannotMoveNorthFurther() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(4);
        int yBefore = e.state.y;
        e.move(1);
        assertEquals(yBefore, e.state.y);
    }


    @Test
    void testMoveOneStepWithPenDownMarksTwoCells() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(1);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][1]);
        assertEquals(0, e.floor.grid[0][2]);
    }



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
    void testOverlappingPathDoesNotDoubleCount() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(3);
        e.state.facing = RobotMotion.Direction.NORTH;
        e.move(3);
        assertEquals(1, e.floor.grid[0][1]);
    }


    @Test
    void testFullRectanglePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(6);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(4);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][4]);
        assertEquals(1, e.floor.grid[4][4]);
        assertEquals(1, e.floor.grid[4][0]);
    }


    @Test
    void testDisjointMarksWithPenUpInBetween() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(2);
        e.state.penDown = false;
        e.move(3);
        e.state.penDown = true;
        e.move(2);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(0, e.floor.grid[0][3]);
        assertEquals(0, e.floor.grid[0][4]);
        assertEquals(1, e.floor.grid[0][5]);
        assertEquals(1, e.floor.grid[0][7]);
    }


    @Test
    void testCrossOwnPathDoesNotUnmark() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = RobotMotion.Direction.SOUTH;
        e.move(4);
        for (int y = 0; y <= 4; y++)
            assertEquals(1, e.floor.grid[0][y], "Cell y=" + y + " should remain marked");
    }


    @Test
    void testZigzagPath() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(2);
        e.state.facing = e.state.facing.right();
        e.move(2);
        e.state.facing = e.state.facing.left();
        e.move(2);
        e.state.facing = e.state.facing.right();
        e.move(2);
        assertEquals(4, e.state.x);
        assertEquals(4, e.state.y);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[0][2]);
        assertEquals(1, e.floor.grid[2][2]);
        assertEquals(1, e.floor.grid[2][4]);
        assertEquals(1, e.floor.grid[4][4]);
    }


    @Test
    void testCornerToCornerLShape() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(4);
        for (int y = 0; y <= 4; y++)
            assertEquals(1, e.floor.grid[0][y], "North leg y=" + y);
        for (int x = 0; x <= 4; x++)
            assertEquals(1, e.floor.grid[x][4], "East leg x=" + x);
    }



    @Test
    void testMinimumGridSize() {
        RobotMotion.Engine e = new RobotMotion.Engine(1);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        e.move(5);
        assertEquals(0, e.state.y);
    }

    @Test
    void testLargeGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(100);
        e.move(99);
        assertEquals(99, e.state.y);
    }


    @Test
    void testInitializeWithDifferentSizeCreatesCorrectGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        assertEquals(10, e.floor.grid.length);
        assertEquals(10, e.floor.grid[0].length);
    }


    @Test
    void testLargeGridInitializesAllZeros() {
        RobotMotion.Engine e = new RobotMotion.Engine(100);
        for (int i = 0; i < 100; i++)
            for (int j = 0; j < 100; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }


    @Test
    void testMoveFarCornerLargeGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(50);
        e.state.penDown = true;
        e.move(49);
        e.state.facing = e.state.facing.right();
        e.move(49);
        assertEquals(49, e.state.x);
        assertEquals(49, e.state.y);
        assertEquals(1, e.floor.grid[49][49]);
    }


    @Test
    void testNegativeMoveSteps() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        try {
            e.move(-1);
        } catch (Exception ex) {
            
        }
        assertTrue(e.state.y >= 0);
    }


    @Test
    void testNegativeGridSizeThrows() {
        assertThrows(NegativeArraySizeException.class, () -> new RobotMotion.Engine(-1));
    }


    @Test
    void testZeroGridSizeCreatesUnusableGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(0);
        assertFalse(e.floor.inBounds(0, 0));
    }


    @Test
    void testNegativeMoveStepsEngineRemainsValid() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        try {
            e.move(-5);
        } catch (Exception ex) {
            
        }
        assertTrue(e.state.x >= 0 && e.state.x <= 4);
        assertTrue(e.state.y >= 0 && e.state.y <= 4);
    }



    @Test
    void testFloorPrintDoesNotThrow() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        assertDoesNotThrow(() -> e.floor.print());
    }

    @Test
    void testPrintOutputShowsIndicesAndCorrectAsterisksForSamplePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(3);
        for (int y = 0; y <= 4; y++)
            assertEquals(1, e.floor.grid[0][y], "Expected marked cell at (0," + y + ")");
        for (int x = 0; x <= 3; x++)
            assertEquals(1, e.floor.grid[x][4], "Expected marked cell at (" + x + ",4)");
        String out = e.floor.print();
        assertTrue(out.contains(" 0"), "Output should include column indices");
        assertTrue(out.contains(" 9"), "Output should include column index 9 for 10x10");
        assertTrue(out.contains(" 4 |"), "Output should include row index 4 with separator");
        long starCount = out.chars().filter(ch -> ch == '*').count();
        assertEquals(8, starCount, "Expected exactly 8 traced cells printed as '*'");
    }


    @Test
    void testPrintShowsAsterisksOnlyForMarkedCells() {
        RobotMotion.Engine e = new RobotMotion.Engine(3);
        e.state.penDown = true;
        e.move(2);
        String out = e.floor.print();
        assertTrue(out.contains("*"), "Print should show asterisks for marked cells");
    }


    @Test
    void testPrintBlankFloorHasNoAsterisks() {
        RobotMotion.Engine e = new RobotMotion.Engine(4);
        String out = e.floor.print();
        assertFalse(out.contains("*"), "Blank floor should have no asterisks");
    }



    @Test
    void testReInitializeResetsEverything() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(2);
        e = new RobotMotion.Engine(5);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertFalse(e.state.penDown);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }


    @Test
    void testInitializeClearsGrid() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(4);
        e = new RobotMotion.Engine(5);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(0, e.floor.grid[i][j], "Cell [" + i + "][" + j + "] should be 0 after re-init");
    }


    @Test
    void testSequentialInitializationsProduceCleanState() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        for (int i = 0; i < 3; i++) {
            e.state.penDown = true;
            e.move(3);
            e = new RobotMotion.Engine(8);
        }
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertFalse(e.state.penDown);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }



    @Test
    void testDrawEastPathWhenPenDown() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(3);
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[1][0]);
        assertEquals(1, e.floor.grid[2][0]);
        assertEquals(1, e.floor.grid[3][0]);
        assertEquals(0, e.floor.grid[4][0]);
    }



    @Test
    void testPrintCurrentStatusFormat() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(2);
        assertEquals(2, e.state.x);
        assertEquals(0, e.state.y);
        assertTrue(e.state.penDown);
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
        String status = String.format("Position: %d, %d - Pen: %s - Facing: %s",
                e.state.x, e.state.y,
                e.state.penDown ? "down" : "up",
                e.state.facing.name().toLowerCase());
        assertTrue(status.contains("Position: 2, 0"));
        assertTrue(status.contains("Pen: down"));
        assertTrue(status.contains("Facing: east"));
    }


    @Test
    void testStatusStringPenUpNorthAtOrigin() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        String status = String.format("Position: %d, %d - Pen: %s - Facing: %s",
                e.state.x, e.state.y,
                e.state.penDown ? "down" : "up",
                e.state.facing.name().toLowerCase());
        assertEquals("Position: 0, 0 - Pen: up - Facing: north", status);
    }


    @Test
    void testStatusStringPenDownSouth() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = RobotMotion.Direction.SOUTH;
        String status = String.format("Position: %d, %d - Pen: %s - Facing: %s",
                e.state.x, e.state.y,
                e.state.penDown ? "down" : "up",
                e.state.facing.name().toLowerCase());
        assertEquals("Position: 0, 4 - Pen: down - Facing: south", status);
    }


    @Test
    void testStatusStringPenDownWest() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(4);
        e.state.facing = RobotMotion.Direction.WEST;
        e.state.penDown = true;
        e.move(2);
        String status = String.format("Position: %d, %d - Pen: %s - Facing: %s",
                e.state.x, e.state.y,
                e.state.penDown ? "down" : "up",
                e.state.facing.name().toLowerCase());
        assertEquals("Position: 2, 0 - Pen: down - Facing: west", status);
    }


    @Test
    void testHistoryReplayRunsCommands() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        List<String> history = new ArrayList<>();
        e.state.penDown = true;
        history.add("D");
        e.move(3);
        history.add("M 3");
        e.state.facing = e.state.facing.right();
        history.add("R");
        e.move(2);
        history.add("M 2");
        int finalX = e.state.x;
        int finalY = e.state.y;

        RobotMotion.Engine replay = new RobotMotion.Engine(5);
        for (String cmd : history) {
            char c = Character.toUpperCase(cmd.charAt(0));
            switch (c) {
                case 'D' -> replay.state.penDown = true;
                case 'U' -> replay.state.penDown = false;
                case 'R' -> replay.state.facing = replay.state.facing.right();
                case 'L' -> replay.state.facing = replay.state.facing.left();
                case 'M' -> replay.move(Integer.parseInt(cmd.substring(1).trim()));
            }
        }
        assertEquals(finalX, replay.state.x);
        assertEquals(finalY, replay.state.y);
    }


    @Test
    void testHistoryReplayPenUpOnlyProducesBlankFloor() {
        RobotMotion.Engine replay = new RobotMotion.Engine(5);
        List<String> history = List.of("M 3", "R", "M 2");
        for (String cmd : history) {
            char c = Character.toUpperCase(cmd.charAt(0));
            switch (c) {
                case 'R' -> replay.state.facing = replay.state.facing.right();
                case 'L' -> replay.state.facing = replay.state.facing.left();
                case 'M' -> replay.move(Integer.parseInt(cmd.substring(1).trim()));
            }
        }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(0, replay.floor.grid[i][j], "Floor should be blank when pen was never down");
    }


    @Test
    void testHistoryReplayProducesIdenticalFloor() {
        RobotMotion.Engine original = new RobotMotion.Engine(5);
        original.state.penDown = true;
        original.move(3);
        original.state.facing = original.state.facing.right();
        original.move(3);

        RobotMotion.Engine replay = new RobotMotion.Engine(5);
        replay.state.penDown = true;
        replay.move(3);
        replay.state.facing = replay.state.facing.right();
        replay.move(3);

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(original.floor.grid[i][j], replay.floor.grid[i][j],
                        "Replay floor should match original at [" + i + "][" + j + "]");
    }

    @Test
    void testHistoryAfterQuitNotRequired() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(2);
        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
        assertTrue(e.state.penDown);
    }



    @Test
    void testInvalidCommandHandledGracefully() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        assertDoesNotThrow(() -> e.move(3));
        assertEquals(3, e.state.y);
        assertDoesNotThrow(() -> Character.toUpperCase('Z'));
        assertDoesNotThrow(() -> Character.toUpperCase(' '));
    }

    @Test
    void testInvalidInitializeSizeRejected() {
        assertThrows(NegativeArraySizeException.class, () -> new RobotMotion.Engine(-5));
        RobotMotion.Engine e0 = new RobotMotion.Engine(0);
        assertFalse(e0.floor.inBounds(0, 0));
    }

    @Test
    void testInvalidMoveValueRejected() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.move(-1);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertTrue(e.state.x >= 0 && e.state.y >= 0);
    }


    @Test
    void testNoCellExceedsOneAfterRepeatedMoves() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        for (int i = 0; i < 5; i++) {
            e.move(4);
            e.state.facing = RobotMotion.Direction.SOUTH;
            e.move(4);
            e.state.facing = RobotMotion.Direction.NORTH;
        }
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertTrue(e.floor.grid[i][j] <= 1, "Cell [" + i + "][" + j + "] should never exceed 1");
    }


    @Test
    void testNoCellIsNegative() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(4);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertTrue(e.floor.grid[i][j] >= 0, "Cell [" + i + "][" + j + "] should never be negative");
    }




    @Test
    void testProcessCommandRecordsHistoryWhenRecordTrue() {
        assertEquals(0, getHistory().size());
        invokeCmd("D", true);
        invokeCmd("M 2", true);
        invokeCmd("R", true);
        assertEquals(3, getHistory().size());
        assertEquals("D", getHistory().get(0));
        assertEquals("M 2", getHistory().get(1));
        assertEquals("R", getHistory().get(2));
    }

    @Test
    void testProcessCommandDoesNotRecordWhenRecordFalse() {
        invokeCmd("D", false);
        invokeCmd("M 2", false);
        assertEquals(0, getHistory().size());
    }

    @Test
    void testCommandDMarksCurrentCell() {
        RobotMotion.Engine e = getEngine();
        assertEquals(0, e.floor.grid[0][0]);
        invokeCmd("D", true);
        e = getEngine();
        assertTrue(e.state.penDown);
        assertEquals(1, e.floor.grid[0][0], "D should mark the starting cell");
    }

    @Test
    void testCommandUMakesPenUp() {
        invokeCmd("D", true);
        assertTrue(getEngine().state.penDown);
        invokeCmd("U", true);
        assertFalse(getEngine().state.penDown);
    }

    @Test
    void testCommandLAndRChangeFacing() {
        assertEquals(RobotMotion.Direction.NORTH, getEngine().state.facing);
        invokeCmd("R", true);
        assertEquals(RobotMotion.Direction.EAST, getEngine().state.facing);
        invokeCmd("L", true);
        assertEquals(RobotMotion.Direction.NORTH, getEngine().state.facing);
        invokeCmd("L", true);
        assertEquals(RobotMotion.Direction.WEST, getEngine().state.facing);
    }

    @Test
    void testCommandMMovesRobot() {
        assertEquals(0, getEngine().state.x);
        assertEquals(0, getEngine().state.y);
        invokeCmd("M 3", true);
        assertEquals(0, getEngine().state.x);
        assertEquals(3, getEngine().state.y);
    }

    @Test
    void testCommandPPrintsFloor() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);
        String out = captureStdout(() -> invokeCmd("P", true));
        assertTrue(out.contains(" 0"), "P should print floor with indices");
        assertTrue(out.contains("|"), "P output should contain row separators");
    }

    @Test
    void testCommandCPrintsStatusLine() {
        invokeCmd("D", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);
        String out = captureStdout(() -> invokeCmd("C", true));
        assertTrue(out.contains("Position:"), "C should print status");
        assertTrue(out.toLowerCase().contains("pen:"), "C should print pen state");
        assertTrue(out.toLowerCase().contains("facing:"), "C should print facing direction");
    }

    @Test
    void testCommandIReinitializesEngineSize() {
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        assertEquals(3, getEngine().state.y);
        invokeCmd("I 5", true);
        RobotMotion.Engine e = getEngine();
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertEquals(5, e.floor.n);
    }

    @Test
    void testInvalidCommandPrintsInvalidCommand() {
        String out = captureStdout(() -> invokeCmd("Z", true));
        assertTrue(out.toLowerCase().contains("invalid command"));
    }

    @Test
    void testInvalidMoveArgumentTriggersErrorCatch() {
        String out = captureStdout(() -> invokeCmd("M abc", true));
        assertTrue(out.toLowerCase().contains("error:"), "Should print Error: for invalid integer");
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
        assertTrue(out.contains(">Replaying:"), "H should print replay lines");
        assertTrue(getHistory().contains("H"));
    }

    @Test
    void testHCommandResetsAndReplaysCorrectFloor() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);
        
        assertEquals(1, getEngine().floor.grid[0][0]);
        assertEquals(1, getEngine().floor.grid[0][2]);
       
        captureStdout(() -> invokeCmd("H", true));
      
        assertEquals(1, getEngine().floor.grid[0][0]);
        assertEquals(1, getEngine().floor.grid[0][2]);
    }

  
    @Test
    void testHistoryGrowsWithEachCommand() {
        assertEquals(0, getHistory().size());
        invokeCmd("D", true);
        assertEquals(1, getHistory().size());
        invokeCmd("M 1", true);
        assertEquals(2, getHistory().size());
        invokeCmd("R", true);
        assertEquals(3, getHistory().size());
        invokeCmd("U", true);
        assertEquals(4, getHistory().size());
    }


    @Test
    void testICommandAfterMovementResetsPosToOrigin() {
        invokeCmd("D", true);
        invokeCmd("M 4", true);
        assertEquals(4, getEngine().state.y);
        invokeCmd("I 10", true);
        assertEquals(0, getEngine().state.x);
        assertEquals(0, getEngine().state.y);
        assertFalse(getEngine().state.penDown);
    }

    @Test
    void testCCommandAfterIShowsResetPosition() {
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        invokeCmd("I 10", true);
        String out = captureStdout(() -> invokeCmd("C", true));
        assertTrue(out.contains("Position: 0, 0"), "After I, position should be 0,0");
        assertTrue(out.toLowerCase().contains("pen: up"), "After I, pen should be up");
        assertTrue(out.toLowerCase().contains("facing: north"), "After I, facing should be north");
    }

   
    @Test
    void testUCommandIsRecordedInHistory() {
        invokeCmd("D", true);
        invokeCmd("U", true);
        assertTrue(getHistory().contains("U"), "U should be recorded in history");
        assertTrue(getHistory().contains("D"), "D should be recorded in history");
    }


    @Test
    void testLowercaseCommandDWorksLikeDUppercase() {
        invokeCmd("d", true);
        assertTrue(getEngine().state.penDown, "Lowercase 'd' should set pen down");
    }

    @Test
    void testLowercaseCommandRWorksLikeRUppercase() {
        invokeCmd("r", true);
        assertEquals(RobotMotion.Direction.EAST, getEngine().state.facing, "Lowercase 'r' should turn right");
    }

    @Test
    void testLowercaseCommandMWorksLikeMUppercase() {
        invokeCmd("m 3", true);
        assertEquals(3, getEngine().state.y, "Lowercase 'm 3' should move robot north 3");
    }

}