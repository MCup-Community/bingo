package mcup.gamemode.bingo.stages;

import mcup.core.Core;
import mcup.core.local.data.Team;
import mcup.core.stages.GamemodeStage;
import mcup.gamemode.bingo.Bingo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Ending extends GamemodeStage {

  @Override
  public void load() {
    super.load();

    plugin.core.apiManager.playerManager.setPlayersGamemode(GameMode.SPECTATOR);

    for (Player player : Bukkit.getOnlinePlayers()) {

      Team playerTeam = core.apiManager.teamManager.getTeamByPlayer(player.getName());

      player.sendTitle(
        ChatColor.GOLD + "Охота окончена!",
        (playerTeam == null) ? "" : "Вы заработали " + core.apiManager.scoreManager.getTeamDeltaScore(playerTeam.name) + " очков",
        5,
        70,
        30
      );
    }

    plugin.core.apiManager.playerManager.playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, Bukkit.getOnlinePlayers());

    plugin.core.apiManager.teamManager.writeRepository();
  }

  protected Bingo plugin;
  public Ending(Core core_, JavaPlugin plugin_) {
    super(core_, plugin_);
    plugin = (Bingo)plugin_;
  }
}
