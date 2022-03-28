package chillz.Report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chillz.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ReportInventory implements Listener {

    String player, target;
    Inventory reportInventory;

    public ReportInventory(String player, String target) {
        this.player = player;
        this.target = target;

        reportInventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&b&lReport"));
        ItemStack bluePane = createItem(Material.BLUE_STAINED_GLASS, "&b&lReport &f&lGUI", Arrays.asList(" "), (byte) 14);
        for (int i = 9; i < 18; i++) {
            reportInventory.setItem(i, bluePane);
        }
        reportInventory.setItem(24, createItem(Material.WHITE_WOOL, "&4Exit", Arrays.asList("&cExit the REPORT menu!"), (byte) 14));


        reportInventory.setItem(0, createItem(Material.DIAMOND_SWORD, "&4Kill Aura", Arrays.asList("&cReport: " + target)));

        reportInventory.setItem(1, createItem(Material.FEATHER, "&4Fly", Arrays.asList("&eReport: " + target)));

        reportInventory.setItem(2, createItem(Material.SUGAR, "&4Speed", Arrays.asList("&aReport: " + target)));

        reportInventory.setItem(3, createItem(Material.DIAMOND_CHESTPLATE, "&4AntiKB", Arrays.asList("&dReport: " + target)));

        reportInventory.setItem(4, createItem(Material.DIAMOND_ORE, "&4XRay", Arrays.asList("&cReport: " + target)));

        reportInventory.setItem(5, createItem(Material.SOUL_SAND, "&4No Slowdown", Arrays.asList("&4Report: " + target)));

        reportInventory.setItem(6, createItem(Material.WATER_BUCKET, "&4Jesus (Water Walking)", Arrays.asList("&bReport: " + target)));

        reportInventory.setItem(7, createItem(Material.ENDER_PEARL, "&4Blink", Arrays.asList("&5Report: " + target)));

        reportInventory.setItem(8, createItem(Material.PAPER, "&4Other Blacklisted Modifications", Arrays.asList("&7Report: " + target)));

        reportInventory.setItem(20, skull(target));
        Main.registerEvent(this);
        openInventory();
    }

    private ItemStack createItem(Material material, String name, List<String> lore, byte data) {
        ItemStack itemStack = new ItemStack(material, 1, data);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> tempLore = new ArrayList<>();
        lore.forEach(s -> tempLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(tempLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> tempLore = new ArrayList<>();
        lore.forEach(s -> tempLore.add(ChatColor.translateAlternateColorCodes('&', s)));
        itemMeta.setLore(tempLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack skull(String nick) {
        ItemStack item = new ItemStack(Material.SKELETON_SKULL, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Target");
        meta.setOwner(nick);
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Player: " + nick));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory() {
        Bukkit.getPlayer(player).openInventory(reportInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory().equals(reportInventory)) {
            event.setCancelled(true);
            ClickType type = event.getClick();
            if (type.equals(ClickType.LEFT)) {
                ItemStack item = event.getCurrentItem();
                if (item != null) {
                    if (item.getType().equals(Material.SKELETON_SKULL)) return;
                    if (!item.getType().equals(Material.WHITE_WOOL)) {
                        String reason = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                        Main.createReport(player, target, reason);
                    }
                    InventoryClickEvent.getHandlerList().unregister(this);
                    event.getWhoClicked().closeInventory();
                }
            }
        }
    }
}