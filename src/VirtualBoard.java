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

/**
 * Created by Patrick Stillhart on 25.12.2015.
 * <p>
 * This is the minesweeper adapter, it finds it and
 * translates the values between minesweeper and the solver
 * <p>
 * The images used are 1 pixel high and 16 pixel wide - they have a top offset of 3px
 */
public class VirtualBoard  {

    private int countColumn;
    private int countRow;
    private int countMines;
    private State[][] field;

    public VirtualBoard() throws MBoard.BoardException {
        countColumn = 6;
        countRow = 8;
        countMines = 11;
        field = new State[][]{
                new State[]{State.BLOCK_EMPTY, State.BLOCK_EMPTY, State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_TWO, State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_EMPTY},
                new State[]{State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_FLAG, State.BLOCK_THREE, State.BLOCK_FLAG, State.BLOCK_THREE, State.BLOCK_ONE},
                new State[]{State.BLOCK_FLAG, State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_THREE, State.BLOCK_FLAG, State.BLOCK_FOUR, State.BLOCK_FLAG},
                new State[]{State.BLOCK_ONE, State.BLOCK_ONE, State.BLOCK_EMPTY, State.BLOCK_EMPTY, State.BLOCK_ONE, State.BLOCK_TWO, State.BLOCK_FOUR, State.BLOCK_FLAG},
                new State[]{State.BLOCK_EMPTY, State.BLOCK_ONE, State.BLOCK_TWO, State.BLOCK_TWO, State.BLOCK_TWO, State.BLOCK_TWO, State.BLOCK_FLAG, State.BLOCK_CLOSED},
                new State[]{State.BLOCK_EMPTY, State.BLOCK_ONE, State.BLOCK_FLAG, State.BLOCK_FLAG, State.BLOCK_TWO, State.BLOCK_FLAG, State.BLOCK_THREE, State.BLOCK_CLOSED},
        };
    }

    class BoardException extends Exception {

        public BoardException(String message) {
            super(message);
        }

    }


    /**
     * Restarts the game
     * @return true if successful
     */
    public boolean restart() {
        return true;
    }

    /**
     * Mirrors the value from the real minesweeper in the internal array
     *
     * @return true if values have changed
     * @throws BoardException If the game ended
     */
    public boolean refresh(){


        return false;


    }

    /**
     * Open a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void open(int x, int y) {
        System.out.println(String.format("OPEN x:%s, y:%s", x, y));
    }

    /**
     * Opens all fields surrounding a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void openSurrounding(int x, int y) {
        System.out.println(String.format("OPEN SURROUNDING x:%s, y:%s", x, y));
    }

    /**
     * Flags a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void flag(int x, int y) {
        System.out.println(String.format("FLAG x:%s, y:%s", x, y));
    }

    /**
     * Flags all fields surrounding a field
     *
     * @param x why are you reading this?
     * @param y you seriously should understand it
     */
    public void flagSurrounding(int x, int y) {
        System.out.println(String.format("FLAG SURROUNDING x:%s, y:%s", x, y));
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


    public void end() {

    }

}
