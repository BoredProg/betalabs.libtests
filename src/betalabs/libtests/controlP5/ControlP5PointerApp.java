/**
 * *****************************************************************************
 * ControlP5PointerApp.java
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
package betalabs.libtests.controlP5;

import controlP5.*;
import processing.core.*;

/**
 * ControlP5PointerApp
 * 
* Default mouse actions use the Pointer class to trigger events. you can
 * manipulate the x and y fields of the Pointer class for customizing input
 * events for example when using a different input than the mouse. Here in this
 * example the mouse coordiates are reveresed.
 * 
 * That can be VERY USEFUL to transform our TUIO inputs into simple mouse clicks
 * and this way, use the standard GUI libraries.
 */
public class ControlP5PointerApp extends PApplet
{

    ControlP5 cp5;

    /**
     *
     */
    @Override
    public void setup()
    {
        size(400, 600);

        cp5 = new ControlP5(this);
  // disable outodraw because we want to draw our 
        // custom cursor on to of controlP5
        cp5.setAutoDraw(false);

        cp5.addSlider("hello", 0, 100, 50, 40, 40, 100, 20);

        // enable the pointer (and disable the mouse as input) 
        cp5.getPointer().enable();
        cp5.getPointer().set(width / 2, height / 2);
    }

    /**
     *
     */
    @Override
    public void draw()
    {
        background(cp5.get("hello").getValue());
        // first draw controlP5
        cp5.draw();

        // the draw our pointer
        cp5.getPointer().set(width - mouseX, height - mouseY);
        pushMatrix();
        translate(cp5.getPointer().getX(), cp5.getPointer().getY());
        stroke(255);
        line(-10, 0, 10, 0);
        line(0, -10, 0, 10);
        popMatrix();
        println(cp5.isMouseOver());
    }

    /**
     *
     */
    @Override
    public void mousePressed()
    {
        cp5.getPointer().pressed();
    }

    /**
     *
     */
    @Override
    public void mouseReleased()
    {
        cp5.getPointer().released();
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
            //"--present",
            "--bgcolor=#000000",
            "--hide-stop",
            "--present",
            "betalabs.libtests.controlP5.ControlP5PointerApp"
        };

        // Launch..
        PApplet.main(params);
    }
    
    

}
