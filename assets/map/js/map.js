var map;
var geocoder;

function initialize() {
	geocoder = new google.maps.Geocoder();
	var mapOptions = {
	  //center: new google.maps.LatLng(0, 0),
	  zoom: 8,
	  mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"),
	    mapOptions);


	codeAddress(getURLParameter("address"));

}
google.maps.event.addDomListener(window, 'load', initialize);


console.log(getURLParameter("address"));

/*
* http://stackoverflow.com/questions/1403888/get-url-parameter-with-jquery
*/
function getURLParameter(name) {
    return decodeURI(
        (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
    );
}

function codeAddress(address) {
	geocoder.geocode( { 'address': address}, function(results, status) {
	  if (status == google.maps.GeocoderStatus.OK) {
	    map.setCenter(results[0].geometry.location);
	    var marker = new google.maps.Marker({
	        map: map,
	        position: results[0].geometry.location
	    });
	  } else {
	    alert("Geocode was not successful for the following reason: " + status);
	  }
	});
}