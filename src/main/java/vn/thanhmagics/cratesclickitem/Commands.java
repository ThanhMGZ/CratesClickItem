package vn.thanhmagics.cratesclickitem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import vn.thanhmagics.utils.PlayerData;

import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        PlayerData player = CratesClickItem.getInstance().getPlayerDataMap().get(((Player) commandSender).getUniqueId());
        if (!player.getPlayer().hasPermission("op")) {
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(List.of("/cci create [id] [crateId]","/cci delete [id]"));
        } else if (args.length == 1) {
            player.sendMessage(List.of("/cci create [id] [crateId]","/cci delete [id]"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                if (CratesClickItem.getInstance().getClickCrateMap().containsKey(args[1])) {
                    CratesClickItem.getInstance().getClickCrateMap().remove(args[1]);
                } else {
                    player.sendMessage("&cid ko ton tai");
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!CratesClickItem.getInstance().getClickCrateMap().containsKey(args[1])) {
                    Crate crate = ExcellentCratesAPI.getCrateManager().getCrateById(args[2]);
                    if (crate != null) {
                        if (player.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                            ClickCrate clickCrate = new ClickCrate(args[1]);
                            clickCrate.setCrate(crate);
                            clickCrate.setItemStack(player.getPlayer().getInventory().getItemInMainHand());
                            player.sendMessage("tạo thành công!");
                        } else {
                            player.sendMessage("cầm key trên tay");
                        }
                    } else {
                        player.sendMessage("crateID ko tồn tại");
                    }
                } else {
                    player.sendMessage("&cid da ton tai");
                }
            }
        }
        return true;
    }
}
