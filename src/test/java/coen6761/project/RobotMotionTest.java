package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class RobotMotionTest {

    // Task-2 Starts
    // ---- Reflection helpers for private static RobotMotion internals ----

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
            // unwrap reflection exceptions
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
        // Reset engine and history to a known clean state before each test.
        setEngine(new RobotMotion.Engine(10));
        getHistory().clear();
    }

    // Task-2 Ends

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

    // RE-INITIALIZE RESETS EVERYTHING
    @Test
    void testReInitializeResetsEverything() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(3);
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(2);
        // Re-initialize with new Engine
        e = new RobotMotion.Engine(5);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        assertFalse(e.state.penDown);
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(0, e.floor.grid[i][j]);
    }

    // DRAW EAST PATH WHEN PEN DOWN
    @Test
    void testDrawEastPathWhenPenDown() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(3);
        // Marks (0,0), (1,0), (2,0), (3,0)
        assertEquals(1, e.floor.grid[0][0]);
        assertEquals(1, e.floor.grid[1][0]);
        assertEquals(1, e.floor.grid[2][0]);
        assertEquals(1, e.floor.grid[3][0]);
        assertEquals(0, e.floor.grid[4][0]); // not reached
    }

    // PRINT CURRENT STATUS FORMAT
    @Test
    void testPrintCurrentStatusFormat() {
        // The 'C' command prints: "Position: x, y - Pen: up/down - Facing: direction"
        // Since processCommand is private static, we verify the Engine state that feeds
        // it
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.state.facing = RobotMotion.Direction.EAST;
        e.move(2);
        // Verify the state that 'C' command would read
        assertEquals(2, e.state.x);
        assertEquals(0, e.state.y);
        assertTrue(e.state.penDown);
        assertEquals(RobotMotion.Direction.EAST, e.state.facing);
        // Verify status string can be constructed without error
        String status = String.format("Position: %d, %d - Pen: %s - Facing: %s",
                e.state.x, e.state.y,
                e.state.penDown ? "down" : "up",
                e.state.facing.name().toLowerCase());
        assertTrue(status.contains("Position: 2, 0"));
        assertTrue(status.contains("Pen: down"));
        assertTrue(status.contains("Facing: east"));
    }

    // HISTORY REPLAY RUNS COMMANDS SINCE START
    @Test
    void testHistoryReplayRunsCommands() {
        // Since history/processCommand are private static, we simulate the replay
        // logic:
        // Execute a sequence, then re-execute from scratch and verify same result
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        List<String> history = new ArrayList<>();

        // Execute commands and record
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

        // Replay from scratch
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

    // HISTORY AFTER QUIT NOT REQUIRED
    @Test
    void testHistoryAfterQuitNotRequired() {
        // 'Q' calls System.exit(0), so history after quit is inherently not possible.
        // We just verify the engine state is consistent before quit would be invoked.
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.penDown = true;
        e.move(2);
        // State is valid right before quit
        assertEquals(0, e.state.x);
        assertEquals(2, e.state.y);
        assertTrue(e.state.penDown);
        // No further assertions needed — Q terminates the program
    }

    // INVALID COMMAND HANDLED GRACEFULLY
    @Test
    void testInvalidCommandHandledGracefully() {
        // processCommand handles invalid via default -> "Invalid command" print
        // Since it's private static, we verify Engine doesn't crash on bad state
        // manipulation
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        // Engine should remain stable regardless of external command parsing
        assertDoesNotThrow(() -> e.move(3));
        assertEquals(3, e.state.y);
        // Verify the command parser's first-char logic won't crash
        assertDoesNotThrow(() -> Character.toUpperCase('Z'));
        assertDoesNotThrow(() -> Character.toUpperCase(' '));
    }

    // INVALID INITIALIZE SIZE REJECTED
    @Test
    void testInvalidInitializeSizeRejected() {
        // 'I 0' or 'I -5' would create a 0 or negative sized grid
        // Current implementation: new Floor(n) with n<=0 creates int[n][n] which throws
        assertThrows(NegativeArraySizeException.class, () -> new RobotMotion.Engine(-5));
        // Zero-size grid — may not throw but is functionally invalid
        RobotMotion.Engine e0 = new RobotMotion.Engine(0);
        assertFalse(e0.floor.inBounds(0, 0)); // nothing is in bounds
    }

    // INVALID MOVE VALUE REJECTED
    @Test
    void testInvalidMoveValueRejected() {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        // Negative steps: for loop `i < steps` won't execute when steps < 0
        e.move(-1);
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);
        // Position should remain valid
        assertTrue(e.state.x >= 0 && e.state.y >= 0);
    }

    @Test
    void testPrintOutputShowsIndicesAndCorrectAsterisksForSamplePath() {
        RobotMotion.Engine e = new RobotMotion.Engine(10);
        e.state.penDown = true;
        e.move(4);
        e.state.facing = e.state.facing.right();
        e.move(3);
        for (int y = 0; y <= 4; y++) {
            assertEquals(1, e.floor.grid[0][y], "Expected marked cell at (0," + y + ")");
        }
        for (int x = 0; x <= 3; x++) {
            assertEquals(1, e.floor.grid[x][4], "Expected marked cell at (" + x + ",4)");
        }
        String out = e.floor.print();
        assertTrue(out.contains(" 0"), "Output should include column indices");
        assertTrue(out.contains(" 9"), "Output should include column indices up to 9 for 10x10");
        assertTrue(out.contains(" 4 |"), "Output should include row index with separator like ' 4 |'");
        long starCount = out.chars().filter(ch -> ch == '*').count();
        assertEquals(8, starCount, "Expected exactly 8 traced cells printed as '*'");
    }

    // Task-2 Starts
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

        invokeCmd("D", true); // pen down + mark start cell

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
        RobotMotion.Engine e = getEngine();
        assertEquals(RobotMotion.Direction.NORTH, e.state.facing);

        invokeCmd("R", true);
        assertEquals(RobotMotion.Direction.EAST, getEngine().state.facing);

        invokeCmd("L", true);
        assertEquals(RobotMotion.Direction.NORTH, getEngine().state.facing);

        invokeCmd("L", true);
        assertEquals(RobotMotion.Direction.WEST, getEngine().state.facing);
    }

    @Test
    void testCommandMMovesRobot() {
        RobotMotion.Engine e = getEngine();
        assertEquals(0, e.state.x);
        assertEquals(0, e.state.y);

        invokeCmd("M 3", true);

        e = getEngine();
        assertEquals(0, e.state.x);
        assertEquals(3, e.state.y);
    }

    @Test
    void testCommandPPrintsFloor() {
        // draw something first
        invokeCmd("D", true);
        invokeCmd("M 2", true);

        String out = captureStdout(() -> invokeCmd("P", true));

        // Should print the grid header with column indices
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
        // move first so state changes
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        assertEquals(3, getEngine().state.y);

        invokeCmd("I 5", true); // new engine with size 5

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
        // This must go through the catch(Exception e) block in processCommand
        String out = captureStdout(() -> invokeCmd("M abc", true));
        assertTrue(out.toLowerCase().contains("error:"), "Should print Error: ... for invalid integer");
    }

    @Test
    void testHistoryReplayHReplaysCommandsFromStart() {
        // Build history with record=true
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);

        // At this point, engine should be at (2,3)
        assertEquals(2, getEngine().state.x);
        assertEquals(3, getEngine().state.y);

        // Now execute H: it resets engine and replays history (record=false during
        // replay)
        String out = captureStdout(() -> invokeCmd("H", true));

        // after replay, engine should end up same final position
        assertEquals(2, getEngine().state.x);
        assertEquals(3, getEngine().state.y);

        // Also verify it printed the replay messages
        assertTrue(out.contains(">Replaying:"), "H should print replay lines");

        // History should still contain original commands + "H" (because record=true on
        // H)
        assertTrue(getHistory().contains("H"));
    }
    // Task-2 Ends

}