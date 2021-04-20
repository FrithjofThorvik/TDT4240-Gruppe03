var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var time = 0;

server.listen(8080, function() {
	console.log("Server is now running...");
});

io.on('connection', function(socket) {
    console.log("Player Connected!");
    players.push(new player(socket.id, 0, 0)); // Add player to array

    // Fire application events from server
    socket.emit('socketID', { id: socket.id });
    socket.emit('getPlayers', players);
    socket.broadcast.emit('newPlayer', { id: socket.id }); // Fire event to all other players

    // Handle "playerMoved" event from game, and emit event to all other players
    socket.on('playerMoved', function(data){
        console.log("Player moved!");
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);

        for (var i = 0; i < players.length; i++){
            if (players[i].id == data.id) {
                players[i].x = data.x;
                players[i].y = data.y;
            }
        }
    });

    // Handle event listeners, fired from the application
	socket.on('disconnect', function() {
	    console.log("Player Disconnected");
        socket.broadcast.emit('playerDisconnected', { id: socket.id }); // Fire event to other players

        // Loop through all players, and remove the disconnected player from array
        for(var i = 0; i < players.length; i++){
            if(players[i].id == socket.id){
                players.splice(i, 1);
            }
        }
	});
});



// Stores players with id & position into an array
function player(id, x, y){
	this.id = id;
	this.x = x;
	this.y = y;
}