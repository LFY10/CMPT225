package fifteenpuzzle;


class Node1 implements Comparable<Node1> {
    int size;

    Node1(int size) {
        this.size = size;
    }

    int getSize() {
        return size;
    }

    void setSize(int size) {
        this.size = size;
    }

    @Override
    public int compareTo(Node1 other) {
        return Integer.compare(this.size, other.size);
    }
}