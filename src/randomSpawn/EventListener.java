package randomSpawn;
/*
 * Description: This is the event Listener of this plugin
 * 
 * Author: maxxie114
 * 
 * version: 3.0.0
 */

//import all dependencies from nukkitx
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
//import cn.nukkit.utils.TextFormat;
//import cn.nukkit.plugin.PluginBase;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.HashSet;
//import dependencies from java.util for random number generation
import java.util.Random;

//create a class
public class EventListener implements Listener{
	// define final variables
	private final randomSpawn rndspawn; 
	// define private variables
	private int x;
	private int y;
	private int z;
	//private PluginBase base;
	private HashSet<String> isJoining = new HashSet<String>(Arrays.asList(""));
	//HashSet<String> existingPlayers = new HashSet<String>();
	//private boolean isJoin;
	
	//Constructor method
	public EventListener(randomSpawn randspawn) {
		this.rndspawn = randspawn;
	}
	
	//When player login
	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		isJoining.add(name);
	}

	//When player join
	@EventHandler
	public void onJoin(PlayerJoinEvent event) { 
		Player player = event.getPlayer();
		String name = player.getName();
		if(!rndspawn.playerList.contains(name)) {
			rndspawn.playerList.add(name);
			//Random spawn should also occur to new players on join
			Random rnd = new Random();
			//define x, y, and z positions
			x = rnd.nextInt(rndspawn.maxRange) + 50;
			z = rnd.nextInt(rndspawn.maxRange) + 50;
			y = player.getLevel().getHighestBlockAt(x, z) + 3;
			
			//Prevent fall from high place and died, specific to 2b2tbe Map
			if(y >= 130){
				y = 65;
			}
			

			//set the random re-spawn positions
			Location pos = new Location(x, y, z, player.getLevel());
			event.getPlayer().teleport(pos);
			
		}

	}

	//When players Re-spawn
	@EventHandler // (ignoreCancelled = false) //watch out
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		//This if statement prevent all players from being teleported to random places 
			//This if statement prevent players with who has a spawnpoint set by a bed from losing their spawnpoint
			if (player.getSpawn().getX() == rndspawn.worldspawn.getX()
					&& player.getSpawn().getZ() == rndspawn.worldspawn.getZ() && !isJoining.contains(name)) {

				//Create a random number generator
				Random rnd = new Random();
				//define x, y, and z positions
				x = rnd.nextInt(rndspawn.maxRange) + 50;
				z = rnd.nextInt(rndspawn.maxRange) + 50;
				y = player.getLevel().getHighestBlockAt(x, z) + 3;

				//Prevent fall from high place and died, specific to 2b2tbe Map
				if(y >= 130){
					y = 65;
				}
			

				//set the random respawn positions
				event.setRespawnPosition(new Position(x, y, z, player.getLevel()));
			}
			isJoining.remove(name);

	}

}
	