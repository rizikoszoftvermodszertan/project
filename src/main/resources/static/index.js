var user = null
var lobbyId = null

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

//Itt kezeljük ha a server frissítésre késztet.
sockJs.onmessage = function (e) {
    console.log("socketmessage:", e.data)
    switch (e.data){
        case 'updatelobby':
            updatePartyUI()
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

function isUserInsideLobby(lobby){
    for (let i = 0; i < lobby.joinedUsers.length; i++){
        const usr = lobby.joinedUsers[i]
        if(usr.userId === user.userId){
            return true;
        }
    }
    return false;
}

function closePartyUI(){
    partyTable.innerHTML = ""
    createPartyButton.disabled = false;
    joinPartyButton.disabled = false;
    leavePartyButton.disabled = true;
    partyIdInput.value = ""
    partyIdInput.disabled = false;
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

    if(isLeader){
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
    sockJs.send(user.userId)
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


