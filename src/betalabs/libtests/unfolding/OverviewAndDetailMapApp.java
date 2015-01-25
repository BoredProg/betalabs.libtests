/*******************************************************************************
 *  OverviewAndDetailMapApp.java
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
import de.fhpotsdam.unfolding.events.EventDispatcher;
import de.fhpotsdam.unfolding.interactions.MouseHandler;

/**
 * Shows an overview, and a detail map. The overview is a small-scale map, and
 * shows the same area as the large-scale detail map.
 *
 * Both maps are interactive, and can be navigated. Each interaction is
 * reflected in both maps. This Overview + Detail example shows how to setup
 * simple connected map views.
 *
 */
public class OverviewAndDetailMapApp extends PApplet
{

    UnfoldingMap mapDetail;
    UnfoldingMap mapOverview;

    public void setup()
    {
        size(800, 600, OPENGL);

        mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580);
        mapDetail.setTweening(true);
        mapDetail.zoomToLevel(4);
        mapOverview = new UnfoldingMap(this, "overview", 605, 10, 185, 185);
        mapOverview.setTweening(true);

        EventDispatcher eventDispatcher = new EventDispatcher();

        // Add mouse interaction to both maps
        MouseHandler mouseHandler = new MouseHandler(this, mapDetail, mapOverview);
        eventDispatcher.addBroadcaster(mouseHandler);

        // Maps listen to each other, i.e. each interaction in one map is reflected in the other
        eventDispatcher.register(mapDetail, "pan", mapDetail.getId(), mapOverview.getId());
        eventDispatcher.register(mapDetail, "zoom", mapDetail.getId(), mapOverview.getId());
        eventDispatcher.register(mapOverview, "pan", mapDetail.getId(), mapOverview.getId());
        eventDispatcher.register(mapOverview, "zoom", mapDetail.getId(), mapOverview.getId());
    }

    public void draw()
    {
        background(0);

        mapDetail.draw();
        mapOverview.draw();
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
            "betalabs.libtests.unfolding.OverviewAndDetailMapApp"
        };

        // Launch..
        PApplet.main(params);
    }

    

}
