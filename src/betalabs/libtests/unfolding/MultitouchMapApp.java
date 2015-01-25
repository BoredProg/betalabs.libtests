
/*******************************************************************************
 *  MultitouchMapApp.java
 * 
 * ® Sébastien Parodi (capturevision), 2015.
 *   http://capturevision.wordpress.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package betalabs.libtests.unfolding;

import org.apache.log4j.Logger;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.providers.*;

/**
 * An interactive map which users can zoom, pan, and rotate with finger
 * gestures.
 *
 * You'll need a TUIO-capable touch device to run this example! See
 * http://www.tuio.org/?software for more information. Start as application for
 * full-screen.
 *
 * See {@link MultitouchMapExternalTuioApp} for how to handle multitouch input
 * for both your app and the map.
 *
 */
public class MultitouchMapApp extends PApplet
{

    public static Logger log = Logger.getLogger(MultitouchMapApp.class);

    public static final boolean DISABLE_ROTATING = false;

    public static boolean FULLSCREEN = false;

    UnfoldingMap map;
    TuioCursorHandler tuioCursorHandler;

    public void setup()
    {
        if (FULLSCREEN)
        {
            size(1920, 1080, OPENGL);
        } else
        {
            size(800, 600, OPENGL);
        }

        // Init the map
        map = new UnfoldingMap(this, new Microsoft.HybridProvider ());

        EventDispatcher eventDispatcher = new EventDispatcher();
        // Create multitouch input handler, and register map to listen to pan and zoom events.
        tuioCursorHandler = new TuioCursorHandler(this, map);
        eventDispatcher.addBroadcaster(tuioCursorHandler);
        eventDispatcher.register(map, "pan");
        eventDispatcher.register(map, "zoom");
    }

    public void draw()
    {
        background(0);

        if (DISABLE_ROTATING)
        {
            map.rotateTo(0);
        }
        map.draw();

        // Shows position of fingers for debugging.
        tuioCursorHandler.drawCursors();
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            "--present",
            "--bgcolor=#000000",
            "--hide-stop",
            "betalabs.libtests.unfolding.MultitouchMapApp"
        };

        FULLSCREEN = true;
        PApplet.main(params);
    }

}
