/**
 * @(#)Pokemon.java
 *
 *
 * @Leon Ouyang
 * @A class that stores all the vital information of Pokemon objects. It also has the logic behind attack.
 */

import java.util.*;
public class Pokemon {
	
	//variables are named after what they represent for ex, name is the name of the Pokemon
	private String name,type,resistance,weakness;
	private int hp,attacknum,energy,originalhp;
	private Attack[] attacks;//an Attack array that contains the pokemon's attacks 
	private boolean stunned,disabled; //status variables for if the pokemon is stunned or disabled

    public Pokemon(String line) { //constructor  takes in a line from the text file 
    	String[] items = line.split(","); //splits the line at commas
    	//assigns the appropriate information to each field 
    	name = items[0];
    	hp = Integer.parseInt(items[1]);
    	originalhp = hp;
    	type = items[2];
    	resistance = items[3];
    	weakness = items[4];
    	attacknum = Integer.parseInt(items[5]);
    	attacks = new Attack[attacknum];
    	for (int i=0;i<attacknum;i++){
    		//Adds new Attack objects to the Attack array for each attack that the Pokemon has
    		attacks[i] = new Attack(items[i*4+6],items[i*4+7],items[i*4+8],items[i*4+9]);
    	}
    	energy = 50; //Pokemon starts with full energy and not stunned nor disabled
    	stunned = false;
    	disabled = false;
    		
    }
    
    public boolean resistant(Pokemon attacker){ //checks if this Pokemon is resistant to the attacker
    	return attacker.type.equals(resistance);
    }
    
    public boolean weak(Pokemon attacker){ //checks if this Pokemon is weak to the attacker
    	return attacker.type.equals(weakness);
    }
    
    public boolean canUse(Attack atk){ //checks if the Pokemon has enough energy to use the attack
    	return energy>=atk.getEcost();
    }
    	
    public void attack(Pokemon enemy, Attack atk){ //attack method 
    	if (canUse(atk) && !(stunned)){ //if the Pokemon can use the attack and he isn't stunned
	    	int damage = atk.getDmg(); //gets the damage and modifies the damage(x2 or /2 or -10) if necessary
	    	if (enemy.resistant(this)){
	    		damage*=0.5;
	    	}
	    	else if (enemy.weak(this)){
	    		damage*=2;
	    	}
	    	if (disabled){ //if this Pokemon is disabled, reduce his damage by 10
	    		damage = Math.max(0,damage-10);
	    	}
	    	//if the attack is a special, it will perform the appropriate actions
	    	if (atk.stun()){
	    		stun(enemy);
	    	}
	    	if (atk.disable()){
	    		disable(enemy);
	    	}
	    	if (atk.recharge()){
	    		recharge(20);
	    	}
	    	
	    	if (atk.wildstorm()){
    			if (enemy.resistant(this)){
		    		System.out.println("It was not very effective...");
		    	}
		    	else if (enemy.weak(this)){
		    		System.out.println("It was super duper effective!");
		    	}
	    		wildstorm(enemy, damage);
	    	}
	    	
	    	else if (!(atk.wildcard() && miss())){ //if the attack isn't a special or it is a wildcard and didn't miss
	    		if (enemy.resistant(this)){
		    		System.out.println("It was not very effective...");
		    	}
		    	else if (enemy.weak(this)){
		    		System.out.println("It was super duper effective!");
		    	}
	    		enemy.hp-=damage; //the enemy takes damage
	    		
	    	}
	    	else{//if it missed
	    		System.out.println(name+" missed!");
	    	}
	    	energy-=atk.getEcost();//reduce the energy used
    	}
    	if (stunned){ //Prints that the Pokemon is stunned if it's stunned
    		System.out.println(name+" can't attack, he's stunned!!!");
    	}
    }
    
    public void stun(Pokemon enemy){ //stun attack
    	double x = Math.random();
    	if (x<.5){
    		enemy.stunned = true;//the enemy will be stunned 50% of the time
    		System.out.println(enemy+" was stunned!!!");
    	}
    }
    
    public boolean miss(){ //returns whether the attack missed (used in wildstorm and wildcard)
    	double x = Math.random();
    	return (x<.5); //50% chance of missing
    }
    
    public void wildstorm(Pokemon enemy, int damage){ //wildstorm attack
    	int counter = 0; //counter to keep track of how many hits were landed
    	while (!(miss())){ //keeps hitting while it doesn't miss
    		counter+=1;
    		enemy.hp-=damage;
    		System.out.println(counter+" hit(s)!");
    	}
    	if (counter == 0){
    		System.out.println(name+" missed!");
    	}
    }
    
    public void disable(Pokemon enemy){ //disable attack
    	enemy.disabled = true; //disables the enemy
    	System.out.println(enemy+" was disabled!!!");
    }
    
    public void recharge(int x){ //adds x amount of energy to the Pokemon
    	energy = Math.min(energy+x,50);
    }
    
    public void heal(int x){ //adds x amount of health to the Pokemon
    	hp = Math.min(hp+x,originalhp);
    }
    
    public String toString(){ //returns the Pokemon's name if printed
    	return name;
    }
    
    //get functions
    public int getHp(){
    	return hp;
    }
    
    public int getEnergy(){
    	return energy;
    }
    
    public String getType(){
    	return type;
    }
    
    public Attack[] getAttacks(){
    	return attacks;
    }
    
    public int getAttacknum(){
    	return attacknum;
    }
    
    //reset functions for the two statuses
    public void resetStun(){
    	stunned = false;
    }
    
    public void resetDisable(){
    	disabled = false;
    }
    
    //functions to check if the pokemon is disabled or stunned
    public boolean isDisabled(){
    	return disabled;
    }
    
    public boolean isStunned(){
    	return stunned;
    }
    
   
    
    
    
    
}