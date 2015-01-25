
/*******************************************************************************
 *  AnimationWithAniLibApp.java
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


import processing.core.PApplet;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.looksgood.ani.Ani;

/**
 * Custom map animation with easing functions. Click anywhere to smoothly pan there. Press 'z' or 'Z' to zoom in and out
 * smoothly.
 * 
 * Demonstrates how to create own animations, instead of the built-in tweening functionality. In this example, the
 * external Ani library is used.
 */
public class AnimationWithAniLibApp extends PApplet {

	UnfoldingMap map;

	float lat = 52.5f, lon = 13.4f;
	Location location = new Location(lat, lon);

	float currentZoom = 10;
	float targetZoom = currentZoom;

	public void setup() {
		size(1200, 600, P2D);

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		map.zoomAndPanTo(location, (int) currentZoom);

		Ani.init(this);
	}

	public void draw() {
		// NB Zoom before pan

		map.zoomTo(currentZoom);

		location.setLat(lat);
		location.setLon(lon);
		map.panTo(location);

		map.draw();
	}

	public void keyPressed() {
		if (key == 'z' || key == 'Z') {
			if (key == 'z') {
				targetZoom++;
			}
			if (key == 'Z') {
				targetZoom--;
			}
			Ani.to(this, 0.5f, "currentZoom", targetZoom, Ani.LINEAR);
		}
	}

	public void mouseReleased() {
		Location targetLocation = map.getLocation(mouseX, mouseY);

		Ani.to(this, 0.5f, "lat", targetLocation.getLat(), Ani.LINEAR);
		Ani.to(this, 0.5f, "lon", targetLocation.getLon(), Ani.LINEAR);
	}
        
                 /**
     * @param passedArgs the command line arguments
     */
    static public void main(String[] passedArgs)
    {
            // TODO code application logic here
            PApplet.main(new String[]
            {
                /* "--present",*/  "betalabs.libtests.unfolding.AnimationWithAniLibApp"
            });
    }


}