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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("crystal") && args.length == 0){

			if (isPlayer(sender)){
				Player player = (Player) sender;
				Material itemType = Material.getMaterial(this.getConfig().getString("required-item"));
				boolean required = this.getConfig().getBoolean("require-item");
				int amount = this.getConfig().getInt("amount-required");
				executeCrystalLogic(required, itemType, player, amount);
				reloadCrystal(player, cmd, args);
			}
		}
		return false;
	}

	public static void removeItem(Inventory inventory, Material itemType, int amount) {
		if (amount <= 0) return;
		int size = inventory.getSize();
		for (int slot = 0; slot < size; slot++) {
			ItemStack is = inventory.getItem(slot);
			if (is == null) continue;
			if (itemType == is.getType()) {
				int newAmount = is.getAmount() - amount;
				if (newAmount > 0) {
					is.setAmount(newAmount);
					break;
				} else {
					inventory.clear(slot);
					amount = -newAmount;
					if (amount == 0) break;
				}
			}
		}
	}

	public boolean executeCrystalLogic(boolean required, Material itemType, Player player, int amount){ //calls removeItem
		if (player.hasPermission("crystalhello.greetings")) {//permissions start
			if (required) {//required
				if (player.getItemInHand().getType().equals(itemType.toString())) {
					removeItem(player.getInventory(), itemType, amount);
					Bukkit.broadcastMessage(ChatColor.valueOf("successful-color") + this.getConfig().getString("successful-message") + player.getName() + "!");
				} 
				else {
					player.sendMessage(ChatColor.valueOf("failed-color") + this.getConfig().getString("failed-message"));
					return false;
				}
			}//required ends
			else if (!required) {//not required
				Bukkit.broadcastMessage(ChatColor.valueOf("successful-color") + this.getConfig().getString("successful-message") + player.getName() + "!");
				return true;
			}//not required ends
		}//permissions end
		return false;
	}//method ends

	public boolean isPlayer(CommandSender sender) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You must be a player to use this command.");
			return false;
		} else{
			return true;
		}		
	}

	public boolean reloadCrystal(Player player, Command cmd, String[] args){
		if (cmd.getName().equalsIgnoreCase("crystal") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if(player.isOp()){
				this.reloadConfig();
				player.sendMessage(ChatColor.GRAY + "Configuration reloaded!");
				return true;
			}
			return false;
		}
		return false;
	}
}
