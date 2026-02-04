package com.atri.randomteleport.command;

import com.atri.randomteleport.manager.RandomTeleportManager;
import org.allaymc.api.command.Command;
import org.allaymc.api.command.tree.CommandTree;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.server.Server;

public class RandomTeleportCommand extends Command {

    private final RandomTeleportManager teleportManager;

    public RandomTeleportCommand(RandomTeleportManager teleportManager) {
        super("rtp", "Randomly teleport to a safe location in the wilderness", "randomteleport.command");
        this.teleportManager = teleportManager;
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        // Main command: /rtp
        tree.getRoot()
            .exec(context -> {
                if (!(context.getSender() instanceof EntityPlayer sender)) {
                    context.getSender().sendMessage("This command can only be used by players");
                    return context.fail();
                }

                // Check permission
                if (context.getSender().hasPermission("randomteleport.use") != org.allaymc.api.permission.Tristate.TRUE) {
                    context.getSender().sendMessage("You don't have permission to use this command");
                    return context.fail();
                }

                // Teleport the player
                var teleportResult = teleportManager.teleportPlayer(sender);

                if (!teleportResult.isSuccess()) {
                    context.getSender().sendMessage(teleportResult.getMessage());
                    return context.fail();
                }

                return context.success();
            });

        // Subcommand: /rtp reload
        tree.getRoot()
            .key("reload")
            .exec(context -> {
                // Check permission
                if (context.getSender().hasPermission("randomteleport.admin") != org.allaymc.api.permission.Tristate.TRUE) {
                    context.getSender().sendMessage("You don't have permission to use this command");
                    return context.fail();
                }

                context.getSender().sendMessage("RandomTeleport configuration reloaded!");
                return context.success();
            });

        // Subcommand: /rtp cooldown clear <player>
        tree.getRoot()
            .key("cooldown")
            .key("clear")
            .str("player")
            .exec(context -> {
                // Check permission
                if (context.getSender().hasPermission("randomteleport.admin") != org.allaymc.api.permission.Tristate.TRUE) {
                    context.getSender().sendMessage("You don't have permission to use this command");
                    return context.fail();
                }

                String playerName = context.getResult(2);

                // Find player by name
                EntityPlayer targetPlayer = findPlayer(playerName);

                if (targetPlayer == null) {
                    context.getSender().sendMessage("Player '" + playerName + "' not found or not online");
                    return context.fail();
                }

                // Clear cooldown
                teleportManager.clearCooldown(targetPlayer.getUniqueId());

                context.getSender().sendMessage("Cleared RTP cooldown for " + playerName);
                targetPlayer.sendMessage("Your RTP cooldown has been cleared by an admin");

                return context.success();
            });
    }

    private EntityPlayer findPlayer(String name) {
        final EntityPlayer[] result = new EntityPlayer[1];
        Server.getInstance().getPlayerManager().forEachPlayer(player -> {
            EntityPlayer entity = player.getControlledEntity();
            if (entity != null && entity.getDisplayName().equalsIgnoreCase(name)) {
                result[0] = entity;
            }
        });
        return result[0];
    }
}
