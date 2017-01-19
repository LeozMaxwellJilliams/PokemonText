/**
 * @(#)Attack.java
 *
 *
 * @Leon Ouyang 
 * @A class that stores information about the Pokemons' attacks. This includes the name, cost, damage, and its 
 *  special. A single pokemon can have more than one attack.
 */


public class Attack {
	
	private int ecost, dmg; //ecost is energy cost, dmg is damage
	private String name, special;

    public Attack(String name, String ecost, String dmg, String special) { //constructor  takes parameters and sets them to the fields
    	this.name = name;
    	this.ecost = Integer.parseInt(ecost);
    	this.dmg = Integer.parseInt(dmg);
    	this.special = special;
    }
    //get functions
    public int getEcost(){
    	return ecost;
    }
    
    public int getDmg(){
    	return dmg;
    }
    
    public String getName(){
    	return name;
    }
    
    public String getSpecial(){
    	return special;
    }
    //functions that returns whether the attack is each type of special. Used to determine which type of special the
    //attack is, if any
    public boolean stun(){
    	return special.equals("stun");
    }
    
    public boolean wildcard(){
    	return special.equals("wild card");
    }
    
    public boolean wildstorm(){
    	return special.equals("wild storm");
    }	
    
    public boolean disable(){
    	return special.equals("disable");
    }
    
    public boolean recharge(){
    	return special.equals("recharge");
    }
    
    public String toString(){ //returns the attack name if its printed
    	return name;
    }
    
    
}