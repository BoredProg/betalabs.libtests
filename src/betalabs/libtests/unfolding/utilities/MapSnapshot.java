/*******************************************************************************
 * MapSnapshot.java
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

import processing.core.*;
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.geo.*;

/**
 * Stores a thumbnail, and additional information of the current state of the
 * map.
 */
public class MapSnapshot
{

    private static final float THUMBNAIL_SCALE = 0.2f;

    protected PApplet p;

    /**
     * Stored map location.
     */
    public Location location;
    /**
     * Stored map zoom level.
     */
    public int zoomLevel;

    /**
     * Thumbnail image of the map.
     */
    public PImage thumbnail;
    // Position and size of thumbnail.
    public float x;
    public float y;
    public float width;
    public float height;

    public MapSnapshot(PApplet p, UnfoldingMap map)
    {
        this.p = p;
        snapshot(map);
    }

    public void snapshot(UnfoldingMap map)
    {
        // Stores information of the current state of the map
        this.location = map.getCenter();
        this.zoomLevel = map.getZoomLevel();

        // Stores image data of the current map
        PGraphics pg = map.mapDisplay.getOuterPG();
        thumbnail = pg.get();
    }

    public void set(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw()
    {
        draw(x, y, width, height);
    }

    public void draw(float x, float y)
    {
        draw(x, y, thumbnail.width * THUMBNAIL_SCALE, thumbnail.height * THUMBNAIL_SCALE);
    }

    public void draw(float x, float y, float width, float height)
    {
        set(x, y, width, height);
        p.fill(0, 100);
        //p.noStroke();
        p.stroke(255,150);
        p.rect(x + 2, y + 2, width, height);
        p.image(thumbnail, x, y, width, height);
    }

    public boolean isInside(float checkX, float checkY)
    {
        return checkX > x && checkX < x + width && checkY > y && checkY < y + height;
    }

}
