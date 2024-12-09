var user = null
var lobbyId = null
var game = null
var thisPlayerID = null
const baseUrl = window.location.origin;

//WebSocket kezelő
var sockJs = new SockJS(baseUrl + "/socket");


//konstans HTML Elemek DOM hivatkozásai
const createUserButton = document.getElementById("setUsername")
const usernameInput = document.getElementById("username")
const partyTable = document.getElementById("partyTable")
const partyIdInput = document.getElementById("party")
const createPartyButton = document.getElementById("createParty")
const joinPartyButton = document.getElementById("joinParty")
const leavePartyButton = document.getElementById("leaveParty")
const gameModeMenu = document.getElementById("gameModeMenu")
const gameModeSelect = document.getElementById("gameModeSelect")
const startGameButton = document.getElementById("startGame")
const error = document.getElementById("errors")
const partyMenu = document.getElementById("partyMenu")

//Itt kezeljük ha a server frissítésre késztet.
sockJs.onmessage = function (e) {
    console.log("socketmessage:", e.data)
    switch (e.data){
        case 'updatelobby':
            updateUI()
            break;
    }
}

async function updateUI(){
    var lobby = await getLobby()
    if(lobby.gameStarted){
        closeMenuUI()
        updatePartyUI()
        return
    }
    refreshGameState()
    updateGameUI()
}

async function getLobby(){
    try {
        const url = baseUrl + "/lobby/" + lobbyId
        const response = await fetch(url)

        if (!response.ok) {
            throw new Error(response.status)
        }
        return response.json()
    } catch (e) {
        console.log(e)
    }
}

async function getGame(){
    try {
        const url = baseUrl + "/lobby/" + lobbyId + "/game"
        const response = await fetch(url)

        if (!response.ok) {
            throw new Error(response.status)
        }
        return response.json()
    } catch (e) {
        console.log(e)
    }
}

function isUserInsideLobby(lobby){
    for (let i = 0; i < lobby.joinedUsers.length; i++){
        const usr = lobby.joinedUsers[i]
        if(usr.userId === user.userId){
            return true;
        }
    }
    return false;
}
//Partifelület megjelenítése
function closePartyUI(){
    partyTable.innerHTML = ""
    createPartyButton.disabled = false;
    joinPartyButton.disabled = false;
    leavePartyButton.disabled = true;
    partyIdInput.value = ""
    partyIdInput.disabled = false;
    gameModeMenu.hidden = true;
}

function closeMenuUI(){
    partyMenu.hidden = true;
}

function openMenuUI(){
    partyMenu.hidden = false;
}

//A parti táblázátot állítja, illetve az aktív gombokat/inputokat
async function updatePartyUI() {
    if(lobbyId == null){
        closePartyUI()
        return;
    }

    var lobby = await getLobby()
    console.log("getPartyResponse:", JSON.stringify(lobby));

    if(!isUserInsideLobby(lobby)){
        closePartyUI()
        lobbyId = null
        return;
    }

    partyIdInput.value = lobby.lobbyId;
    partyIdInput.disabled = true;
    createPartyButton.disabled = true;
    joinPartyButton.disabled = true;
    leavePartyButton.disabled = false;
    gameModeMenu.hidden = false;
    gameModeSelect.disabled = true;
    startGameButton.disabled = true;

    const isLeader = user.userId === lobby.leader.userId
    const leaderActionHeader = isLeader ? "<th>Akció</th>" : ""
    partyTable.innerHTML = '<tr><th>Felhasználónév</th><th>Leader</th>' + leaderActionHeader + '</tr>'
    lobby.joinedUsers.forEach((user) => {
        const userIsLeader = user.userId === lobby.leader.userId
        const leaderCheck = userIsLeader ? "*" : ""
        const leaderActions = isLeader && !userIsLeader ? "<td><button class='kickButton' value='"+ user.userId +"'>Kirúgás</button></td>" : ""
        partyTable.innerHTML += '<tr><td>' + user.name + '</td><td>' + leaderCheck
            + '</td>'
            + leaderActions
            + '</tr>'

    })

    const options = gameModeSelect.getElementsByTagName("option")
    const gameMode = lobby.gameMode
    for(let i = 0; i < options.length; i++){
        if(options[i].value === gameMode){
            console.log(gameMode)
            options[i].selected = true;
        }
    }


    if(isLeader){
        gameModeSelect.disabled = false;
        startGameButton.disabled = false;

        const kickButtons = document.getElementsByClassName("kickButton")
        for(let i = 0; i < kickButtons.length; i++) {
            kickButtons[i].addEventListener("click", async () => {
                const url = baseUrl + "/lobby/leave"
                const response = await fetch(url, {method: "POST", body: kickButtons[i].value});

                if (!response.ok) {
                    console.log("leaveUser:", response.status);
                    return;
                }
            })
        }
    }
}

/**
 * Kattintás események a menühöz
 */

startGameButton.addEventListener("click", async () => {
    const newGameMode = gameModeSelect.value;
    const url = baseUrl + "/lobby/" + lobbyId + "/start";
    const response = await fetch(url, {method: "POST"});
    if (!response.ok) {
        console.log(response.status);
        return;
    }
})

gameModeSelect.addEventListener("change", async (e) => {
    const newGameMode = gameModeSelect.value;
    const url = baseUrl + "/lobby/" + lobbyId + "/gamemode";
    const response = await fetch(url, {method: "POST", body: gameModeSelect.value});
})

