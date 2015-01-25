/*******************************************************************************
 *  OverviewAndDetailFinderBoxMapApp.java
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
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;

/**
 * Three maps are shown: One large navigable detail map, and two overview maps. The currently selected area in the
 * detail map is shown as finder rectangle in the overview maps. One overview map always shows the whole world, with its
 * finder rectangle changing size on zoom. The other overview map shows the same view in another zoom level, with its
 * finder rectangle keeping its size.
 * 
 * This Overview + Detail example shows how to setup connected map views.
 */
public class OverviewAndDetailFinderBoxMapApp extends PApplet 
{

	UnfoldingMap mapDetail;
	UnfoldingMap mapOverview;
	UnfoldingMap mapOverviewStatic;

	public void setup() {
		size(800, 600, OPENGL);

		// Detail map with default mouse and keyboard interactions
		mapDetail = new UnfoldingMap(this, "detail", 10, 10, 585, 580);
		mapDetail.zoomToLevel(4);
		mapDetail.setZoomRange(4, 10);
		mapDetail.setTweening(true);
		EventDispatcher eventDispatcher = MapUtils.createDefaultEventDispatcher(this, mapDetail);

		// Overview map listens to interaction events from the detail map
		mapOverview = new UnfoldingMap(this, "overview", 605, 10, 185, 185);
		mapOverview.zoomToLevel(1);
		mapOverview.setZoomRange(1, 7);
		mapOverview.setTweening(true);
		eventDispatcher.register(mapOverview, "pan", mapDetail.getId());
		eventDispatcher.register(mapOverview, "zoom", mapDetail.getId());

		// Static overview map
		mapOverviewStatic = new UnfoldingMap(this, "overviewStatic", 605, 205, 185, 185);
	}

	public void draw() {
		background(0);

		mapDetail.draw();
		mapOverview.draw();
		mapOverviewStatic.draw();

		// Finder box for overview map 3 levels zoomed in
		ScreenPosition tl1 = mapOverview.getScreenPosition(mapDetail.getTopLeftBorder());
		ScreenPosition br1 = mapOverview.getScreenPosition(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl1, br1);

		// Finder box for static overview map
		ScreenPosition tl2 = mapOverviewStatic.getScreenPosition(mapDetail.getTopLeftBorder());
		ScreenPosition br2 = mapOverviewStatic.getScreenPosition(mapDetail.getBottomRightBorder());
		drawDetailSelectionBox(tl2, br2);
	}

	public void drawDetailSelectionBox(ScreenPosition tl, ScreenPosition br) {
		noFill();
		stroke(251, 114, 0, 240);
		float w = br.x - tl.x;
		float h = br.y - tl.y;
		rect(tl.x, tl.y, w, h);
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
            "betalabs.libtests.unfolding.OverviewAndDetailFinderBoxMapApp"
        };

        // Launch..
        PApplet.main(params);
    }
}