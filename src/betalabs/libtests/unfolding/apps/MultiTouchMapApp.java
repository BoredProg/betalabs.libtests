/*******************************************************************************
 * MultitouchMapApp.java
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
package betalabs.libtests.unfolding.apps;

// Java and logging..
import org.apache.log4j.Logger;
import java.util.*;

// Map imports..
import de.fhpotsdam.unfolding.*;
import de.fhpotsdam.unfolding.events.*;
import de.fhpotsdam.unfolding.interactions.*;
import de.fhpotsdam.unfolding.providers.*;

// TUIO (included in Unfolding)
import TUIO.*;

// our little libraries..
import betalabs.libtests.unfolding.MultitouchMapExternalTuioApp;

// ControlP5 (GUI).
import controlP5.*;

// Processing
import processing.core.*;

//import com.temboo.core.*;
//import com.temboo.Library.Google.Geocoding.*;

// Our classes..
import betalabs.libtests.unfolding.utilities.*;
import de.fhpotsdam.unfolding.utils.MapUtils;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

/*******************************************************************************
 * MapApplication
 * 
 * 
 * TUIO :
 * The map has a handler for TUIO cursors, with this application acting as
 * TUIO listener, and forwarding the TUIO events to the handler. This allows
 * reacting to other touch interactions in the application (e.g. map markers, or
 * other interface elements), as well.
 *
 * See simpler {@link MultitouchMapApp} if you want to use multitouch
 * interaction for the map only.
 ******************************************************************************/
