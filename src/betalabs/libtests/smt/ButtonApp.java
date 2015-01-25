/**
 * *****************************************************************************
 * ButtonApp.java
 *
 * ® Sébastien Parodi (capturevision), 2015. http://capturevision.wordpress.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *****************************************************************************
 */
package betalabs.libtests.smt;

import processing.core.*;
import vialab.SMT.*;

/**
 *
 * @author SEB__ALIEN
 */
public class ButtonApp extends PApplet
{

    public void setup()
    {
        size(1280, 720, SMT.RENDERER);
        SMT.init(this, TouchSource.AUTOMATIC);
        SMT.add(new ButtonZone("TestButton", 100, 100, 200, 200, "Button Text"));
    }

    public void draw()
    {
        background(79, 129, 189);
    }

    void pressTestButton()
    {
        println("Button Pressed");
    }

// SEB
// Test if subclass allows redrawing because it's ugly !!
    public class MyButtonZone extends ButtonZone
    {

        public void draw()
        {
            box(100);
        }
    }
    
/**
     * Main methods
     *
     * @param args PApplet arguments.
     */
    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            //"--bgcolor=#000000",
            //"--hide-stop",
            //"--present",
            "betalabs.libtests.smt.ButtonApp"
        };

        // Launch..
        PApplet.main(params);
    }    
     
    
}
