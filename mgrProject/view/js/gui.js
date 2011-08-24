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
	
	//czyszczenie starej strasy
	$("#tabs-1").text(" ");
	if(path.length!=0){
		przystankiLayer.removeFeatures(path);
		path = [];
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// SEAM REMOTING

	// zaczyna sie kolejka zapytan
	Seam.Remoting.startBatch();
	Seam.Remoting.getContext().setConversationId(seamConversationId);
	var homeBean = Seam.Component.getInstance("homeBean");
	var algorithmBean = Seam.Component.getInstance("algorithmBean");

	var setStartPointCallback = function(result) {
		if (!result) {
			alert("bledne parametry przystanku startowego");
			Seam.Remoting.cancelBatch();
			return;
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
			return;
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
			return;
		}
	}
	homeBean.setStartTime(startTime, setStartTimeCallback);
	homeBean.setStartPoint(startLonLat.lon, startLonLat.lat, setStartPointCallback);
	homeBean.setStopPoint(stopLonLat.lon, stopLonLat.lat, setStopPointCallback);
	
	var findRouteCallback = function(result) {
	
		
		if (result == null) {
			alert("Obliczanie trasy nie powiodlo sie!");
			Seam.Remoting.cancelBatch();
			return;
		}
		else{
			drawRoute(result);
		}
	};
	
	homeBean.findRoute(findRouteCallback);


	// odpalenie zapytan
	Seam.Remoting.executeBatch();
}

/**
 * funkcja inicjalizujaca okno modalne (formularz) dodawania przystanku
 */