public class MultiTouchMapApp extends PApplet 
    implements  TuioListener, 
                PConstants
{
    // Logging.
    public static Logger log = Logger.getLogger(MultitouchMapExternalTuioApp.class);
    
    // General Parameters.
    int         SKETCH_WIDTH        = 1920;
    int         SKETCH_HEIGHT       = 1200;//1090;
    
    // GUI Variables
    ControlP5   cp5;
    int         GUI_ZONE_HEIGHT     = 240;
    String      GUI_FONT_BIG_NAME   = "Tahoma";
    String      GUI_FONT_SMALL_NAME = "Tahoma";
    int         GUI_FONT_SMALL_SIZE = 11;
    int         GUI_FONT_BIG_SIZE   = 19;

    
    Println     GUI_CONSOLE;
    
    // Map Variables..
    int MAP_X       = 0;
    int MAP_Y       = 0;
    int MAP_WIDTH   = 1920;
    int MAP_HEIGHT  = 900;        
    
    ArrayList<LocationWithZoomLevel>        _locations;
    ArrayList<MapSnapshot>                  _mapSnapshots; 
    HashMap<Integer, AbstractMapProvider>   _providers;
        
    LocationWithZoomLevel                   _currentLocation;

    UnfoldingMap                            map;
    EventDispatcher                         eventDispatcher;
    
    LocationWithZoomLevel                   _currentMapLocation;
    float                                   _currentMapZoomLevel;
    String                                  _currentMapProviderName;
    
    int                                     _maxNumberOfSnapshots;
    Boolean                                 _logTuioEvents = false;
    
    
    // TUIO Variables.
    TuioCursorHandler   tuioCursorHandler;
    TuioClient          tuioClient;

    
    
    /***************************************************************************
     *  SETUP
     **************************************************************************/

    /**
     * Setup and initialization.
     */
    @Override
    public void setup()
    {   
        // setup sketch (also works with P2D).
        size(SKETCH_WIDTH, SKETCH_HEIGHT, P3D);
        smooth(32);
        frame.setLocation(1921,0);

        // inits the list of locations displayed in the Combo..
        initLocations();
        
        // initializes the list of map Providers.
        initProviders();
        
        // Initializes the map with the above parameters.
        initMap();
        
        // inits TUIO handling (included in the "Unfolding" library.
        initTUIO();
        
        // inits the snapshots list.
        initSnapshots();
                
        // creates the controlP5 gui
        initGUI();        
        
        
        
    }
    
    

    /***************************************************************************
     *  DRAW
     **************************************************************************/    
   
    /**
     * Draw Loop
     */
    @Override
    public void draw()
    {
        background(70, 71, 74);

        // first draw controlP5
        cp5.draw();
        
        // HACK : Emulate mouse click
        // the draw our pointer
        /*
        pushMatrix();
        translate(cp5.getPointer().getX(), cp5.getPointer().getY());
        stroke(255);
        line(-10,0,10,0);
        line(0,-10,0,10);
        popMatrix();
        println(cp5.isMouseOver());
        */


        map.draw();

        drawTuioCursors();

        drawSnapshots();

        updateGui();

    }
    

    /**
     * Key Pressed Event.
     */
    public void keyPressed()
    {
        if (key == 's')         // "save"
        {
             takeSnapshot();
        }
        if (key == 'd')         // "destroy"
        {
             clearSnapshot();
        }
    }
    
    /**
     * mouseClicked
     */
    public void mouseClicked()
    {
        // Handles clicks on snapshots..
        for (MapSnapshot mapSnapshot : _mapSnapshots)
        {
            if (mapSnapshot.isInside(mouseX, mouseY))
            {
                map.zoomAndPanTo(mapSnapshot.location, mapSnapshot.zoomLevel);
            }
        }
    }
    
//    /**
//     * mousePressed
//     */
//    @Override
//    public void mousePressed()
//    {
//       // cp5.getPointer().pressed();
//    }
//
//    /**
//     * mouseReleased
//     */
//    @Override
//    public void mouseReleased()
//    {
//        //cp5.getPointer().released();
//    }


    /***************************************************************************
     * 
     *  MAPS MANAGEMENT. 
     * 
     **************************************************************************/
      
    
    private void initMap()
    {
        // MAP
        //
        // Setup Unfolding Map
        map = new UnfoldingMap(this,
                               MAP_X,           // x
                               MAP_Y,           // y
                               MAP_WIDTH,       // width
                               MAP_HEIGHT,     // height
                               _providers.get(0));
       
        // no good for multitouch.
        map.setTweening(false);
        
        // Enables default mouse handling. The other handler is the TUIO handler
        // which is created in the "initTuio()" method
        MapUtils.createDefaultEventDispatcher(this, map);
        
        
        // zoom and pan the current map to first location.
        _currentLocation = _locations.get(0);
        map.zoomAndPanTo(13, _currentLocation);        
        
    }
    

    /***************************************************************************
     * 
     *  SNAPSHOTS MANAGEMENT. 
     * 
     **************************************************************************/
    
    private void initSnapshots()
    {
        
        // creates the list..
        _mapSnapshots = new ArrayList<>();
        
        // and sets the max number of snapshots the user can take.
        _maxNumberOfSnapshots = 10;
    }
    
    /**
     * Takes a Snapshot
     */
    public void takeSnapshot()
    { 
        if (_mapSnapshots.size() < _maxNumberOfSnapshots)
        {
            MapSnapshot mapSnapshot = new MapSnapshot(this, map);            
            _mapSnapshots.add(mapSnapshot);
            println("Bookmarked map at Lat / Long : " + mapSnapshot.location + ", with zoom level " + mapSnapshot.zoomLevel);
        }
    }
    
    /**
     * Clears the last Snapshot
     */
    private void clearSnapshot()
    {
        if (_mapSnapshots.size() >= 1)
        {
            _mapSnapshots.remove(_mapSnapshots.size()-1);
        }
    }

    /**
     * Draws all the currently saved snapshots.
     */
    private void drawSnapshots()
    {
        int x = 540;
        int y = MAP_HEIGHT + 90;
        for (MapSnapshot mapSnapshot : _mapSnapshots)
        {
            mapSnapshot.draw(x, y, 48, 48);
            x += 60;
            
            //println(x);
            
            if (x > 780)
            {
                x = 540;
                y += 90;
            }
        }
    }
    
    /***************************************************************************
     * 
     *  Providers MANAGEMENT. 
     * 
     **************************************************************************/   
    private void initProviders()
    {
        // Creates the HashMap.
        _providers = new HashMap<>();
        
        _providers.put(1, new Microsoft.HybridProvider());
        _providers.put(0, new Google.GoogleMapProvider());        
        _providers.put(2, new OpenStreetMap.OpenStreetMapProvider());        
        _providers.put(3, new Google.GoogleTerrainProvider());
        _providers.put(4, new EsriProvider.WorldShadedRelief () );
        _providers.put(5, new Microsoft.AerialProvider());
        
    }
      
    private void fillProvidersList()
    {
          // TODO HERE AND REMOVE FROM createGUI() !!
//        list2.addItem("Google.GoogleMapProvider", 0);
//        list2.addItem("Microsoft.HybridProvider", 1);
//        list2.addItem("OpenStreetMap.OpenStreetMapProvider", 2);
//        
//        list2.addItem("Google.GoogleTerrainProvider", 3);
//        list2.addItem("EsriProvider.WorldShadedRelief", 4);        
//        list2.addItem("Microsoft.AerialProvider", 5);        
//        
//        list2.setValue(1);        
    }
    
    
    /***************************************************************************
     * 
     *  AVAILABLES LOCATION MANAGEMENT.
     * 
     **************************************************************************/
    
    
    /**
     * Init dropdownList locations.
     */
    private void initLocations()
    {
        // Parameters                                                           Lat            Lon       Zoom Level.
        LocationWithZoomLevel locationChamonix   = new LocationWithZoomLevel(45.9167f,      6.8667f,        14);
        LocationWithZoomLevel locationParis      = new LocationWithZoomLevel(48.8566140f,   2.3522219f,     13);
        LocationWithZoomLevel locationCanyon     = new LocationWithZoomLevel(36.114526f,    -113.240014f,   13); 
        LocationWithZoomLevel locationEverest    = new LocationWithZoomLevel(27.98002f,     86.921543f,     13);
        LocationWithZoomLevel locationIguacu     = new LocationWithZoomLevel(-25.645533f,   -54.491379f,    8); 
        LocationWithZoomLevel locationTulum      = new LocationWithZoomLevel(20.214987f,   -87.429117f,     10);
         
        
        // Creates the ArrayList and fill it with the locations.
        _locations = new ArrayList<LocationWithZoomLevel>();
        
        _locations.add(0, locationChamonix);
        _locations.add(1, locationParis);
        _locations.add(2, locationCanyon);
        _locations.add(3, locationEverest);
        _locations.add(4, locationIguacu);
        _locations.add(5, locationTulum);     
    }
    
    private void fillLocationsList()
    {
        // TODO HERE AND REMOVE FROM createGUI() !!
//        list.addItem("Chamonix, France", 0);
//        list.addItem("Paris, France", 1);
//        list.addItem("Grand Canyon, USA", 2);
//        list.addItem("Everest, Nepal", 3);
//        list.addItem("Iguçu Falls, Brasil", 4);
//        list.addItem("Tulum, Mexico", 5); 
    }
    
    /***************************************************************************
     * 
     * GUI METHODS
     * 
    ***************************************************************************/
    
    
    /**
     * Updates the GUI Controllers (labels, etc).
     */
    private void updateGui()
    {
        cp5.getController("LatitudeVal").setValueLabel(Float.toString(map.getCenter().getLat()));
        cp5.getController("LongitudeVal").setValueLabel(Float.toString(map.getCenter().getLon()));
        cp5.getController("ZoomLevelVal").setValueLabel(Integer.toString(map.getZoomLevel()));
    }
    
    
    /**
     * initializeGUI()
     */
    private void initGUI()
    {
        // initialize ControlP5..
        MultiTouchMapApp parent;        
        parent  = this;
        cp5     = new ControlP5(parent);
        
        /*
        * Try to emulate the MousePointer wih the controlP5 Pointer class
        * following this discussion :
        *   http://forum.processing.org/two/discussion/9153/controlp5-with-tuio#latest
        */
        
        // disable autodraw because we want to draw our 
        // custom cursor on to of controlP5
        cp5.setAutoDraw(false);
        
        // HACK : Emulate mouse click
        // enable the pointer (and disable the mouse as input) 
        //cp5.getPointer().enable();
        //cp5.getPointer().set(width / 2, height / 2);
 

        
        PFont pfont      = createFont(GUI_FONT_BIG_NAME, 
                                      GUI_FONT_BIG_SIZE, 
                                      true);
        ControlFont font = new ControlFont(pfont, GUI_FONT_BIG_SIZE);
        
        cp5.setFont(createFont(GUI_FONT_SMALL_NAME, 
                               GUI_FONT_SMALL_SIZE ,
                               true));
        
        
        ////////////////////////////////////
        // Create ControlP5 Controllers
        ////////////////////////////////////

        
        // Title Section I
        cp5.addTextlabel("MapInformations")
                    .linebreak()
                    .setText("Map Informations")
                    .setPosition(10, 10 + MAP_HEIGHT)                    
                    .setFont(font);
        
        // Providers List
        cp5.addTextlabel("lblChooseProvider")
            .linebreak()
            .setText("CHOOSE YOUR MAP PROVIDER : ")
            .setPosition(10, 40 + MAP_HEIGHT);
        
          
        DropdownList list2 = cp5.addDropdownList("ProviderList")     // "handlePlaces" could be the handler method name.
                            .setCaptionLabel("Providers")
                            .setPosition(10,  80  + MAP_HEIGHT)
                            .setWidth(250)
                            .setBarHeight(19)
                            .setItemHeight(18)
                            .setHeight(80);
         
        list2.addItem("Google.GoogleMapProvider", 0);
        list2.addItem("Microsoft.HybridProvider", 1);
        list2.addItem("OpenStreetMap.OpenStreetMapProvider", 2);
        
        list2.addItem("Google.GoogleTerrainProvider", 3);
        list2.addItem("EsriProvider.WorldShadedRelief", 4);        
        list2.addItem("Microsoft.AerialProvider", 5);        
        
        list2.setValue(1);

        // Latitude and Longitude :
        // Title section I.a
        cp5.addTextlabel("Location")
                    .linebreak()
                    .setText("LOCATION : ")
                    .setPosition(10, 160 + MAP_HEIGHT);   
       
        // Latitude title        
        cp5.addTextlabel("Latitude")
                    .linebreak()
                    .setText("latitude : ")
                    .setPosition(10, 180 + MAP_HEIGHT);   
       
        // Latitude value
        cp5.addTextlabel("LatitudeVal")
                    .linebreak()
                    .setText(Float.toString(map.getCenter().getLat()))
                    .setPosition(80, 180 + MAP_HEIGHT);   
        
        // Longitude title.
        cp5.addTextlabel("Longitude")
                    .linebreak()
                    .setText("Longitude : ")
                    .setPosition(10, 200 + MAP_HEIGHT);   
       
        // longitude value.
        cp5.addTextlabel("LongitudeVal")
                    .linebreak()
                    .setText(Float.toString(map.getCenter().getLon()))
                    .setPosition(80, 200 + MAP_HEIGHT);   
        
        // Longitude title.
        cp5.addTextlabel("ZoomLevel")
                    .linebreak()
                    .setText("Zoom Level : ")
                    .setPosition(10, 220 + MAP_HEIGHT);   
       
        // zoom value.
        cp5.addTextlabel("ZoomLevelVal")
                    .linebreak()
                    .setText(Float.toString(map.getCenter().getLon()))
                    .setPosition(80, 220 + MAP_HEIGHT);           
        
        

        
        
        
        
        
        
    // Title Section II "World Places"
    cp5.addTextlabel("WorldPlaces")
                    .linebreak()
                    .setText("World Places")
                    .setPosition(280, 10 + MAP_HEIGHT)                    
                    .setFont(font);
                    
        
     
    // Title section II.a
    cp5.addTextlabel("ChoosePlaceLabel")
                    .linebreak()
                    .setText("ENTER YOUR PLACE NAME : ")
                    .setPosition(280, 40 + MAP_HEIGHT);       
         
    cp5.addTextfield("ChoosePlaceLabelTextField")
                    .setText("")
                    .linebreak()
                    .setWidth(250)
                    .setPosition(280, 60 + MAP_HEIGHT).getCaptionLabel().setText("");                
             
         
    // Title section II.b
    cp5.addTextlabel("Places")
                    .linebreak()
                    .setText("CHOOSE YOUR PLACE : ")
                    .setPosition(280, 90 + MAP_HEIGHT);        
        
        DropdownList list = cp5.addDropdownList("handlePlaces")     // "handlePlaces" could be the handler method name.
                    .setCaptionLabel("Places")
                    .setPosition(280, 130 + MAP_HEIGHT)
                    .setWidth(250)
                    .setBarHeight(19)
                    .setItemHeight(18)
                    .setHeight(250);                   
                    
        list.addItem("Chamonix, France", 0);
        list.addItem("Paris, France", 1);
        list.addItem("Grand Canyon, USA", 2);
        list.addItem("Everest, Nepal", 3);
        list.addItem("Iguçu Falls, Brasil", 4);
        list.addItem("Tulum, Mexico", 5); 
                
        
 
        
        
        
        
        // Title Section III "Your Bookmarked Places"
        cp5.addTextlabel("YourBookmarks")
                    .linebreak()
                    .setText("Your Bookmarked Places")
                    .setPosition(540, 10 + MAP_HEIGHT)                    
                    .setFont(font);
         

                 
        cp5.addTextlabel("btnTakeSnapshot")
                    .linebreak()
                    .setText("TAKE A SNAPSHOT : ")
                    .setPosition(540, 40 + MAP_HEIGHT);

        
         
        Button btnSnapChot = cp5.addButton("TakeSnapshotButton")
                                   .linebreak();
        btnSnapChot.getCaptionLabel().setText("Take SnapChot");
        btnSnapChot.setPosition(540, 60 + MAP_HEIGHT);
        btnSnapChot.setSize(95, 15);

        
         
        cp5.addTextlabel("lblClearLastSnapshot")
                    .linebreak()
                    .setText("Clear Last Snapshot : ")
                    .setPosition(700, 40 + MAP_HEIGHT);
        
        
        Button btnClearSnapChot = cp5.addButton("btnClearSnapshotButton")
                                       .linebreak();
        btnClearSnapChot.getCaptionLabel().setText("Clear Last Snapshot");
        btnClearSnapChot.setPosition(700, 60 + MAP_HEIGHT);
        btnClearSnapChot.setSize(125, 15);

         
        
         
          // Title Section IV "TUIO"
         cp5.addTextlabel("TuioDebug")
                    .linebreak()
                    .setText("Tuio and Debug Infos")
                    .setPosition(970, 10 + MAP_HEIGHT)                    
                    .setFont(font);

        // Title section II.a
        cp5.addTextlabel("DebugConsole")
                    .linebreak()
                    .setText("DEBUG CONSOLE : ")
                    .setPosition(970, 40 + MAP_HEIGHT); 
        
        
        // Log TUIO Events ?
        // Title section II.b
        cp5.addTextlabel("LogTuioEventsLabel")
                    .linebreak()
                    .setText("LOG TUIO EVENTS : ")
                    .setPosition(1450, 40 + MAP_HEIGHT); 
        
        cp5.addToggle("toggleLogTuioEvents")         
                    .setSize(11, 11)
                    .setPosition(1555, 42 + MAP_HEIGHT)
                    //.setValue(true)
                    //.setMode(ControlP5.SWITCH)
                    .getCaptionLabel().setText("");
                    
                    
        

        Textarea   myTextarea = cp5.addTextarea("cp5Console")
                  .setPosition(970, 60 + MAP_HEIGHT)
                  .setSize(600, 163)
                  .setLineHeight(14)
                  .setColor(color(200))
                  .setColorBackground(color(0, 100))
                  .setColorForeground(color(255, 140));         
        
        
        // Creates the GUI Console.
        // From this point, all the console?.out-related methods are redirected
        // to this TextArea..
        GUI_CONSOLE = cp5.addConsole(myTextarea);
         

    }
    
          

    /**
     * Control Event (ControlP5).
     * @param event the ControlEvent from the control
     */
    public void controlEvent(ControlEvent event)
    {
        // DropdownList is of type ControlGroup.
        // A controlEvent will be triggered from inside the ControlGroup class.
        // therefore you need to check the originator of the Event with
        // if (theEvent.isGroup()) to avoid an error message thrown by controlP5.
        
       
        
        //int value = (int)event.getController().getValue();
        if (event.isGroup())
        {
            if (event.getName() == "handlePlaces")
            {

                // Get the value from the DropDownList.
                println("event from group : " + event.getGroup().getValue() + " from " + event.getGroup());

                int value = (int) Math.round(event.getGroup().getValue());

                println("DropDownList Value : " + value);

                // Selects the new Map with the selected ID..
                _currentMapLocation = _locations.get(value);

                // and zooms and pan into it..
                map.zoomAndPanTo(_currentLocation.getZoomLevel(), _currentMapLocation);

                // updates the GUI with the current map infos.
                updateGui();
            }
            if (event.getName() == "ProviderList")
            {
                println(event.getGroup().getValue());
                int selectedProviderIndex = (int) Math.round(event.getGroup().getValue());
                
                map.mapDisplay.setProvider(_providers.get(selectedProviderIndex));
                String providerName = _providers.get(selectedProviderIndex).getClass().getName();               
                println("Setting Provider to \"" + providerName + "\"");
                
            }
        }
        // if the source of the event is not a "Group" (list, group, menu, tab, etc..).
        else
        {
            // "Take Snapshot Button").
            if( event.getName() == "TakeSnapshotButton")
            {
                takeSnapshot();
                
            }
            
            // "Clear Snapshot" Button 
            if ( event.getName() == "btnClearSnapshotButton")
            {
                clearSnapshot();
            }
 
//            if (event.getName() =="LogTuioEvents") 
//            {             
//                
//                print("got an event from " + event.getController().getName() + "\t\n");
//                // checkbox uses arrayValue to store the state of 
//                // individual checkbox-items. usage:
//                println(event.getController().getArrayValue());
//                int col = 0;
//                for (int i = 0; i < event.getController().getArrayValue().length; i++)
//                {
//                    int n = (int) event.getController().getArrayValue()[i];
//                    print(n);
//                    if (n == 1)
//                    {
//                        //myColorBackground += checkbox.getItem(i).internalValue();
//                    }
//                }            
//            }
            
                
        }
        
    }

    
        
  
    

    /**
     * *************************************************************************
     *
     * TUIO METHODS
     *
     ***************************************************************************
     */
    
    /**
     * toggleTuioEvents 
     * 
     * This method is called by reflection by the controlP5 chekcBox with
     * the same name as this method.
     */
    public void toggleLogTuioEvents()
    {
        _logTuioEvents = ! _logTuioEvents;
    }

    
    /**
     * drawTuioCursors : draws each TUOCursor as a circle with its ID on it.
     */
    private void drawTuioCursors()
    {
        // Draw each Tuio Cursor..
        //
        // each cursor is a circle ..
        fill(255, 200);
        for (TuioCursor tcur : tuioClient.getTuioCursors())
        {
            ellipse(tcur.getScreenX(width), tcur.getScreenY(height), 20, 20);
            stroke(0);
        }
        
        // draws the cursor ID on top
        tuioCursorHandler.drawCursors();
    }
    

    
    
    /**
     * Initializes TUIO.
     */
     private void initTUIO()
    {
        // TUIO Setup.
        //
        // Creates the EventDispatcher for the tuioCursorHandler..
        eventDispatcher     = new EventDispatcher();
        tuioCursorHandler   = new TuioCursorHandler(this, false, map);
        
        eventDispatcher.addBroadcaster(tuioCursorHandler);
        
        eventDispatcher.register(map, "pan");
        eventDispatcher.register(map, "zoom");
        //eventDispatcher.register(map, "rotate"); already rotating..

        tuioClient = tuioCursorHandler.getTuioClient();
        tuioClient.addTuioListener(this);
    }
    
      
    
    
    /**
     * Happens when a cursor is added.
     *
     * @param tuioCursor the TUIO Cursor
     */
    @Override
    public void addTuioCursor(TuioCursor tuioCursor)
    {
        int x = tuioCursor.getScreenX(width);
        int y = tuioCursor.getScreenY(height);

        log.debug("Add " + tuioCursor.getCursorID() + ": " + x + ", " + y);
        
        if (_logTuioEvents)
        {
            System.out.println(("TuioCursor Add :   " + tuioCursor.getCursorID() + ", Session ID : " + tuioCursor.getSessionID() + ", X;Y : " + x + ";" + y));
            
            // HACK : Emulate mouse click
            //cp5.getPointer().set(x,y);
 
        }
        
        tuioCursorHandler.addTuioCursor(tuioCursor);          
        
    }
      
    @Override
    public void updateTuioCursor(TuioCursor tuioCursor)
    {
        int x = tuioCursor.getScreenX(width);
        int y = tuioCursor.getScreenY(height);
        
        log.debug("Update " + tuioCursor.getCursorID() + ": " + x + ", " + y);
 
        if (_logTuioEvents)
        { 
            System.out.println(("TuioCursor Update :" + tuioCursor.getCursorID() + ", Session ID : " + tuioCursor.getSessionID() +  ", X;Y : " + x + ";" + y));
        }
        
        // HACK : Emulate mouse click
        //cp5.getPointer().set(x , y);
        //cp5.getPointer().pressed();
        
        tuioCursorHandler.updateTuioCursor(tuioCursor);
    }
    

    @Override
    public void removeTuioCursor(TuioCursor tuioCursor)
    {
        log.debug("Remove " + tuioCursor.getCursorID());
        
        if (_logTuioEvents)
        {
            System.out.println("TuioCusor Remove :  " + tuioCursor.getCursorID() + ", Session ID : " + tuioCursor.getSessionID());

        }
        

        // HACK : Emulate mouse click        
        //cp5.getPointer().set(-1000, -1000);
        //cp5.getPointer().released();
        
        
        tuioCursorHandler.removeTuioCursor(tuioCursor);
    }
    
    // Section for fiducial markers, not for simple multi-touch.

    @Override
    public void addTuioObject(TuioObject arg0)
    {
        // No objects used
    }    

    @Override
    public void refresh(TuioTime arg0)
    {
        // Not used
    }    

    @Override
    public void removeTuioObject(TuioObject arg0)
    {
        // No objects used
    }    

    @Override
    public void updateTuioObject(TuioObject arg0)
    {
        // No objects used
    }
    
    
    /***************************************************************************
     * Utilities Methods.
    ***************************************************************************/
    
    /**
     * prints the standard output Stream.
     * @param text 
     */
    private void toConsole(String text)
    {
        println(text);
    }
    
    /***************************************************************************
     * Main Method
    ***************************************************************************/
    
    
    
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
            "--present",
            "betalabs.libtests.unfolding.apps.MultiTouchMapApp",
            
        };

        
        // Launch..
        PApplet.main(params);
    }   

}


