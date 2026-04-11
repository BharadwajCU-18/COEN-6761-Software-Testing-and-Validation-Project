package coen6761.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Regression tests for the command history defects reported by QA.
 */
class RegressionHistoryTest extends RobotMotionTestBase {

    @Test
    void testInitializeClearsPriorHistoryAndResetsGridSize() {
        invokeCmd("D", true);
        invokeCmd("M 2", true);

        invokeCmd("I 5", true);

        assertEquals(5, getEngine().floor.n);
        assertTrue(getHistory().isEmpty());
    }

    @Test
    void testQuitIsNotSavedInHistory() {
        invokeCmd("D", true);

        invokeCmd("Q", true);

        assertTrue(RobotMotion.stopExecution);
        assertIterableEquals(List.of("D"), getHistory());
    }

    @Test
    void testReplayIsNotSavedAndDoesNotMutateRecordedCommands() {
        invokeCmd("D", true);
        invokeCmd("M 3", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);

        List<String> beforeReplay = List.copyOf(getHistory());

        invokeCmd("H", true);

        assertIterableEquals(beforeReplay, getHistory());
        assertFalse(getHistory().contains("H"));
    }

    @Test
    void testReplayAfterReinitializeUsesOnlyCommandsIssuedAfterInitialization() {
        invokeCmd("D", true);
        invokeCmd("M 4", true);
        invokeCmd("I 5", true);
        invokeCmd("R", true);
        invokeCmd("M 2", true);

        invokeCmd("H", true);

        assertEquals(2, getEngine().state.x);
        assertEquals(0, getEngine().state.y);
        assertEquals(RobotMotion.Direction.EAST, getEngine().state.facing);
        assertIterableEquals(List.of("R", "M 2"), getHistory());
    }
}
