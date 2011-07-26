/**
 * W tym pliku znajduj¹ sie pomocne funkcje i zmienne obslugujace gui
 */

var dniTygodnia = [ 'pon.', 'wt.', 'sr.', 'czw.', 'pt.', 'sob.', 'niedz.' ];

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
 * funkcja inicjalizujaca okno modalne (formularz) dodawania przystanku
 */
function homeAddPrzystanekDialogInit() {
	$( "#przystanekTypRadio" ).buttonset();
	$(".addPrzystanekDialog").dialog(
			{
				autoOpen : false,
				height : 350,
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
			&& checkRegexp($("#przystanekNazwa"),
					/^[a-zA-Z]([0-9a-z])+(\s)?([0-9a-z])*$/i,
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

		// pobiera instancje componentu ejb przystanekDAO
		var przystanekDAO = Seam.Component.getInstance("przystanekDAO");
		var savePrzystanekCallback = function(p) {
			if (p) {
				
				
				var lon_lat = new OpenLayers.LonLat(p.location.x,
						p.location.y).transform(new OpenLayers.Projection(
						"EPSG:4326"), map.getProjectionObject());

				var point = new OpenLayers.Geometry.Point(lon_lat.lon,
						lon_lat.lat);
				
				var vect = new OpenLayers.Feature.Vector(point, {
					nazwa : p.nazwa,
					id : p.id,
					typ : p.typ
				});
				
			
				przystanki.push(vect);
				przystankiLayer.removeAllFeatures();
				przystankiLayer.addFeatures(przystanki);
				updatePrzystankiView();
				alert("Dodano przystanek");
				$(".addPrzystanekDialog").dialog("close");
			} else {
				alert("Nie dodano, czegos brakuje");
			}
		}
		
		var typString = $("#przystanekTypRadio input:checked").val();
		
		przystanekDAO.savePrzystanek(parseFloat($("#przystanekLon").val()),
				parseFloat($("#przystanekLat").val()), $("#przystanekNazwa")
						.val(), typString, savePrzystanekCallback);

		// odpalenie zapytan
		Seam.Remoting.executeBatch();
	}
}

/**
 * inicjacja datapickera na stronie glownej
 */
function homeDatapickerInit() {
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
}

/**
 * Funkcja inincjalizuj¹ca guziki w lewo i w prawo na stronie glównej
 */
function homeFormButtonInit() {

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

	$(".godzinaLeftButton").button({
		icons : {
			primary : "ui-icon-carat-1-w"
		},
		text : false
	}).click(function() {
		var time = startTime.getTime();
		// time -= 10minute (time in ms)
		time -= 1000 * 600;
		startTime.setTime(time);

		$("#godzina").val(hoursMinutesToString(time));

	});

	$(".godzinaRightButton").button({
		icons : {
			primary : "ui-icon-carat-1-e"
		},
		text : false
	}).click(function() {
		var time = startTime.getTime();
		// time += 10minute (time in ms)
		time += 1000 * 600;
		startTime.setTime(time);

		$("#godzina").val(hoursMinutesToString(time));
	});
}



/**
 * zamienia czas w stringa o aktualnej godzinie ( 16:54 )
 */
function hoursMinutesToString(time) {
	var dataGodzinaText = (time.getHours() < 10 ? ("0" + time.getHours())
			: startTime.getHours())
			+ ":";
	dataGodzinaText += (time.getMinutes() < 10 ? ("0" + time.getMinutes())
			: startTime.getMinutes());
	return dataGodzinaText;

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