/* Map Providers


AcetateProvider 
AcetateProvider.All 
AcetateProvider.Basemap 
AcetateProvider.Foreground 
AcetateProvider.GenericAcetateProvider 
AcetateProvider.Hillshading 
AcetateProvider.Labels 
AcetateProvider.Roads 
AcetateProvider.Terrain 

CartoDBProvider 

EsriProvider 
EsriProvider.DeLorme 
EsriProvider.GenericEsriProvider 
EsriProvider.NatGeoWorldMap 
EsriProvider.OceanBasemap 
EsriProvider.WorldGrayCanvas 
EsriProvider.WorldPhysical 
EsriProvider.WorldShadedRelief 
EsriProvider.WorldStreetMap 
EsriProvider.WorldTerrain 
EsriProvider.WorldTopoMap 

GeoMapApp.GeoMapAppProvider 
GeoMapApp.TopologicalGeoMapProvider 

Google 
Google.GoogleMapProvider 
Google.GoogleProvider 
Google.GoogleSimplified2Provider 
Google.GoogleSimplifiedProvider 
Google.GoogleTerrainProvider 

ImmoScout 
ImmoScout.HeatMapProvider 
ImmoScout.ImmoScoutProvider 

MapBox 
MapBox.BlankProvider 
MapBox.ControlRoomProvider 
MapBox.LacquerProvider 
MapBox.MapBoxProvider 
MapBox.MuseDarkStyleProvider 
MapBox.PlainUSAProvider 
MapBox.WorldLightProvider 

MapQuestProvider 
MapQuestProvider.Aerial 
MapQuestProvider.GenericMapQuestProvider 
MapQuestProvider.OSM 

MBTilesMapProvider 

Microsoft 
Microsoft.AerialProvider 
Microsoft.HybridProvider 
Microsoft.MicrosoftProvider 
Microsoft.RoadProvider 

OpenMapSurferProvider 
OpenMapSurferProvider.GenericOpenMapSurferProvider 
OpenMapSurferProvider.Grayscale 
OpenMapSurferProvider.Roads 

OpenStreetMap 
OpenStreetMap.CloudmadeProvider 
OpenStreetMap.GenericOpenStreetMapProvider 
OpenStreetMap.OpenStreetMapProvider 
OpenStreetMap.OSMGrayProvider 

OpenWeatherProvider 
OpenWeatherProvider.Clouds 
OpenWeatherProvider.CloudsClassic 
OpenWeatherProvider.GenericOpenWeatherMapProvider 
OpenWeatherProvider.Precipitation 
OpenWeatherProvider.PrecipitationClassic 
OpenWeatherProvider.Pressure 
OpenWeatherProvider.PressureContour 
OpenWeatherProvider.Rain 
OpenWeatherProvider.RainClassic 
OpenWeatherProvider.Snow 
OpenWeatherProvider.Temperature 
OpenWeatherProvider.Wind 

StamenMapProvider 
StamenMapProvider.Toner 
StamenMapProvider.TonerBackground 
StamenMapProvider.TonerLite 
StamenMapProvider.WaterColor 

ThunderforestProvider 
ThunderforestProvider.GenericThunderforestProvider 
ThunderforestProvider.Landscape 
ThunderforestProvider.OpenCycleMap 
ThunderforestProvider.Outdoors 
ThunderforestProvider.Transport 

Yahoo 
Yahoo.AerialProvider 
Yahoo.HybridProvider 
Yahoo.RoadProvider 
Yahoo.YahooProvider

*/