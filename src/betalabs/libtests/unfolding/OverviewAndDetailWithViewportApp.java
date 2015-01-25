/*******************************************************************************
 *  OverviewAndDetailWithViewportApp.java
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
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Shows an navigable detail map, and a static overview map with a viewport. The
 * area in the detail map is shown as finder rectangle (aka viewport) in the
 * overview map. The finder can be dragged around.
 *
 * This Overview + Detail example shows how to use custom interaction without
 * relying on Unfolding's internal event mechanism.
 */
public class OverviewAndDetailWithViewportApp extends PApplet
{

    // Big map showing a detailed area
    UnfoldingMap mapDetail;

    // Small map showing the overview, i.e. the world
    UnfoldingMap mapOverviewStatic;

    // Interactive finder box atop the overview map.
    ViewportRect viewportRect;

    public void setup()
    {
        size(800, 600, OPENGL);

        // Detail map with default mouse and keyboard interactions
        mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580 );
        //mapDetail.setTweening(true);
        mapDetail.zoomToLevel(4);
        mapDetail.setZoomRange(4, 10);
        MapUtils.createDefaultEventDispatcher(this, mapDetail);

        // Static overview map
        mapOverviewStatic = new UnfoldingMap(this, "overviewStatic", 605, 10, 185, 185);

        viewportRect = new ViewportRect();
    }

    public void draw()
    {
        background(0);

        mapDetail.draw();
        mapOverviewStatic.draw();

        // Viewport is updated by the actual area of the detail map
        ScreenPosition tl = mapOverviewStatic.getScreenPosition(mapDetail.getTopLeftBorder());
        ScreenPosition br = mapOverviewStatic.getScreenPosition(mapDetail.getBottomRightBorder());
        viewportRect.setDimension(tl, br);
        viewportRect.draw();
    }

    public void panViewportOnDetailMap()
    {
        float x = viewportRect.x + viewportRect.w / 2;
        float y = viewportRect.y + viewportRect.h / 2;
        Location newLocation = mapOverviewStatic.mapDisplay.getLocation(x, y);
        mapDetail.panTo(newLocation);
    }

    class ViewportRect
    {

        float x;
        float y;
        float w;
        float h;
        boolean dragged = false;

        public boolean isOver(float checkX, float checkY)
        {
            return checkX > x && checkY > y && checkX < x + w && checkY < y + h;
        }

        public void setDimension(ScreenPosition tl, ScreenPosition br)
        {
            this.x = tl.x;
            this.y = tl.y;
            this.w = br.x - tl.x;
            this.h = br.y - tl.y;
        }

        public void draw()
        {
            noFill();
            stroke(251, 114, 0, 240);
            rect(x, y, w, h);
        }

    }

    float oldX;
    float oldY;

    public void mousePressed()
    {
        if (viewportRect.isOver(mouseX, mouseY))
        {
            viewportRect.dragged = true;
            oldX = mouseX - viewportRect.x;
            oldY = mouseY - viewportRect.y;
        }
    }

    public void mouseReleased()
    {
        viewportRect.dragged = false;
    }

    public void mouseDragged()
    {
        if (viewportRect.dragged)
        {
            viewportRect.x = mouseX - oldX;
            viewportRect.y = mouseY - oldY;

            panViewportOnDetailMap();
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
            "betalabs.libtests.unfolding.OverviewAndDetailWithViewportApp"
        };

        // Launch..
        PApplet.main(params);
    }

}
