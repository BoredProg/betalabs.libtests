/**
 * *****************************************************************************
 * FadeTwoMapsApp.java
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
package betalabs.libtests.unfolding.animation;

import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Load two maps and fade between.
 *
 * Press key '1' to fade once, and press key '2' to start fading animation.
 */
public class FadeTwoMapsApp extends PApplet
{

    UnfoldingMap map1;
    UnfoldingMap map2;

    // Create and set variable we need to fade between our two maps.
    boolean fadeOnce = false;
    boolean fadeAlways = false;
    int fadeVal = 255;
    int fadeDelta = 5;
    int fadeMin = 0;
    int fadeMax = 255;

    public void setup()
    {
        size(600, 400, OPENGL);

        // Set the position and size of our two maps.
        int mapXposition = 0;
        int mapYposition = 30;
        int mapWidth = width;
        int mapHeight = height - mapYposition;
        // Set our location of the maps
        float lon = 52.5f;
        float lat = 13.4f;

        // Initialize two maps
        map1 = new UnfoldingMap(this, mapXposition, mapYposition, mapWidth, mapHeight, new Microsoft.RoadProvider());
        map1.zoomAndPanTo(new Location(lon, lat), 10);
        map2 = new UnfoldingMap(this, mapXposition, mapYposition, mapWidth, mapHeight, new Microsoft.AerialProvider());
        map2.zoomAndPanTo(new Location(lon, lat), 10);
        MapUtils.createDefaultEventDispatcher(this, map1, map2);
    }

    public void draw()
    {
        background(0);

        // Calculate Fade Value
        if (fadeAlways == true)
        {
            fadeOnce = false;
            if (fadeVal == 0 || fadeVal == 255)
            {
                fadeDelta = -fadeDelta;
            }
            fadeVal += fadeDelta;
        }

        if (fadeOnce == true)
        {
            if (fadeVal == 0 || fadeVal == 255)
            {
                fadeDelta = -fadeDelta;
                fadeOnce = false;
            }
            fadeVal += fadeDelta;
        }

        // Draw maps
        tint(255);
        map1.draw();
        tint(255, fadeVal);
        map2.draw();

        // Description at the Top
        fill(255);
        text("Press key '1' to fade once   |   Press key '2' to fade always", 10, 20);
    }

    public void keyPressed()
    {
        switch (key)
        {
            case '1':
                fadeAlways = false;
                fadeOnce = true;
                break;
            case '2':
                fadeAlways = true;
                fadeOnce = false;
                break;
        }
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            // "--present",
            // "--bgcolor=#000000",
            // "--hide-stop",
            "betalabs.libtests.unfolding.animation.FadeTwoMapsApp"
        };

        PApplet.main(params);
    }
}
