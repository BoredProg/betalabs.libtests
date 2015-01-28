/**
 * *****************************************************************************
 * MapImageOverlayApp.java
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
package betalabs.libtests.unfolding;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows a static image laid over an interactive background map.
 *
 * See {@link ImageOverlayApp} for more information.
 */
public class MapImageOverlayApp extends PApplet
{

    UnfoldingMap map;
    Location center = new Location(52.396, 13.058);

    PImage visImg;
    Location visNorthWest = new Location(52.687, 13.06);
    Location visSouthEast = new Location(52.328, 13.78);

    public void setup()
    {
        size(1400, 800, OPENGL);

        visImg = loadImage("http://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Occupied_Berlin.svg/2000px-Occupied_Berlin.svg.png");

        map = new UnfoldingMap(this, "Satellite Map", new Microsoft.AerialProvider());
        map.zoomAndPanTo(center, 14);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void draw()
    {
        tint(255);
        map.draw();

        ScreenPosition topRight = map.getScreenPosition(visNorthWest);
        ScreenPosition bottomLeft = map.getScreenPosition(visSouthEast);

        float width = bottomLeft.x - topRight.x;
        float height = bottomLeft.y - topRight.y;

        tint(255, 110);
        image(visImg, topRight.x, topRight.y, width, height);
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            "--present",
            "--bgcolor=#000000",
            "--hide-stop",
            "betalabs.libtests.unfolding.MapImageOverlayApp"
        };

        PApplet.main(params);
    }

   
}
