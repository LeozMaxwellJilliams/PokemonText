/**
 * @(#)PokemonArena.java
 *
 *
 * @Leon Ouyang
 * @The class that handles the actual pokemon arena game. It creates Pokemon objects and allows you two pick 4 and attempt to beat them all!
 */

import java.util.*;
import java.io.*;
public class PokemonArena {
	
	public static final int ATTACK = 1; //values assigned to the 3 possible actions to avoid the use of magic numbers
	public static final int RETREAT = 2;
	public static final int PASS = 3;
	public static Pokemon KeepEnemy= null; //global variable used to store the enemy Pokemon when your Pokemon dies in battle.
	//it is used to determine when the battle is over. when it isn't null, only your pokemon died and the enemy must stay 
	//out and when it is null, the enemy died and the battle is over.
	
    public static void main(String[] args) { //main
    	//read in the Pokemon data file
    	Scanner infile = null;
    	
    	try{ //catch errors in file IO
    		infile = new Scanner(new File("pokemon.txt"));
    	}
    	catch(IOException ex){
    		System.out.println(ex);
    	}
    	int n;
    	n = Integer.parseInt(infile.nextLine());
    	
    	ArrayList<Pokemon>allPoke = new ArrayList<Pokemon>();//array list containing all the Pokemon
    	
    	for(int i=0;i<n;i++){ //creates the Pokemon objects and stores them in allPoke
    		String line = infile.nextLine();
    		//System.out.println(line);
    		Pokemon p = new Pokemon(line);
    		allPoke.add(p);
    	}
    	
    	ArrayList<Pokemon>roster = new ArrayList<Pokemon>();//array list containing the 4 pokemon you chose
    	roster = pickstarters(allPoke); //pick your 4 pokemon
    	
    	while (!(roster.size()==0 || allPoke.size()==0)){//while your team and the enemy team still has alive Pokemon, battle
    		battle(roster,allPoke); 
    	}
    	if (roster.size()==0){ //if your team has died
    		System.out.println("Good effort out there pal, but you LOST!");;
    	}
    	else{//if you killed all the enemies
    		System.out.println("Congratulations trainer, you won!!!!!!!!!!!!!");
    	}	
 	    	
    }
    
    public static ArrayList<Pokemon> pickstarters(ArrayList<Pokemon> pokes){ //method that picks 4 Pokemon to be on your team
    	Scanner kb = new Scanner(System.in);
    	System.out.println("Choose 4 pokemon!\n");
    	for (Pokemon p: pokes){//prints the possible pokemon to choose from
    		System.out.println(Integer.toString(pokes.indexOf(p)+1)+") "+p);
    	}
    	System.out.println("");
    	ArrayList<Pokemon>roster = new ArrayList<Pokemon>(); //array list to store your picks
    	while (roster.size()<4){ //while you don't have 4 pokemon
    		try{
    			System.out.println("Pick a pokemon! (Enter its cooresponding number)");
    			int n = Integer.parseInt(kb.nextLine())-1; //the index of the pokemon in pokes
	    		Pokemon choice = pokes.get(n); //the chosen pokemon
	    		if (roster.contains(choice)){ //if you already picked that pokemon, don't add it
	    			System.out.println("You already picked that pokemon!\n");
	    		}
	    		else{
	    			roster.add(choice); //add the choice to your team
	    			System.out.println("You added "+choice+" to your team!\n");
	    		}
    		}
    		catch(IndexOutOfBoundsException ex){ //catches number errors
    			System.out.println("Choose a valid pokemon pls\n");
    		}
    		catch(Exception ex){ //catches other errors (ex. user enters a word)
    			System.out.println("How about you enter a number next time?\n");
    		}
    	}
    	for (Pokemon p:roster){ //remove your pokemon from the original array list so that you can't fight your own team
    		pokes.remove(p);
    	}
    	System.out.println("Your team is!\n"); 
    	System.out.println("+----------+");
    	for (Pokemon p : roster){ //displays your team
    		System.out.println(p+"!");
    	}
    	System.out.println("+----------+\n");
    	return roster;
    	    	    	
    }
    
