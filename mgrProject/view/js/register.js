
$(document).ready(function() {
	
	alert("jestem");
	$(".verifyInput").keyup(function(e){
		var verify = $(".verifyInput").text();
		var pass = $(".passwordInput").text();
		alert(pass);
		if(verify != pass){
			alert("ok");
		}
	});
}); 
