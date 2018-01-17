package net.draycia.LagFinder;

import org.bukkit.plugin.java.JavaPlugin;

import net.draycia.LagFinder.Commands.CommandFindLag;

public class Main extends JavaPlugin {
	
    @Override
    public void onEnable() {        
        this.getCommand("findlag").setExecutor(new CommandFindLag());
    }

    @Override
    public void onDisable() {
    	
    }
    
}
