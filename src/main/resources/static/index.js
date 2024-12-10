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

const gameUI = document.getElementById("game")
const playerTurnSpan = document.getElementById("playerturn")
const deployUI = document.getElementById("deployphase")
const attackUI = document.getElementById("attackphase")
const fortifyUI = document.getElementById("fortifyphase")
const aphaseButton = document.getElementById("aphaseButton")
const phaseButton = document.getElementById("phaseButton")
const playerList = document.getElementById("playerList")
const combatList = document.getElementById("combatList")
const cardList = document.getElementById("cardList")
const winner = document.getElementById("winner")

//Itt kezeljük ha a server frissítésre késztet.
sockJs.onmessage = function (e) {
    console.log("socket", e.data)
    switch (e.data){
        case 'updatelobby':
            updateUI()
            break;
    }
}

async function updateUI(){
    var lobby = await getLobby()
    console.log("updatedFromServer", lobby)
    if(lobby.started === false){
        openMenuUI()
        updatePartyUI()
        return
    }
    closeMenuUI()
    await refreshGameState()
    if(game.gamePhase === "FINISHED"){
        winner.hidden = false
        winner.innerHTML = "Győztes" + game.winner
    }
    else{
        await updateGameUI()
    }

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
        var lobby = await getLobby()
        return lobby.gameInstance
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
    gameUI.hidden = false;
}

function openMenuUI(){
    partyMenu.hidden = false;
    gameUI.hidden = true;
}

