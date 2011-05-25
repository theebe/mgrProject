/**
 * @author: Szymon Skupien
 */

 
 var SHADOW_Z_INDEX = 10;
var MARKER_Z_INDEX = 11;

 
 
/**
 * obiekt mapy OpenLayers
 */
var map;

/**
 * Tablica z obiektami przystankow
 */
var przystanki =  [];


        

var icon;

/**
 * Warstwy na których pracujemy
 */
var przystankiLayer;
var start_stopLayer;
var layer;
var start = null;
var stop = null;
var size;
var calculateOffset;

var DIAMETER = 200;
var NUMBER_OF_FEATURES = 15;


/**
 * FUNKCJA STARTUJACA PO ZALADOWANIU STRONY MAIN
 */
$(document).ready(function() {

	mapInit();

	// dynamiczna zmiana rozmiaru strony
	findSize();
	// zdarzenie resizu okna
	$(window).resize(function() {
		findSize();
	});
	// alert("jestem");

});//

/**
 * Funkcja inicjalizuj¹ca mape
 */
function mapInit() {
	// Start position for the map (hardcoded here for simplicity)
	var lon = 19.937036260585;
	var lat = 50.064963154336;
	var zoom = 13;

	//inicjalizuje konstruktor i opcje handlera eventu onClick na mapie
	addOnClickAction();
	//kontrolka klikania, wymaga osobnej aktywacji po dodaniu do mapy
	var clickAction = new OpenLayers.Control.Click();
	map = new OpenLayers.Map("map", {
		// dodanie kontrolek na mape
		controls : [ new OpenLayers.Control.Navigation(),
				new OpenLayers.Control.PanZoomBar(),
				new OpenLayers.Control.LayerSwitcher(),
				new OpenLayers.Control.MousePosition(),
			    new OpenLayers.Control.Permalink('permalink'), 
				new OpenLayers.Control.ScaleLine(),
				clickAction,
				new OpenLayers.Control.KeyboardDefaults() /*
															 * , new
															 * OpenLayers.Control.Attribution()
															 */
		],

		/*
		 * maxExtent: new
		 * OpenLayers.Bounds(-20037508.34,-20037508.34,20037508.34,20037508.34),
		 */
		/* maxResolution: 156543.0399, */
		numZoomLevels : 20,
		units : 'm',
		projection : new OpenLayers.Projection("EPSG:900913"),
		displayProjection : new OpenLayers.Projection("EPSG:4326")
	});

	clickAction.activate();
	
	
	
	// Warstwy:
	var layerMapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
	map.addLayer(layerMapnik);

	var layerTilesAtHome = new OpenLayers.Layer.OSM.Osmarender("Osmarender");
	map.addLayer(layerTilesAtHome);

	var gmap = new OpenLayers.Layer.Google("Google Streets", {
		visibility : false
	});
	map.addLayer(gmap);
	
	/*var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;
	start_stopLayer = new OpenLayers.Layer.Vector(
                "Start Stop",
                {
                    styleMap: new OpenLayers.StyleMap({
                        // Set the external graphic and background graphic images.
                        externalGraphic: "../openlayers/img/marger-gold.png",
                        backgroundGraphic: "../openlayers/img/marker_shadow.png",
                        
                        // Makes sure the background graphic is placed correctly relative
                        // to the external graphic.
                        backgroundXOffset: 0,
                        backgroundYOffset: -7,
                        
                        // Set the z-indexes of both graphics to make sure the background
                        // graphics stay in the background (shadows on top of markers looks
                        // odd; let's not do that).
                        graphicZIndex: MARKER_Z_INDEX,
                        backgroundGraphicZIndex: SHADOW_Z_INDEX,
                        
                        pointRadius: 10
                    }),
                    rendererOptions: {yOrdering: true},
                    renderers: renderer
                }
            );
	map.addLayer(start_stopLayer);

	*/
     przystankiLayer = new OpenLayers.Layer.Markers("Przystanki");
	map.addLayer(przystankiLayer);
	

    // allow testing of specific renderers via "?renderer=Canvas", etc
    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;

    layer = new OpenLayers.Layer.Vector(
        "Marker Drop Shadows",
        {
            styleMap: new OpenLayers.StyleMap({
                // Set the external graphic and background graphic images.
                externalGraphic: "../openlayers/img/marker-gold.png",
                backgroundGraphic: "../openlayers/img/marker_shadow.png",
                
                // Makes sure the background graphic is placed correctly relative
                // to the external graphic.
                backgroundXOffset: 0,
                backgroundYOffset: -7,
                
                // Set the z-indexes of both graphics to make sure the background
                // graphics stay in the background (shadows on top of markers looks
                // odd; let's not do that).
                graphicZIndex: MARKER_Z_INDEX,
                backgroundGraphicZIndex: SHADOW_Z_INDEX,
                
                pointRadius: 10
            }),
            isBaseLayer: true,
            rendererOptions: {yOrdering: true},
            renderers: renderer
        }
    );
    
    map.addLayers([layer]);
    
    // Add a drag feature control to move features around.
    var dragFeature = new OpenLayers.Control.DragFeature(layer);
    
    map.addControl(dragFeature);
    
    dragFeature.activate();
                
    drawFeatures();
	

       size = new OpenLayers.Size(21, 25);
       calculateOffset = function(size) {
                   return new OpenLayers.Pixel(-(size.w/2), -size.h); };
       icon = new OpenLayers.Icon(
           'http://www.openlayers.org/dev/img/marker.png',
           size, null, calculateOffset);
       
	 if (!map.getCenter()) { //if potrzebny do permalink
		map.setCenter(new OpenLayers.LonLat(lon, lat).transform(
				new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()),
				zoom);
	 }
	 
	 
	 
	 
	// add behavior to html
	/*
	 * var animate = document.getElementById("animate"); animate.onclick =
	 * function() { for (var i=map.layers.length-1; i>=0; --i) {
	 * map.layers[i].animationEnabled = this.checked; } };
	 */
	 
	  drawFeatures();
}

