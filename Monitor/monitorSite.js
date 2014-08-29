var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var monitorServer = require ('./monitorServer.js');

server.listen(8080);

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function (socket) {
  socket.on('packet', function (data) {
    if(!data.hasOwnProperty("name")) {
      socket.emit("error", { message: "Invalid packet structure!"} );
      return;
    }

    switch(data.name) {
      case "refresh":
        console.log(monitorServer.getServerList());
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
