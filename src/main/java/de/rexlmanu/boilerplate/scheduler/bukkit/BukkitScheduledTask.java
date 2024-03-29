package de.rexlmanu.boilerplate.scheduler.bukkit;

import de.rexlmanu.boilerplate.scheduler.BaseScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitScheduledTask implements BaseScheduledTask {

  BukkitTask task;

  boolean isRepeating;

  public BukkitScheduledTask(final BukkitTask task) {
    this.task = task;
    this.isRepeating = false;
  }

  public BukkitScheduledTask(final BukkitTask task, boolean isRepeating) {
    this.task = task;
    this.isRepeating = isRepeating;
  }

  @Override
  public void cancel() {
    task.cancel();
  }

  @Override
  public boolean isCancelled() {
    return task.isCancelled();
  }

  @Override
  public Plugin getOwningPlugin() {
    return task.getOwner();
  }

  @Override
  public boolean isCurrentlyRunning() {
    return Bukkit.getServer()
        .getScheduler()
        .isCurrentlyRunning(this.task.getTaskId()); // There's no other way. Fuck bukkit
  }

  @Override
  public boolean isRepeatingTask() {
    return isRepeating;
  }
}
