var express = require('express');
var app = express();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var monitorServer = require ('./monitorServer.js');

server.listen(5050);

var private = require('express-http-auth').realm('Index');

app.get('/', private, function(req, res) {
  if (req.username == 'monitor' && req.password == 'cometServer9900') {
    res.sendFile(__dirname + '/index.html');
  } else {
    res.send("access forbidden");
  }
});

io.on('connection', function (socket) {
  socket.on('packet', function (data) {
    if(!data.hasOwnProperty("name")) {
      socket.emit("error", { message: "Invalid packet structure!"} );
      return;
    }

    switch(data.name) {
      case "refresh":
        socket.emit("packet", { name: 'serverList', message: monitorServer.getServerList() });
        break;

      case "readConsole":
        var serverIndex = data.message;

        socket.readingConsole = serverIndex;
        // Send the console output...
        break;
    }
  });
});
