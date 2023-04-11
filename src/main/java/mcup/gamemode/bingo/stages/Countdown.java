package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class Countdown extends GamemodeStage {

  @Override
  public void load() {
    super.load();
    initBossBarCountdown();
    core.apiManager.playerManager.setPlayersGamemode(GameMode.ADVENTURE);
    core.apiManager.playerManager.clearPlayersInventory();

    core.apiManager.playerManager.sendTitle(
      ChatColor.YELLOW + "" + getSecondsLeft(),
      (getSecondsLeft() % 10 == 0 ) ? "секунд до начала охоты" : "",
      5,
      10,
      5,
      Bukkit.getOnlinePlayers()
    );

    core.apiManager.playerManager.playSound(Sound.UI_BUTTON_CLICK, 1.0f, Bukkit.getOnlinePlayers());
  }

  @Override
  public void tickSecond() {
    super.tickSecond();

    if (getSecondsLeft() == 20 || getSecondsLeft() <= 10) {
      core.apiManager.playerManager.sendTitle(
        ChatColor.YELLOW + "" + getSecondsLeft(),
        (getSecondsLeft() % 10 == 0 ) ? "секунд до начала охоты" : "",
        5,
        10,
        5,
        Bukkit.getOnlinePlayers()
      );

      core.apiManager.playerManager.playSound(Sound.UI_BUTTON_CLICK, 1.0f, Bukkit.getOnlinePlayers());
    }

  }

  protected Bingo plugin;

  public Countdown(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
    timeLimit = 30 * 20;
    bossBarCountdownLabelPrefix = "Подготовка: ";
  }
}
