error id: file://<HOME>/Documents/Projects/COEN-6761%20(Software_Testing_and_Validation_Project)/src/main/java/coen6761/project/RobotMotion.java
file://<HOME>/Documents/Projects/COEN-6761%20(Software_Testing_and_Validation_Project)/src/main/java/coen6761/project/RobotMotion.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[175,1]

error in qdox parser
file content:
```java
offset: 5495
uri: file://<HOME>/Documents/Projects/COEN-6761%20(Software_Testing_and_Validation_Project)/src/main/java/coen6761/project/RobotMotion.java
text:
```scala
// package coen6761.project;

// import java.util.Scanner;
// import java.util.ArrayList;
// import java.util.List;

// public class RobotMotion {

//     /* Direction */
//     enum Direction {
//         NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

//         final int dx, dy;

//         Direction(int dx, int dy) {
//             this.dx = dx;
//             this.dy = dy;
//         }

//         Direction left() {
//             return switch (this) {
//                 case NORTH -> WEST;
//                 case WEST -> SOUTH;
//                 case SOUTH -> EAST;
//                 case EAST -> NORTH;
//             };
//         }

//         Direction right() {
//             return switch (this) {
//                 case NORTH -> EAST;
//                 case EAST -> SOUTH;
//                 case SOUTH -> WEST;
//                 case WEST -> NORTH;
//             };
//         }
//     }

//     /* Robot State */
//     static class RobotState {
//         int x = 0, y = 0;
//         boolean penDown = false;
//         Direction facing = Direction.NORTH;
//     }

//     /* Floor */
//     static class Floor {
//         final int n;
//         final int[][] grid;

//         Floor(int n) {
//             this.n = n;
//             grid = new int[n][n];
//         }

//         boolean inBounds(int x, int y) {
//             return x >= 0 && x < n && y >= 0 && y < n;
//         }

//         void mark(int x, int y) {
//             if (inBounds(x, y))
//                 grid[x][y] = 1;
//         }

//         String print() {
//             StringBuilder sb = new StringBuilder();
//             sb.append("    ");
//             for (int i = 0; i < n; i++)
//                 sb.append(String.format("%2d", i));
//             sb.append("\n");

//             for (int y = n - 1; y >= 0; y--) {
//                 sb.append(String.format("%2d |", y));
//                 for (int x = 0; x < n; x++) {
//                     sb.append(grid[x][y] == 1 ? " *" : "  ");
//                 }
//                 sb.append("\n");
//             }
//             return sb.toString();
//         }
//     }

//     /* Engine */
//     static class Engine {
//         RobotState state = new RobotState();
//         Floor floor;

//         Engine(int n) {
//             floor = new Floor(n);
//         }

//         void move(int steps) {
//             if (state.penDown)
//                 floor.mark(state.x, state.y);

//             for (int i = 0; i < steps; i++) {
//                 int nx = state.x + state.facing.dx;
//                 int ny = state.y + state.facing.dy;

//                 if (!floor.inBounds(nx, ny))
//                     break;

//                 state.x = nx;
//                 state.y = ny;
//                 if (state.penDown)
//                     floor.mark(nx, ny);
//             }
//         }
//     }

//     private static Engine engine = new Engine(10);
//     private static final List<String> history = new ArrayList<>();

//     /* Main */
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);

//         System.out.println("Robot Motion Simulator");

//         while (true) {
//             System.out.print(">Enter command: ");
//             String line = sc.nextLine().trim();
//             if (line.isEmpty())
//                 continue;

//             processCommand(line, true);
//         }
//     }

//     private static void processCommand(String line, boolean record) {
//         char cmd = Character.toUpperCase(line.charAt(0));

//         try {
//             switch (cmd) {
//                 case 'U' -> engine.state.penDown = false;
//                 case 'D' -> {
//                     engine.state.penDown = true;
//                     engine.floor.mark(engine.state.x, engine.state.y);
//                 }
//                 case 'L' -> engine.state.facing = engine.state.facing.left();
//                 case 'R' -> engine.state.facing = engine.state.facing.right();
//                 case 'M' -> engine.move(Integer.parseInt(line.substring(1).trim()));
//                 case 'P' -> System.out.println(engine.floor.print());
//                 case 'C' -> System.out.printf(
//                         "Position: %d, %d - Pen: %s - Facing: %s%n",
//                         engine.state.x, engine.state.y,
//                         engine.state.penDown ? "down" : "up",
//                         engine.state.facing.name().toLowerCase());
//                 case 'H' -> {
//                     List<String> currentHistory = new ArrayList<>(history);
//                     engine = new Engine(engine.floor.n); // Reset to initial state for replay
//                     for (String h : currentHistory) {
//                         System.out.println(">Replaying: " + h);
//                         processCommand(h, false);
//                     }
//                 }
//                 case 'I' -> engine = new Engine(Integer.parseInt(line.substring(1).trim()));
//                 case 'Q' -> {
//                     System.out.println("End of program");
//                     System.exit(0);
//                 }
//                 default -> System.out.println("Invalid command");
//             }
//             if (record) {
//                 history.add(line);
//             }
//         } catch (Exception e) {
//             System.out.println("Error: " + e.getMessage());
//         }
//     }
// }



@@
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:560)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:691)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:688)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:688)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:936)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	java.base/java.lang.Thread.run(Thread.java:1575)
```
#### Short summary: 

QDox parse error in file://<HOME>/Documents/Projects/COEN-6761%20(Software_Testing_and_Validation_Project)/src/main/java/coen6761/project/RobotMotion.java