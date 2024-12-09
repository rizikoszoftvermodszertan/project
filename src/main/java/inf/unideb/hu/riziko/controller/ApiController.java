package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.actions.Combat;
import inf.unideb.hu.riziko.model.GameBoard;
import inf.unideb.hu.riziko.model.GameInstance;
import inf.unideb.hu.riziko.model.GameMode;
import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import inf.unideb.hu.riziko.model.actions.Deploy;
import inf.unideb.hu.riziko.model.actions.Fortify;
import inf.unideb.hu.riziko.model.map.Territory;
import inf.unideb.hu.riziko.requests.CombatRequest;
import inf.unideb.hu.riziko.requests.DeployRequest;
import inf.unideb.hu.riziko.requests.FortifyRequest;
import inf.unideb.hu.riziko.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    LobbyService lobbyService;

    public void FindLobbyService()
    {
        if(lobbyService!=null)
        {
            return;
        }
        lobbyService=LobbyService.getInstance();
    }

    private GameInstance FetchGameInstance(Lobby lobby)
    {

        if(lobby.getGameInstance()==null)
        {
            return null;
        }
        return lobby.getGameInstance();
    }

    public boolean FetchService()
    {
        FindLobbyService();
        if(lobbyService==null)
        {
            System.out.println("LobbyService is not found");
            return false;
        }
        System.out.println("LobbyService is found");
        return true;
    }

    @PostMapping("/game/combat")
    public ResponseEntity<String> ResolveCombat(@RequestBody CombatRequest request)//nem nézi meg hogy szomszédos területek-e
    {
        FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyService.GetLobbyByLoobyID(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }

        String attackingterritoryname=request.getAttackingterritoryname();
        String defendingterritoryname=request.getDefendingterritoryname();
        if(attackingterritoryname==null||defendingterritoryname==null)
        {
            System.out.println("attackingterritoryname or defendingterritoryname is null");
            return ResponseEntity.badRequest().body("attackingterritoryname or defendingterritoryname is null");
        }
        GameBoard gameBoard=gameInstance.getGameBoard();
        Territory attackingterritory=gameBoard.findTerritoryByName(attackingterritoryname);
        Territory defendingterritory=gameBoard.findTerritoryByName(defendingterritoryname);
        Combat combat=new Combat(attackingterritory,defendingterritory);
        return ResponseEntity.ok().body("Combat resolved");


    }

    @PostMapping("/game/fortify")
    public ResponseEntity<String> ResolveFortify(@RequestBody FortifyRequest request)
    {
        FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyService.GetLobbyByLoobyID(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }
        String movefromname= request.getFrom();
        String movetoname=request.getTo();
        if(movefromname==null||movetoname==null)//todo megteheti-e a lépést
        {
            System.out.println("movefromname or movetoname is null");
            return ResponseEntity.badRequest().body("movefromname or movetoname is null");
        }
        GameBoard gameBoard=gameInstance.getGameBoard();
        Territory movefromterritory=gameBoard.findTerritoryByName(movefromname);
        Territory movetoterritory=gameBoard.findTerritoryByName(movetoname);
        Fortify fortify=new Fortify(movefromterritory,movetoterritory, request.getAmount());
        return ResponseEntity.ok().body("Fortify resolved");
    }

    @PostMapping("/game/deploy")
    public ResponseEntity<String> ResolveDeploy(@RequestBody DeployRequest request)
    {
        FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyService.GetLobbyByLoobyID(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }
        String deployname=request.getDeploy();
        if (deployname==null)
        {
            System.out.println("deploy is null");
            return ResponseEntity.badRequest().body("deploy is null");
        }
        GameBoard gameBoard=gameInstance.getGameBoard();
        Territory deployterritory=gameBoard.findTerritoryByName(deployname);
        Deploy deploy=new Deploy(deployterritory,request.getAmount());
        return ResponseEntity.ok().body("Deploy resolved");
    }
}


