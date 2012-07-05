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
 * Tablica z obiektami przystankow typ obiektow OpenLayers.Feature.Vector:
 */
var przystanki = [];

/**
 * Warstwy na których pracujemy
 */
var przystankiLayer;
var przystankiLayerStyle;
var start_stopLayer;

/**
 * Punkty poczatku i konca trasy
 */
var start = null;
var stop = null;
var startTime = null

/**
 * Popup wyskakujacy po kliknieciu na mape
 */
var popup = null;

var przystanekInfoPopup = null;


/**
 * Linia na ktorej pracujemy
 */
var liniaVect = null;

/**
 * trasa, tablica obiektow typu OpenLayers.Feature.Vector
 */
var path = [];

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


// ///////////////////////////////////////////////////////////
// Funkcje

/**
 * FUNKCJA STARTUJACA PO ZALADOWANIU STRONY dodaje glowne zdarzenia
 */
$(document).ready(function() {

	
	// now
	startTime = new Date();

	// dynamiczna zmiana rozmiaru strony
//	findSize();

	// zdarzenie resizu okna
//	$(window).resize(function() {
//		findSize();
//	});

	// inicjalizuje ikonki i guziki i eventy
	homeGuiInit();

	getPrzystankiFeatures();

	// inicjacja mapy, dodanie warstw...
	mapInit();

});//

/**
 * Funkcja inicjalizuj¹ca mape
 */
