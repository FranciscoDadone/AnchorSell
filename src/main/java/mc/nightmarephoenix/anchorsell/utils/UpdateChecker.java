package mc.nightmarephoenix.anchorsell.utils;

import mc.nightmarephoenix.anchorsell.storage.Global;
import org.bukkit.Bukkit;
import org.bukkit.util.Consumer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(Global.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                Global.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }

    private int resourceId;
    public static String updateString = "";

}