    public static void battle(ArrayList<Pokemon>roster, ArrayList<Pokemon>enemies){ //performs a battle
    //one of my 'battles' ends whenever anyone dies, but everything still works so that a real battle
    //is over when the enemy dies or you lose
    	Scanner kb = new Scanner(System.in);
    	Pokemon fighter = pickfighter(roster), enemy; //picks your fighter
    	if (KeepEnemy == null){ //if the enemy did die, so if the real battle is over
    		enemy = pickenemy(enemies);//pick a new enemy
    	}
    	else{//if the enemy didn't die, so if the real battle isn't over
    		enemy = KeepEnemy;//enemy stays out
    		System.out.println("Finish off "+enemy+"!!!");
    	}
    	System.out.println("");
    	double x = Math.random(); //used to decide who goes first
    	while(!(fighter.getHp()<=0 || enemy.getHp()<=0)){ //while neither Pokemon is dead
    		//System.out.println("========================================");
    		System.out.println("                BATTLE!");
	    	display(fighter,enemy); //displays the fighter and enemy's current health and energy
	    	displaystatus(fighter,enemy); //displays if anyone is disabled or stunned
	    	if (x<0.5){ //50% chance you go first
	    		System.out.println("");
	    		fighter = fight(fighter,enemy,roster); //your fighter performs an action
	    		System.out.println("");
	    		display(fighter,enemy); //display info
	    		if (enemy.getHp()>0){ //if the enemy isn't dead yet
	    			System.out.println("");
		    		enemyfight(enemy,fighter); //enemy performs an action
		    		System.out.println("");
		    		display(fighter,enemy); //display info
	    		}
	    	}
	    	else{ //50% chance enemy goes first
	    		System.out.println("");
	    		enemyfight(enemy,fighter); //enemy performs an action
	    		System.out.println("");
	    		display(fighter,enemy);
	    		if (fighter.getHp()>0){ //if you aren't dead yet
	    			System.out.println("");
		    		fighter = fight(fighter,enemy,roster); //you perform an action
		    		System.out.println("");
		    		display(fighter,enemy);
	    		}
	    	}
	    	//recharges 10 energy to your roster and the enemy each turn
	    	for (Pokemon p:roster){
	    		p.recharge(10);
	    	}
    		enemy.recharge(10);
    		System.out.println("");
    	}
    	if (fighter.getHp()<=0){ //if the enemy won
    		System.out.println(enemy+" won!!!!!!!!");
    		roster.remove(fighter); //removes your fighter from your roster list
    		KeepEnemy = enemy; //stores the enemy in KeepEnemy
    	}
    	else if (enemy.getHp()<=0){ //if you won
    		System.out.println(fighter+" won!!!!!");
    		enemies.remove(enemy); //removes the enemy from the enemy list
    		KeepEnemy = null;
    	}
    	System.out.println("");
    	if(KeepEnemy == null){ //if the real battle is over (if the enemy died)
	    	for (Pokemon p:roster){ //reset the stun, disable, and heal and recharge your remaining team
		    	p.resetStun();
	    		p.resetDisable();
	    		p.heal(20);
	    		p.recharge(50);	
		    }
    	}
    	displayroster(roster); //displays your current team's stats
    	System.out.println("There are "+enemies.size()+" enemies left!");
    	System.out.println("");
    }
    
    public static Pokemon pickfighter(ArrayList<Pokemon>roster){ //method to allow you to pick a fighter
    	Scanner kb = new Scanner(System.in);
    	Pokemon fighter=null;
    	System.out.println("Pick which pokemon will fight! (Enter its cooresponding number)\n");
    	for (Pokemon p: roster){ //display your choices
    		System.out.println(Integer.toString(roster.indexOf(p)+1)+") "+p);
    	}
    	while(fighter==null){
	    	try{
				int n = Integer.parseInt(kb.nextLine())-1; //the index of the choice
	    		fighter = roster.get(n); //the chosen pokemon
	    		System.out.println(fighter+", I choose you!!!!!!");
	    	}
	    	//catch input errors
			catch(IndexOutOfBoundsException ex){ 
				System.out.println("Choose a valid pokemon pls\n");
			}
			catch(Exception ex){
				System.out.println("How about you enter a number next time?\n");
			}
    	}
		return fighter; //return the chosen Pokemon
    }
    
