package landmaster.landcore.item;

import java.util.*;
import java.util.stream.*;

import javax.annotation.*;

import landmaster.landcore.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.stats.*;
import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import net.minecraft.enchantment.*;
import net.minecraft.world.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fml.relauncher.*;

public class ItemModBow extends ItemBow {
	private float damage;
	private float arrowSpeed;
	private int enchantability;
	
	public ItemModBow(float damage, int durability, float arrowSpeed, int enchantability, String name) {
		setMaxDamage(durability);
		this.damage = damage;
		this.arrowSpeed = arrowSpeed;
		this.enchantability = enchantability;
		maxStackSize = 1;
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                if (entityIn == null) return 0.0F;
                ItemStack itemstack = entityIn.getActiveItemStack();
                return itemstack != null && itemstack.stackSize > 0 && itemstack.getItem() == ItemModBow.this ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 5.0F : 0.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
        this.setUnlocalizedName(name).setRegistryName(name);
        this.setCreativeTab(LandCore.creativeTab);
	}
	
	@Override
	public int getItemEnchantability() {
		return enchantability;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("tooltip.landcore.bow.bonus", damage));
    }
	
	@Override
    @Nonnull
    public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}
	
	@Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return true;
	}
	
    public ItemStack findAmmo(EntityPlayer player) {
        return this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)) ?
        		player.getHeldItem(EnumHand.OFF_HAND) :
        			this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)) ?
        					player.getHeldItem(EnumHand.MAIN_HAND) :
        						IntStream.range(0, player.inventory.getSizeInventory())
        						.mapToObj(i -> player.inventory.getStackInSlot(i))
        						.filter(this::isArrow).findFirst().orElse(null);
    }
    
    public float getVelocityOfArrow(ItemStack stack) {
    	return arrowSpeed;
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            boolean requiredConditions = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(player);

            int useDuration = this.getMaxItemUseDuration(stack) - timeLeft;
            useDuration = ForgeEventFactory.onArrowLoose(stack, world, (EntityPlayer) entityLiving, useDuration, (itemstack != null && itemstack.stackSize > 0) || requiredConditions);
            if (useDuration < 0)
                return;

            if ((itemstack != null && itemstack.stackSize > 0) || requiredConditions) {
                if (itemstack == null || itemstack.stackSize <= 0) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float arrowVelocity = getArrowVelocity(useDuration);

                if ((double) arrowVelocity >= 0.1D) {
                    boolean secondaryConditions = requiredConditions && itemstack.getItem() == Items.ARROW;

                    if (!world.isRemote) {
                        ItemArrow itemarrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW));
                        EntityArrow entityArrow = itemarrow.createArrow(world, itemstack, player);

                        float newArrowVelocity = arrowVelocity * getVelocityOfArrow(stack);
                        entityArrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, newArrowVelocity, 1.0F);

                        if (newArrowVelocity == 0) {
                            world.playSound(null, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.4F, 1.0F);
                            return;
                        }

                        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        entityArrow.setDamage(entityArrow.getDamage() + damage + (powerLevel > 0 ? powerLevel * 0.5 + 0.5 : 0));

                        int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (punchLevel > 0) {
                            entityArrow.setKnockbackStrength(punchLevel);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityArrow.setFire(100);
                        }

                        stack.damageItem(1, player);

                        if (secondaryConditions) {
                            entityArrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        world.spawnEntityInWorld(entityArrow);
                    }

                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + arrowVelocity * 0.5F);

                    if (!secondaryConditions) {
                        --itemstack.stackSize;

                        if (itemstack == null || itemstack.stackSize <= 0) {
                            player.inventory.deleteStack(itemstack);
                        }
                    }

                    player.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }
}
