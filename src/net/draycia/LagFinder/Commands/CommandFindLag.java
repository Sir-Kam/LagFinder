package net.draycia.LagFinder.Commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;


public class CommandFindLag implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args != null && args.length > 0 && args[0].equalsIgnoreCase("types")) {
			String values = "";
			for (EntityType entityType : EntityType.values()) {
				values += entityType.name() + ", ";
			}
			sender.sendMessage(values);
			return true;
		}
		
		String worldString = null;
		String entityName = null;
		
		for (String str : args) {
			if (str.length() > 2) {
				if (str.toLowerCase().startsWith("w:")) {
					worldString = str.substring(2);
				} else if (str.toLowerCase().startsWith("e:")) {
					entityName = str.substring(2);
				}
			}
		}
		
		String worldName = (worldString != null) ? worldString : "world";
		World world = Bukkit.getServer().getWorld(worldName);
		
		if (world == null) {
			sender.sendMessage(ChatColor.RED + "That world (" + worldName + ") does not exist!");
			return true;
		}
		
		Chunk[] chunks = world.getLoadedChunks();
		List<Pair<Location, Integer>> chunkList = new ArrayList<Pair<Location, Integer>>();
		String entityType = (args.length > 1) ? entityName : null;
			
		for (Chunk chunk : chunks) {
			Pair<Location, Integer> entityPair = entityCount(chunk, entityType);
			if (entityPair.getRight() != 0) {
				chunkList.add(entityPair);
			}
		}
		
		chunkList = sortList(chunkList);
		
		sender.sendMessage(ChatColor.GOLD + "-=-=-=-=<" + ChatColor.GRAY + "TOP 10 MOST ENTITY POPULATED CHUNKS" + ChatColor.GOLD + ">=-=-=-=-");
		
		int iterationCount = (chunkList.size() >= 10) ? 10 : chunkList.size();
		
		for (int i = 0; i < iterationCount; i++) {
			Pair<Location, Integer> chunk = chunkList.get(i);
			Location loc = chunk.getLeft();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6 X: &f" + fixFontSize(Integer.toString((int)(loc.getX() * 16)), 10) + "&6Z: &f" + fixFontSize(Integer.toString((int)(loc.getZ() * 16)), 10) + "&6Count: &f" + fixFontSize(Integer.toString(chunk.getRight()), 10)));
		}
		
		return true;
	}
	
	public static Pair<Location, Integer> entityCount(Chunk chunk, String entityName) {
		int count = 0;
		Location returnLoc = new Location(chunk.getWorld(), chunk.getX(), 0, chunk.getZ());
		Entity[] entities = chunk.getEntities();

		if (entityName != null) {
			EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
	
			for (Entity entity : entities) {
				if (entity.getType() == entityType)
					count++;
			}
		} else {
			count = entities.length;
		}
		
		
		return new MutablePair<Location, Integer>(returnLoc, count);
	}
	
	public static List<Pair<Location, Integer>> sortList(List<Pair<Location, Integer>> pairList) {
		Function<Pair<Location, Integer>, Integer> t = Pair::getValue;
		Comparator<Pair<Location, Integer>> cmp = Comparator.comparing(t).reversed();
		pairList.sort(cmp);
		return pairList;
	}
	
	/********************************************************
	* Fix string spaces to align text in minecraft chat
	*
	* @author David Toledo ([EMAIL]david.oracle@gmail.com[/EMAIL])
	* @param String - String to be resized
	* @param Integer - Size to align
	* @return New aligned String
	*/
	public static String fixFontSize(String s, int size) {
	 
	    String ret = s.toUpperCase();
	 
	    if ( s != null ) {
	 
	        for (int i=0; i < s.length(); i++) {
	            if ( s.charAt(i) == 'I' || s.charAt(i) == ' ') {
	                ret += " ";
	            }
	        }
	 
	        int faltaEspacos = size - s.length();
	        faltaEspacos = (faltaEspacos * 2);
	 
	        for (int i=0; i < faltaEspacos; i++) {
	            ret += " ";
	        }
	    }
	 
	    return (ret);
	}
}










