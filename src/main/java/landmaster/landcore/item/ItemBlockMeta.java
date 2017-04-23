package landmaster.landcore.item;

import landmaster.landcore.block.*;
import mcjty.lib.compat.*;
import net.minecraft.block.*;
import net.minecraft.item.*;

public class ItemBlockMeta extends CompatItemBlock {
	public ItemBlockMeta(Block block) {
		super(block);
		if (!(block instanceof IMetaBlockName)) {
            throw new IllegalArgumentException(String.format("The given Block %s is not an instance of ISpecialBlockName!", block.getUnlocalizedName()));
        }
        setMaxDamage(0);
        setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + ((IMetaBlockName)block).getSpecialName(stack);
    }
}
