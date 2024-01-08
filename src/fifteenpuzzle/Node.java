
package fifteenpuzzle;

import java.util.*;

class Node implements Comparable<Node> {
    int size;

    int[][] state;
    Node parent;
    String move;
    int g;

    Node(int[][] state, Node parent, String move, int g, int size) {
        this.state = state;
        this.size = size;
        this.parent = parent;
        this.move = move;
        this.g = g;
    }

    int f() {
        return g + h();
    }

    int getSize() {
        return size;
    }

    void setSize(int size) {
        this.size = size;
    }

    int h() {
        int distance = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] == 0) continue;
                int value = state[i][j] - 1;
                distance += Math.abs(i - value / state.length) + Math.abs(j - value % state[0].length);
            }
        }
        return distance;
    }

    List<String> getMoves() {
        List<String> moves = new LinkedList<>();
        for (Node node = this; node.parent != null; node = node.parent) {
            moves.add(0, node.move);
        }
        return moves;
    }

    List<Node> getNeighbors() {
        List<Node> neighbors = new ArrayList<>();
        int[] blankPosition = findBlankPosition();
        int x = blankPosition[0];
        int y = blankPosition[1];

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        String[] directionNames = {"D", "U", "R", "L"};

        for (int i = 0; i < directions.length; i++) {
            int newX = x + directions[i][0];
            int newY = y + directions[i][1];
            if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                int[][] newState = copyState(state);
                newState[x][y] = newState[newX][newY];
                newState[newX][newY] = 0;
                String moveDescription = newState[x][y] + directionNames[i];
                neighbors.add(new Node(newState, this, moveDescription, g + 1, size));
            }
        }
        return neighbors;
    }

    int[] findBlankPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (state[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    int[][] copyState(int[][] state) {
        int[][] newState = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(state[i], 0, newState[i], 0, size);
        }
        return newState;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.f(), other.f());
    }


}