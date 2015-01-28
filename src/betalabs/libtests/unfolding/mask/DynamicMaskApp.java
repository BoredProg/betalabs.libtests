/*******************************************************************************
 *   DynamicMaskApp.java
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


package betalabs.libtests.unfolding.mask;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.mapdisplay.OpenGLMapDisplay;
import de.fhpotsdam.unfolding.mapdisplay.shaders.MaskedMapDisplayShader;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * This example shows the use of an gray-scale mask applied to the map.
 */
public class DynamicMaskApp extends PApplet {

	UnfoldingMap map;

	PGraphics mask;
	MaskedMapDisplayShader mapDisplayShader;

	public void setup() {
		size(830, 420, OPENGL);
		map = new UnfoldingMap(this, "map1", 10, 10, 400, 400, true, false, new Microsoft.AerialProvider() );
		MapUtils.createDefaultEventDispatcher(this, map);

		mapDisplayShader = new MaskedMapDisplayShader(this, 400, 400);
		((OpenGLMapDisplay) map.mapDisplay).setMapDisplayShader(mapDisplayShader);

		mask = mapDisplayShader.getMask();
		mask.beginDraw();
		mask.background(0);
		mask.endDraw();
	}

	public void draw() {
		background(0);

		updateMask();
		map.draw();

		// shows the mask next to the map
		image(mask, 420, 10);
	}

	public void keyPressed() {
		resetMask();
	}

	// draw the grayscale mask on an mask object
	// 255 = invisible
	// 0 = visible
	public void updateMask() {
		mask.beginDraw();
		if (mouseX != 0 && mouseY != 0) {
			mask.noStroke();
			mask.fill(255, 127);
			mask.ellipse(mouseX, mouseY, 50, 50);
		}
		mask.endDraw();
	}

	public void resetMask() 
        {
		mask.beginDraw();
		mask.clear();
		mask.endDraw();
	}
        
    public static void main(String[] args)
    {
        String[] params = new String[]
        {
           // "--present",
           // "--bgcolor=#000000",
           // "--hide-stop",
            "betalabs.libtests.unfolding.mask.DynamicMaskApp"
        };
        
         PApplet.main(params);
    }
        

}
