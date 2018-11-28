/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App.View;

import App.Core.ClientRoom;
import java.util.ArrayList;
import java.util.Map;

/*
 * Permet de sauvegarder les Rooms au niveau de client. 
 * @author Gaëtan Perrot, Barbarian
 */
public final class ClientRoomController {

    private ArrayList<ClientRoom> clientRooms = new ArrayList<ClientRoom>();

    public ClientRoomController() {

    }

    public void getAllRoomsFromServers(Map<Integer,Integer> rooms) {
        for(Map.Entry<Integer, Integer> entry : rooms.entrySet()) {
            Integer id = entry.getKey();
            Integer nbPlayer = entry.getValue();
            addRoom(id,nbPlayer);
        }   
    }

    public ArrayList<ClientRoom> getClientRooms() {
        return this.clientRooms;
    }

    public int numberOfRooms() {
        return this.clientRooms.size();
    }

    public void addRoom(ClientRoom cr) {
        clientRooms.add(cr);
    }

    public void addRoom(int id, int members) {
        clientRooms.add(new ClientRoom(id, members));
    }

    public void connectToRoom(ClientRoom room) {
        room.addMember();
        ClientRoom roomBis = getRoom(room.getId());
        roomBis.addMember();
    }

    public ClientRoom getRoom(int id) {
        ClientRoom room = null;
        for (ClientRoom cR : clientRooms) {
            if (cR.isRoom(id)) {
                room = cR;
            }
        }
        return room;
    }

}
