/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 CrystalCraftMC.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.crystalcraftmc.crystalhello;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class CrystalHello extends JavaPlugin {
    public void onEnable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been initialized!");
    }

    public void onDisable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been stopped by the server.");
    }

    // Insert attributes and behaviors here!
    //So far: basic online of a few main command checks and (hopefully) some code that will determine whether player is holding ice.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		//----------------------------COMMAND (1 of 4)-----------------------------
		if(cmd.getName().equalsIgnoreCase("<command name> needs to be added to our (title goes here).yml file")){

			//-----------------------NON-PLAYER (2 of 4)---------------------------
			if(!(sender instanceof Player)){
				//maybe additional statements
				sender.sendMessage("You are not a player.");
				return false;
			} 
			//------------------------PLAYER (3 of 4) [and] ICE Check (4 of 4)---------------------------
			//Note there is no specific check for (sender instanceof Player) Just the confirmation that it is not- a non-player
			else {
				sender.sendMessage("You are a player.");
				Player player = (Player) sender;
				//if .getItemInHand().getData is equal to "ICE" Then this should work. I don't know for sure though.
				//.getItemInHand might work by itself if .getData() has an undesired data type (effect).
				if(player.getItemInHand().equals(Material.ICE)){
					player.sendMessage("Your hands must be cold, after holding that ice for so long.");
				//This line/area here is reserved for what ever happens when the command, ice in hand, and player come together
					return true;
				} else {
					player.sendMessage("This command requires icy hands, not an icy heart! You need to hold some ice.");
					return false;
				} 
			} 
		}
		return false;
	}
    // See here(http://wiki.bukkit.org/Plugin_Tutorial) for extra help!
}
