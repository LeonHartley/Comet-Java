var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var monitorServer = require ('./monitorServer.js');

server.listen(8080);

app.get('/', function (req, res) {
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function (socket) {
  socket.emit('packet', { name: 'serverList', data: monitorServer.getServerList() });

  socket.on('packet', function (data) {
    if(!data.hasOwnProperty("name")) {
      socket.emit("error", { message: "Invalid packet structure!"} );
      return;
    }

    
  });
});
