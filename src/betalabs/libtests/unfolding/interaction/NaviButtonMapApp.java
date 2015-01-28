/**
 * *****************************************************************************
 * NaviButtonMapApp.java
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
package betalabs.libtests.unfolding.interaction;

import processing.core.PApplet;
import processing.core.PFont;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

/**
 * Simple manual navigation example. Click on one of the two buttons to jump to
 * specific locations.
 */
public class NaviButtonMapApp extends PApplet
{

    Location berlinLocation = new Location(52.51861f, 13.408056f);
    int berlinZoomLevel = 10;
    Location universityLocation = new Location(52.411613f, 13.051779f);
    int universityZoomLevel = 14;

    UnfoldingMap map;
    PFont font;

    public void setup()
    {
        size(800, 600, OPENGL);
        smooth();
        font = createFont("sans-serif", 14);

        map = new UnfoldingMap(this, "map", 0, 0, 600, 600, true, false,   new Microsoft.AerialProvider());
        map.setTweening(true);
        map.zoomToLevel(3);
        MapUtils.createDefaultEventDispatcher(this, map);
    }

    public void draw()
    {
        background(0);
        map.draw();

        drawButtons();
    }

    public void mouseReleased()
    {
        if (mouseX > 610 && mouseX < 790 && mouseY > 10 && mouseY < 90)
        {
            map.zoomAndPanTo(berlinLocation, berlinZoomLevel);

        } else if (mouseX > 610 && mouseX < 790 && mouseY > 110 && mouseY < 190)
        {
            map.zoomAndPanTo(universityLocation, universityZoomLevel);
        }
    }

    public void drawButtons()
    {
        textFont(font);
        textSize(14);

        // Simple Berlin button
        fill(127);
        stroke(200);
        strokeWeight(2);
        rect(610, 10, 180, 80);
        fill(0);
        text("Berlin (zoom 10)", 620, 52);

        // FHP button
        fill(127);
        rect(610, 110, 180, 80);
        fill(0);
        text("University (zoom 14)", 620, 152);
    }

   public static void main(String[] args)
    {
        String[] params = new String[]
        {
           // "--present",
           // "--bgcolor=#000000",
           // "--hide-stop",
            "betalabs.libtests.unfolding.interaction.NaviButtonMapApp"
        };
        
         PApplet.main(params);
    }


}
