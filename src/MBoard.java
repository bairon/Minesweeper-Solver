/*
The MIT License (MIT)

Copyright (c) 2015 Patrick Stillhart

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by Patrick Stillhart on 25.12.2015.
 * <p>
 * This is the minesweeper adapter, it finds it and
 * translates the values between minesweeper and the solver
 * <p>
 * The images used are 1 pixel high and 16 pixel wide - they have a top offset of 3px
 */
public class MBoard implements Board {
    public static void main(String[] args) throws BoardException, AWTException, IOException {
        MBoard b = new MBoard(11, 6, 8);
        b.refresh();
        System.out.println(b.toString());
    }

    public void printValues() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < countRow; ++i) {
            for (int j = 0; j < countColumn; ++j) {
                int x = j * BLOCK_SIDE_X + BLOCK_SIDE_X / 2 + (j >= 4 ? 1 : 0);
                int y = i * BLOCK_SIDE_Y + BLOCK_SIDE_Y / 2;
                sb.append(board.getRGB(x, y));
                sb.append(" ");// + x + "," + y + "  ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());

    }

    private String rgbtostr(int rgb) {
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
        return String.format("(%s,%s,%s)", red, green, blue);
    }

    static final int BLOCK_SIDE_X = 18;
    static final int BLOCK_SIDE_Y = 20;
    // The values are the RGB code of the pixel row on 3

    public Robot robot;

    public Rectangle boardRect;
    public BufferedImage board;

    public int countColumn, countRow, countMines;
    public State[][] field;

    public MBoard(int mines, int rows, int columns) throws AWTException, BoardException, IOException {
        this.countMines = mines;
        this.countRow = rows;
        this.countColumn = columns;
        restart();
    }
    public boolean restart() throws AWTException, BoardException, IOException {
        field = new State[countColumn][countRow];
        for (State[] row : field) Arrays.fill(row, State.BLOCK_CLOSED);
        robot = new Robot();
        Rectangle screenRect = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
        }
        BufferedImage capture = robot.createScreenCapture(screenRect);
        Point start = findZero(capture);
        if (start == null) throw new BoardException("A Game? ... Computer says no");
        boardRect = new Rectangle(start.x, start.y, countColumn * BLOCK_SIDE_X, countRow * BLOCK_SIDE_Y);
        board = robot.createScreenCapture(boardRect);
        return true;
    }

    /**
     * Mirrors the value from the real minesweeper in the internal array
     *
     * @return true if values have changed
     * @throws BoardException If the game ended
     */
    public boolean refresh() throws BoardException {

        State tmp;
        boolean change = false;

        board = robot.createScreenCapture(boardRect);

        for (int y = 0; y < countRow; y++) {
            for (int x = 0; x < countColumn; x++) {

                // we'll only check the ones who were closed in the last screenshot
                //tmp = field[x][y];
                //if (tmp == State.BLOCK_CLOSED) {
                    field[x][y] = read(x, y);
                //    if (field[x][y] != tmp) change = true;
                //} else if (tmp == State.BLOCK_MINE_EXPLODED) throw new BoardException("Well... there was a mine at (" + (x + 1) + "/" + (y + 1) + ")");
            }
        }

        return change;

    }

    /**
     * Open a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void open(int x, int y) {
        if (this.getField(x, y) == State.BLOCK_CLOSED) {
            field[x][y] = State.OPEN_ME;
            System.out.println(this);
            throw new EndMoveException();
        }
    }

    /**
     * Opens all fields surrounding a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void openSurrounding(int x, int y) {
        if (y > 0) {
            if (x > 0) open(x - 1, y - 1);    // top ■□□
            open(x, y - 1);    // top □■□
            if (x < field.length - 1) open(x + 1, y - 1);  // top □□■
        }

        if (x > 0) open(x - 1, y);  // middle ■□□
        if (x + 1 < field.length - 1) open(x + 1, y); // middle □□■

        if (y + 1 < field[0].length - 1) {
            if (x > 0) open(x - 1, y + 1);  // bottom ■□□
            open(x, y + 1);    // bottom □■□
            if (x < field.length - 1) open(x + 1, y + 1); // bottom □□■
        }
    }

    @Override
    public State getField(int x, int y) {
        return field[x][y];
    }

    /**
     * Flags a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void flag(int x, int y) {
        if (this.getField(x, y) == State.BLOCK_CLOSED) {
            field[x][y] = State.FLAG_ME;
            System.out.println(this);
            throw new EndMoveException();
        }
    }

    /**
     * Flags all fields surrounding a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void flagSurrounding(int x, int y) {
        if (y > 0) {
            if (x > 0) flag(x - 1, y - 1);    // top ■□□
            flag(x, y - 1);    // top □■□
            if (x < field.length - 1) flag(x + 1, y - 1);  // top □□■
        }

        if (x > 0) flag(x - 1, y);  // middle ■□□
        if (x + 1 < field.length - 1) flag(x + 1, y); // middle □□■

        if (y + 1 < field[0].length - 1) {
            if (x > 0) flag(x - 1, y + 1);  // bottom ■□□
            flag(x, y + 1);    // bottom □■□
            if (x < field.length - 1) flag(x + 1, y + 1); // bottom □□■
        }
    }

    @Override
    public int getVal(int x, int y) {
        return field[x][y].getVal();
    }

    /**
     * Returns the cached field array
     *
     * @return the field array
     */
    public State[][] getField() {
        return field;
    }

    public int getCountColumn() {
        return countColumn;
    }

    public int getCountRow() {
        return countRow;
    }

    public int getCountMines() {
        return countMines;
    }

    /**
     * Gives the value from a field read from the screenshot back as state
     *
     * @param col why are you reading this?
     * @param row you seriously should understand it
     * @return the state
     */
    private State read(int col, int row) {
        int rgb = board.getRGB(col * BLOCK_SIDE_X + BLOCK_SIDE_X / 2 + (col >= 4 ? 1 : 0), row * BLOCK_SIDE_Y + BLOCK_SIDE_Y / 2);
        return State.fromRGB(rgb);
    }

    public void end() {
    }

    /**
     * Finds the starting point of the minesweeper game board on the screenshot
     *
     * @param capture the screenshot
     * @return ZeroPoint
     */
    private Point findZero(BufferedImage capture) throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("fb.png");
        BufferedImage fbImage = ImageIO.read(resourceAsStream);
        Point p = findInclusion(capture, fbImage);
        if (p == null) return null;
        // 102 19
        // 55 63
        //-47 44
        return new Point (p.x - 47, p.y + 44);
    }

    private Point findInclusion(BufferedImage capture, BufferedImage fbImage) {
        for ( int x = 0; x < capture.getWidth() - fbImage.getWidth(); x++) {
            for (int y = 0; y < capture.getHeight() - fbImage.getHeight(); y++) {
                boolean found = true;
                for (int i = 0; i < fbImage.getWidth(); i++) {
                    for (int j = 0; j < fbImage.getHeight(); j++) {
                        if (!close(capture.getRGB(x + i, y + j), fbImage.getRGB(i, j))) {
                            found = false;
                        }
                    }
                }
                if (found) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private boolean close(int rgb1, int rgb2) {
        int red1 = (rgb1 >> 16) & 0xff;
        int green1 = (rgb1 >> 8) & 0xff;
        int blue1 = (rgb1) & 0xff;
        int red2 = (rgb2 >> 16) & 0xff;
        int green2 = (rgb2 >> 8) & 0xff;
        int blue2 = (rgb2) & 0xff;
        return Math.sqrt(Math.pow(red1 - red2, 2) + Math.pow(green1 - green2, 2) + Math.pow(blue1 - blue2, 2)) < 40;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < getCountRow(); x++) {
            for (int y = 0; y < getCountColumn(); y++) {

                switch (getField(y, x)) {
                    case BLOCK_EMPTY:sb.append("□   ");break;
                    case BLOCK_CLOSED:sb.append("■   ");break;
                    case BLOCK_ONE:sb.append("1   ");break;
                    case BLOCK_TWO:sb.append("2   ");break;
                    case BLOCK_THREE:sb.append("3   ");break;
                    case BLOCK_FOUR:sb.append("4   ");break;
                    case BLOCK_FIVE:sb.append("5"   );break;
                    case BLOCK_SIX:sb.append("6   ");break;
                    case BLOCK_SEVEN:sb.append("7   ");break;
                    case BLOCK_EIGHT:sb.append("8   ");break;
                    case BLOCK_FLAG:sb.append("F   ");break;
                    case OPEN_ME:sb.append("O   ");break;
                    case FLAG_ME:sb.append("M   ");break;
                    case BLOCK_MINE_EXPLODED:sb.append("X   ");break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
