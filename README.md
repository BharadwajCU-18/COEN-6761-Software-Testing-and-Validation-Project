# Robot Motion Simulator - COEN 6761 Project

This project is a Java-based simulator for a robot moving on an $n \times n$ floor grid. The robot can draw on the floor when its pen is down, and users can replay their command history.

## Prerequisites

- **Java JDK 17 or higher**
- **Maven** (for dependencies/testing)

## How to Build and Run

### Manual Compilation
To compile the project without using Maven:
```bash
javac -d target/classes src/main/java/coen6761/project/RobotMotion.java
```

### Running the Simulator
To run the interactive CLI:
```bash
java -cp target/classes coen6761.project.RobotMotion
```

---

## Supported Commands

| Command | Action |
| :--- | :--- |
| `U` | **Pen Up**: Stop drawing. |
| `D` | **Pen Down**: Start drawing. |
| `L` | **Left**: Turn left 90°. |
| `R` | **Right**: Turn right 90°. |
| `M n` | **Move**: Move forward `n` spaces. |
| `P` | **Print**: Display the current grid with `*` for marked cells. |
| `C` | **Current**: Show position, pen state, and direction. |
| `H` | **History**: **Automated Replay** of all previous commands. |
| `I n` | **Initialize**: Create a new grid of size `n` (resets state). |
| `Q` | **Quit**: Terminate the program. |

---

## Example Session

```text
Robot Motion Simulator
>Enter command: I 10
>Enter command: D
>Enter command: M 4
>Enter command: R
>Enter command: M 3
>Enter command: P
     0 1 2 3 4 5 6 7 8 9
 9 |                    
 8 |                    
 7 |                    
 6 |                    
 5 |                    
 4 | * * * *            
 3 | *                  
 2 | *                  
 1 | *                  
 0 | *                  

>Enter command: C
Position: 3, 4 - Pen: down - Facing: east
>Enter command: H
>Replaying: I 10
>Replaying: D
...
```

## Testing

Unit tests are written using JUnit 5 and can be found in `src/test/java/coen6761/project/RobotMotionTest.java`.
To run tests with Maven:
```bash
mvn test
```

### Tests implemented

| Test Case ID | Description | Input | Expected Result | Test Function | Result |
|---|---|---|---|---|---|
| TC01 | Initialize system sets correct defaults | I 5 then C | Position (0,0), pen up, facing north, floor is 5×5 all zeros | `testInitialPosition`, `testInitialPenUp`, `testInitialDirectionIsNorth`, `testInitialFloorBlank` | Pass |
| TC02 | Re-initialize resets everything | I 10, D, M 3, R, M 2, then I 10, then C | After second I 10: Position (0,0), pen up, facing north, floor cleared to zeros | `testReInitializeResetsEverything` | Pass |
| TC03 | Pen up does not draw | I 5, U, M 3, P | No * drawn anywhere (all blanks) | `testMoveWithPenUp` | Pass |
| TC04 | Pen down draws on movement (north) | I 5, D, M 3, P | Cells on path from (0,0) to (0,3) marked * including destination | `testMoveWithPenDown`, `testPenDownMarksAllCells` | Pass |
| TC05 | Turning right changes direction to east | I 5, R, C | Facing becomes east | `testTurnRight` | Pass |
| TC06 | Turning left from north changes direction to west | I 5, L, C | Facing becomes west | `testTurnLeft` | Pass |
| TC07 | Multiple turns wrap correctly (4 rights) | I 5, R, R, R, R, C | Facing returns to north | `testFullRotationRight` | Pass |
| TC08 | Move east updates x correctly | I 6, R, M 4, C | Position (4,0), facing east | `testMoveEast` | Pass |
| TC09 | Move west updates x correctly | I 6, L, M 2, C | Position (-2,0) should not be allowed; expect boundary handling | `testMoveWest`, `testBoundaryStopWest` | Pass |
| TC10 | Draw east path when pen down | I 6, D, R, M 4, P | * drawn along (0,0) to (4,0) | `testDrawEastPathWhenPenDown` | Pass |
| TC11 | Draw after turn (L-shape) | I 10, D, M 4, R, M 3, P | Vertical line (0,0)→(0,4) and horizontal line (0,4)→(3,4) | `testLShapePath` | Pass |
| TC12 | Boundary check: move north beyond grid | I 5, D, M 10 | No crash; stop at edge (0,4) | `testBoundaryStopNorth` | Pass |
| TC13 | Boundary check: move west below 0 | I 5, L, M 1 | No crash; stop at (0,0) | `testBoundaryStopWest` | Pass |
| TC14 | Move zero spaces does nothing | I 5, D, M 0, C | Position stays (0,0) and no drawing change | `testMoveZeroSteps` | Pass |
| TC15 | Pen state changes correctly | I 5, D, C, U, C | First C: pen down. Second C: pen up | `testPenToggle` | Pass |
| TC16 | Re-tracing same cell stays 1 (no duplicates) | I 6, D, M 2, R, R, M 2, P | Same path traced back; cells remain * (still 1) | `testOverlappingPathDoesNotDoubleCount` | Pass |
| TC17 | Print current status format | I 5, C | Output includes position, pen state, and facing direction | `testPrintCurrentStatusFormat` | Pass |
| TC18 | History replay runs commands since start | I 6, D, M 2, R, M 2, then I 6, then H, then P | After H, floor reflects replayed drawing; robot ends at same final position | `testHistoryReplayRunsCommands` | Pass |
| TC19 | History after quit not required | I 5, Q, then H | Program terminates at Q; H is not processed | `testHistoryAfterQuitNotRequired` | Pass |
| TC20 | Invalid command handled gracefully | I 5, X, C | No crash; error shown or ignored; C still shows defaults | `testInvalidCommandHandledGracefully` | Pass |
| TC21 | Invalid initialize size rejected | I 0 | No crash; error message and system not initialized or unchanged | `testInvalidInitializeSizeRejected` | Pass |
| TC22 | Invalid move value rejected | I 5, M -3 | No crash; error message and position unchanged | `testInvalidMoveValueRejected` | Pass |