package inf.unideb.hu.riziko.service;

import inf.unideb.hu.riziko.model.Lobby.Lobby;
import inf.unideb.hu.riziko.model.Lobby.User;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyService {
    private final Map<String, Lobby> lobbies=new ConcurrentHashMap<>();
    private LobbyService() {}
    private static class SingletonHelper{private static final LobbyService instance=new LobbyService();}
    public static LobbyService getInstance() {return SingletonHelper.instance;}

    public Lobby CreateLobby(User user) {Lobby lobby=new Lobby(user);lobbies.put(lobby.getLobbyId(), lobby);return lobby;}
    public boolean JoinLobby(User user,String lobbyId)
    {if (lobbies.containsKey(lobbyId))
    {
        lobbies.get(lobbyId).joinLobby(user);
    return true;//ha létezik ez a lobby akkor csatlakozik a user nem nézi hogy maxon van e a lobby
    }
    return false;
    }

    public Lobby GetLobbyByUser(User user)
    {
        Lobby LobbytoFind=null;
        for (Lobby lobby : lobbies.values())
        {
            if(lobby.getJoinedUsers().contains(user))
            {
                LobbytoFind=lobby;
                break;
            }
        }
    return LobbytoFind;
    }
    public User GetUserByUserID(String userID)
    {
     for (Lobby lobby : lobbies.values())
     {
        HashSet<User> h= lobby.getJoinedUsers();
        for(User u:h)
        {
            if (u.getUserId().equals(userID))
            {
                return u;
            }
        }
     }
    return null;
    }

    public Lobby GetLobbyByLoobyID(String lobbyId)
    {
        return lobbies.get(lobbyId);
    }

    public void AddLobby(Lobby lobby)
    {
        lobbies.put(lobby.getLobbyId(), lobby);
    }
    public void RemoveLobby(Lobby lobby)
    {
        lobbies.remove(lobby.getLobbyId());
    }
}
