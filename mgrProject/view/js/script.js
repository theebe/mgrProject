/**
 * @author: Szymon Skupien
 */

////////////////////////////////////////////////////////////////////
// Zmienne

/**
 * obiekt mapy OpenLayers
 */
var map;

/**
 * Tablica z obiektami przystankow
 */
var przystanki =  [];


/**
 * Warstwy na których pracujemy
 */
var przystankiLayer;
var start_stopLayer;

/**
 * Punkty poczatku i konca trasy
 */
var start = null;
var stop = null;

/**
 * Popup wyskakujacy po kliknieciu na mape
 */
var popup = null;
var popupContent;

/**
 * ikonki
 */
var iconCross = null;
var iconStart = null;
var iconStop = null;

/**
 * marker krzyza oznaczajacy klikniecie
 */
var crossMarker = null


/////////////////////////////////////////////////////////////
//Funkcje


/**
 * FUNKCJA STARTUJACA PO ZALADOWANIU STRONY
 * dodaje glowne zdarzenia 
 */
$(document).ready(function() {

	popupContent=$(".popupContent").html();
	
	
	//inicjacja mapy, dodanie warstw...
	mapInit();
	

	// dynamiczna zmiana rozmiaru strony
	findSize();

	// zdarzenie resizu okna
	$(window).resize(function() {
		findSize();
	});
	
	
	iconCross = new OpenLayers.Icon("./openlayers/img/cross.png", new OpenLayers.Size(20, 20), new OpenLayers.Pixel(-10, -10));
	iconStart = new OpenLayers.Icon("./openlayers/img/marker-green.png", new OpenLayers.Size(30, 40), new OpenLayers.Pixel(-(15), -40));
	iconStop = new OpenLayers.Icon("./openlayers/img/marker.png", new OpenLayers.Size(30, 40), new OpenLayers.Pixel(-(15), -40));



});//


/**
 * Funkcja inicjalizuj¹ca mape
 */
function mapInit() {
	// Start position for the map (hardcoded here for simplicity)
	var lon = 19.937036260585;
	var lat = 50.064963154336;
	var zoom = 17;

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
		numZoomLevels : 20,
		units : 'm',
		projection : new OpenLayers.Projection("EPSG:900913"),
		displayProjection : new OpenLayers.Projection("EPSG:4326")
	});

	clickAction.activate();
	
	
	
	// Warstwy map:
	var layerMapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
	map.addLayer(layerMapnik);

	var layerTilesAtHome = new OpenLayers.Layer.OSM.Osmarender("Osmarender");
	map.addLayer(layerTilesAtHome);

	var gmap = new OpenLayers.Layer.Google("Google Streets", {
		visibility : false
	});
	map.addLayer(gmap);
	
	
	var gsat = new OpenLayers.Layer.Google("Google Satellite",
		    {type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 22});
	map.addLayer(gsat);
	
	// Warsty robocze
   // przystankiLayer = new OpenLayers.Layer.Markers("Przystanki");
	//map.addLayer(przystankiLayer);
	
	start_stopLayer = new OpenLayers.Layer.Markers("Start_Stop");
	map.addLayer(start_stopLayer);

	
	// Ustawia centrum mapy
	 if (!map.getCenter()) { //if potrzebny do permalink
		map.setCenter(new OpenLayers.LonLat(lon, lat).transform(
				new OpenLayers.Projection("EPSG:4326"), map.getProjectionObject()),
				zoom);
	 }
	 
	 
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
	var widthInfoPanel = $(".infoPanelContainer").width() + 44;
	
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

         onClick: function(e) {
        	 onClickEvent(e);

         }

     });
}



/**
 * Ta funcja wykona siê po klikniêciu na mapie
 * Wyswietla popup menu
 * @param e event
 */
function onClickEvent(e){
	
	var lonlat = map.getLonLatFromViewPortPx(e.xy);
	
	
	//dodanie markera krzyza na mape
	if(crossMarker){
		start_stopLayer.removeMarker(crossMarker);
		crossMarker = null;
	}
	crossMarker = new OpenLayers.Marker(lonlat, iconCross.clone());
	start_stopLayer.addMarker(crossMarker);
	
	
	//dodanie popupu
	 if(popup) map.removePopup(popup);
	 popup = new OpenLayers.Popup('menu', 
			  lonlat, 
			  new OpenLayers.Size(100,100),
			  popupContent, 
			  true);
	 
	 popup.setBackgroundColor('darkblue');	
	 map.addPopup(popup);
	 
	 
	 //Eventy popup menu: Start stop etc
	 $(".popupStart").click(function(){
		 
		 var startLonLat = popup.lonlat;
		 if(start){
			 start_stopLayer.removeMarker(start);
             start = null;
		 } 
		 start = new OpenLayers.Marker(startLonLat,iconStart.clone()); 
		 start_stopLayer.addMarker(start);
		 map.removePopup(popup);	
		 start_stopLayer.removeMarker(crossMarker);
			crossMarker = null;
	 });
	 $(".popupStop").click(function(){
		 
		 var stopLonLat = popup.lonlat;
		 if(stop){
			 start_stopLayer.removeMarker(stop);
			 stop = null;
		 } 
		 stop = new OpenLayers.Marker(stopLonLat,iconStop.clone()); 
		 start_stopLayer.addMarker(stop);
		 map.removePopup(popup);
		 start_stopLayer.removeMarker(crossMarker);
			crossMarker = null;
		});
	 $(".popupDodajPrzystanek").click(function(){
		 
		 lonlat.transform(map.getProjectionObject(), new OpenLayers.Projection("EPSG:4326"));
         przystanki.push(
        		 {
                 "type":"Point", 
                 "coordinates":[lonlat.lon, lonlat.lat]
        		 }
         );
         map.removePopup(popup);
         start_stopLayer.removeMarker(crossMarker);
			crossMarker = null;
         updatePrzystankiView();
		});
	 
}


function updatePrzystankiView(){
	
	
	var przystanek = przystanki[przystanki.length-1];
	$("#przystanki").append('[' + przystanek.coordinates[0] + ', ' + przystanek.coordinates[1] + ']<br />');

	
	
}