    public static Pokemon pickenemy(ArrayList<Pokemon>enemies){ //method to choose an enemy
    	int x = (int)(Math.random()*enemies.size());
		Pokemon enemy = enemies.get(x); //randomly chooses an enemy from the enemy list
		System.out.println("You're facing "+enemy+"!!!!!!!!!");
		return enemy; //return the chosen Pokemon
    }
    
    public static void display(Pokemon fighter, Pokemon enemy){//shows the fighter and the enemy's hp and energy (and type just for fun)
    	System.out.println("========================================");
    	System.out.println(fighter+" Hp: "+fighter.getHp()+"  Energy: "+fighter.getEnergy()+"  Type: "+fighter.getType());
    	System.out.println(enemy+" Hp: "+enemy.getHp()+"  Energy: "+enemy.getEnergy()+"  Type: "+enemy.getType()); 
    	System.out.println("========================================"); 	
    }
    
    public static void displayroster(ArrayList<Pokemon>roster){//shows your team's stats
    	System.out.println("~~~~~~~~~~~~~~~~Your Team~~~~~~~~~~~~~~~");
    	for (Pokemon p: roster){
    		System.out.println(p+" Hp: "+p.getHp()+"  Energy: "+p.getEnergy()+"  Type: "+p.getType());
    	}
    	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    public static void displaystatus(Pokemon fighter, Pokemon enemy){//shows if anyone is disabled or stunned
    	if (fighter.isDisabled()){ 
	    	System.out.println(fighter+" is disabled!");
	    }
    	if (enemy.isDisabled()){
    		System.out.println(enemy+" is disabled!");
    	}
    	if (fighter.isStunned()){ 
    		System.out.println(fighter+" is stunned!");
    	}
    	if (enemy.isStunned()){
    		System.out.println(enemy+" is stunned!");
		}
    }
    
    public static Pokemon fight(Pokemon fighter, Pokemon enemy, ArrayList<Pokemon> roster){ //a method that allows your pokemon to perform an action
    	Scanner kb = new Scanner(System.in);
    	System.out.println("What do you want to do?");
    	System.out.println("1) Attack\n2) Retreat\n3) Pass");
    	int action = 0;
    	//choose an action
    	while (!(action==ATTACK || action==RETREAT || action==PASS)){ //while the action is not one of the 3 valid actions
    		try{
    			System.out.println("Trainer, pick an action!");
    			action = Integer.parseInt(kb.nextLine());
    			if (action==RETREAT && fighter.isStunned()){ //if the pokemon tries to retreat while stunned
    				System.out.println(fighter+" can't retreat, he's stunned!!!");
    				action = 0;
    			}
    		}
    		//catch input errors
    		catch(Exception ex){
				System.out.println("Senpai pls enter a number kawaii desu desu");
			}
    		
    	}
    	if (action == ATTACK){ //attack action
    		ArrayList<Attack>useableattacks = new ArrayList<Attack>();//array list of attacks you have enough energy to use
    		for (Attack atk:fighter.getAttacks()){
    			if (fighter.canUse(atk)){
    				useableattacks.add(atk);
    			}
    		}
    		if (useableattacks.size()==0){ //if you can't do any attacks, choose either to retreat or pass
    			System.out.println(fighter+" doens't have enough energy for any attacks!");
    			System.out.println("What do you want to do?");
    			System.out.println("2) Retreat\n3) Pass");
    			action = 0;
    			while (!(action==RETREAT || action==PASS)){
    				try{
    					System.out.println("Trainer, pick an action!");
    					action = Integer.parseInt(kb.nextLine());
    					if (action==RETREAT && fighter.isStunned()){
    					System.out.println(fighter+" can't retreat, he's stunned!!!");
    					action = 0;
    					}
    				}
    				catch(Exception ex){
						System.out.println("Senpai pls enter a number kawaii desu desu");
					}
    		
    			}
    			
    		}
    		else{ //if you can use an attack
	    		System.out.println(fighter+", attack!\n\nWhat attack do you want to use?");
	    		int len = fighter.getAttacknum(),atkchoice=0;
	    		for (int i=1;i<=len;i++){
	    			System.out.println(i+") "+fighter.getAttacks()[i-1]);
	    		}
	    		while(!(atkchoice>=1 && atkchoice<=len)){ //choose a valid attack
	    			try{
	    				System.out.println("Trainer, pick an attack!");
	    				atkchoice = Integer.parseInt(kb.nextLine());
	    				if (!(fighter.canUse(fighter.getAttacks()[atkchoice-1]))){//if you can't do the selected attack, choose again
	    					System.out.println(fighter+" doesn't have enough energy to use "+fighter.getAttacks()[atkchoice-1]+"!!!");
	    					atkchoice = 0;
	    				}
	    			}
	    			catch(Exception ex){
						System.out.println("Senpai pls enter a number kawaii desu desu baka");
					}
	    		}
	    		System.out.println(fighter+", use "+fighter.getAttacks()[atkchoice-1]+"!!!!!!!!!!!!");
	    		fighter.attack(enemy,fighter.getAttacks()[atkchoice-1]); //perform the attack  		
    		}
    	}
    	if (action == RETREAT){//retreat action
    		ArrayList<Pokemon>rosterchoices = new ArrayList<Pokemon>(); //array list of your other pokemon 
    		for (Pokemon p: roster){
    			if (p!=fighter){
    				rosterchoices.add(p);
    			}
    		}
    		if (rosterchoices.size()==0){ //if you have no other pokemon, pass instead
    			System.out.println("All your other Pokemon died! "+fighter+" must pass instead of retreating!");
    			action = PASS;
    		}
    		else{
    		
	    		System.out.println(fighter+", retreat! You've let me down enough.\n");
	    		System.out.println("Who do you want to replace that piece of trash?");
	    		
	    		for (Pokemon p: rosterchoices){
	    			System.out.println(Integer.toString(rosterchoices.indexOf(p)+1)+") "+p);
	    		}
	    		fighter = null;
	    		while(fighter==null){ //choose a new fighter to replace the current one with
			    	try{
						int n = Integer.parseInt(kb.nextLine())-1;
			    		fighter = rosterchoices.get(n);
			    		System.out.println(fighter+", I choose you!!!!!!");
			    	}
					catch(IndexOutOfBoundsException ex){
						System.out.println("Choose a valid pokemon pls");
					}
					catch(Exception ex){
						System.out.println("How about you enter a number next time?");
					}
	    		}
	    	}
    		
    	}
    	if (action == PASS){ //pass the turn (do nothing)
    		System.out.println(fighter+", pass the turn!");
    	}
    	if (fighter.isStunned()){ //if your pokemon was stunned for the turn, he is no longer stunned
    		fighter.resetStun();
    		System.out.println(fighter+" snapped out of being stunned!");
    	}
    	return fighter;
    }
    
    public static void enemyfight(Pokemon fighter, Pokemon enemy){ //method to allow the enemy to perform actions
    	ArrayList<Attack>useableattacks = new ArrayList<Attack>(); //array list of the attacks the enemy can perform
    	for (Attack atk:fighter.getAttacks()){
    		if (fighter.canUse(atk)){
    			useableattacks.add(atk);
    		}
    	}
    	if (useableattacks.size()==0){ //if the enemy can't do any attacks, pass
    		System.out.println(fighter+" can't do anything!?!?!");
    	}
    	else{ //choose a random attack to use
    		int x = (int)(Math.random()*useableattacks.size());
    		Attack choice = useableattacks.get(x);
    		System.out.println(fighter+" used "+choice+"!!!!!!!");
    		fighter.attack(enemy,choice); //perform the attack
    	}
    	if (fighter.isStunned()){ //if the enemy was stunned, reset his stun
    		fighter.resetStun();
    		System.out.println(fighter+" snapped out of being stunned!");
    	}
    	   	
    	
    }
    	
    
    
    
    
}


    
    
    
