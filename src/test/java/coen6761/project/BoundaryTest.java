package coen6761.project;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
 
class BoundaryTest {
 
    @ParameterizedTest(name = "Move {0} beyond boundary of {1}x{1} grid results in {2},{3}")
    @CsvSource({
            "NORTH, 2, 0, 1",
            "EAST,  3, 2, 0",
            "SOUTH, 3, 0, 0",
            "WEST,  3, 0, 0"
    })
    void testBoundaryStop(RobotMotion.Direction dir, int size, int expectedX, int expectedY) {
        RobotMotion.Engine e = new RobotMotion.Engine(size);
        e.state.facing = dir;

        e.move(100);
        assertEquals(expectedX, e.state.x);
        assertEquals(expectedY, e.state.y);
    }
}
 