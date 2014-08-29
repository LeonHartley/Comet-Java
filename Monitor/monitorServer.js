var net = require('net');

var clients = [];

var handshake = {
  "name": "hello",
  "message": ""
}

net.createServer(function (socket) {

  socket.instanceData = {
    "name": "",
    "version": "",
    "status": {},
    "logs": []
  };

  clients.push(socket);

  socket.write(JSON.stringify(handshake));

  socket.on('data', function (data) {
    var dataString = data.toString();

    if(dataString.indexOf("}{") > -1) {
      dataString = dataString.split("}{")[0] + "}";
    }

    var obj = JSON.parse(dataString);

    switch(obj.name) {
      case "hello":
        console.log("Server connected with version: " + obj.message);
        socket.instanceData.version = obj.message;
      break;

      case "status":
        socket.instanceData.status = obj.message;
        break;

      case "appendLog":
        socket.instanceData.logs.push(obj.message);
        console.log(obj);
        break;
    }
  });

  socket.on('end', function () {
    clients.splice(clients.indexOf(socket), 1);
  });

}).listen(1337);

setInterval(function() {
  broadcast({name: "heartbeat", message: ""});
}, 1000);

var broadcast = function(msg) {
  clients.forEach(function(cli) {
    cli.write(JSON.stringify(msg));
  });
}

var getServerList = function() {
  var serverList = [];

  clients.forEach(function(cli) {
    serverList.push(cli.instanceData);
  });

  return serverList;
}

exports.broadcast = broadcast;
exports.getServerList = getServerList;