function mapInit() {
	// Start position for the map (hardcoded here for simplicity)
	var lon = 19.937036260585;
	var lat = 50.064963154336;
	var zoom = 15;

	iconCross = new OpenLayers.Icon("./openlayers/img/cross.png",
			new OpenLayers.Size(20, 20), new OpenLayers.Pixel(-10, -10));
	iconStart = new OpenLayers.Icon("./openlayers/img/marker-green.png",
			new OpenLayers.Size(30, 40), new OpenLayers.Pixel(-(15), -40));
	iconStop = new OpenLayers.Icon("./openlayers/img/marker.png",
			new OpenLayers.Size(30, 40), new OpenLayers.Pixel(-(15), -40));

	// inicjalizuje konstruktor i opcje handlera eventu onClick na mapie
	addOnClickAction();
	// kontrolka klikania, wymaga osobnej aktywacji po dodaniu do mapy
	var clickAction = new OpenLayers.Control.Click();
	map = new OpenLayers.Map("map", {
		// dodanie kontrolek na mape
		controls : [ new OpenLayers.Control.Navigation(),
				new OpenLayers.Control.PanZoomBar(),
				new OpenLayers.Control.LayerSwitcher(),
				new OpenLayers.Control.MousePosition(),
				new OpenLayers.Control.Permalink('permalink'),
				new OpenLayers.Control.ScaleLine(), clickAction,
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
	map.Z_INDEX_BASE = {
		BaseLayer : 1,
		Overlay : 2,
		Feature : 3,
		Popup : 1000,
		Control : 5
	};
	clickAction.activate();

	// Warstwy map:
	var layerMapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
	map.addLayer(layerMapnik);


	var gmap = new OpenLayers.Layer.Google("Google Streets", {
		visibility : false
	});
	map.addLayer(gmap);

	var gsat = new OpenLayers.Layer.Google("Google Satellite", {
		type : google.maps.MapTypeId.SATELLITE,
		numZoomLevels : 22
	});
	map.addLayer(gsat);

	///////////////////
	// Warsty robocze

	//styl do warstwy
	przystankiLayerStyle = new OpenLayers.Style(
	// the first argument is a base symbolizer
	// all other symbolizers in rules will extend this one
	{
		fillColor : "#ff00ED",
		fillOpacity : 1,
		strokeColor : "#9800FF",
		strokeWidth: 4,
		strokeLinecap: "square",
		strokeDashstyle: "solid",
		pointRadius : 1,
		graphicOpacity: 1,
		graphicWidth : 30,
		graphicHeight : 30,
		graphicXOffset : -30 / 2,
		graphicYOffset : -30 / 2
	}, // the second argument will include all rules
	{
		rules : [ new OpenLayers.Rule({
			// a rule contains an optional filter
			filter : new OpenLayers.Filter.Comparison({
				type : OpenLayers.Filter.Comparison.EQUAL_TO,
				property : "typ", // the "foo" feature attribute
				value : "A"
			}),

			symbolizer : {
				externalGraphic : "./openlayers/img/autobus.png"
			}
		}), new OpenLayers.Rule({
			elseFilter : true,
			symbolizer : {
				externalGraphic : "./openlayers/img/tramwaj.png"
			}
		}) ]
	}

	);
	//dodanie stylu
	przystankiLayer = new OpenLayers.Layer.Vector("Przystanki", {
		styleMap : new OpenLayers.StyleMap(przystankiLayerStyle)
	});

	map.addLayer(przystankiLayer);

	start_stopLayer = new OpenLayers.Layer.Markers("Start_Stop");
	map.addLayer(start_stopLayer);

	// Ustawia centrum mapy
	if (!map.getCenter()) { // if potrzebny do permalink
		map.setCenter(new OpenLayers.LonLat(lon, lat).transform(
				new OpenLayers.Projection("EPSG:4326"), map
						.getProjectionObject()), zoom);
	}

}

/**
 * FUNKCJA DOSTOSOWUJE WYSOKOSC CONTENEROW DO WIELKOSCI OKNA
 */
function findSize() {
	// pobiera wysokosc szerokosc okna
	var height = $(window).height();
	var width = $(window).width();

	// wysokosc i szerokosc 2panelow: na gorze i po prawej
	var heightTopPanel = $(".topPanelContainer").height() + 70;
	var widthInfoPanel = $(".infoPanelContainer").width() + 44;

	// ustawia wielkosci panelow mapy i info
	$(".mapa").height(height - heightTopPanel);
	$(".infoPanelContainer").height(height - heightTopPanel);
	$(".mapa").width(width - widthInfoPanel);
}

/**
 * Funkcja ktora dodaje i parametryzyje event klikniecia na mapie
 */
function addOnClickAction() {
	OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {
		defaultHandlerOptions : {
			'single' : true,
			'double' : false,
			'pixelTolerance' : 0,
			'stopSingle' : false,
			'stopDouble' : false
		},

		initialize : function(options) {
			this.handlerOptions = OpenLayers.Util.extend({},
					this.defaultHandlerOptions);
			OpenLayers.Control.prototype.initialize.apply(this, arguments);
			this.handler = new OpenLayers.Handler.Click(this, {
				'click' : this.onClick
			}, this.handlerOptions);
		},

		onClick : function(e) {
			onClickEvent(e);

		}

	});
}

/**
 * Ta funcja wykona siê po klikniêciu na mapie Wyswietla popup menu
 * 
 * @param e
 *            event
 */
function onClickEvent(e) {

	var lonlat = map.getLonLatFromViewPortPx(e.xy);

	// dodanie markera krzyza na mape
	if (crossMarker) {
		start_stopLayer.removeMarker(crossMarker);
		crossMarker = null;
	}
	crossMarker = new OpenLayers.Marker(lonlat, iconCross.clone());
	start_stopLayer.addMarker(crossMarker);

	// dodanie popupu
	if (popup)//jezeli popup juz jest skasuj poprzedni
		map.removePopup(popup);
	
	popup = new OpenLayers.Popup('menu', lonlat, new OpenLayers.Size(100, 120),
			$(".popupContent").html(), true);

	popup.setBackgroundColor('darkblue');
	popup.panMapIfOutOfView = true;
	map.addPopup(popup);

	// Eventy popup menu: Start stop etc
	$(".popupStart")
			.click(
					function() {
						popupStartStopMarker('start', lonlat, iconStart);
						removePopupFromMap();
						var startLonLat2 = new OpenLayers.LonLat(lonlat.lon,
								lonlat.lat).transform(
								map.getProjectionObject(),
								new OpenLayers.Projection("EPSG:4326"))
						$("#start").val(
								startLonLat2.lon.toFixed(5) + " ,  "
										+ startLonLat2.lat.toFixed(5));
					});
	$(".popupStop").click(
			function() {

				popupStartStopMarker('stop', lonlat, iconStop);
				removePopupFromMap();
				var stopLonLat2 = new OpenLayers.LonLat(lonlat.lon, lonlat.lat)
						.transform(map.getProjectionObject(),
								new OpenLayers.Projection("EPSG:4326"));
				// stopLonLat.transform(map.getProjectionObject(),
				// new OpenLayers.Projection("EPSG:4326"));
				$("#stop").val(
						stopLonLat2.lon.toFixed(5) + " ,  "
								+ stopLonLat2.lat.toFixed(5));
			});
	$(".popupDodajPrzystanek").click(
			function() {
				lonlat.transform(map.getProjectionObject(),
						new OpenLayers.Projection("EPSG:4326"));

				removePopupFromMap();

				// open dialog form
				$(".addPrzystanekDialog").dialog("open");
				$("#przystanekLon").val(lonlat.lon.toFixed(10));
				$("#przystanekLat").val(lonlat.lat.toFixed(10));
			});
	$(".popupDodajLinie").click(function(){
		$("ul.listaPrzystankowLinii li").each(function(index) {
			$(this).remove();
			$(".listaPrzystankow").append($(this));
			 $(this).children("span").removeClass("ui-icon-circle-arrow-w arrowPrzystL").addClass("ui-icon-circle-arrow-e arrowPrzystR");
		  });
		
		$(".arrowPrzystR").click(function() {
			var element = $(this).parent().get();
			
			element.remove();
			$(".listaPrzystankowLinii").append(element);
			$(".addLiniaDialog ul").sortable( "refresh" );
			 $(this).removeClass("ui-icon-circle-arrow-e arrowPrzystR").addClass("ui-icon-circle-arrow-w arrowPrzystL");
			 $(this).parent().removeClass("ui-state-hover");
			 $(".arrowPrzystL").click(function() {
					var element = $(this).parent().get();
					element.remove();
					$(".listaPrzystankow").append(element);
					$(".addLiniaDialog ul").sortable( "refresh" );
					 $(this).removeClass("ui-icon-circle-arrow-w arrowPrzystL").addClass("ui-icon-circle-arrow-e arrowPrzystR");
					 map.removePopup(przystanekInfoPopup);
					 $(this).parent().removeClass("ui-state-hover");
				});
		});
		
		 $(".addLiniaDialog ul").sortable( "refresh" );
		 $("#liniaTypA").attr("checked", "checked");
		 $("#liniaTypT").removeAttr("checked");
		$(".addLiniaDialog").dialog("open");
		removePopupFromMap();
	});

}

/**
 * FUnkcja ustawia marker starotwy i koncowy
 * @param s typ markera, 'start' 'stop'
 * @param sLonLat openlayers.lonlat
 * @param ic ikona
 */
function popupStartStopMarker(s, sLonLat, ic) {
	if (s == 'start') {
		if (start) {
			start_stopLayer.removeMarker(start);
			start = null;
		}
		start = new OpenLayers.Marker(sLonLat, ic.clone());
		start_stopLayer.addMarker(start);
	} else if (s == 'stop') {
		if (stop) {
			start_stopLayer.removeMarker(stop);
			stop = null;
		}
		stop = new OpenLayers.Marker(sLonLat, ic.clone());
		start_stopLayer.addMarker(stop);
	}

}

function removePopupFromMap() {
	map.removePopup(popup);
	start_stopLayer.removeMarker(crossMarker);
	crossMarker = null;
}



/**
 * Funkcja wykonuje sie po wyslaniu zapytania ajax pokazuje loading message
 * 
 * @returns
 */
var displayLoadingMessage = function() {
	$(".loadingMessage").css("display", "block");
}

/**
 * funkcja wykonuje sie po otrzymaniu odp z ajax chowa loading message
 * 
 * @returns
 */
var hideLoadingMessage = function() {
	$(".loadingMessage").css("display", "none");
}

/**
 * Funkcja ustawiajaca GUI
 */
function homeGuiInit() {

	// ///////////////////////////////////////////////////////////////////////////////////////
	// BUTTONY LEWO PRAWO w dacie i godzinie
	homeFormButtonInit();

	// ///////////////////////////////////////////////////////////////////////////////////////
	// DATAPICKER
	homeDatapickerInit();

	// szukaj button
	$(".searchButton").button();
	$(".searchButton").click(function(e) {
		searchButtonClick(e);
	});

	
	//Tabs
	$( "#tabs" ).tabs();
	
	// ///////////////////////////////////////////////////////////////////////////////////////
	// Add przystanek dialog form
	homeAddPrzystanekDialogInit();
	
	homeAddLiniaDialogInit();
	
	
	

	// ustawienie loading message
	Seam.Remoting.displayLoadingMessage = displayLoadingMessage;
	Seam.Remoting.hideLoadingMessage = hideLoadingMessage;
}

/**
 * Parsuje pola takie jak start
 * 
 * @returns
 */
function parseInputParameters() {
	var ret = null;
	if (start == null) {
		ret = "Nale¿y ustawiæ punkt startowy";
	} else if (stop == null) {
		ret = "Nale¿y ustawiæ punkt docelowy";
	} else if (startTime == null) {
		ret = "Nale¿y ustawiæ date i czas";
	}

	return ret;
}

/**
 * Pobiera przystanki z bazy
 * 
 * @returns tablica z obiektami OpenLayers.Feature.Vector
 */
function getPrzystankiFeatures() {
	if (przystanki.length == 0) {

		// Pobieranie przystankow z bazy
		Seam.Remoting.startBatch();
		
		Seam.Remoting.getContext().setConversationId(seamConversationId );
		var przystanekDAO = Seam.Component.getInstance("przystanekDAO");

		var getAllPrzystankiCallback = function(p) {
			var i = 0;
			for (i; i < p.length; i += 1) {

				var vect = createVectorPrzystanek(p[i]);
				
				przystanki.push(vect);

			}

			przystankiLayer.addFeatures(przystanki);
		};

		przystanekDAO.getPrzystanekList(getAllPrzystankiCallback);

		Seam.Remoting.executeBatch();
	}

}

/**
 * Tworzy obiekt vector z encji pl.mgrProject.model.Przystanek
 * @param przystanek
 * @returns {OpenLayers.Feature.Vector}
 */
function createVectorPrzystanek(przystanek){
	var lon_lat = new OpenLayers.LonLat(przystanek.location.x,
			przystanek.location.y).transform(new OpenLayers.Projection(
			"EPSG:4326"), map.getProjectionObject());

	var point = new OpenLayers.Geometry.Point(lon_lat.lon,
			lon_lat.lat);
	var vect = new OpenLayers.Feature.Vector(point, {
		nazwa : przystanek.nazwa,
		id : przystanek.id,
		typ : przystanek.typ
	});
	return vect;
}

/**
 * pobiera z lokalnej tablicy przystankow (obiekty openlayers.feature.vector)
 * indeks obiektu przystanku o zadanym id
 * @param id -  id przystanku
 * @returns obiekt openlayers.feature.vector - przystanek
 */
function getIPrzystnekFromId(id){
	var i =0, id_tmp=0;
	for(i=0; i<przystanki.length; ++i){
		id_tmp = przystanki[i].attributes.id;
		if(id == id_tmp)
			return i;
	}
	return null;
}

function deletePrzystanekFromMap(id){

	var i = getIPrzystnekFromId(id);
	//kasowanie
	przystanki.splice(i,1);
	przystankiLayer.removeAllFeatures();
	przystankiLayer.addFeatures(przystanki);
	alert("Usuniêto przystanek");
}

function showPrzystanekOnMap(id){
	var przystanekFeature = przystanki[getIPrzystnekFromId(id)];
	var lonLat = new OpenLayers.LonLat(
			przystanekFeature.geometry.x,
			przystanekFeature.geometry.y);

	przystanekInfoPopup = new OpenLayers.Popup.FramedCloud(
			"przystanekFramedCloud", lonLat, null,
			przystanekFeature.attributes.nazwa, null,
			false);
	map.addPopup(przystanekInfoPopup);
}

function hidePrzystanek(){
	if(przystanekInfoPopup != null)
		map.removePopup(przystanekInfoPopup);
	przystanekInfoPopup = null;
}


function showLiniaOnMap(linia){
	if(liniaVect != null) 
		hideLinia();
	
	var przystTablList = linia.przystanekTabliczka;
	var points = [];

	var i =0
	for(i; i<przystTablList.length; ++i){
		var przystTabl = przystTablList[i];
		var przystanekGeometry = przystanki[getIPrzystnekFromId(przystTabl.przystanek.id)].geometry;
		points.push(przystanekGeometry);
	}
	var lineString = new OpenLayers.Geometry.LineString(points);
	liniaVect = new OpenLayers.Feature.Vector(lineString,{
		id: 	linia.id,
		numer: 	linia.numer
	});
	
	przystankiLayer.addFeatures([liniaVect]);
	
}

function hideLinia(){
	przystankiLayer.removeFeatures([liniaVect]);
	liniaVect = null;
}
