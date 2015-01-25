/*******************************************************************************
 *  MultitouchMapExternalTuioApp.java
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
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.interactions.TuioCursorHandler;
import de.fhpotsdam.unfolding.providers.*;

/**
 * Multitouch map with a simple button atop. A tap on the button does not affect
 * the map.
 *
 * Creates a map and a handler for TUIO cursors, with this application acting as
 * TUIO listener, and forwarding the TUIO events to the handler. This allows
 * reacting to other touch interactions in the application (e.g. map markers, or
 * other interface elements), as well.
 *
 * See simpler {@link MultitouchMapApp} if you want to use multitouch
 * interaction for the map only.
 */
public class MultitouchMapExternalTuioApp extends PApplet implements TuioListener
{

    public static Logger log = Logger.getLogger(MultitouchMapExternalTuioApp.class);

    Location _locationChamonix  = new Location(45.9167,  6.8667);
    Location _locationParis     = new Location(48.8566140, 2.3522219);
    Location _currentLocation; 
    
    
    UnfoldingMap map;
    EventDispatcher eventDispatcher;
    TuioCursorHandler tuioCursorHandler;
    TuioClient tuioClient;

    boolean activeButton = false;
    int buttonX = 50;
    int buttonY = 50;
    int buttonSize = 40;

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            "--present", 
            "--bgcolor=#000000", 
            "--hide-stop",
            "betalabs.libtests.unfolding.MultitouchMapExternalTuioApp"
        };
        // Launch..
        PApplet.main(params);
    }

    /**
     *
     */
    @Override
    public void setup()
    {
        size(1920, 1080, OPENGL);
        // size(1920, 1080, OPENGL);

        map = new UnfoldingMap(this, new Microsoft.HybridProvider());
        map.setTweening(false);
        
        // zoom and pan to current location..
        _currentLocation = _locationParis;
        
        map.zoomAndPanTo(13, new Location(_locationChamonix)); 

         // restrict panning to a certain zone range..
        //map.setPanningRestriction(new Location(1.283f, 103.833f), 30);
        
        // Creates the EventDispatcher for the tuioCursorHandler..
        eventDispatcher = new EventDispatcher();

        tuioCursorHandler = new TuioCursorHandler(this, false, map);
        eventDispatcher.addBroadcaster(tuioCursorHandler);
        eventDispatcher.register(map, "pan");
        eventDispatcher.register(map, "zoom");
        //eventDispatcher.register(map, "rotate");

        tuioClient = tuioCursorHandler.getTuioClient();
        tuioClient.addTuioListener(this);
    }

    /**
     *
     */
    @Override
    public void draw()
    {
        map.draw();

        //log.debug("map.center: " + map.getCenter());

        if (activeButton)
        {
            fill(255, 0, 0, 150);
        } 
        else
        {
            fill(255, 150);
        }
        noStroke();
        ellipse(buttonX, buttonY, buttonSize, buttonSize);

        // tuioCursorHandler.drawCursors();
        fill(255, 100);
        for (TuioCursor tcur : tuioClient.getTuioCursors())
        {
            ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 20, 20);
        }
    }

    @Override
    public void keyPressed()
    {
        switch (key)
        {
            case '1':
                _currentLocation =  _locationChamonix;
                break;
           
            case '2':
                _currentLocation =  _locationParis ;
                break;
        }
        
        map.zoomAndPanTo(13, _currentLocation);
    }
    
    
    @Override
    public void addTuioCursor(TuioCursor tuioCursor)
    {
        int x = tuioCursor.getScreenX(width);
        int y = tuioCursor.getScreenY(height);

        log.debug("Add " + tuioCursor.getCursorID() + ": " + x + ", " + y);

        if (dist(x, y, buttonX, buttonY) < buttonSize / 2)
        {
            activeButton = !activeButton;
        } else
        {
            tuioCursorHandler.addTuioCursor(tuioCursor);
        }
    }

    @Override
    public void updateTuioCursor(TuioCursor tuioCursor)
    {
        int x = tuioCursor.getScreenX(width);
        int y = tuioCursor.getScreenY(height);
        log.debug("Update " + tuioCursor.getCursorID() + ": " + x + ", " + y);

        tuioCursorHandler.updateTuioCursor(tuioCursor);
    }

    @Override
    public void removeTuioCursor(TuioCursor tuioCursor)
    {
        log.debug("Remove " + tuioCursor.getCursorID());

        tuioCursorHandler.removeTuioCursor(tuioCursor);
    }

    @Override
    public void addTuioObject(TuioObject arg0)
    {
        // No objects used
        
    }

    @Override
    public void refresh(TuioTime arg0)
    {
        // Not used
    }

    @Override
    public void removeTuioObject(TuioObject arg0)
    {
        // No objects used
    }

    @Override
    public void updateTuioObject(TuioObject arg0)
    {
        // No objects used
    }

}
