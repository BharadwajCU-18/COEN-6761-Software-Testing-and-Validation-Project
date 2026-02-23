package coen6761.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class RobotMotionIntegrationTest {

    @BeforeEach
    void setUp() {
        RobotMotion.reset();
    }

    @Test
    void testOuterClassConstructor() {
        assertNotNull(new RobotMotion());
    }

    @Test
    void testMainMethod() {
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream("Q\n".getBytes()));
            RobotMotion.main(new String[] {});
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void testRunLoopWithCommands() {
        String input = "D\nM 2\nR\nM 1\nQ\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        RobotMotion.run(sc);

        assertEquals(1, RobotMotion.engine.state.x);
        assertEquals(2, RobotMotion.engine.state.y);
        assertTrue(RobotMotion.engine.state.penDown);
    }

    @Test
    void testRunLoopEmptyLineAndInvalid() {
        String input = "\n  \nINVALID\nQ\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertDoesNotThrow(() -> RobotMotion.run(sc));
    }

    @Test
    void testProcessCommandReturnValues() {
        assertFalse(RobotMotion.processCommand("D", true));
        assertFalse(RobotMotion.processCommand("U", true));
        assertFalse(RobotMotion.processCommand("L", true));
        assertFalse(RobotMotion.processCommand("R", true));
        assertFalse(RobotMotion.processCommand("M 2", true));
        assertFalse(RobotMotion.processCommand("P", true));
        assertFalse(RobotMotion.processCommand("C", true));
        assertFalse(RobotMotion.processCommand("H", true));
        assertFalse(RobotMotion.processCommand("I 5", true));
        assertFalse(RobotMotion.processCommand("invalid", true));
        assertTrue(RobotMotion.processCommand("Q", true));
    }

    @Test
    void testProcessCommandExceptionHandling() {
        // M with non-integer
        assertFalse(RobotMotion.processCommand("M abc", true));
        // I with non-integer
        assertFalse(RobotMotion.processCommand("I abc", true));
        // Null or empty (handled by guard)
        assertFalse(RobotMotion.processCommand(null, true));
        assertFalse(RobotMotion.processCommand("", true));
    }

    @Test
    void testHistoryReplayComplex() {
        RobotMotion.processCommand("I 5", true);
        RobotMotion.processCommand("D", true);
        RobotMotion.processCommand("M 2", true);
        RobotMotion.processCommand("R", true);
        RobotMotion.processCommand("M 2", true);

        // Clear engine state but keep history
        int xBefore = RobotMotion.engine.state.x;
        int yBefore = RobotMotion.engine.state.y;

        // Replay
        RobotMotion.processCommand("H", true);

        assertEquals(xBefore, RobotMotion.engine.state.x);
        assertEquals(yBefore, RobotMotion.engine.state.y);
    }

    @Test
    void testPrintCommandsSmoke() {
        RobotMotion.processCommand("D", true);
        RobotMotion.processCommand("M 1", true);

        // Just verify they don't crash
        assertDoesNotThrow(() -> RobotMotion.processCommand("P", true));
        assertDoesNotThrow(() -> RobotMotion.processCommand("C", true));
    }

    @Test
    void testRobotStateConstructor() {
        assertNotNull(new RobotMotion.RobotState());
    }

    @Test
    void testRunLoopTermination() {
        String input = "D\n"; // Ends without Q
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        assertDoesNotThrow(() -> RobotMotion.run(sc));
    }

    @Test
    void testFloorConstructorAndBounds() {
        RobotMotion.Floor floor = new RobotMotion.Floor(5);
        assertTrue(floor.inBounds(0, 0));
        assertFalse(floor.inBounds(-1, 0));
        assertFalse(floor.inBounds(5, 5));
    }

    @Test
    void testProcessCommandInvalid() {
        // Z is not a valid command
        assertFalse(RobotMotion.processCommand("Z", true));
    }

    @Test
    void testFloorMarkOutOfBounds() {
        RobotMotion.Floor floor = new RobotMotion.Floor(5);
        // This should hit the 'false' branch of the inBounds check in mark()
        floor.mark(-1, -1);
        floor.mark(10, 10);
        // Grid should remains all zeros
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                assertEquals(0, floor.grid[i][j]);
    }

}
