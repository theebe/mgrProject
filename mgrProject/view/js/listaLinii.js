/**
 * Linia na ktorej pracujemy
 */
var linia = null;

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
				// id="deleteLiniaJSFunction"
				deleteLinia();
				$(this).dialog("close");
			},
			"Anuluj" : function() {
				$(this).dialog("close");
			}
		}
	});

};

function initEdycjaLinii(typ, id) {

	// zamien widoki
	$(".listaLiniiContainerAll").attr("style", "display: none;");
	$(".edycjaLiniiContainerAll").removeAttr("style");

	// button wstecz
	if ($(".backToList")) {
		$(".backToList").button();
	}

	// jest formularz
	if ($(".formularzEdycjiLinii")) {

		// spinner (czas do next)
		$(".czasDoNastSpinner").spinner({
			min : 0,
			max : 60,
			allowNull : false,
			showOn : "always"
		}); 
		
		$(".firstStopPo").timepicker({});
		$(".firstStopSw").timepicker({});
		
		// zdarzenie najechania na nazwe przystanku
		$(".przystanekRow")
				.hover(
						// on
						function() {
							$(this).addClass("ui-state-hover");
							var idTyp = getIdTypeFromIdAttr($(this));
							var przystanekFeature = przystanki[getIPrzystnekFromId(idTyp[0])];
							var lonLat = new OpenLayers.LonLat(
									przystanekFeature.geometry.x,
									przystanekFeature.geometry.y);

							przystanekInfoPopup = new OpenLayers.Popup.FramedCloud(
									"przystanekFramedCloud", lonLat, null,
									przystanekFeature.attributes.nazwa, null,
									false);
							map.addPopup(przystanekInfoPopup);
						},
						// off
						function() {
							$(this).removeClass("ui-state-hover");
							map.removePopup(przystanekInfoPopup);
						});

		$(".zapiszLinieButton").button();

		$(".cancelButton").button();

	}

};


function showSelectedLinia(){
	linia = getSelectedLinia();
	
}


function getSelectedLinia(){
	
	var liniaRet = null;
	
	Seam.Remoting.startBatch();
	Seam.Remoting.getContext().setConversationId(seamConversationId );
	// pobiera instancje componentu ejb przystanekDAO
	var liniaDAO = Seam.Component.getInstance("liniaDAO");
	
	var getLiniaCallback = function (l){
		if(l){
			
			liniaRet = l;
		}
	}
	liniaDAO.getSelectedLinia(getLiniaCallback);
	
	// odpalenie zapytan
	Seam.Remoting.executeBatch();
	
	
	return liniaRet;
}


