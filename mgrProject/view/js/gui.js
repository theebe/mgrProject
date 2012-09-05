/**
 * W tym pliku znajduja sie pomocne funkcje i zmienne obslugujace gui
 */

var dniTygodnia = [ 'pon.', 'wt.', 'sr.', 'czw.', 'pt.', 'sob.', 'niedz.' ];

/**
 * Zdarzenie guzika 'szukaj'
 * 
 * @param e
 */
function searchButtonClick(e) {
	hideLinia();
	// parsowanie start stop, data godzina
	var parseResult = parseInputParameters();
	if (parseResult) {
		alert("parse error: " + parseResult);
		return;
	}

	// czyszczenie starej strasy
	$("#tabs-1").text(" ");
	if (path.length != 0) {
		przystankiLayer.removeFeatures(path);
		path = [];
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// SEAM REMOTING

	// zaczyna sie kolejka zapytan
	Seam.Remoting.startBatch();
	Seam.Remoting.getContext().setConversationId(seamConversationId);
	var homeBean = Seam.Component.getInstance("homeBean");

	var setStartPointCallback = function(result) {
		if (!result) {
			alert("Błędne parametry przystanku startowego.");
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
			alert("Błędne parametry przystanku końcowego.");
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
			alert("Błędna data i czas startowy.");
			Seam.Remoting.cancelBatch();
			return;
		}
	}
	homeBean.setStartTime(startTime, setStartTimeCallback);
	homeBean.setStartPoint(startLonLat.lon, startLonLat.lat,
			setStartPointCallback);
	homeBean.setStopPoint(stopLonLat.lon, stopLonLat.lat, setStopPointCallback);

	var findRouteCallback = function(result) {

		if (result == null) {
			alert("Obliczanie trasy nie powiodło się!");
			Seam.Remoting.cancelBatch();
			return;
		} else {
			drawRoute(result);
		}
	};

	homeBean.findRoute(findRouteCallback);

	// odpalenie zapytan
	Seam.Remoting.executeBatch();
}

function homeTabsInit() {
	$("#tabs").tabs();

	// zdarzenie najechania na nazwe przystanku
	$(".przystanekLink").hover(
	// on
	function() {
		$(this).addClass("ui-state-hover");
		var id = parseInt(this.id);
		showPrzystanekOnMap(id);
	},
	// off
	function() {
		$(this).removeClass("ui-state-hover");
	});

	$(".liniaLink").hover(
	// on
	function() {
		$(this).addClass("ui-state-hover");
		var id = parseInt(this.id);
		showLiniaId(id);

	},
	// off
	function() {
		$(this).removeClass("ui-state-hover");

	});

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

function homeAddLiniaRozkladDialog() {

	liniaRozkladDialog = $(".liniaRozklaDialog").dialog({
		autoOpen : false,
		height : 550,
		width : 450,
		modal : false,
		buttons : {
			"Zamknij" : function() {
				$(this).dialog("close");
			}
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
	bValid = bValid && checkLength($("#przystanekLon"), "dlugość", 1, 20);
	bValid = bValid && checkLength($("#przystanekLat"), "szerokość", 1, 20);

	bValid = bValid
			&& checkRegexp($("#przystanekNazwa"),
					/^[a-zA-Z]([0-9a-z])+(\s)?([0-9a-z])*$/i,
					"Nazwa może się składać ze znaków 'a-z' i '0-9'");
	bValid = bValid
			&& checkRegexp($("#przystanekLon"), /^[0-9][0-9].([0-9])+$/i,
					"Długość (np. 19.123)");
	bValid = bValid
			&& checkRegexp($("#przystanekLat"), /^[0-9][0-9].([0-9])+$/i,
					"Szerokość (np. 50.123)");

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

				alert("Dodano przystanek.");
				$(".addPrzystanekDialog").dialog("close");
			} else {
				alert("Nie dodano, czegoś brakuje.");
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
				alert("Dodano linię!");
			} else {
				if (l)
					alert(l);
				else {
					alert("Błąd w połączeniu.");
				}
			}
		};
		var exeptionHandler = function(ex) {
			alert("Wystapił błąd: " + ex.getMessage());
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
 * Funkcja inincjalizuj�ca guziki w lewo i w prawo na stronie gl�wnej
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
			: time.getHours())
			+ ":";
	dataGodzinaText += (time.getMinutes() < 10 ? ("0" + time.getMinutes())
			: time.getMinutes());

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
		updateTips("Dlugość '" + n + "' musi być pomiędzy " + min + " a "
				+ max + " znaków.");
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
		updateTips(n + "musi być większa od" + min);
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
			"Usuń" : function() {
				// funkcja zdefiniowana w pliku listaLinii.xhtml,
				// id="deletePrzystanekJSFunction"
				deletee();
				$(this).dialog("close");
			},
			"Anuluj" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function drawRoute(odpowiedz) {

	trasa = odpowiedz.ptList;
	tabelaGodzin = odpowiedz.dateList;

	if (trasa == null || trasa.length == 0) {
		if ($("#tabs"))
			$("#tabs-1").text("Brak trasy");
		else
			alert("Brak trasy.");
		return;
	}

	// cala trasa
	var tablicaLinii = [];
	var points = [];

	// od start do przystanku
	// dodajemy punkt startowy
	points.push(new OpenLayers.Geometry.Point(start.lonlat.lon,
			start.lonlat.lat));
	// dodajemy punkt przystanku nablizszego do start
	points
			.push(przystanki[getIPrzystnekFromId(trasa[0].przystanek.id)].geometry);
	// tworze linie
	var liniaDoStart = new OpenLayers.Geometry.LineString(points);
	// tworze obiekt wektorowy linii wraz z stylem
	var liniaStartVect = new OpenLayers.Feature.Vector(liniaDoStart, {}, {
		strokeColor : "#AAAAFF",
		strokeWidth : 4,
		strokeLinecap : "square",
		strokeDashstyle : "solid"
	});

	// dodaje wektor do tablicy w ktorej jest cala wyznaczona trasa
	path.push(liniaStartVect);
	points = [];

	// obliczam odleglosc z buta do najblizszego przystanku (float w metrach)
	var odlPieszoDoStart = liniaDoStart.getGeodesicLength(map
			.getProjectionObject());
	// z zalozenia predkosc = 4km/h
	var czasDoStart = parseInt(((odlPieszoDoStart / 1000.0) * 60) / 4.0);
	// czas wyruszyc
	var timeStart_ = tabelaGodzin[0].getTime();
	timeStart_ -= 1000 * 60 * czasDoStart;
	timeStart_ = new Date(timeStart_);

	$("#tabs-1").append("<ol class=\"glownaListaOl\"></ol>");
	$("#tabs-1 ol.glownaListaOl").append(
			"<li>Start: " + hoursMinutesToString(timeStart_)
					+ "<br />Pieszo ok. " + czasDoStart
					+ " min  w linii prostej (" + parseInt(odlPieszoDoStart)
					+ " m) </li>");

	var i = 0;
	var idLinii = null;
	// trasa wyznaczona przez alg dijkstry
	for (i; i < trasa.length; ++i) {

		// wykryto przesiadke
		if (idLinii != trasa[i].linia.id) {

			idLinii = trasa[i].linia.id;

			// malowanie przesiadki na mapie
			if (i != 0) {
				var colorHex = "#"
						+ (Math.floor(Math.random() * 8388607) + 4194303)
								.toString(16);

				var liniaVect = new OpenLayers.Feature.Vector(
						new OpenLayers.Geometry.LineString(points), {}, {
							strokeColor : colorHex,
							strokeWidth : 4,
							strokeLinecap : "square",
							strokeDashstyle : "solid"
						});
				path.push(liniaVect);
				points = [];

				points
						.push(przystanki[getIPrzystnekFromId(trasa[i - 1].przystanek.id)].geometry);
				points
						.push(przystanki[getIPrzystnekFromId(trasa[i].przystanek.id)].geometry);

				var liniaPrzesiadka = new OpenLayers.Geometry.LineString(points);
				// tworze obiekt wektorowy linii wraz z stylem
				var liniaPrzesiadkaVect = new OpenLayers.Feature.Vector(
						liniaPrzesiadka, {}, {
							strokeColor : "#AAAAFF",
							strokeWidth : 4,
							strokeLinecap : "square",
							strokeDashstyle : "solid"
						});

				// dodaje wektor do tablicy w ktorej jest cala wyznaczona trasa
				path.push(liniaPrzesiadkaVect);
				points = [];

				var odlPrzesiadka = liniaPrzesiadka.getGeodesicLength(map
						.getProjectionObject());
				var czasPrzesiadka = parseInt(((odlPrzesiadka / 1000.0) * 60) / 4.0);

				$("#tabs-1 ol.glownaListaOl").append(
						"<li>Pieszo ok. " + czasPrzesiadka
								+ " min w linii prostej ("
								+ parseInt(odlPrzesiadka) + " m)</li>");
				points
						.push(przystanki[getIPrzystnekFromId(trasa[i].przystanek.id)].geometry);

			}

			$("#tabs-1 ol.glownaListaOl").append(
					"<li id=\"liniaList-" + idLinii + "\">Linia nr "
							+ trasa[i].linia.numer + ":</li>");
			$("#liniaList-" + idLinii).append("<ol></ol>");

		}

		$("#liniaList-" + idLinii + " ol").append(
				"<li>" + trasa[i].przystanek.nazwa + ": \t "
						+ hoursMinutesToString(tabelaGodzin[i]) + "</li>");
		points
				.push(przystanki[getIPrzystnekFromId(trasa[i].przystanek.id)].geometry);

	}

	var liniaVect = new OpenLayers.Feature.Vector(
			new OpenLayers.Geometry.LineString(points), {}, {
				strokeColor : "#00CC00",
				strokeWidth : 4,
				strokeLinecap : "square",
				strokeDashstyle : "solid"
			});
	path.push(liniaVect);
	points = [];

	// od ostatniego przystanku do stop
	points
			.push(new OpenLayers.Geometry.Point(stop.lonlat.lon,
					stop.lonlat.lat));
	points
			.push(przystanki[getIPrzystnekFromId(trasa[trasa.length - 1].przystanek.id)].geometry);
	var liniaDoStop = new OpenLayers.Geometry.LineString(points);
	var liniaStopVect = new OpenLayers.Feature.Vector(liniaDoStop, {}, {
		strokeColor : "#AAAAFF",
		strokeWidth : 4,
		strokeLinecap : "square",
		strokeDashstyle : "solid"
	});

	path.push(liniaStopVect);
	points = [];
	var odlPieszoDoStop = liniaDoStop.getGeodesicLength(map
			.getProjectionObject());
	// z zalozenia predkosc = 4km/h
	var czasDoStop = parseInt(((odlPieszoDoStop / 1000.0) * 60) / 4);
	var timeStop_ = tabelaGodzin[tabelaGodzin.length - 1].getTime();
	timeStop_ += 1000 * 60 * czasDoStop;
	timeStop_ = new Date(timeStop_);
	$("#tabs-1 ol.glownaListaOl").append(
			"<li>Pieszo ok. " + czasDoStop + " min w linii prostej ("
					+ parseInt(odlPieszoDoStop) + " m) <br /> Stop: "
					+ hoursMinutesToString(timeStop_) + " </li>");
	przystankiLayer.addFeatures(path);

}

function showLiniaId(id) {

	// Pobieranie przystankow z bazy
	Seam.Remoting.startBatch();

	Seam.Remoting.getContext().setConversationId(seamConversationId);
	var liniaDAO = Seam.Component.getInstance("liniaDAO");

	var getLiniaCallback = function(l) {
		if (l)
			showLiniaOnMap(l);
		else
			console.debug("ERROR");
	};

	liniaDAO.getLinia(id, getLiniaCallback);

	Seam.Remoting.executeBatch();

}
