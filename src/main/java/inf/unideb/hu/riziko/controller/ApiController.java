package inf.unideb.hu.riziko.controller;

import inf.unideb.hu.riziko.model.*;
import inf.unideb.hu.riziko.model.actions.Combat;
import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;
import inf.unideb.hu.riziko.model.actions.Deploy;
import inf.unideb.hu.riziko.model.actions.Fortify;
import inf.unideb.hu.riziko.model.map.Territory;
import inf.unideb.hu.riziko.repository.LobbyRepository;
import inf.unideb.hu.riziko.requests.CombatRequest;
import inf.unideb.hu.riziko.requests.DeployRequest;
import inf.unideb.hu.riziko.requests.FortifyRequest;
import inf.unideb.hu.riziko.service.LobbyService;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
public class ApiController {

    //LobbyService lobbyService;
    LobbyRepository lobbyRepository;

    public ApiController(LobbyRepository lobbyRepository) {this.lobbyRepository = lobbyRepository;}
    /*
    public void FindLobbyService()
    {
        if(lobbyService!=null)
        {
            return;
        }
        lobbyService=LobbyService.getInstance();
    }
    */
    private GameInstance FetchGameInstance(Lobby lobby)
    {

        if(lobby.getGameInstance()==null)
        {
            return null;
        }
        return lobby.getGameInstance();
    }

    /*
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
     */

    @PostMapping("/game/combat")
    public ResponseEntity<String> ResolveCombat(@RequestBody CombatRequest request)//nem nézi meg hogy szomszédos területek-e
    {
        //FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyRepository.getLobby(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }

        String attacker=request.getAttackingterritoryname();
        String defender=request.getDefendingterritoryname();
        if(attacker==null||defender==null)
        {
            System.out.println("attackingterritoryname or defendingterritoryname is null");
            return ResponseEntity.badRequest().body("attackingterritoryname or defendingterritoryname is null");
        }
        gameInstance.attack(attacker, defender);
        return ResponseEntity.ok().body("Combat resolved");


    }

    @PostMapping("/lobby/{lobbyId}/end")
    public void endPhase(@PathVariable String lobbyId){
        lobbyRepository.getLobby(lobbyId).getGameInstance().getCurrentTurn().advanceTurnState();
    }

    @PostMapping("/game/fortify")
    public ResponseEntity<String> ResolveFortify(@RequestBody FortifyRequest request)
    {
        //FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyRepository.getLobby(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }
        String origin = request.getFrom();
        String destination = request.getTo();
        int amount = request.getAmount();
        if(origin==null||destination==null)//todo megteheti-e a lépést
        {
            System.out.println("movefromname or movetoname is null");
            return ResponseEntity.badRequest().body("movefromname or movetoname is null");
        }

        gameInstance.fortify(origin, destination, amount);
        return ResponseEntity.ok().body("Fortify resolved");
    }

    @PostMapping("/game/deploy")
    public ResponseEntity<String> ResolveDeploy(@RequestBody DeployRequest request)
    {
        //FetchService();
        GameInstance gameInstance=FetchGameInstance(lobbyRepository.getLobby(request.getLobbyID()));
        if(gameInstance==null)
        {
            return ResponseEntity.badRequest().body("Game is not found or started yet");
        }
        gameInstance.deploy(request.getDeployments());


        return ResponseEntity.ok().body("Deploy resolved");
    }
}


