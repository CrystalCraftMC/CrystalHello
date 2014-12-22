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

//package com.crystalcraftmc.crystalhello;

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
		ItemStack toRemove = new ItemStack(itemType);
		for( int i = 0 ; i < inv.getSize() ; i++ ){
			ItemStack invSlot = inv.getItem(i);
			
			if (!invSlot.getType().equals(toRemove.getType())) continue;
			else if (invSlot.getType().equals(toRemove.getType())) {
				if(leftToRemove >= 0){//if more to remove then...
					if(invSlot.getAmount() < leftToRemove){//if found
						leftToRemove -= invSlot.getAmount();
						inv.clear(i);
					} else if (invSlot.getAmount() == leftToRemove){
						leftToRemove -= invSlot.getAmount();
						leftToRemove = 0;
						inv.clear(i);
						continue;
					} else if (invSlot.getAmount() > leftToRemove){
						if(leftToRemove == 0) break;
						else if (leftToRemove != 0) {
						invSlot.setAmount((invSlot.getAmount() - leftToRemove));
						break;
						}
					}
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
		Inventory inv = player.getInventory(); 
			int tally = 0;
			ItemStack toCount = new ItemStack(itemType);
			for (int i = 0; i < inv.getSize(); i++){
				ItemStack invSlot = inv.getItem(i);
				if( invSlot == null) continue;
				else if (!invSlot.getType().equals(toCount.getType())) continue;
				else if (invSlot.getType().equals(toCount.getType())) tally += invSlot.getAmount();
				else break;
			}
			if (tally < amountToRemove){
			return false;
			} 
			else if (tally >= amountToRemove){
				return true;
			}
		return false;
	}
}
