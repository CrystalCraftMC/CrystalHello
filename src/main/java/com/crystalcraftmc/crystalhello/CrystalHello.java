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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public class CrystalHello extends JavaPlugin {
    public void onEnable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been initialized!");

        // Here, we are checking to see if a config.yml already exists. If no, generate a new one!
        try {
            File database = new File(getDataFolder(), "config.yml");
            if (!database.exists()) saveDefaultConfig();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void onDisable() {
        getLogger().info(ChatColor.AQUA + "CrystalHello has been stopped by the server.");
    }

    // So far: basic online of a few main command checks and (hopefully)
    /// some code that will determine whether player is holding ice.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		//----------------------------COMMAND (1 of 4)-----------------------------
		if(cmd.getName().equalsIgnoreCase("crystal")){

			//-----------------------NON-PLAYER (2 of 4)---------------------------
			if(!(sender instanceof Player)){
				//maybe additional statements
				sender.sendMessage("You must be a player to use this command.");
				return false;
			} 
			//------------------------PLAYER (3 of 4) [and] ICE Check (4 of 4)---------------------------
			//Note there is no specific check for (sender instanceof Player) Just the confirmation that it is not- a non-player
			else {
				Player player = (Player) sender;
				//REQUIRE PERMISSIONS
				//REQUIRE ITEM: FALSE (1 of 2)
				if (player.hasPermission("crystalhello.greetings")) {
					if (this.getConfig().getBoolean("require-item")) {
						if (player.getItemInHand().getType().equals(Material.getMaterial(this.getConfig().getString("required-item")))) {
							Bukkit.broadcastMessage(ChatColor.AQUA + "CrystalCraftMC has decided to grant the wish trapped deepest in your heart:\n" + "Hello, " + player.getName() + "!");
							return true;
						} else {
							player.sendMessage(ChatColor.RED + "This command requires icy hands, not an icy heart!");
							return false;
						}
					}
					//REQUIRE ITEM: TRUE (2 of 2)
					else if (!this.getConfig().getBoolean("require-item")) {
						Bukkit.broadcastMessage(ChatColor.AQUA + "CrystalCraftMC has decided to grant the wish trapped deepest in your heart:\n" + "Hello, " + player.getName() + "!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
