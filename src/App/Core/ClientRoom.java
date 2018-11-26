/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package App.Core;

/**
 * Client Room definition
 * @author Gaëtan Perrot, Barbarian
 */

final public class ClientRoom {
    private int id;
    private int members;
    private int membersMax = 2;

    public ClientRoom(int id, int members){
        addMembers(members);
        setId(id);
    }

    public ClientRoom(int id, int members, int membersMax){
        addMembers(members);
        setId(id);
        this.membersMax = membersMax;
    }

    public void addMembers(int count){
        if(count < (membersMax - members)){
            members += count;
        }
    }
    
    public void addMember(){
    	members += 1;
    }

    public void removeMembers(int count){
        if(count > (membersMax - members)){
            members -= count;
        }
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public int getMembers(){
        return this.members;
    }

    public int getMembersMax(){
        return this.membersMax;
    }

    public boolean isRoom(int id){
        return id == this.id;
    }

}