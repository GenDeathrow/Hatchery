package com.gendeathrow.hatchery.api.tileentities;

import net.minecraft.entity.passive.EntityAnimal;

/**
 * Will Allow Placing custom chickens into Nesting Pen.<br>
 * Also allows you to control what the chickens do in pens <br><br>
 * <b>Note:</b> <i>If you extend from Vanilla Chickens, you don't need this.</i>
 *     
 * @author GenDeathrow
 * 
 */
public interface IChickenNestingPen 
{

	/**
	 * Should this chicken drop custom Hatchery Egg? <br>
	 * Hatchery Eggs normally contain an entity that will be hatched from Egg Nests.<br>
	 * 
	 * @return boolean
	 */
	public abstract boolean shouldDropHatcheryEgg();
	
	/**
	 *  <b>if ShouldDRopHatcheryEgg() == true</b> <br><br>
	 * Return child from breading two types of Chickens in nesting pens.<br>
	 * This entity will be placed in Hatchery egg to be ready for hatching in a egg nest.<br>
	 * @param parent1
	 * @param parent2
	 * @return EntityAnimal
	 */
	public abstract EntityAnimal getChild(EntityAnimal parent1, EntityAnimal parent2);

}