function homeAddPrzystanekDialogInit() {
	$("#przystanekTypRadio").buttonset();
	$(".addPrzystanekDialog").dialog(
			{
				autoOpen : false,
				height : 350,
				width : 450,
				modal : false,
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
		Seam.Remoting.getContext().setConversationId(seamConversationId);
		// pobiera instancje componentu ejb przystanekDAO
		var przystanekDAO = Seam.Component.getInstance("przystanekDAO");
		var savePrzystanekCallback = function(p) {
			if (p) {

				var lon_lat = new OpenLayers.LonLat(p.location.x, p.location.y)
						.transform(new OpenLayers.Projection("EPSG:4326"), map
								.getProjectionObject());

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
 * Funckja inicjalizujaca okno dodawania linii
 */
function homeAddLiniaDialogInit() {

	// buttony radio
	$("#liniaTypRadio").buttonset();

	// drag&drop
	$(".addLiniaDialog ul")
			.sortable(
					{
						connectWith : ".addLiniaDialog ul",
						scroll : true,
						placeholder : "ui-state-highlight",
						receive : function(event, ui) {
							var id = "#" + ui.item.attr("id");
							if ($(this).hasClass("listaPrzystankow")) {
								$(id)
										.children("span")
										.removeClass(
												"ui-icon-circle-arrow-w arrowPrzystL")
										.addClass(
												"ui-icon-circle-arrow-e arrowPrzystR");
							} else if ($(this)
									.hasClass("listaPrzystankowLinii")) {
								$(id)
										.children("span")
										.removeClass(
												"ui-icon-circle-arrow-e arrowPrzystR")
										.addClass(
												"ui-icon-circle-arrow-w arrowPrzystL");
							}
							map.removePopup(przystanekInfoPopup);
						}

					});
	$(".listaPrzystankow, .listaPrzystankowLinii").disableSelection();

	// okno dialogowe
	$(".addLiniaDialog").dialog({
		autoOpen : false,
		height : 450,
		width : 450,
		modal : false,
		position : [ 500, 500 ],
		buttons : {
			"Dodaj linie" : dodajLinieButtonClick,
			Cancel : function() {
				$(this).dialog("close");
			}
		},
		close : function() {
			$(".validateTips").text("");
			$([]).add($("#liniaNumer")).val("").removeClass("ui-state-error");
		}
	});

	// zdarzenia strzalek <- ->
	$(".arrowPrzystR")
			.click(
					function() {
						var element = $(this).parent().get();

						element.remove();
						$(".listaPrzystankowLinii").append(element);
						$(".addLiniaDialog ul").sortable("refresh");
						$(this)
								.removeClass(
										"ui-icon-circle-arrow-e arrowPrzystR")
								.addClass("ui-icon-circle-arrow-w arrowPrzystL");
						map.removePopup(przystanekInfoPopup);
						$(this).parent().removeClass("ui-state-hover");
						$(".arrowPrzystL")
								.click(
										function() {
											var element = $(this).parent()
													.get();
											element.remove();
											$(".listaPrzystankow").append(
													element);
											$(".addLiniaDialog ul").sortable(
													"refresh");
											$(this)
													.removeClass(
															"ui-icon-circle-arrow-w arrowPrzystL")
													.addClass(
															"ui-icon-circle-arrow-e arrowPrzystR");
											$(this).parent().removeClass(
													"ui-state-hover");
											map
													.removePopup(przystanekInfoPopup);
										});
					});

	// wysietlanie przystankow A lub T po zmianie typu linii
	parseListPrzystankow($(".liniaTyp:checked").val());
	// zdarzenie zmiany typu
	$(".liniaTyp").change(function() {
		parseListPrzystankow($(".liniaTyp:checked").val());
	});

	// zdarzenie najechania na nazwe przystanku
	$(".przytanekLi").hover(
	// on
	function() {
		$(this).addClass("ui-state-hover");
		var idTyp = getIdTypeFromIdAttr($(this));
		showPrzystanekOnMap(idTyp[0]);
	},
	// off
	function() {
		$(this).removeClass("ui-state-hover");
		hidePrzystanek();
	});

}

/**
 * zdarzenie dodawania nowej linii
 */
function dodajLinieButtonClick() {
	var bValid = true;

	$(".validateTips").text("");
	$([]).add($("#liniaNumer")).removeClass("ui-state-error");

	// validacja
	bValid = bValid && checkLength($("#liniaNumer"), "numer", 1, 3);

	bValid = bValid
			&& checkRegexp($("#liniaNumer"), /^[0-9]+$/i,
					"Numer lini = tylko cyfry");

	bValid = bValid
			&& checkContainer($(".listaPrzystankowLinii li"),
					"Liczba przystanków", 1);

	if (bValid) {

		var listaIdPrzystankow = prepareListeIdPrzystanokow($(".listaPrzystankowLinii li"));

		// zaczyna sie kolejka zapytan
		Seam.Remoting.startBatch();
		Seam.Remoting.getContext().setConversationId(seamConversationId);
		var liniaDAO = Seam.Component.getInstance("liniaDAO");

		var saveLiniaCallback = function(l) {
			if (l == 'success') {
				alert("Dodano Linie");
			} else {
				if (l)
					alert(l);
				else {
					alert("b³¹d w po³¹czeniu");
				}
			}
		};
		var exeptionHandler = function(ex) {
			alert("wystapi³ b³¹d: " + ex.getMessage());
			alert(ex.printStackTrace());
		};

		liniaDAO.saveLinia(parseInt($("#liniaNumer").val()), $(
				"#liniaTypRadio input:checked").val(), listaIdPrzystankow,
				false,// $("input#liniaPowrotna").is(":checked"),
				saveLiniaCallback, exeptionHandler);

		// odpalenie zapytan
		Seam.Remoting.executeBatch();
		$(".addLiniaDialog").dialog("close");
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

		$("#godzina").val(hoursMinutesToString(startTime));

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

		$("#godzina").val(hoursMinutesToString(startTime));
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

/**
 * Pobiera id & typ z objektu jQuery z atrybutu id
 * 
 * @param obj
 *            obiekt jQuery
 * @returns tablica = [int id, String typ]
 */
function getIdTypeFromIdAttr(obj) {
	var ret = obj.attr("id").split("-");
	ret[0] = parseInt(ret[0]);

	return ret;
}

/**
 * parsuje liste przystankow w formularzu dodawania lini, (aby linia autobusowa
 * nie korzystala z przystankow tramwajowych)
 */
function parseListPrzystankow(typ) {
	$(".listaPrzystankowConterner li").each(function(index) {

		var typPrzyst = getIdTypeFromIdAttr($(this))[1];
		if (typPrzyst != typ) {
			$(this).attr("style", "display:none;");
		} else {
			$(this).removeAttr("style");
		}
	});

}

/**
 * Przygotowuje liste z id przystankow danej linii
 * 
 * @param l -
 *            lista jako obiekty DOM
 *            <li>
 */
function prepareListeIdPrzystanokow(l) {
	var lista = [];
	var typLinii = $(".liniaTyp:checked").val();
	l.each(function(index) {
		// [0] = id, [1] = typ
		var idTyp = getIdTypeFromIdAttr($(this));
		if (idTyp[1] == typLinii) {
			lista.push(idTyp[0]);
		}

	});
	return lista;
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

function checkContainer(o, n, min) {
	if (o.length < min) {
		updateTips(n + "musi byæ wieksza od" + min);
		return false;
	} else
		return true;
}

function updateTips(t) {
	$(".validateTips").text(t).addClass("ui-state-highlight");
	setTimeout(function() {
		$(".validateTips").removeClass("ui-state-highlight", 1500);
	}, 500);
}

function deleteDialogOpen() {

	$("#dialog-deleteconfirm").dialog({
		autoOpen : true,
		resizable : false,
		height : 200,
		width : 300,
		modal : true,
		buttons : {
			"Usuñ" : function() {
				// funkcja zdefiniowana w pliku listaLinii.xhtml,
				// id="deletePrzystanekJSFunction"
				deletePrzystanek();
				$(this).dialog("close");
			},
			"Anuluj" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function drawRoute(trasa) {
	
	
	if (trasa == null || trasa.length == 0) {
		if ($("#tabs"))
			$("#tabs-1").text("Brak trasy");
		else
			alert("Brak trasy");
		return;
	}

	// cala trasa
	var tablicaLinii = [];
	var points = [];

	// od start do przystanku
	// dodajemy punkt startowy
	points.push( new OpenLayers.Geometry.Point(start.lonlat.lon,
			start.lonlat.lat) );
	//dodajemy punkt przystanku nablizszego do start
	points.push( przystanki[getIPrzystnekFromId(trasa[0].przystanek.id)].geometry );
	//tworze linie
	var liniaDoStart = new OpenLayers.Geometry.LineString(points);
	//tworze obiekt wektorowy linii wraz z stylem
	var liniaStartVect = new OpenLayers.Feature.Vector(
					liniaDoStart, 
					{},
					{strokeColor : "#BBBBFF",
						strokeWidth: 4,
						strokeLinecap: "square",
						strokeDashstyle: "solid"});
	
	//dodaje wektor do tablicy w ktorej jest cala wyznaczona trasa
	path.push(liniaStartVect);
	points = [];
	// obliczam odleglosc z buta do najblizszego przystanku  (float w metrach)
	var odlPieszoDoStart = liniaDoStart.getGeodesicLength(map.getProjectionObject());
	
	$("#tabs-1").append("<ol></ol>")
	$("#tabs-1 ol").append("<li>Pieszo ok "+ parseInt(odlPieszoDoStart) +" m w linii prostej </li>" );
	
	var i = 0;
	var idLinii = null;
	//trasa wyznaczona przez alg dijkstry
	for (i; i < trasa.length; ++i) {
		
		if(idLinii != trasa[i].linia.id){
			////////////////////////////////////////////////////////////////////////////////////////////
			// TODO: wyswietlanie przesiadki na mapie
			
			idLinii = trasa[i].linia.id;
			$("#tabs-1 ol").append("<li id=\"liniaList-" + idLinii + "\">Linia nr "+ trasa[i].linia.numer + ":</li>");
			$("#liniaList-" + idLinii).append("<ul></ul>");
		}
		
		$("#liniaList-" + idLinii + " ul").append("<li>" + trasa[i].przystanek.nazwa + "</li>");
		points.push(przystanki[getIPrzystnekFromId(trasa[i].przystanek.id)].geometry );
	}
	
	var liniaVect = new OpenLayers.Feature.Vector(
			new OpenLayers.Geometry.LineString(points));
	path.push(liniaVect);
	points = [];
	
	// od ostatniego przystanku do stop
	points.push( new OpenLayers.Geometry.Point(stop.lonlat.lon,
			stop.lonlat.lat) );
	points.push( przystanki[getIPrzystnekFromId(trasa[trasa.length-1].przystanek.id)].geometry );
	var liniaDoStop = new OpenLayers.Geometry.LineString(points);
	var liniaStopVect = new OpenLayers.Feature.Vector(
			liniaDoStop, 
			{},
			{strokeColor : "#AAAAFF",
				strokeWidth: 4,
				strokeLinecap: "square",
				strokeDashstyle: "solid"});
	
	path.push(liniaStopVect);
	points = [];
	var odlPieszoDoStop = liniaDoStop.getGeodesicLength(map.getProjectionObject());
	$("#tabs-1 ol").append( "<li>Pieszo ok " + parseInt(odlPieszoDoStop) + " m w linii prostej </li>" );
	przystankiLayer.addFeatures(path);
	
}
