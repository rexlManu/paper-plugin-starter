package de.rexlmanu.boilerplate.scheduler;

import org.bukkit.plugin.Plugin;

public interface BaseScheduledTask {
  /** Cancels executing task */
  void cancel();

  /**
   * @return true if task is cancelled, false otherwise
   */
  boolean isCancelled();

  /**
   * @return The plugin under which the task was scheduled.
   */
  Plugin getOwningPlugin();

  /**
   * @return true if task is currently executing, false otherwise
   */
  boolean isCurrentlyRunning();

  /**
   * @return true if task is repeating, false otherwise
   */
  boolean isRepeatingTask();
}
