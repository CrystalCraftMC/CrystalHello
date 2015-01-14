/*
 * Copyright 2015 CrystalCraftMC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.crystalcraftmc.crystalhello;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("crystal") && args.length == 0){
			if (isPlayer(sender)){//isPlayer
				Player player = (Player) sender;
				Material itemType = Material.getMaterial(this.getConfig().getString("required-item").toUpperCase().trim());
				boolean required = this.getConfig().getBoolean("require-item");
				int amount = this.getConfig().getInt("amount-required");
				if (player.hasPermission("crystalhello.greetings")) {
					if (required) {
						if (player.getItemInHand().getType().equals(itemType)) {
							if (enoughItems(player, itemType, amount)) {
								removeCrystalItem(player, itemType, amount);
								Bukkit.broadcastMessage(ChatColor.valueOf(this.getConfig().getString("successful-color").toUpperCase().trim()) + this.getConfig().getString("successful-message") + player.getName() + "!");
								return true;
							} else {
								player.sendMessage(ChatColor.valueOf(this.getConfig().getString("failed-color").toUpperCase().trim()) + this.getConfig().getString("lacking-quantity"));
								return false;
							}
						} else {
							player.sendMessage(ChatColor.valueOf(this.getConfig().getString("failed-color").toUpperCase().trim()) + this.getConfig().getString("failed-message"));
							return false;
						}
					}
					else if (!required) {
						Bukkit.broadcastMessage(ChatColor.valueOf(this.getConfig().getString("successful-color").toUpperCase().trim()) + this.getConfig().getString("successful-message") + player.getName() + "!");
						return true;
					}
				}
			} 
			return false;
		}
		else if (cmd.getName().equalsIgnoreCase("crystal") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			return (crystalReload(cmd, sender, args));
		}
		return false;
	}

	public static void removeCrystalItem(Player player, Material itemType, int amountToRemove) {
		Inventory inv = player.getInventory();
		int leftToRemove = amountToRemove;
		for( int i = 0 ; i < inv.getSize() ; i++ ){
			ItemStack invSlot = inv.getItem(i);
			ItemStack toRemove = new ItemStack(itemType);

			if (!invSlot.getType().equals(toRemove.getType())) continue;
			else if (invSlot.getType().equals(toRemove.getType())) {
				if(leftToRemove > 0){//if more to remove then...
					if(invSlot.getAmount() < leftToRemove){//if found
						leftToRemove -= invSlot.getAmount();
						inv.clear(i);
						continue;
					} else if (invSlot.getAmount() == leftToRemove){
						leftToRemove -= invSlot.getAmount();
						inv.clear(i);
						leftToRemove = 0;
						continue;
					} else if (invSlot.getAmount() > leftToRemove){
						invSlot.setAmount((invSlot.getAmount() - leftToRemove));
						leftToRemove = 0;
						continue;
					} else break;
				}
			}
		}
	}

	public boolean isPlayer(CommandSender sender) {
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.valueOf(this.getConfig().getString("failed-color").toUpperCase().trim()) + "You must be a player to use this command.");
			return false;
		} else{
			return true;
		}		
	}

	public boolean crystalReload(Command cmd, CommandSender sender, String[] args){
		if(sender.isOp()){
			this.reloadConfig();
			sender.sendMessage(ChatColor.GRAY + "Configuration reloaded!");
			return true;
		}
		else {
			return false;
		}
	}

	public boolean enoughItems(Player player, Material itemType, int amountToRemove){
		ItemStack item = new ItemStack(itemType);
		int tally = 0;
		for (ItemStack i : player.getInventory()) {
			if (i.getType() == item.getType()) {
				tally += i.getAmount();
			}
		}
		if (tally >= amountToRemove) return true;
		return false;
	}
}
