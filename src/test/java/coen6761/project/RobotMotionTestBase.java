package coen6761.project;

import org.junit.jupiter.api.BeforeEach;

/**
 * Base class for RobotMotion tests.
 * Provides reflection-based helper methods to access private static fields
 * and methods in RobotMotion, as well as stdout capture for verification.
 */
public abstract class RobotMotionTestBase {

    @BeforeEach
    void setUp() {
        // Reset RobotMotion static fields before each test to ensure isolation
        RobotMotion.stopExecution = false;
        setField("engine", new RobotMotion.Engine(10));
        ((java.util.List<?>) getField("history")).clear();
    }

    protected void invokeCmd(String line, boolean record) {
        try {
            java.lang.reflect.Method m = RobotMotion.class.getDeclaredMethod("processCommand", String.class,
                    boolean.class);
            m.setAccessible(true);
            m.invoke(null, line, record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected RobotMotion.Engine getEngine() {
        return (RobotMotion.Engine) getField("engine");
    }

    @SuppressWarnings("unchecked")
    protected java.util.List<String> getHistory() {
        return (java.util.List<String>) getField("history");
    }

    protected String captureStdout(Runnable r) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(out));
        try {
            r.run();
            return out.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    private Object getField(String name) {
        try {
            java.lang.reflect.Field f = RobotMotion.class.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(String name, Object val) {
        try {
            java.lang.reflect.Field f = RobotMotion.class.getDeclaredField(name);
            f.setAccessible(true);
            f.set(null, val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
