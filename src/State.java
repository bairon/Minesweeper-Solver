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
 * Created by Patrick Stillhart on 26.12.2015.
 * <p>
 * The different states a block can have,
 * basically what you see.. like the numbers, flags, etc.
 */
public enum State {

    BLOCK_MINE_EXPLODED(-3, 9999999),
    BLOCK_CLOSED(-2, -12111845),
    BLOCK_FLAG(-1, -16052720),
    BLOCK_EMPTY(0, -13361904),
    BLOCK_ONE(1, -16744961),
    BLOCK_TWO(2, -10926031, -10067906, -10266822, -10596810, -10728651),
    BLOCK_THREE(3, -5963263, -8169152, -10005702, -10597323),
    BLOCK_FOUR(4, -3223602),
    BLOCK_FIVE(5, -9873606, -7177666, -10465741),
    BLOCK_SIX(6, -10058913),
    BLOCK_SEVEN(7, 9999999),
    BLOCK_EIGHT(8, 9999999),
    OPEN_ME(9, 9999999),
    FLAG_ME(10, 9999999);

    private int[] rgb;
    private int val;

    State(int val, int ... rgb) {
        this.val = val;this.rgb = rgb;
    }

    public static State fromRGB(int rgb) {
        State closest = null;
        double closestDiff = 0;
        int red = (rgb >> 16) & 0xff;
        int green = (rgb >> 8) & 0xff;
        int blue = (rgb) & 0xff;
        for (State state : values()) {
            for (int statergb : state.rgb) {
                int red2 = (statergb >> 16) & 0xff;
                int green2 = (statergb >> 8) & 0xff;
                int blue2 = (statergb) & 0xff;

                double diff = Math.sqrt(Math.pow(red - red2, 2) + Math.pow(green - green2, 2) + Math.pow(blue - blue2, 2));
                if ( closest == null || diff < closestDiff) {
                    closest = state;
                    closestDiff = diff;
                }
            }
        }
        //System.out.println("Diff: " + closestDiff);
        return closest;
    }

    public int getVal() {
        return val;
    }

}
