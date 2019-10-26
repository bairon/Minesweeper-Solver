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
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Minesweeper Solver
 * Created by Patrick Stillhart on 25.12.2015.
 */
public class Msnsel {

    public static void main(String[] args) {
        try {
            new Msnsel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BoardException e) {
            e.printStackTrace();
        }
    }

    private Board board;

    private Solver solver;

    public Msnsel() throws InterruptedException, AWTException, BoardException, IOException {

        board  = new MBoard(11, 6, 8);
        solver = new SolverImpl(board);
        while (true) {
            board.refresh();
            board.printValues();
            System.out.println(board);

            try {
                solver.solve();
            }catch (EndMoveException em) {
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Thread.sleep(5000);
        }
    }

    /**
     * Asks the user to enter a number via keyboard
     *
     * @param msg the message that should be shown
     * @return the number the user entered
     */
    private int getUserInput(String msg) {
        boolean ok = false;
        int input = -1;

        System.out.println(msg);

        while (!ok) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                input = Integer.parseInt(br.readLine());
                ok = true;
            } catch (IOException | NumberFormatException e) {
                System.err.println("Not a good number, try again!");
            }
        }

        return input;
    }
}
