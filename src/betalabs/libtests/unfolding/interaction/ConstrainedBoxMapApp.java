/**
 * *****************************************************************************
 * ConstrainedBoxMapApp.java
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
package betalabs.libtests.unfolding.interaction;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.events.MapEvent;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Manually constrains the map to a rectangular area. Users can only pan within
 * this specified box.
 *
 * See {@link ConstrainedMapApp} for Unfolding's built-in (but radial)
 * constraint methods.
 */


public class ConstrainedBoxMapApp extends PApplet
{

    UnfoldingMap map;

    Location boundTopLeft = new Location(52.8, 12.6);
    Location boundBottomRight = new Location(52.0, 14.5);

    public void setup()
    {
        size(800, 600, OPENGL);

        map = new UnfoldingMap(this, new Microsoft.AerialProvider());
        map.zoomAndPanTo(new Location(52.5, 13.4f), 10);
        map.setZoomRange(10, 12);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void draw()
    {
        background(0);
        map.draw();
    }

    public void mapChanged(MapEvent mapEvent)
    {
        restrictPanning();
    }

    public void restrictPanning()
    {
        Location mapTopLeft = map.getTopLeftBorder();
        Location mapBottomRight = map.getBottomRightBorder();

        ScreenPosition mapTopLeftPos = map.getScreenPosition(mapTopLeft);
        ScreenPosition boundTopLeftPos = map.getScreenPosition(boundTopLeft);
        if (boundTopLeft.getLon() > mapTopLeft.getLon())
        {
            map.panBy(mapTopLeftPos.x - boundTopLeftPos.x, 0);
        }
        if (boundTopLeft.getLat() < mapTopLeft.getLat())
        {
            map.panBy(0, mapTopLeftPos.y - boundTopLeftPos.y);
        }

        ScreenPosition mapBottomRightPos = map.getScreenPosition(mapBottomRight);
        ScreenPosition boundBottomRightPos = map.getScreenPosition(boundBottomRight);
        if (boundBottomRight.getLon() < mapBottomRight.getLon())
        {
            map.panBy(mapBottomRightPos.x - boundBottomRightPos.x, 0);
        }
        if (boundBottomRight.getLat() > mapBottomRight.getLat())
        {
            map.panBy(0, mapBottomRightPos.y - boundBottomRightPos.y);
        }
    }


   public static void main(String[] args)
    {
        String[] params = new String[]
        {
           // "--present",
           // "--bgcolor=#000000",
           // "--hide-stop",
            "betalabs.libtests.unfolding.interaction.ConstrainedBoxMapApp"
        };
        
         PApplet.main(params);
    }


}
