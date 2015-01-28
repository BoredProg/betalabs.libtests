/**
 * *****************************************************************************
 * java.java
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
package betalabs.libtests.unfolding.mask;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.BlurredMapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MapDisplayShader;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Shows two different MapDisplayShader, one shading map and marker (mask), one
 * only map (blur).
 *
 * Whether the shader also affects marker depends on the implementation in the
 * Shader class.
 *
 * Switch shader by setting the useShaderWithMarker.
 *
 */
public class MaskedMarkerAndMapApp extends PApplet
{

    boolean useShaderWithMarker = true;

    String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.atom";

    UnfoldingMap map;

    MapDisplayShader shader;

    public void setup()
    {
        size(600, 600, OPENGL);
        map = new UnfoldingMap(this, 100, 100, 400, 400);
        MapUtils.createDefaultEventDispatcher(this, map);
        
        
        List<Feature> features = GeoRSSReader.loadDataGeoRSS(this, earthquakesURL);
        List<Marker> markers = MapUtils.createSimpleMarkers(features);
        for (Marker m : markers)
        {
            m.setColor(color(255, 0, 0));
        }
        map.addMarkers(markers);

        if (useShaderWithMarker)
        {
            // Mask shader also shades markers
            PImage maskImage = loadImage("data/mask-circular.png");
            shader = new MaskedMapDisplayShader(this, 400, 400, maskImage);
        } else
        {
            // Blur shader does not shade marker
            shader = new BlurredMapDisplayShader(this);
        }

        ((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(shader);
                
    }

    public void draw()
    {
        background(0);
        map.draw();
    }

    public static void main(String[] args)
    {
        String[] params = new String[]
        {
           // "--present",
            // "--bgcolor=#000000",
            // "--hide-stop",
            "betalabs.libtests.unfolding.mask.MaskedMarkerAndMapApp"
        };
        PApplet.main(params);
    }

}
