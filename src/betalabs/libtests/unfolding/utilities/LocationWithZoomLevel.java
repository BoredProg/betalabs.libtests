/*******************************************************************************
 * LocationWithZoomLevel.java
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

import de.fhpotsdam.unfolding.geo.*;

/**
 *
 * @author SEB__ALIEN
 */
public class LocationWithZoomLevel extends Location
{
    private int _zoomLevel;

    public LocationWithZoomLevel(float lat, float lon, int zoomLevel)
    {
        super(lat, lon);
        setZoomLevel(zoomLevel);
    }
    
    
    /**
     * @return the _zoomLevel
     */
    public final int getZoomLevel()
    {
        return _zoomLevel;
    }

    /**
     * @param _zoomLevel the _zoomLevel to set
     */
    public final void setZoomLevel(int _zoomLevel)
    {
        this._zoomLevel = _zoomLevel;
    }
    
    
}
