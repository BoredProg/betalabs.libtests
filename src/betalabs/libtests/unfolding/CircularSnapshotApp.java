
/*******************************************************************************
 *  CircularSnapshotApp.java
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


import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;
import de.fhpotsdam.unfolding.utils.*;
import de.fhpotsdam.unfolding.providers.*;

import java.util.*;

import processing.core.*;

import betalabs.libtests.unfolding.utilities.*;

/**
 *
 * @author SEB__ALIEN
 */
public class CircularSnapshotApp extends PApplet
{

    UnfoldingMap map;

    List<MapSnapshot> mapSnapshots = new ArrayList<MapSnapshot>();

    @Override
    public void setup()
    {
        size(600, 400, P2D);

        map = new UnfoldingMap(this, 0, 0, 400, 400, new Microsoft.AerialProvider());
        map.zoomAndPanTo(new Location(51.507222, -0.1275), 10);

        MapUtils.createDefaultEventDispatcher(this, map);
    }

    @Override
    public void draw()
    {
        background(0);
        map.draw();
        
        drawSnapshots();
    }

    private void drawSnapshots()
    {
        int x = 415;
        int y = 20;
        for (MapSnapshot mapSnapshot : mapSnapshots)
        {
            mapSnapshot.draw(x, y, 80, 80);
            x += 90;
            if (x > width - 90)
            {
                x = 415;
                y += 90;
            }
        }
    }

    @Override
    public void mouseClicked()
    {
        for (MapSnapshot mapSnapshot : mapSnapshots)
        {
            if (mapSnapshot.isInside(mouseX, mouseY))
            {
                map.zoomAndPanTo(mapSnapshot.location, mapSnapshot.zoomLevel);
            }
        }
    }

    public void keyPressed()
    {
        if (key == 's')
        {
            MapSnapshot mapSnapshot = new CircularMapSnapshot(this, map);
            println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
            mapSnapshots.add(mapSnapshot);
        }
    }

    
    public void takeSnapshot()
    {
        CircularMapSnapshot x = new CircularMapSnapshot(this, map);

        CircularMapSnapshot mapSnapshot = new CircularMapSnapshot(this, map);
        println("Bookmarked map at " + mapSnapshot.location + " with " + mapSnapshot.zoomLevel);
        mapSnapshots.add(mapSnapshot);
    }

   
    
    
    /**
     * @param passedArgs the command line arguments
     */
    static public void main(String[] passedArgs)
    {
        // TODO code application logic here
        PApplet.main(new String[]
        {
            /* "--present",*/ "betalabs.libtests.unfolding.CircularSnapshotApp"
        });
    }

}
