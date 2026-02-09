package coen6761.project;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class RobotMotion {

    /* Direction */
    enum Direction {
        NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

        final int dx, dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        Direction left() {
            return switch (this) {
                case NORTH -> WEST;
                case WEST -> SOUTH;
                case SOUTH -> EAST;
                case EAST -> NORTH;
            };
        }

        Direction right() {
            return switch (this) {
                case NORTH -> EAST;
                case EAST -> SOUTH;
                case SOUTH -> WEST;
                case WEST -> NORTH;
            };
        }
    }

    /* Robot State */
    static class RobotState {
        int x = 0, y = 0;
        boolean penDown = false;
        Direction facing = Direction.NORTH;
    }

    /* Floor */
    static class Floor {
        final int n;
        final int[][] grid;

        Floor(int n) {
            this.n = n;
            grid = new int[n][n];
        }

        boolean inBounds(int x, int y) {
            return x >= 0 && x < n && y >= 0 && y < n;
        }

        void mark(int x, int y) {
            if (inBounds(x, y))
                grid[x][y] = 1;
        }

        String print() {
            StringBuilder sb = new StringBuilder();
            sb.append("    ");
            for (int i = 0; i < n; i++)
                sb.append(String.format("%2d", i));
            sb.append("\n");

            for (int y = n - 1; y >= 0; y--) {
                sb.append(String.format("%2d |", y));
                for (int x = 0; x < n; x++) {
                    sb.append(grid[x][y] == 1 ? " *" : "  ");
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    /* Engine */
    static class Engine {
        RobotState state = new RobotState();
        Floor floor;

        Engine(int n) {
            floor = new Floor(n);
        }

        void move(int steps) {
            if (state.penDown)
                floor.mark(state.x, state.y);

            for (int i = 0; i < steps; i++) {
                int nx = state.x + state.facing.dx;
                int ny = state.y + state.facing.dy;

                if (!floor.inBounds(nx, ny))
                    break;

                state.x = nx;
                state.y = ny;
                if (state.penDown)
                    floor.mark(nx, ny);
            }
        }
    }

    private static Engine engine = new Engine(10);
    private static final List<String> history = new ArrayList<>();

    /* Main */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Robot Motion Simulator");

        while (true) {
            System.out.print(">Enter command: ");
            String line = sc.nextLine().trim();
            if (line.isEmpty())
                continue;

            processCommand(line, true);
        }
    }

    private static void processCommand(String line, boolean record) {
        char cmd = Character.toUpperCase(line.charAt(0));

        try {
            switch (cmd) {
                case 'U' -> engine.state.penDown = false;
                case 'D' -> {
                    engine.state.penDown = true;
                    engine.floor.mark(engine.state.x, engine.state.y);
                }
                case 'L' -> engine.state.facing = engine.state.facing.left();
                case 'R' -> engine.state.facing = engine.state.facing.right();
                case 'M' -> engine.move(Integer.parseInt(line.substring(1).trim()));
                case 'P' -> System.out.println(engine.floor.print());
                case 'C' -> System.out.printf(
                        "Position: %d, %d - Pen: %s - Facing: %s%n",
                        engine.state.x, engine.state.y,
                        engine.state.penDown ? "down" : "up",
                        engine.state.facing.name().toLowerCase());
                case 'H' -> {
                    List<String> currentHistory = new ArrayList<>(history);
                    engine = new Engine(engine.floor.n); // Reset to initial state for replay
                    for (String h : currentHistory) {
                        System.out.println(">Replaying: " + h);
                        processCommand(h, false);
                    }
                }
                case 'I' -> engine = new Engine(Integer.parseInt(line.substring(1).trim()));
                case 'Q' -> {
                    System.out.println("End of program");
                    System.exit(0);
                }
                default -> System.out.println("Invalid command");
            }
            if (record) {
                history.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
