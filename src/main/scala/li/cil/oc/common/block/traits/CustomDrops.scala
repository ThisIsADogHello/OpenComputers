package li.cil.oc.common.block.traits

import java.util

import li.cil.oc.common.block.SimpleBlock
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

import scala.reflect.ClassTag

trait CustomDrops[Tile <: TileEntity] extends SimpleBlock {
  protected def tileTag: ClassTag[Tile]

  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): util.List[ItemStack] = new java.util.ArrayList[ItemStack]()

  override def breakBlock(world: World, pos: BlockPos, state: IBlockState): Unit = {}


  override def removedByPlayer(world: World, pos: BlockPos, player: EntityPlayer, willHarvest: Boolean): Boolean = {
    if (!world.isRemote) {
      val matcher = tileTag
      world.getTileEntity(pos) match {
        case matcher(tileEntity) => doCustomDrops(tileEntity, player, willHarvest)
        case _ =>
      }
    }
    super.removedByPlayer(world, pos, player, willHarvest)
  }

  override def onBlockPlacedBy(world: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack): Unit = {
    super.onBlockPlacedBy(world, pos, state, placer, stack)
    if (!world.isRemote) {
      val matcher = tileTag
      world.getTileEntity(pos) match {
        case matcher(tileEntity) => doCustomInit(tileEntity, placer, stack)
        case _ =>
      }
    }
  }

  protected def doCustomInit(tileEntity: Tile, player: EntityLivingBase, stack: ItemStack): Unit = {}

  protected def doCustomDrops(tileEntity: Tile, player: EntityPlayer, willHarvest: Boolean): Unit = {}
}