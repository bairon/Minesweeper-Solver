import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;

public interface Board {

    boolean refresh() throws BoardException;

    int getCountColumn();

    int getCountRow();

    void flagSurrounding(int x, int y);

    int getVal(int x, int y);

    void openSurrounding(int x, int y);

    State getField(int x, int y);

    State [][] getField();

    void flag(int x, int y);

    void open(int x, int y);

    int getCountMines();

    void printValues();
}
