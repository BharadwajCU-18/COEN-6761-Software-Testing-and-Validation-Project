package coen6761.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Miscellaneous tests for constructors, edge cases like out-of-bounds marking,
 * and the main loop/CLI entry point behavior.
 */
class MiscTest extends RobotMotionTestBase {

    @Test
    void testRobotMotionConstructor() {
        assertNotNull(new RobotMotion());
    }

    @Test
    void testFloorMarkOutOfBounds() {
        RobotMotion.Floor floor = new RobotMotion.Floor(5);
        // This should not throw and should not mark anything
        assertDoesNotThrow(() -> floor.mark(-1, -1));
        assertDoesNotThrow(() -> floor.mark(5, 5));
    }

    @Test
    void testCommandCWhenPenUp() {
        invokeCmd("U", true);
        String out = captureStdout(() -> invokeCmd("C", true));
        assertTrue(out.toLowerCase().contains("pen: up"));
    }

    @Test
    void testCommandCWhenPenDown() {
        invokeCmd("D", true);
        String out = captureStdout(() -> invokeCmd("C", true));
        assertTrue(out.toLowerCase().contains("pen: down"));
    }

    @Test
    void testCommandQ() {
        RobotMotion.stopExecution = false;
        invokeCmd("Q", true);
        assertTrue(RobotMotion.stopExecution);
    }

    @Test
    void testMainMethod() {
        String input = "I 5\nU\nQ\n";
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        RobotMotion.stopExecution = false;
        try {
            RobotMotion.main(new String[] {});
            assertTrue(RobotMotion.stopExecution);
        } finally {
            System.setIn(originalIn);
            RobotMotion.stopExecution = false;
        }
    }

    @Test
    void testMainMethodWithEmptyLine() {
        String input = "\nI 5\nQ\n";
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        RobotMotion.stopExecution = false;
        try {
            RobotMotion.main(new String[] {});
            assertTrue(RobotMotion.stopExecution);
        } finally {
            System.setIn(originalIn);
            RobotMotion.stopExecution = false;
        }
    }

    @Test
    void testMainMethodTerminatesOnEOF() {
        String input = "I 5\nU"; // No Q here
        java.io.InputStream originalIn = System.in;
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        RobotMotion.stopExecution = false;
        try {
            RobotMotion.main(new String[] {});
        } finally {
            System.setIn(originalIn);
            RobotMotion.stopExecution = false;
        }
    }
}
