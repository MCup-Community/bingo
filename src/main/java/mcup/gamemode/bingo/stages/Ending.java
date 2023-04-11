package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class Ending extends GamemodeStage {

  @Override
  public void load() {
    super.load();

    plugin.core.apiManager.playerManager.setPlayersGamemode(GameMode.SPECTATOR);

    plugin.core.apiManager.playerManager.sendTitle(
      ChatColor.GOLD + "Охота окончена!",
      "Вы заработали %core_team_game_score% очков",
      5,
      70,
      30,
      Bukkit.getOnlinePlayers()
    );

    plugin.core.apiManager.playerManager.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, Bukkit.getOnlinePlayers());

    plugin.core.apiManager.teamManager.writeRepository();
  }

  protected Bingo plugin;
  public Ending(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
  }
}
