/*******************************************************************************
 * CircularMapSnapshot.java
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
package betalabs.libtests.unfolding.utilities;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;

/**
 * Stores a circular thumbnail of the current map, and current map status data
 * (from MapSnapshot).
 */
public class CircularMapSnapshot extends MapSnapshot
{

    public CircularMapSnapshot(PApplet p, UnfoldingMap map)
    {
        super(p, map);
    }

    @Override
    public void snapshot(UnfoldingMap map)
    {
        super.snapshot(map);

        thumbnail = getCircularImage(thumbnail, 200, 5);
    }

    // By amnon.owed, http://forum.processing.org/topic/extract-circle-texture-from-background-with-alpha-channel
    public PImage getCircularImage(PImage img, int radius, int feather)
    {
        PGraphics temp = p.createGraphics(img.width, img.height, PConstants.JAVA2D);
        temp.beginDraw();
        temp.smooth();
        temp.translate(temp.width / 2, temp.height / 2);
        temp.imageMode(PConstants.CENTER);
        temp.image(img, 0, 0);
        temp.endDraw();
        PImage saveArea = p.createImage(temp.width, temp.height, PConstants.ARGB);
        for (int y = 0; y < saveArea.height; y++)
        {
            for (int x = 0; x < saveArea.width; x++)
            {
                int index = y + x * saveArea.width;
                float d = PApplet.dist(x, y, radius, radius);
                if (d > radius)
                {
                    saveArea.pixels[index] = 0;
                } 
                else if (d >= radius - feather)
                {
                    int c = temp.pixels[index];
                    int r = (c >> 16) & 0xff;
                    int g = (c >> 8) & 0xff;
                    int b = (c) & 0xff;
                    c = p.color(r, g, b, PApplet.map(d, radius - feather, radius, 255, 0));
                    saveArea.pixels[index] = c;
                } else
                {
                    saveArea.pixels[index] = temp.pixels[index];
                }
            }
        }
        saveArea.updatePixels();
        return saveArea;
    }

}
