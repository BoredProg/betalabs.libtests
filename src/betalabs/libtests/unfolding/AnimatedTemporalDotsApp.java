
/*******************************************************************************
 *  AnimatedTemporalDotsApp.java
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


/**
 *
 * @author SEB__ALIEN
 */
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import processing.core.PApplet;
import processing.core.PVector;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

import de.looksgood.ani.Ani;

/**
 * Displays earthquake markers from an RSS feed over time.
 *
 * Animates through earthquakes in 1h steps, and fades out dots.
 *
 * Press SPACE for starting or stopping the animation. Press LEFT ARROW or RIGHT
 * ARROW to step through time.
 */
public class AnimatedTemporalDotsApp extends PApplet
{

    String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/atom/4.5/week";

    UnfoldingMap map;
    List<Marker> markers;

    DateTime startTime;
    DateTime endTime;
    DateTime currentTime;
    boolean animating = true;

    int _guiHeight = 200;

    public void setup()
    {
        size(1900, 1000, OPENGL);
        frame.setResizable(true);
        smooth();

        //map = new UnfoldingMap(this);
        map = new UnfoldingMap(this, 0, 0, 1900, 800, new Microsoft.HybridProvider());
        map.zoomToLevel(2);
        map.setTweening(true);
        MapUtils.createMouseEventDispatcher(this, map);

        
        print("Loading earthquakes from web feed ... ");
        List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
        println("done.");
        markers = MapUtils.createSimpleMarkers(features);

        // Earthquakes are ordered from latest to oldest
        startTime = new DateTime(features.get(features.size() - 1).getProperty("date"));
        endTime = new DateTime(features.get(0).getProperty("date"));
        currentTime = startTime.plus(0);
        println("Dates of earthquakes ranges from " + startTime + " to " + endTime);

        Ani.init(this);
    }

    public void draw()
    {
        background(0);
        map.draw();

        for (Marker marker : markers)
        {
            DateTime markerTime = new DateTime(marker.getProperty("date"));
            if (markerTime.isBefore(currentTime))
            {
                ScreenPosition pos = map.getScreenPosition(marker.getLocation());

                drawGrowingEarthquakeDots(pos, markerTime);
            }
        }

        // Each 4 frame (and if currently animating)
        if (animating)
        {
            currentTime = currentTime.plusMinutes(10);
            // Loop: If end is reached start at beginning again.
            if (currentTime.isAfter(endTime))
            {
                currentTime = startTime.plus(0);
            }
        }

        noStroke();
        fill(0, 200);
        rect(10, 10, 270, 20);
        fill(255);
        text("Time: " + currentTime, 13, 24);
    }

    public void drawEarthquakeDots(PVector pos, DateTime time)
    {
        fill(255, 0, 0, 100);
        stroke(255, 0, 0, 200);
        strokeWeight(1);

        // Size of circle depends on age of earthquake, with 12h = max (20px)
        int hours = Hours.hoursBetween(time, currentTime).getHours();
        float size = constrain(map(hours, 0, 12, 20, 0), 0, 20);
        scale(1);

        ellipse(pos.x, pos.y, size * 3, size * 3);
    }

    public void drawGrowingEarthquakeDots(PVector pos, DateTime time)
    {

        // Marker grows over time
        int minutes = Minutes.minutesBetween(time, currentTime).getMinutes();
        int maxMinutes = 12 * 60;
        float size = constrain(map(minutes, 0, maxMinutes, 0, 30), 0, 30);

        // But fades away the colors
        float alphaValue = constrain(map(minutes, 0, maxMinutes, 100, 0), 0, 100);
        float alphaStrokeValue = constrain(map(minutes, 0, maxMinutes, 255, 0), 0, 255);

        // Draw outer (growing) ring
        fill(255, 0, 0, alphaValue);
        stroke(255, 0, 0, alphaStrokeValue);
        strokeWeight(1);
        ellipse(pos.x, pos.y, size * 3, size * 3);

        // Always draw the epicenter
        fill(255, 0, 0);
        ellipse(pos.x, pos.y, 4, 4);
    }

    public void keyPressed()
    {
        if (key == ' ')
        {
            animating = !animating;
        }
        if (key == CODED)
        {
            if (keyCode == LEFT)
            {
                currentTime = currentTime.minusHours(1);
            }
            if (keyCode == RIGHT)
            {
                currentTime = currentTime.plusHours(1);
            }
        }
    }

    /**
     * @param passedArgs the command line arguments
     */
    static public void main(String[] passedArgs)
    {
        // TODO code application logic here
        PApplet.main(new String[]
        {
            /* "--present",*/ "betalabs.libtests.unfolding.AnimatedTemporalDotsApp"
        });
    }

}
