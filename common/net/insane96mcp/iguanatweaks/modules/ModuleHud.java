package net.insane96mcp.iguanatweaks.modules;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import net.insane96mcp.iguanatweaks.IguanaTweaks;
import net.insane96mcp.iguanatweaks.capabilities.IPlayerData;
import net.insane96mcp.iguanatweaks.capabilities.PlayerDataProvider;
import net.insane96mcp.iguanatweaks.lib.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModuleHud {
	public static boolean HideHealthBar(ElementType type, EntityPlayer player) {
		if (!Properties.Global.hud)
			return false;
		
		if (type != ElementType.HEALTH)
			return false;
		
		if (!Properties.Hud.hideHealthBar)
			return false;
		
		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
		int totalTime = (int) player.world.getTotalWorldTime();
		
		if (Math.ceil(player.getHealth()) >= Properties.Hud.hideHealthBarThreshold && player.getAbsorptionAmount() == 0f)
		{
			int delay = totalTime - playerData.getHideHealthBarLastTimestamp();
			if (delay >= Properties.Hud.hideHealthBarDelay * 20)
				return true;
			else if (delay < 0)
				playerData.setHideHealthBarLastTimestamp(totalTime);
		}
		else
			playerData.setHideHealthBarLastTimestamp(totalTime);
		return false;
	}
	
	public static boolean HideHungerBar(ElementType type, EntityPlayer player) {
		if (!Properties.Global.hud)
			return false;
		
		if (type != ElementType.FOOD)
			return false;

		if (!Properties.Hud.hideHungerBar)
			return false;

		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
		int totalTime = (int) player.world.getTotalWorldTime();
		
		if (player.getFoodStats().getFoodLevel() >= Properties.Hud.hideHungerBarThreshold)
		{
			int delay = totalTime - playerData.getHideHungerBarLastTimestamp();
			if (delay >= Properties.Hud.hideHungerBarDelay * 20)
				return true;
			else if (delay < 0)
				playerData.setHideHungerBarLastTimestamp(totalTime);
		}
		else 
			playerData.setHideHungerBarLastTimestamp(totalTime);
		
		return false;
	}
	
	public static boolean HideExperienceBar(ElementType type, EntityPlayer player) {
		if (!Properties.Global.hud)
			return false;
		
		if (type != ElementType.EXPERIENCE)
			return false;

		if (!Properties.Hud.hideExperienceBar)
			return false;

		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
		int totalTime = (int) player.world.getTotalWorldTime();
		
		int delay = totalTime - playerData.getHideXpBarLastTimestamp();
		if (delay >= Properties.Hud.hideExperienceBarDelay * 20)
			return true;
		else if (delay < 0)
			playerData.setHideXpBarLastTimestamp(totalTime);
		
		return false;
	}
	
	/*public static boolean HideHotbar(ElementType type, EntityPlayer player) {
		if (type != ElementType.HOTBAR)
			return false;
		
		if (!Properties.Hud.hideHotbar)
			return false;
		
		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
		int totalTime = (int) player.world.getTotalWorldTime();
		
		int delay = totalTime - playerData.getHideHotbarLastTimestamp();
		if (delay >= Properties.Hud.hideHotbarDelay * 20)
			return true;
		else if (delay < 0)
			playerData.setHideHotbarLastTimestamp(totalTime);
		
		return false;
	}*/

	//private static Field KEYBIND_ARRAY = null;
	public static void HotbarCheckKeyPress(Phase phase) {
		/*if (Properties.Hud.hideHotbar)
			return;
		if (KEYBIND_ARRAY == null) {
			try {
				KEYBIND_ARRAY = KeyBinding.class.getDeclaredField("KEYBIND_ARRAY");
				KEYBIND_ARRAY.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (phase.equals(Phase.END)) {
			Map<String, KeyBinding> binds = null;
			try {
				binds = (Map<String, KeyBinding>) KEYBIND_ARRAY.get(null);
				for (String bind : binds.keySet()) {
					if(binds.get(bind).isKeyDown()){
						if (binds.get(bind).getKeyCode() >= 2 && binds.get(bind).getKeyCode() <= 9) {
							Minecraft mc = Minecraft.getMinecraft();
							if (mc.currentScreen == null)
							{
								EntityPlayerSP player = mc.player;
								IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
								playerData.setHideHotbarLastTimestamp((int) player.world.getTotalWorldTime());
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	
		}*/
	}

	public static void HotbarCheckMouse(int dwheel) {
		/*if (!Properties.Global.hud)
			return;
		
		if (dwheel != 0) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
			playerData.setHideHotbarLastTimestamp((int) player.world.getTotalWorldTime());
		}*/
	}

	public static void CheckExperienceNear(EntityLivingBase living) {
		if (!Properties.Global.hud)
			return;
		
		EntityPlayerSP player;
		
		if (!(living instanceof EntityPlayerSP))
			return;
		
		player = (EntityPlayerSP) living;

		BlockPos corner1 = player.getPosition().add(4, 4, 4);
		BlockPos corner2 = player.getPosition().add(-4, -4, -4);
		AxisAlignedBB axisAlignedBB = new AxisAlignedBB(corner1, corner2);
		List<EntityXPOrb> xpOrbs = player.world.getEntitiesWithinAABB(EntityXPOrb.class, axisAlignedBB);
		
		if (xpOrbs.isEmpty())
			return;
		
		int timestamp = (int) player.world.getTotalWorldTime();
		
		IPlayerData playerData = Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA_CAP, null);
		playerData.setHideXpBarLastTimestamp(timestamp);
	}
}
