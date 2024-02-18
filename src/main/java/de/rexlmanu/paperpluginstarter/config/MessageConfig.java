package de.rexlmanu.paperpluginstarter.config;

import de.exlll.configlib.Configuration;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("FieldMayBeFinal")
@Configuration
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageConfig {
  String prefix =
      "<gray><dark_gray>»</dark_gray> <gradient:#BE95C4:#9F86C0:#5E548E>Starter</gradient><dark_gray>∙ </dark_gray>";
  String pluginReloaded = "<prefix>Plugin was <green>successfully</green> reloaded.";
  String itemReceived = "<prefix>You received a item.";

  Map<String, String> cloudCommandMessages =
      new HashMap<>() {
        {
          // Cloud translations
          this.put("argument.parse.failure.boolean", "Could not parse boolean from '<input>'");
          this.put(
              "argument.parse.failure.number",
              "'<input>' is not a valid number in the range <min> to <max>");
          this.put("argument.parse.failure.char", "'<input>' is not a valid character");
          this.put(
              "argument.parse.failure.string",
              "'<input>' is not one of the following: <acceptableValues>");
          this.put(
              "argument.parse.failure.uuid",
              "'<input>' is not a valid string of type <stringMode>");
          this.put("argument.parse.failure.enum", "'<input>' is not a valid UUID");
          this.put("argument.parse.failure.regex", "'<input>' does not match '<pattern>'");
          this.put("argument.parse.failure.flag.unknown", "Unknown flag '<flag>'");
          this.put("argument.parse.failure.flag.duplicate_flag", "Duplicate flag '<flag>'");
          this.put(
              "argument.parse.failure.flag.no_flag_started",
              "No flag started. Don't know what to do with '<input>'");
          this.put("argument.parse.failure.flag.missing_argument", "Missing argument for '<flag>'");
          this.put(
              "argument.parse.failure.flag.no_permission",
              "You don't have permission to use '<flag>'");
          this.put("argument.parse.failure.color", "'<input>' is not a valid color");
          this.put("argument.parse.failure.duration", "'<input>' is not a duration format");
          this.put("argument.parse.failure.aggregate.missing", "Missing component '<component>'");
          this.put(
              "argument.parse.failure.aggregate.failure",
              "Invalid component '<component>': <failure>");
          this.put(
              "argument.parse.failure.either",
              "Could not resolve <primary> or <fallback> from '<input>'");
          this.put(
              "exception.unexpected",
              "An internal error occurred while attempting to perform this command.");
          this.put("exception.invalid_argument", "Invalid Command Argument: <cause>.");
          this.put("exception.no_such_command", "Unknown Command.");
          this.put(
              "exception.no_permission",
              "I'm sorry, but you do not have permission to perform this command.");
          this.put(
              "exception.invalid_sender",
              "<actual> is not allowed to execute that command. Must be of type <expected>.");
          this.put(
              "exception.invalid_syntax",
              "Invalid Command Syntax. Correct command syntax is: <syntax>.");

          // Minecraft translations
          this.put("argument.parse.failure.enchantment", "'<input>' is not a valid enchantment");
          this.put("argument.parse.failure.material", "'<input>' is not a valid material name");
          this.put("argument.parse.failure.offlineplayer", "No player found for input '<input>'");
          this.put("argument.parse.failure.player", "No player found for input '<input>'");
          this.put("argument.parse.failure.world", "'<input>' is not a valid Minecraft world");
          this.put(
              "argument.parse.failure.selector.unsupported",
              "Entity selector argument type not supported below Minecraft 1.13.");
          this.put(
              "argument.parse.failure.location.invalid_format",
              "'<input>' is not a valid location. Required format is '<x> <y> <z>'");
          this.put(
              "argument.parse.failure.location.mixed_local_absolute",
              "Cannot mix local and absolute coordinates. (either all coordinates use '^' or none do)");
          this.put(
              "argument.parse.failure.namespacedkey.namespace",
              "Invalid namespace '<input>'. Must be [a-z0-9._-]");
          this.put(
              "argument.parse.failure.namespacedkey.key",
              "Invalid key '<input>'. Must be [a-z0-9/._-]");
          this.put(
              "argument.parse.failure.namespacedkey.need_namespace",
              "Invalid input '<input>', requires an explicit namespace.");
          this.put("argument.parse.failure.warp", "The warp <aqua><input></aqua> was not found");
        }
      };
}
