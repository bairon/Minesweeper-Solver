public interface Board {

    boolean refresh();

    int getCountColumn();

    int getCountRow();

    void flagSurrounding(int x, int y);

    int getVal(int x, int y);
}
