# Robot Motion Simulator - COEN 6761 Project

This project is a Java-based simulator for a robot moving on an $n \times n$ floor grid. The robot can draw on the floor when its pen is down, and users can replay their command history.

## Prerequisites

- **Java JDK 17 or higher**
- **Maven** (for dependencies/testing)

## How to Build and Run

### Compilation and Testing
To build the project and run all tests with coverage:
```bash
mvn clean verify jacoco:report
```

### Running the Simulator
To run the interactive CLI:
```bash
mvn exec:java -Dexec.mainClass="coen6761.project.RobotMotion"
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

The project uses a modular test suite organized by functionality:
- `DirectionTest.java`: Logic for rotations and enum states.
- `FloorTest.java`: Initialization and boundary checks.
- `EngineTest.java`: Core movement logic and grid marking.
- `CommandTest.java`: Command parsing and state updates.
- `IntegrationTest.java`: Sequence execution and history replay.
- `MiscTest.java`: Entry points and edge cases.

### Code Coverage
The project maintains **100% code coverage** (Instructions, Branches, and Lines). 
After running tests, view the report at:
`target/site/jacoco/index.html`

---

## Design and Implementation

- **Encapsulation**: Core logic is encapsulated in `Engine`, `Floor`, and `RobotState` classes.
- **Reflection Testing**: A custom `RobotMotionTestBase` uses Java Reflection to test private fields and methods without compromising production code visibility.
- **Robust Input**: The simulator handles leading/trailing whitespace and case-insensitivity for command characters.