/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core;

/**
 * Client Room definition
 * @author Gaëtan Perrot, Barbaria
 */

final public class ClientRoom {
    private int id;
    private int members;
    private int membersMax = 2;

    /**
     * Constructor of ClientRoom
     * @param id
     * @param members
     */
    public ClientRoom(int id, int members){
        addMembers(members);
        setId(id);
    }

    /**
     * Constructor of ClientRoom
     * @param id
     * @param members
     * @param membersMax
     */
    public ClientRoom(int id, int members, int membersMax){
        addMembers(members);
        setId(id);
        this.membersMax = membersMax;
    }

    /**
     * add members to the room
     * @param count
     */
    public void addMembers(int count){
        if(count < (membersMax - members)){
            members += count;
        }
    }
    
    /**
     * add a member to the room
     */
    public void addMember(){
    	members += 1;
    }

    /**
     * Remove a member to the room
     * @param count
     */
    public void removeMembers(int count){
        if(count > (membersMax - members)){
            members -= count;
        }
    }

    /**
     * set room id
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * get room id
     * @return id
     */
    public int getId(){
        return this.id;
    }

    /**
     * get nb members
     * @return nb members
     */
    public int getMembers(){
        return this.members;
    }

    /**
     * get members max
     * @return
     */
    public int getMembersMax(){
        return this.membersMax;
    }

    /**
     * id is a room
     * @param id
     * @return
     */
    public boolean isRoom(int id){
        return id == this.id;
    }

}