createUserButton.addEventListener("click", async () => {
    const username = usernameInput.value;
    if (username == null) {
        alert("Nincs megadva felhasználónév!")
        return;
    }
    const url = baseUrl + "/user/create/" + username
    const response = await fetch(url, {method: "PUT"});

    if(!response.ok){
        console.log("createUser:", response.status);
        return;
    }

    user = await response.json()
    createPartyButton.disabled = false;
    joinPartyButton.disabled = false;
    partyIdInput.disabled = false;
    usernameInput.disabled = true;
    createUserButton.disabled = true;
    await sockJs.send(user.userId)
})

createPartyButton.addEventListener("click", async () => {
    const url = baseUrl + "/lobby/create"
    const response = await fetch(url, {method: "POST", body: user.userId})

    if(!response.ok){
        console.log("createPartyResponse:", response.status);
        return;
    }

    lobbyId = await response.text()

    updatePartyUI()
})

joinPartyButton.addEventListener("click", async () => {

    lobbyId = partyIdInput.value
    if (partyIdInput.value === "") {
        alert("Nem adtál meg partyIdt")
        return;
    }

    const url = baseUrl + "/lobby/" + lobbyId + "/join"
    const response = await fetch(url, {method: "POST", body: user.userId})

    if (!response.ok) {
        console.log(response.status)
    }
})

leavePartyButton.addEventListener("click", async () => {
    const url = baseUrl + "/lobby/leave"
    const response = await fetch(url, {method: "POST", body: user.userId});

    if (!response.ok) {
        console.log("leaveUser:", response.status);
        return;
    }
    lobbyId = null
    updatePartyUI()
})


/**
 * Kattintás események vége
 * 
 * Játékmenet kódja
 */

const nodes = await d3.json("territories.json");
//var adjacencies = await d3.json("adjacencies.json")
//var continents = await d3.json("continents.json")

const colorMapping = {
    "neutral":"grey",
    "1":"red",
    "2":"blue",
    "3":"green",
    "4":"brown",
    "5":"purple",
    "6":"orange"
}

var board = []
nodes.forEach((e) => {
    board.push({id: e.Id, coords: e.coords, units: 0, owner: e.owner})
})

const height = document.getElementsByTagName("svg")[0].height.baseVal.value
const width = document.getElementsByTagName("svg")[0].width.baseVal.value
const tester = document.getElementById("tester")

//Melyik IDjú körök vannak kiválasztva: szám
var selected = []

async function refreshGameState(){
    game = await getGame()
    thisPlayerID = game.players[user.userId]
    if(isDeploymentPhase() && isYourTurn()){
        deployableUnits = 10
    }
    board.forEach((row)=>{
        const owner = game.territories[row.name].owner
        const armyCount = game.territories[row.name].armyCount

        const i = board.findIndex( (e) => e.name === row.name)
        board[i].owner = owner
        board[i].units = armyCount
    })

}

//A tábla elemeinek kirajzolása
async function updateGameUI() {
    const svg = d3.select("svg")


    //Kiválaszott elem
    svg.selectAll(".selected").data(selected).join("circle")
        .attr("class", "selected")
        .attr("cx", d => board[d].coords[0] * width / 100)
        .attr("cy", d => board[d].coords[1] * height / 100)
        .attr("r", 15)
        .style("fill", "transparent")
        .style("stroke", "black")

    //Mező
    svg.selectAll(".board")
        .data(board)
        .join("circle")
        .attr("class", "board")
        .attr("cx", d => d.coords[0] * width / 100)
        .attr("cy", d => d.coords[1] * height / 100)
        .attr("r", 10)
        .attr("id", d => d.id)
        .style("stroke", "black")
        .style("fill", d => colorMapping[d.owner])
        .on("click", (e) => {
            boardClicked(e.target.id)
        })

    //Egységek száma
    svg.selectAll("text")
        .data(board)
        .join("text")
        .attr("x", d => d.coords[0] * width / 100 - 4)
        .attr("y", d => d.coords[1] * height / 100 + 4)
        .style("font", "italic 13px sans-serif")
        .style("fill", "white")
        .attr("id", d => d.id)
        .text(d => d.units)
        .on("click", (e) => {
            boardClicked(e.target.id)
        })

}

function territoryOwnedByPlayer(id){
    return game.gameBoard.territories[board[0].name].owner === thisPlayerID
}

function isNeutralTerritory(id){
    return game.gameBoard.territories[board[0].name].owner === "Neutral"
}

var deployableUnits = 0


function isDeploymentPhase(){
    return game.currentTurn.currentState === "DEPLOYMENT"
}

function isAttackPhase(){
    return game.currentTurn.currentState === "ATTACK"
}

function isFortifyPhase(){
    return game.currentTurn.currentState === "FORTIFY"
}

function isYourTurn(){
    return game.currentTurn.activePlayer === thisPlayerID
}

function boardClicked(id){

    if(!isYourTurn){
        return
    }

    selected.push(id)

    if(isDeploymentPhase() && territoryOwnedByPlayer(id)){
        board[id].units++
        deployableUnits--

        if(deployableUnits == 0){
            //Állapot küldése a szervernek
            refreshGameState()
        }
        selected = []
        updateGameUI()
        return;
    }

    if(selected.length < 2){
        return
    }

    if(isAttackPhase()){
        //Támadás selected[0]-ról selected[1]re
        selected = []
        return
    }

    if(isFortifyPhase()){
        selected = []
        return
    }

    updateGameUI()
}
