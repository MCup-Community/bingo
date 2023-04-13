package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.local.data.Player;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.GameMode;
import org.bukkit.plugin.java.JavaPlugin;

public class Countdown extends mcup.core.stages.Countdown {

  @Override
  public void load() {
    super.load();
    initBossBarCountdown();
    core.apiManager.playerManager.setPlayersGamemode(GameMode.ADVENTURE);
    core.apiManager.playerManager.clearPlayersInventory();
    core.apiManager.playerManager.clearPlayersEffects();

    getSpawnLocations();
    spawnPlayers();

    for (Player player : core.apiManager.playerManager.getPlayers())
      plugin.storage.giveEffects(player.nickname, timeLimit + 10 * 20);
  }

  @Override
  protected boolean updateSkipCondition(int secondsRemaining) {
    if (secondsRemaining == 30 || secondsRemaining == 15 || secondsRemaining <= 10)
      return false;

    return true;
  }

  protected Bingo plugin;

  public Countdown(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
    timeLimit = 30 * 20;
    bossBarCountdownLabelPrefix = "Подготовка: ";
  }
}
