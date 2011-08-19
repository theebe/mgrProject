
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
	
	showLinia(id);
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
							showPrzystanekOnMap(idTyp[0]);
						},
						// off
						function() {
							$(this).removeClass("ui-state-hover");
							hidePrzystanek();
						});

		$(".zapiszLinieButton").button();

		$(".cancelButton").button();

	}

};


function showSelectedLinia(){
	Seam.Remoting.startBatch();
	Seam.Remoting.getContext().setConversationId(seamConversationId );
	// pobiera instancje componentu ejb przystanekDAO
	var liniaDAO = Seam.Component.getInstance("liniaDAO");
	
	var getLiniaCallback = function (l){
		if(l){
			showLiniaOnMap(l);
		}
	}
	liniaDAO.getSelectedLinia(getLiniaCallback);
	
	// odpalenie zapytan
	Seam.Remoting.executeBatch();
}

function showLinia(id){
	Seam.Remoting.startBatch();
	Seam.Remoting.getContext().setConversationId(seamConversationId );
	// pobiera instancje componentu ejb przystanekDAO
	var liniaDAO = Seam.Component.getInstance("liniaDAO");
	
	var getLiniaCallback = function (l){
		if(l){
			showLiniaOnMap(l);
		}
	}
	liniaDAO.getLinia(id, getLiniaCallback);
	
	// odpalenie zapytan
	Seam.Remoting.executeBatch();
}

function backView(){
	
	$(".edycjaLiniiContainerAll").attr("style", "display: none;");
	$(".listaLiniiContainerAll").removeAttr("style");
}




