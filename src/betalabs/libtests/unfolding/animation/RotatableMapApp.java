/*******************************************************************************
 *   RotatableMapApp.java
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


package betalabs.libtests.unfolding.animation;

import processing.core.PApplet;
import processing.core.PVector;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Application to compare the two different map rotation styles. Inner-rotate transforms the map itself, and works with
 * all renderers. Outer-rotate transforms the map container, and works with GLGraphics only.
 * 
 * Use r and l to inner rotate clock-wise (right), and counter-clock-wise (left), and respectively R and L to outer
 * rotate. Note, in this example the map rotates around the current mouse pointer.
 */
public class RotatableMapApp extends PApplet {

	UnfoldingMap map;

	PVector rotateCenter = new PVector(350, 250);
	Location location = new Location(51.50939f, -0.11820f);

	boolean fullRotatable = true;

	public void setup() {
		if (fullRotatable) {
			size(800, 600, OPENGL);
		} else {
			size(800, 600);
		}

		map = new UnfoldingMap(this, "map1", 50, 50, 700, 500, true, false, new Microsoft.HybridProvider());
		map.setTweening(false);
		map.zoomToLevel(3);
		MapUtils.createDefaultEventDispatcher(this, map);
	}

	public void draw() {
		background(0);

		map.draw();

		ScreenPosition pos = map.mapDisplay.getScreenPosition(location);
		stroke(255, 0, 0);
		noFill();
		ellipse(pos.x, pos.y, 10, 10);
	}

	public void keyPressed() {
		rotateCenter = new PVector(mouseX, mouseY);

		// Inner rotate (i.e. map) works with both, P2D and GLGraphics
		map.mapDisplay.setInnerTransformationCenter(rotateCenter);
		if (key == 'r') {
			map.rotate(-PI / 8);
		} else if (key == 'l') {
			map.rotate(PI / 8);
		}

		// Outer rotate (i.e. map container) only works with GLGraphics offscreen buffer
		map.mapDisplay.setTransformationCenter(rotateCenter);
		if (key == 'R') {
			map.outerRotate(-PI / 8);
		} else if (key == 'L') {
			map.outerRotate(PI / 8);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "betalabs.libtests.unfolding.animation.RotatableMapApp" });
	}
}