/*******************************************************************************
 * DynamicProviderSwitchApp.java
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
package betalabs.libtests.unfolding.providers;

import processing.core.PApplet;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Enables switching between different tile providers for the same map. All map
 * settings are persistent. Current transformations, markers, interactions, etc
 * will stay the same.
 *
 * After switching the tile cache will be cleared and visible tiles loaded from
 * new provider. Thus, it always holds only one tile set, and consumes less
 * memory. Compare with {@link DynamicMapSwitch}.
 */
public class DynamicProviderSwitchApp extends PApplet
{

    UnfoldingMap map;
    AbstractMapProvider provider1;
    AbstractMapProvider provider2;
    AbstractMapProvider provider3;

    public void setup()
    {
        size(800, 600, OPENGL);

        provider1 = new Google.GoogleMapProvider();
        provider2 = new Microsoft.AerialProvider();
        //provider3 = new OpenStreetMap.CloudmadeProvider(MapDisplayFactory.OSM_API_KEY, 23058);
        provider3 = new Microsoft.RoadProvider();
        
        map = new UnfoldingMap(this, provider1);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void draw()
    {
        background(0);

        map.draw();
    }

    public void keyPressed()
    {
        if (key == '1')
        {
            map.mapDisplay.setProvider(provider1);
        } else if (key == '2')
        {
            map.mapDisplay.setProvider(provider2);
        } else if (key == '3')
        {
            map.mapDisplay.setProvider(provider3);
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
            //"--present",
            "--bgcolor=#000000",
            "--hide-stop",
            "--present",
            "betalabs.libtests.unfolding.providers.DynamicProviderSwitchApp"
        };

        // Launch..
        PApplet.main(params);
    }    

}
