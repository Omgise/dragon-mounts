/*
 ** 2012 August 24
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.dragon.server.cmd;

import com.github.bsideup.jabel.Desugar;
import info.ata4.minecraft.dragon.server.entity.EntityTameableDragon;
import info.ata4.minecraft.dragon.server.entity.breeds.DragonBreed;
import info.ata4.minecraft.dragon.server.entity.helper.DragonBreedRegistry;
import info.ata4.minecraft.dragon.server.entity.helper.DragonLifeStage;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class CommandDragon extends CommandBase {

    @Override
    public String getCommandName() {
        return "dragon";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        String stages = StringUtils.join(DragonLifeStage.values(), '|').toLowerCase();
        String breeds = StringUtils.join(DragonBreedRegistry.getInstance().getBreeds(), '|');
        return String.format("/dragon <stage <%s>|breed <%s> [global]", stages, breeds);
    }

    /**
     * Return the required permission level for this command.
     */
    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] params) {
        if (params.length < 1 || params[0].isEmpty()) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        // last parameter, optional
        boolean global = params[params.length - 1].equalsIgnoreCase("global");

        String command = params[0];
        switch (command) {
            case "stage" -> {
                if (params.length < 2) {
                    throw new WrongUsageException(getCommandUsage(sender));
                }
                DragonLifeStage lifeStage = null;
                String parameter = params[1].toUpperCase();
                if (!parameter.equals("ITEM")) {
                    try {
                        lifeStage = DragonLifeStage.valueOf(parameter);
                    } catch (IllegalArgumentException ex) {
                        throw new SyntaxErrorException();
                    }
                }
                EntityModifier modifier = new LifeStageModifier(lifeStage);
                appyModifier(sender, modifier, global);
            }
            case "breed" -> {
                if (params.length < 2) {
                    throw new WrongUsageException(getCommandUsage(sender));
                }
                String breedName = params[1].toLowerCase();
                DragonBreed breed = DragonBreedRegistry.getInstance().getBreedByName(breedName);
                if (breed == null) {
                    throw new SyntaxErrorException();
                }
                appyModifier(sender, new BreedModifier(breed), global);
            }
            case "tame" -> {
                if (sender instanceof EntityPlayerMP player) {
                    appyModifier(sender, new TameModifier(player), global);
                } else {
                    // console can't tame dragons
                    throw new CommandException("commands.dragon.canttame");
                }
            }
            default -> throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    private void appyModifier(ICommandSender sender, EntityModifier modifier, boolean global) {
        if (!global && sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = getCommandSenderAsPlayer(sender);
            double range = 64;
            AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(
                    player.posX - 1, player.posY - 1, player.posZ - 1,
                    player.posX + 1, player.posY + 1, player.posZ + 1);
            aabb = aabb.expand(range, range, range);
            List<EntityTameableDragon> entities = player.worldObj.getEntitiesWithinAABB(EntityTameableDragon.class, aabb);

            Entity closestEntity = null;
            float minPlayerDist = Float.MAX_VALUE;

            // get closest dragon
            for (Entity entity : entities) {
                float playerDist = entity.getDistanceToEntity(player);
                if (entity.getDistanceToEntity(player) < minPlayerDist) {
                    closestEntity = entity;
                    minPlayerDist = playerDist;
                }
            }

            if (closestEntity == null) {
                throw new CommandException("commands.dragon.nodragons");
            } else {
                modifier.modify((EntityTameableDragon) closestEntity);
            }
        } else {
            // scan all entities on all dimensions
            MinecraftServer server = MinecraftServer.getServer();
            for (WorldServer worldServer : server.worldServers) {
                List<Entity> entities = worldServer.loadedEntityList;

                for (Entity entity : entities) {
                    if (!(entity instanceof EntityTameableDragon)) {
                        continue;
                    }

                    modifier.modify((EntityTameableDragon) entity);
                }
            }
        }
    }

    private interface EntityModifier {
        void modify(EntityTameableDragon dragon);
    }

    @Desugar
    private record LifeStageModifier(DragonLifeStage lifeStage) implements EntityModifier {
    @Override
        public void modify(EntityTameableDragon dragon) {
            if (lifeStage == null) {
                dragon.getLifeStageHelper().transformToEgg();
            } else {
                dragon.getLifeStageHelper().setLifeStage(lifeStage);
            }
        }
    }

    @Desugar
    private record BreedModifier(DragonBreed breed) implements EntityModifier {
    @Override
        public void modify(EntityTameableDragon dragon) {
            dragon.setBreed(breed);
        }
    }

    @Desugar
    private record TameModifier(EntityPlayerMP player) implements EntityModifier {
    @Override
        public void modify(EntityTameableDragon dragon) {
            dragon.tamedFor(player, true);
        }
    }

}
