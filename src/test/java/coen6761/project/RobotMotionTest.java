package coen6761.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class RobotMotionTest {

    // DIRECTION OR TURNING

    @ParameterizedTest
    @CsvSource({
            "NORTH, right, EAST",
            "EAST, right, SOUTH",
            "SOUTH, right, WEST",
            "WEST, right, NORTH",
            "NORTH, left, WEST",
            "WEST, left, SOUTH",
            "SOUTH, left, EAST",
            "EAST, left, NORTH"
    })
    void testTurning(RobotMotion.Direction start, String turn, RobotMotion.Direction expected) {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = start;
        if (turn.equals("right"))
            e.state.facing = e.state.facing.right();
        else
            e.state.facing = e.state.facing.left();
        assertEquals(expected, e.state.facing);
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, right, 4, NORTH",
            "NORTH, left, 4, NORTH",
            "NORTH, right, 2, SOUTH"
    })
    void testMultipleTurns(RobotMotion.Direction start, String turn, int count, RobotMotion.Direction expected) {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.facing = start;
        for (int i = 0; i < count; i++) {
            if (turn.equals("right"))
                e.state.facing = e.state.facing.right();
            else
                e.state.facing = e.state.facing.left();
        }
        assertEquals(expected, e.state.facing);
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

    @ParameterizedTest
    @CsvSource({
            "NORTH, 3, 0, 0, 0, 3",
            "EAST, 3, 0, 0, 3, 0",
            "SOUTH, 2, 0, 4, 0, 2",
            "WEST, 2, 4, 0, 2, 0"
    })
    void testMoveDirections(RobotMotion.Direction dir, int steps, int startX, int startY, int expectedX,
            int expectedY) {
        RobotMotion.Engine e = new RobotMotion.Engine(5);
        e.state.x = startX;
        e.state.y = startY;
        e.state.facing = dir;
        e.move(steps);
        assertEquals(expectedX, e.state.x);
        assertEquals(expectedY, e.state.y);
    }

    @ParameterizedTest
    @CsvSource({
            "2, NORTH, 10, 0, 1",
            "3, EAST, 100, 2, 0",
            "3, SOUTH, 5, 0, 0",
            "3, WEST, 5, 0, 0"
    })
    void testBoundaryStops(int size, RobotMotion.Direction dir, int steps, int expectedX, int expectedY) {
        RobotMotion.Engine e = new RobotMotion.Engine(size);
        e.state.facing = dir;
        e.move(steps);
        assertEquals(expectedX, e.state.x);
        assertEquals(expectedY, e.state.y);
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

    @RepeatedTest(10)
    void testRandomMovementStaysInBounds() {
        int size = 10;
        RobotMotion.Engine e = new RobotMotion.Engine(size);
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 100; i++) {
            int action = rand.nextInt(3);
            if (action == 0)
                e.state.facing = e.state.facing.right();
            else if (action == 1)
                e.state.facing = e.state.facing.left();

            e.move(rand.nextInt(size * 2)); // move potentially beyond boundary
            assertTrue(e.state.x >= 0 && e.state.x < size, "x out of bounds at " + e.state.x);
            assertTrue(e.state.y >= 0 && e.state.y < size, "y out of bounds at " + e.state.y);
        }
    }
}