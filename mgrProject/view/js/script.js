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
var przystanki = [];

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
var startTime = null

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

var dniTygodnia = [ 'pon.', 'wt.', 'sr.', 'czw.', 'pt.', 'sob.', 'niedz.' ];

// ///////////////////////////////////////////////////////////
// Funkcje

/**
 * FUNKCJA STARTUJACA PO ZALADOWANIU STRONY dodaje glowne zdarzenia
 */
$(document)
		.ready(
				function() {

					// now
					startTime = new Date();

					// pobranie contentu dla popupMenu
					popupContent = $(".popupContent").html();

					// dynamiczna zmiana rozmiaru strony
					findSize();

					// zdarzenie resizu okna
					$(window).resize(function() {
						findSize();
					});

					iconCross = new OpenLayers.Icon(
							"./openlayers/img/cross.png", new OpenLayers.Size(
									20, 20), new OpenLayers.Pixel(-10, -10));
					iconStart = new OpenLayers.Icon(
							"./openlayers/img/marker-green.png",
							new OpenLayers.Size(30, 40), new OpenLayers.Pixel(
									-(15), -40));
					iconStop = new OpenLayers.Icon(
							"./openlayers/img/marker.png", new OpenLayers.Size(
									30, 40), new OpenLayers.Pixel(-(15), -40));

					// inicjalizuje ikonki i guziki i eventy
					guiInit();

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
	var zoom = 17;

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
		Popup : 4,
		Control : 5
	}, clickAction.activate();

	// Warstwy map:
	var layerMapnik = new OpenLayers.Layer.OSM.Mapnik("Mapnik");
	map.addLayer(layerMapnik);

	var layerTilesAtHome = new OpenLayers.Layer.OSM.Osmarender("Osmarender");
	map.addLayer(layerTilesAtHome);

	var gmap = new OpenLayers.Layer.Google("Google Streets", {
		visibility : false
	});
	map.addLayer(gmap);

	var gsat = new OpenLayers.Layer.Google("Google Satellite", {
		type : google.maps.MapTypeId.SATELLITE,
		numZoomLevels : 22
	});
	map.addLayer(gsat);

	// Warsty robocze
	// przystankiLayer = new OpenLayers.Layer.Markers("Przystanki");
	// map.addLayer(przystankiLayer);

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
	if (popup)
		map.removePopup(popup);
	popup = new OpenLayers.Popup('menu', lonlat, new OpenLayers.Size(100, 100),
			popupContent, true);

	popup.setBackgroundColor('darkblue');
	map.addPopup(popup);

	// Eventy popup menu: Start stop etc
	$(".popupStart").click(
			function() {

				var startLonLat = popup.lonlat;
				if (start) {
					start_stopLayer.removeMarker(start);
					start = null;
				}
				start = new OpenLayers.Marker(startLonLat, iconStart.clone());
				start_stopLayer.addMarker(start);

				map.removePopup(popup);
				start_stopLayer.removeMarker(crossMarker);
				crossMarker = null;
				var startLonLat2 = new OpenLayers.LonLat(startLonLat.lon,
						startLonLat.lat).transform(map.getProjectionObject(),
						new OpenLayers.Projection("EPSG:4326"))
				$("#start").val(
						startLonLat2.lon.toFixed(5) + " ,  "
								+ startLonLat2.lat.toFixed(5));
			});
	$(".popupStop").click(
			function() {

				var stopLonLat = popup.lonlat;
				if (stop) {
					start_stopLayer.removeMarker(stop);
					stop = null;
				}
				stop = new OpenLayers.Marker(stopLonLat, iconStop.clone());
				start_stopLayer.addMarker(stop);
				map.removePopup(popup);
				start_stopLayer.removeMarker(crossMarker);
				crossMarker = null;
				var stopLonLat2 = new OpenLayers.LonLat(stopLonLat.lon,
						stopLonLat.lat).transform(map.getProjectionObject(),
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
				przystanki.push({
					"type" : "Point",
					"coordinates" : [ lonlat.lon, lonlat.lat ]
				});
				map.removePopup(popup);
				start_stopLayer.removeMarker(crossMarker);
				crossMarker = null;
				updatePrzystankiView();

				// open dialog form
				$(".addPrzystanekDialog").dialog("open");
				$("#przystanekLon").val(lonlat.lon.toFixed(10));
				$("#przystanekLat").val(lonlat.lat.toFixed(10));
			});

}

function updatePrzystankiView() {

	var przystanek = przystanki[przystanki.length - 1];
	$("#przystanki").append(
			'[' + przystanek.coordinates[0] + ', ' + przystanek.coordinates[1]
					+ ']<br />');

}

/**
 * Szuka max z-index na calej stronie plugin jquery
 */
$.maxZIndex = $.fn.maxZIndex = function(opt) {
	// / <summary>
	// / Returns the max zOrder in the document (no parameter)
	// / Sets max zOrder by passing a non-zero number
	// / which gets added to the highest zOrder.
	// / </summary>
	// / <param name="opt" type="object">
	// / inc: increment value,
	// / group: selector for zIndex elements to find max for
	// / </param>
	// / <returns type="jQuery" />
	var def = {
		inc : 10,
		group : "*"
	};
	$.extend(def, opt);
	var zmax = 0;
	$(def.group).each(function() {
		var cur = parseInt($(this).css('z-index'));
		zmax = cur > zmax ? cur : zmax;
	});
	if (!this.jquery)
		return zmax;

	return this.each(function() {
		zmax += def.inc;
		$(this).css("z-index", zmax);
	});
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
function guiInit() {

	// ///////////////////////////////////////////////////////////////////////////////////////
	// BUTTONY LEWO PRAWO
	$(".dataLeftButton").button({
		icons : {
			primary : "ui-icon-carat-1-w"
		},
		text : false
	}).click(function() {
		var time = startTime.getTime();
		// time -= 1day (time in ms)
		time -= 1000 * 60 * 60 * 24;
		startTime.setTime(time);
		$("#data").datepicker("setDate", startTime);
	});

	$(".dataRightButton").button({
		icons : {
			primary : "ui-icon-carat-1-e"
		},
		text : false
	}).click(function() {
		var time = startTime.getTime();
		// time += 1day (time in ms)
		time += 1000 * 60 * 60 * 24;
		startTime.setTime(time);
		$("#data").datepicker("setDate", startTime);
	});

	$(".godzinaLeftButton")
			.button({
				icons : {
					primary : "ui-icon-carat-1-w"
				},
				text : false
			})
			.click(
					function() {
						var time = startTime.getTime();
						// time -= 10minute (time in ms)
						time -= 1000 * 600;
						startTime.setTime(time);

						var dataGodzinaText = (startTime.getHours() < 10 ? ("0" + startTime
								.getHours())
								: startTime.getHours())
								+ ":";
						dataGodzinaText += (startTime.getMinutes() < 10 ? ("0" + startTime
								.getMinutes())
								: startTime.getMinutes());

						$("#godzina").val(dataGodzinaText);

					});
	$(".godzinaRightButton")
			.button({
				icons : {
					primary : "ui-icon-carat-1-e"
				},
				text : false
			})
			.click(
					function() {
						var time = startTime.getTime();
						// time += 10minute (time in ms)
						time += 1000 * 600;
						startTime.setTime(time);

						var dataGodzinaText = (startTime.getHours() < 10 ? ("0" + startTime
								.getHours())
								: startTime.getHours())
								+ ":";
						dataGodzinaText += (startTime.getMinutes() < 10 ? ("0" + startTime
								.getMinutes())
								: startTime.getMinutes());

						$("#godzina").val(dataGodzinaText);
					});

	// ///////////////////////////////////////////////////////////////////////////////////////
	// DATAPICKER

	$("#data")
			.datepicker(
					{
						dayNamesShort : dniTygodnia,
						dayNamesMin : dniTygodnia,
						dateFormat : 'D dd-mm-yy',
						defaultDate : startTime,
						beforeShow : function() {
							// potrzebne aby wywolac zdarzenie afterShow dla
							// zmiany stylu (kalendarz ma byc widoczny a nie
							// przykryty mapa)
							setTimeout(
									"$('#ui-datepicker-div').attr('style', 'position: absolute; top: 99.2833px; left: 97px; z-index: 9000; display: block;')",
									200);
						},
						onClose : function(dateText) {
							var date = $.datepicker.parseDate('D dd-mm-yy',
									dateText, {
										dayNamesShort : dniTygodnia
									});
							startTime.setDate(date.getDate());
							startTime.setMonth(date.getMonth());
							startTime.setFullYear(date.getFullYear());
						}

					});

	$("#data").datepicker("setDate", startTime);

	$("#godzina").val(startTime.getHours() + ":" + startTime.getMinutes());

	$(".searchButton").button();
	$(".searchButton").click(function(e) {
		searchButtonClick(e);
	});

	// ///////////////////////////////////////////////////////////////////////////////////////
	// Add przystanek dialog form
	$(".addPrzystanekDialog").dialog(
			{
				autoOpen : false,
				height : 300,
				width : 450,
				modal : true,
				buttons : {
					"Dodaj przystanek" : dodajPrzystanekButtonClick,
					Cancel : function() {
						$(this).dialog("close");
					}
				},
				close : function() {
					$(".validateTips").text("");
					$([]).add($("#przystanekNazwa")).add($("#przystanekLon"))
							.add($("#przystanekLat")).val("").removeClass(
									"ui-state-error");
				}
			});

	// ustawienie loading message
	Seam.Remoting.displayLoadingMessage = displayLoadingMessage;
	Seam.Remoting.hideLoadingMessage = hideLoadingMessage;
}

/**
 * Zdarzenie guzika 'szukaj'
 * 
 * @param e
 */
function searchButtonClick(e) {
	// parsowanie start stop, data godzina
	var parseResult = parseInputParameters();
	if (parseResult) {
		alert("parse error: " + parseResult);
		return;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// SEAM REMOTING

	// zaczyna sie kolejka zapytan
	Seam.Remoting.startBatch();

	var homeBean = Seam.Component.getInstance("homeBean");
	var setStartPointCallback = function(result) {
		if (!result) {
			alert("bledne parametry przystanku startowego");
			Seam.Remoting.cancelBatch();
		}
	}
	var startLonLat = new OpenLayers.LonLat(start.lonlat.lon, start.lonlat.lat)
			.transform(map.getProjectionObject(), new OpenLayers.Projection(
					"EPSG:4326"));
	homeBean.setStartPoint(startLonLat.lon, startLonLat.lat,
			setStartPointCallback);
	var setStopPointCallback = function(result) {
		if (!result) {
			alert("bledne parametry przystanku koncowego");
			Seam.Remoting.cancelBatch();
		}
	}
	var stopLonLat = new OpenLayers.LonLat(stop.lonlat.lon, stop.lonlat.lat)
			.transform(map.getProjectionObject(), new OpenLayers.Projection(
					"EPSG:4326"));
	homeBean.setStopPoint(stopLonLat.lon, stopLonLat.lat, setStopPointCallback);

	var setStartTimeCallback = function(result) {
		if (!result) {
			alert("bledna data i czas startowy");
			Seam.Remoting.cancelBatch();
		}
	}
	homeBean.setStartTime(startTime, setStartTimeCallback);

	// odpalenie zapytan
	Seam.Remoting.executeBatch();
}

/**
 * Funkcja obsluguje zdarzenie klikniecia guzika 'Dodaj przystanek'
 * 
 * @returns
 */
var dodajPrzystanekButtonClick = function() {
	var bValid = true;

	$([]).add($("#przystanekNazwa")).add($("#przystanekLon")).add(
			$("#przystanekLat")).removeClass("ui-state-error");
	$(".validateTips").text("");

	// validacja
	bValid = bValid && checkLength($("#przystanekNazwa"), "nazwa", 3, 50);
	bValid = bValid && checkLength($("#przystanekLon"), "dlugoœæ", 1, 20);
	bValid = bValid && checkLength($("#przystanekLat"), "szerokoœæ", 1, 20);

	bValid = bValid
			&& checkRegexp($("#przystanekNazwa"), /^[a-z][A-Z]([0-9a-z_])+$/i,
					"Nazwa moze sie sk³adaæ ze znakow 'a-z' i '0-9'");
	bValid = bValid
			&& checkRegexp($("#przystanekLon"), /^[0-9][0-9].([0-9])+$/i,
					"D³ugoœæ (np. 19.123)");
	bValid = bValid
			&& checkRegexp($("#przystanekLat"), /^[0-9][0-9].([0-9])+$/i,
					"Szerokoœæ (np. 50.123)");

	if (bValid) {
		// zaczyna sie kolejka zapytan
		
		Seam.Remoting.startBatch();

		
		var przystanekDAO = Seam.Component.getInstance("przystanekDAO");
		var savePrzystanekCallback = function(result){
			if(result){
				alert("Dodano przystanek");
				$(".addPrzystanekDialog").dialog("close");
				
			}
		}
	
		przystanekDAO.savePrzystanek(parseFloat($("#przystanekLon").val()),
				parseFloat($("#przystanekLat").val()), $("#przystanekNazwa").val(), savePrzystanekCallback);

		// odpalenie zapytan
		Seam.Remoting.executeBatch();
	
		$(".addPrzystanekDialog").dialog("close");
	}
}

/**
 * Parsouje pola takie jak start
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

function checkLength(o, n, min, max) {
	if (o.val().length > max || o.val().length < min) {
		o.addClass("ui-state-error");
		updateTips("Dlugosc " + n + " musi byæ pomiedzy " + min + " a " + max
				+ " znakow.");
		return false;
	} else {
		return true;
	}
}
 
function checkRegexp(o, regexp, n) {
	if (!(regexp.test(o.val()))) {
		o.addClass("ui-state-error");
		updateTips(n);
		return false;
	} else {
		return true;
	}
}

function updateTips(t) {
	$(".validateTips").text(t).addClass("ui-state-highlight");
	setTimeout(function() {
		$(".validateTips").removeClass("ui-state-highlight", 1500);
	}, 500);
}