/**
 * FUNKCJA DOSTOSOWUJE WYSOKOSC CONTENEROW DO WIELKOSCI OKNA
 */
function findSize() {
	// pobiera wysokosc szerokosc okna
	var height = $(window).height();
	var width = $(window).width();
	
	//wysokosc i szerokosc 2panelow: na gorze i po prawej
	var heightTopPanel = $(".topPanelContainer").height() + 70;
	var widthInfoPanel = $(".infoPanelContainer").width() + 5;
	
	//ustawia wielkosci panelow mapy i info
	$(".mapa").height(height - heightTopPanel);
	$(".infoPanelContainer").height(height - heightTopPanel);
	$(".mapa").width(width - widthInfoPanel);
}

/**
 * Funkcja ktora dodaje i parametryzyje event klikniecia na mapie
 */
function addOnClickAction(){	
	 OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {                
         defaultHandlerOptions: {
             'single': true,
             'double': false,
             'pixelTolerance': 0,
             'stopSingle': false,
             'stopDouble': false
         },

         initialize: function(options) {
             this.handlerOptions = OpenLayers.Util.extend(
                 {}, this.defaultHandlerOptions
             );
             OpenLayers.Control.prototype.initialize.apply(
                 this, arguments
             ); 
             this.handler = new OpenLayers.Handler.Click(
                 this, {
                     'click': this.onClick
                 }, this.handlerOptions
             );
         }, 

         /**
          * Ta funcja wykona siê po klikniêciu na mapie
          * @param e event
          */
         onClick: function(e) {
             var lonlat = map.getLonLatFromViewPortPx(e.xy);
             lonlat.transform(map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326"));
             przystanki.push(
            		 {
	                 "type":"Point", 
	                 "coordinates":[lonlat.lon, lonlat.lat]
            		 }
             );
             
             if(start == null){
            	 start = new OpenLayers.Feature.Vector( new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat));
				 setStart();
             }
             else{
            	 stop =  new OpenLayers.Feature.Vector( new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat));
				 setStop();
            //	 zrobRouting();
            	 
           
             }
             
             updatePrzystanki();
             
			 /*
             przystankiLayer.addMarker(
                     new OpenLayers.Marker(lonlat.transform(
             				new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()), icon)); 
							*/
							
			

          	
         }

     });
}


/**
* Funkcja ustawia na mapie ikonki Startu
* @param startPoint
*/
function setStart(){
	//start_stopLayer.removeFeatures([start]);
            
            var features = [];
            // Add the ordering features. These are the gold ones that all have the same z-index
            // and succomb to y-ordering.
                features.push(
                    start
                );
    
             start_stopLayer.addFeatures(features);
}

/**
 * Funkcja ustawiaj¹ca ikonkê mety na mapie
 * @param stopPoint
 */

function setStop(){

//	start_stopLayer.removeFeatures([stop]);
            
            var features = [];
            // Add the ordering features. These are the gold ones that all have the same z-index
            // and succomb to y-ordering.
                features.push(
                    stop
                );
    
             start_stopLayer.addFeatures(features);
}


function zrobRouting(){
	
	$.ajax({
		   type: "GET",
		   url: "http://www.yournavigation.org/api/1.0/gosmore.php?format=geojson&flat=52.215676&flon=5.963946&tlat=52.2573&tlon=6.1799&v=motorcar&fast=1&layer=mapnik",
		   dataType: 'json',
		   success: function(data) {
			    $('.topPanel').html(data);
			    $('.topPanel').appendt("aaa");
		
		 }});
	
	
}


function updatePrzystanki(){
	
	
	var przystanek = przystanki[przystanki.length-1];
	$("#przystanki").append('[' + przystanek.coordinates[0] + ', ' + przystanek.coordinates[1] + ']<br />');

	
	
}

/*
function drawFeatures() {
    
	start_stopLayer.removeFeatures(start_stopLayer.features);
    
    // Create features at random around the center.
    var center = map.getViewPortPxFromLonLat(map.getCenter());
    
    // Add the ordering features. These are the gold ones that all have the same z-index
    // and succomb to y-ordering.
    var features = [];
    
    for (var index = 0; index < NUMBER_OF_FEATURES; index++) {
        // Calculate a random x/y. Subtract half the diameter to make some
        // features negative.
        var x = (parseInt(Math.random() * DIAMETER)) - (DIAMETER / 2);
        var y = (parseInt(Math.random() * DIAMETER)) - (DIAMETER / 2);
        
        var pixel = new OpenLayers.Pixel(center.x + x, center.y + y);
        
        var lonLat = map.getLonLatFromViewPortPx(pixel);
        features.push(
            new OpenLayers.Feature.Vector(
                new OpenLayers.Geometry.Point(lonLat.lon, lonLat.lat)
            )
        );
    }
    
    start_stopLayer.addFeatures(features);
}

*/