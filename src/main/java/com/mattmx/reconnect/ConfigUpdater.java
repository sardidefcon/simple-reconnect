package com.mattmx.reconnect;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Merges the plugin's default config (from {@link ReconnectConfig}) with the
 * existing config.yml on disk. New or renamed keys from the default are added;
 * existing user values are never overwritten. Only writes to disk when
 * merge adds missing keys, then reloads the in-memory config.
 * <p>
 * Same semantic as the Bukkit/SimpleBroadcast ConfigUpdater: add missing keys
 * only, preserve user values. Uses Configurate's {@link ConfigurationNode#mergeFrom(ConfigurationNode)}
 * which sets values from the given node only where not present in this node.
 * Safe to call on every load and reload.
 */
public final class ConfigUpdater {

    private ConfigUpdater() {
    }

    /**
     * Loads the current config from disk, merges it with the default config
     * (adding only missing keys), saves if modified, and reloads. Existing user
     * values are never changed.
     *
     * @param plugin the plugin (must expose {@link ReconnectVelocity#getLoader()} and config path)
     * @return true if config was updated (merge added keys and file was written)
     */
    public static boolean mergeWithDefaults(ReconnectVelocity plugin) {
        Path configPath = plugin.getDataDirectory().resolve("config.yml");
        if (!Files.exists(configPath)) {
            return false;
        }

        YamlConfigurationLoader loader = plugin.getLoader();
        ConfigurationNode defaultNode;
        try {
            defaultNode = BasicConfigurationNode.root(loader.defaultOptions());
            defaultNode.set(ReconnectConfig.class, new ReconnectConfig());
        } catch (Exception e) {
            plugin.getLogger().warn("Could not build default config; skipping merge.", e);
            return false;
        }

        ConfigurationNode current;
        try {
            current = loader.load();
        } catch (Exception e) {
            plugin.getLogger().warn("Could not load config for merge; skipping.", e);
            return false;
        }

        current.mergeFrom(defaultNode);

        try {
            loader.save(current);
            plugin.reloadConfig();
            return true;
        } catch (IOException e) {
            plugin.getLogger().error("Failed to save merged config", e);
            return false;
        }
    }
}
