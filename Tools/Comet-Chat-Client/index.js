var express = require('express'),
	app = express(),
	port = 8081,
	util = require('util'),
	log = require('./logging.js');

app.use(express.static(__dirname + '/html'));
var server = app.listen(port, function() {
	util.print("\u001b[2J\u001b[0;0H");
	console.log("");
	
	log.info("Comet IM app running on port: " + port);
});
