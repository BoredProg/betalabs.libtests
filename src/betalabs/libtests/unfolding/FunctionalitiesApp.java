
/*******************************************************************************
 *  FunctionalitiesApp.java
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



import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

import de.looksgood.ani.Ani;

/**
 * Displays earthquake markers from an RSS feed over time.
 *
 * Animates through earthquakes in 1h steps, and fades out dots.
 *
 * Press SPACE for starting or stopping the animation. Press LEFT ARROW or RIGHT
 * ARROW to step through time.
 */
public class FunctionalitiesApp extends PApplet
{

    String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/atom/4.5/week";

    UnfoldingMap map;
    
    int SKETCH_WIDTH    = 1900;
    int SKETCH_HEIGHT   = 1100;
    String SKETCH_RENDERED = P2D;

    int MAP_WIDTH  = 1900;
    int MAP_HEIGHT = 1000;
    
    int _guiHeight = 150;

    public void setup()
    {
        size(SKETCH_WIDTH, SKETCH_HEIGHT, SKETCH_RENDERED);
        //frame.setResizable(true);
        smooth();

        //map = new UnfoldingMap(this);
        map = new UnfoldingMap(this, 0, 0, MAP_WIDTH, SKETCH_HEIGHT - _guiHeight, new Microsoft.HybridProvider());
        map.zoomToLevel(4);
        map.setTweening(true);
        MapUtils.createMouseEventDispatcher(this, map);

        /*
        print("Loading earthquakes from web feed ... ");
        List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
        println("done.");
        markers = MapUtils.createSimpleMarkers(features);

        // Earthquakes are ordered from latest to oldest
        startTime = new DateTime(features.get(features.size() - 1).getProperty("date"));
        endTime = new DateTime(features.get(0).getProperty("date"));
        currentTime = startTime.plus(0);
        println("Dates of earthquakes ranges from " + startTime + " to " + endTime);
        */
        Ani.init(this);
    }

    public void draw()
    {
        background(212);
        map.draw();
        
        // Let's position under the map.
        translate(0, map.getHeight()-10);
        
        // Test..
        //fill(220,0,0);
        //ellipse(20, 20, 20, 20);
        
    }

    @Override
    public void keyPressed()
    {
    }

    /**
     * @param passedArgs the command line arguments
     */
    static public void main(String[] passedArgs)
    {
        // TODO code application logic here
        PApplet.main(new String[]
        {
            /* "--present",*/ "betalabs.libtests.unfolding.FunctionalitiesApp"
        });
    }

}