//A parti táblázátot állítja, illetve az aktív gombokat/inputokat
async function updatePartyUI() {
    if(lobbyId == null){
        closePartyUI()
        return;
    }

    var lobby = await getLobby()

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
    //gameModeSelect.disabled = true;
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

    /*
    const options = gameModeSelect.getElementsByTagName("option")
    const gameMode = lobby.gameMode
    for(let i = 0; i < options.length; i++){
        if(options[i].value === gameMode){
            console.log(gameMode)
            options[i].selected = true;
        }
    }*/


    if(isLeader){
        //gameModeSelect.disabled = false;
        if(lobby.size >= 2 && lobby.size <= 6) {
            startGameButton.disabled = false;
        }

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

aphaseButton.addEventListener("click", async () => {
    const url = baseUrl + "/lobby/" + lobbyId + "/end";
    const response = await fetch(url, {method: "POST"});
    if (!response.ok) {
        console.log(response.status);
        return;
    }
})

phaseButton.addEventListener("click", async () => {
    const url = baseUrl + "/lobby/" + lobbyId + "/end";
    const response = await fetch(url, {method: "POST"});
    if (!response.ok) {
        console.log(response.status);
        return;
    }
})

startGameButton.addEventListener("click", async () => {
    const url = baseUrl + "/lobby/" + lobbyId + "/start";
    const response = await fetch(url, {method: "POST"});
    if (!response.ok) {
        console.log(response.status);
        return;
    }
    await refreshGameState()
})
/*
gameModeSelect.addEventListener("change", async (e) => {
    const newGameMode = gameModeSelect.value;
    const url = baseUrl + "/lobby/" + lobbyId + "/gamemode";
    const response = await fetch(url, {method: "POST", body: gameModeSelect.value});
})*/

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
    "NEUTRAL":"grey",
    "PLAYER1":"red",
    "PLAYER2":"blue",
    "PLAYER3":"green",
    "PLAYER4":"brown",
    "PLAYER5":"purple",
    "PLAYER6":"orange"
}

var board = []
nodes.forEach((e) => {
    board.push({id: e.Id, coords: e.coords, units: 0, owner: e.owner, name:e.Name})
})

const height = document.getElementsByTagName("svg")[0].height.baseVal.value
const width = document.getElementsByTagName("svg")[0].width.baseVal.value
const tester = document.getElementById("tester")

//Melyik IDjú körök vannak kiválasztva: szám
var selected = []

async function refreshGameState(){
    game = await getGame()
    var lobby = await getLobby()
    thisPlayerID = lobby.players[user.userId]
    if(isDeploymentPhase() && isYourTurn()){
        console.log(game)
        deployableUnits = game.players.find((e) => e.id === thisPlayerID).armyIncome
    }
    board.forEach((row)=>{
        const owner = game.gameBoard.territories[row.name].owner
        const armyCount = game.gameBoard.territories[row.name].armyCount

        const i = board.findIndex( (e) => e.name === row.name)
        board[i].owner = owner
        board[i].units = armyCount
    })

}

//A tábla elemeinek kirajzolása
async function updateGameUI() {
    const svg = d3.select("svg")

    const lobby = await getLobby()
    const currentState = lobby.gameInstance.currentTurn.currentState

    const playernamemap = []
    lobby.joinedUsers.forEach((e) => {
        playernamemap.push({name: e.name, userId: e.userId, player: lobby.players[e.userId]})
    })

    combatList.innerHTML = ""
    game.combatMessages.forEach((e) => {
        combatList.innerHTML += "<li>" + e + "</li>"
    })

    const user = playernamemap.find(e => e.player === lobby.gameInstance.currentTurn.activePlayer)
    playerTurnSpan.innerHTML = "Jelenleg következő játékos: " + user.name + ", fázis: " + currentState
    //console.log(isDeploymentPhase(), isYourTurn(), isDeploymentPhase() && isYourTurn())
    if(isDeploymentPhase() && isYourTurn()){
        deployUI.hidden = false
        const deploySpan = document.getElementById("deployable");
        deploySpan.innerHTML = deployableUnits
    }
    else{
        deployUI.hidden = true
    }

    if(isAttackPhase() && isYourTurn()){
        attackUI.hidden = false
    }
    else{
        attackUI.hidden = true
    }

    if(isFortifyPhase() && isYourTurn()){
        fortifyUI.hidden = false
    }
    else{
        fortifyUI.hidden = true
    }

    playerList.innerHTML = ""
    playernamemap.forEach((player)=>{
        playerList.innerHTML += "<li style='color: " + colorMapping[player.player] + "'>"+ player.name + "</li>"
    })

    cardList.innerHTML = ""
    const player = game.players.find((e) => e.id === thisPlayerID)
    player.cards.forEach((card)=>{
        cardList.innerHTML += "<li>"+card.design +", "+ card.territoryID + "</li>"
    })

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
        .on("click", async (e) => {
            await boardClicked(e.target.id)
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
        .on("click", async (e) => {
            await boardClicked(e.target.id)
        })

}

function territoryOwnedByPlayer(id){
    return board[id].owner === thisPlayerID
}

var deployableUnits = 0
var deployment = { deployments: []}

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

async function boardClicked(id) {

    if (isYourTurn() === true) {

        if (isDeploymentPhase() && territoryOwnedByPlayer(id)) {
            board[id].units++
            deployableUnits--
            deployment.lobbyID = lobbyId
            deployment.userID = user.userId
            if (!deployment.deployments.find((e) => e.deploy === board[id].name)) {
                deployment.deployments.push({deploy: board[id].name, amount: 1})
            } else {
                const i = deployment.deployments.findIndex((e) => e.deploy === board[id].name)
                deployment.deployments[i].amount++
            }
            console.log(deployment)
            if (deployableUnits <= 0) {
                const url = baseUrl + "/game/deploy"
                const response = await fetch(url, {
                    method: "POST", body: JSON.stringify(deployment), headers: {
                        "Content-Type": "application/json",
                    }
                })

                if (!response.ok) {
                    throw new Error(response.status)
                }
                deployment = {deployments: []}
                refreshGameState()
            }
            updateGameUI()
        }
        //console.log(isAttackPhase(), !territoryOwnedByPlayer(id), isAttackPhase() && !territoryOwnedByPlayer(id))
        else if ((isAttackPhase() || isFortifyPhase()) && selected && territoryOwnedByPlayer(id) && !selected[0]) {
            console.log("firstSelect")
            selected[0] = id
        }
        else if(isAttackPhase() && selected[0] && !territoryOwnedByPlayer(id)){
            //Támadás selected[0]-ról selected[1]re
            console.log("tamadas")
            var combat = {}
            combat.lobbyID = lobbyId
            combat.userID = user.userId
            combat.from = board[selected[0]].name
            combat.to = board[id].name
            console.log(combat)
            const url = baseUrl + "/game/combat"
            const response = await fetch(url, {
                method: "POST", body: JSON.stringify(combat), headers: {
                    "Content-Type": "application/json",
                }
            })

            if (!response.ok) {
                throw new Error(response.status)
            }

            refreshGameState()
            selected = []
        }
        else if(isFortifyPhase() && selected[0] && territoryOwnedByPlayer(id)){
            console.log("fortify")
            var fortify = {}
            fortify.lobbyID = lobbyId
            fortify.userID = user.userId
            fortify.from = board[selected[0]].name
            fortify.to = board[id].name
            fortify.amount = 1
            const url = baseUrl + "/game/fortify"
            const response = await fetch(url, {
                method: "POST", body: JSON.stringify(fortify), headers: {
                    "Content-Type": "application/json",
                }
            })

            if (!response.ok) {
                throw new Error(response.status)
            }

            refreshGameState()
            selected = []
        }
        else{
            console.log("else ag")
            selected = []

        }
        updateGameUI()
    }
}
