package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.local.data.Player;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;


public class Hunt extends GamemodeStage {

  @Override
  public void load() {
    super.load();

    initBossBarCountdown();

    core.apiManager.playerManager.setPlayersGamemode(GameMode.SURVIVAL);

    plugin.storage.initCollectedItems();
    plugin.storage.readBingoItems();

    for (Player player : core.apiManager.playerManager.getPlayers())
      plugin.storage.giveDefaultEquipment(player.nickname, timeLimit);

    core.apiManager.playerManager.sendTitle(
      ChatColor.GOLD + "Охота началась!",
      "Станьте первой командой, собравшей бинго!",
      5,
      60,
      10,
      Bukkit.getOnlinePlayers()
    );

    core.apiManager.playerManager.playSound(Sound.BLOCK_BELL_RESONATE, 1.0f, Bukkit.getOnlinePlayers());
  }

  @Override
  public void tickSecond() {
    super.tickSecond();

    if (getSecondsLeft() == 60 || getSecondsLeft() == 10) {
      core.apiManager.playerManager.sendTitle(
        ChatColor.YELLOW + "" + getSecondsLeft(),
        "секунд до конца охоты!",
        5,
        30,
        10,
        Bukkit.getOnlinePlayers()
      );

      core.apiManager.playerManager.playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, Bukkit.getOnlinePlayers());
    }

    if (getSecondsLeft() == 5 * 60) {
      core.apiManager.playerManager.sendTitle(
        ChatColor.YELLOW + "" + getSecondsLeft() / 60,
        "минут до конца охоты!",
        5,
        30,
        10,
        Bukkit.getOnlinePlayers()
      );

      core.apiManager.playerManager.playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, Bukkit.getOnlinePlayers());
    }

    if (getSecondsLeft() < 10)
      core.apiManager.playerManager.playSound(Sound.UI_BUTTON_CLICK, 1.0f, Bukkit.getOnlinePlayers());
  }

  // Get item from Chest - InventoryInteract
  // Craft item - InventoryInteract
  // Pickup item - PlayerPickup



  protected Bingo plugin;
  public Hunt(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
    timeLimit = 10 * 60 * 20;
    bossBarCountdownLabelPrefix = "До конца охоты: ";
  }
}

