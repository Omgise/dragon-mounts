package info.ata4.minecraft.dragon.server.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class EntityAITemptOreDictionary extends EntityAIBase {

    /** The entity using this AI that is tempted by the player. */
    private EntityCreature temptedEntity;
    private double entitySpeed;
    /** X position of player tempting this mob */
    private double targetX;
    /** Y position of player tempting this mob */
    private double targetY;
    /** Z position of player tempting this mob */
    private double targetZ;
    private double targetPitch;
    private double targetYaw;
    /** The player that is tempting the entity that is using this AI. */
    private EntityPlayer temptingPlayer;
    /**
     * A counter that is decremented each time the shouldExecute method is called. The shouldExecute method will always
     * return false if delayTemptCounter is greater than 0.
     */
    private int delayTemptCounter;
    /** True if this EntityAITempt task is running */
    private boolean isRunning;
    private String temptItemOreDictionaryTag;
    /** Whether the entity using this AI will be scared by the tempter's sudden movement. */
    private boolean scaredByPlayerMovement;
    private boolean avoidsWater;

    public EntityAITemptOreDictionary(EntityCreature temptedEntity, double entitySpeed, String temptItemOreDictionaryTag, boolean scaredByPlayerMovement) {
        this.temptedEntity = temptedEntity;
        this.entitySpeed = entitySpeed;
        this.temptItemOreDictionaryTag = temptItemOreDictionaryTag;
        this.scaredByPlayerMovement = scaredByPlayerMovement;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.delayTemptCounter > 0) {
            --this.delayTemptCounter;
            return false;
        } else {
            this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);

            if (this.temptingPlayer == null) {
                return false;
            } else {
                ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
                if(itemstack != null) {
                	itemstack = new ItemStack(itemstack.getItem(), 1);
                	return OreDictionary.getOres(this.temptItemOreDictionaryTag).contains(itemstack);
                }
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        if (this.scaredByPlayerMovement) {
            if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D) {
                if (this.temptingPlayer.getDistanceSq(this.targetX, this.targetY, this.targetZ) > 0.010000000000000002D) {
                    return false;
                }

                if (Math.abs((double)this.temptingPlayer.rotationPitch - this.targetPitch) > 5.0D || Math.abs((double)this.temptingPlayer.rotationYaw - this.targetYaw) > 5.0D) {
                    return false;
                }
            } else {
                this.targetX = this.temptingPlayer.posX;
                this.targetY = this.temptingPlayer.posY;
                this.targetZ = this.temptingPlayer.posZ;
            }

            this.targetPitch = this.temptingPlayer.rotationPitch;
            this.targetYaw = this.temptingPlayer.rotationYaw;
        }

        return this.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.targetX = this.temptingPlayer.posX;
        this.targetY = this.temptingPlayer.posY;
        this.targetZ = this.temptingPlayer.posZ;
        this.isRunning = true;
        this.avoidsWater = this.temptedEntity.getNavigator().getAvoidsWater();
        this.temptedEntity.getNavigator().setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.temptingPlayer = null;
        this.temptedEntity.getNavigator().clearPathEntity();
        this.delayTemptCounter = 100;
        this.isRunning = false;
        this.temptedEntity.getNavigator().setAvoidsWater(this.avoidsWater);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float)this.temptedEntity.getVerticalFaceSpeed());

        if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D) {
            this.temptedEntity.getNavigator().clearPathEntity();
        } else {
            this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.entitySpeed);
        }
    }

    /**
     * @see #isRunning
     */
    public boolean isRunning() {
        return this.isRunning;
    }

}
