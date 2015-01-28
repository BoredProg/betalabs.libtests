/**
 * *****************************************************************************
 * PanAnimationMapApp.java
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
import de.fhpotsdam.unfolding.utils.MapUtils;

import betalabs.libtests.unfolding.interaction.NaviButtonMapApp;
import de.fhpotsdam.unfolding.providers.Microsoft;

/**
 * Pans smoothly between three locations, in an endless loop.
 *
 * Press SPACE to switch tweening off (and on again).
 *
 * See {@link NaviButtonMapApp} for an interactive example on how to animate
 * between two locations.
 */
public class PanAnimationMapApp extends PApplet
{

    UnfoldingMap map;

    Location[] locations = new Location[]
    {
        new Location(52.5, 13.4), new Location(53.6f, 10),
        new Location(51.34, 12.37)
    };
    int currentLocation = 0;

    public void setup()
    {
        size(800, 600, OPENGL);

        map = new UnfoldingMap(this, new Microsoft.HybridProvider());
        map.setTweening(true);
        map.zoomAndPanTo(locations[currentLocation], 8);

        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void draw()
    {
        background(0);
        map.draw();

        if (frameCount % 120 == 0)
        {
            map.panTo(locations[currentLocation]);
            currentLocation++;
            if (currentLocation >= locations.length)
            {
                currentLocation = 0;
            }
        }
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            map.switchTweening();
        }
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
            // "--present",
            // "--bgcolor=#000000",
            // "--hide-stop",
            "betalabs.libtests.unfolding.animation.PanAnimationMapApp"
        };

        PApplet.main(params);
    }

}
