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
