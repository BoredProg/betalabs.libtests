/**
 * *****************************************************************************
 * DynamicMapSwitchApp.java
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
 * ****************************************************************************
 */
package betalabs.libtests.unfolding;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.MapDisplayFactory;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Enables switching between different maps with different tile providers.
 *
 * All maps listen to map events themselves, i.e. all interactions affect each
 * map, resulting in the same region. (Maps are independent though.)
 *
 * Only one map at a time is displayed, and only the tiles of that one will be
 * loaded. Yet, switching is faster than {@link DynamicProviderSwitch} after the
 * tiles are loaded. It takes more memory though.
 */
public class DynamicMapSwitchApp extends PApplet
{

    UnfoldingMap currentMap;

    UnfoldingMap map1;
    UnfoldingMap map2;
    UnfoldingMap map3;

    public void setup()
    {
        size(800, 600, OPENGL);

        map1 = new UnfoldingMap(this, new Google.GoogleMapProvider());
        map2 = new UnfoldingMap(this, new Microsoft.AerialProvider());
        map3 = new UnfoldingMap(this, new Google.GoogleTerrainProvider());

        MapUtils.createDefaultEventDispatcher(this, map1, map2, map3);

        currentMap = map1;
    }

    public void draw()
    {
        background(0);

        currentMap.draw();
    }

    public void keyPressed()
    {
        if (key == '1')
        {
            currentMap = map1;
        } else if (key == '2')
        {
            currentMap = map2;
        } else if (key == '3')
        {
            currentMap = map3;
        }
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            // "--present",
            // "--bgcolor=#000000",
            // "--hide-stop",
            "betalabs.libtests.unfolding.DynamicMapSwitchApp"
        };

        PApplet.main(params);
    }    
}
