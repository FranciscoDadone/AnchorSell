# AnchorSell - Spigot plugin
![](https://s10.gifyu.com/images/untitled659030898b59acbd.gif)


## Characteristics:
 + Can be removed with TNT and keep their characteristics.
 + Upgradeable
 + Particles
 + Permissions
 + WorldGuard hook
 + Spanish translation
 + Anchor Top
 + Quick support
 + Fully configurable
 + And much more!
 

## Spigot link:
+ [AnchorSell](https://www.spigotmc.org/resources/anchorsell.90038/)

## Dependencies
+ [Vault](https://www.spigotmc.org/resources/vault.34315/)

## Soft-dependencies (hooks)
+ [WorldGuard](https://dev.bukkit.org/projects/worldguard)
+ [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
+ [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays)


## Commands and permissions
### Player (anchorsell.player.*)
- /anchor (anchorsell.player.help)
- /anchor buy (anchorsell.player.help)
- /anchor list (anchorsell.player.list)
- /anchor top (anchorsell.player.top)

### Admin (anchorsell.admin.*)
- /anchor (anchorsell.admin.help)
- /anchor reload (anchorsell.admin.reload)
- /anchor give [player] [quantity] [level] (anchorsell.admin.give)
- /anchor upgrades (anchorsell.admin.upgrades)
- /anchor list [playername] (anchorsell.admin.list)
- /anchor changeUpgradeMultiplier [multiplier] (anchorsell.admin.changeUpgradeMultiplier)
- /anchor changeSafeZone [safeZone] (anchorsell.admin.changeSafeZone)
- /anchor getUserFileName [username] (anchorsell.admin.getUserFileName)
- /anchor revalidate [username] (anchorsell.admin.revalidate)
- /anchor createHologramTop (anchorsell.admin.createholo)
- /anchor particles [all/low/off] (anchorsell.admin.particles)
- Access others anchors (anchorsell.admin.anchoradmin)

## Placeholders (PlaceholderAPI support)
- %anchorsell_playerlevel% (Sum of all levels of all anchors from player)
- %anchorsell_playeranchors% (total player anchors)
- %anchorsell_playermoneyperminute% (money that the player generates in a minute)
- %anchorsell_anchorprice% (Anchor price)
- %anchorsell_top[1...100]% (Anchor TOP 1 to 100) (ie. %anchorsell_top1%)
- %anchorsell_top[1...100]-points% (Anchor TOP 1...100 points) (ie. %anchorsell_top1-points%) (Sum of all levels of all anchors from player)


## Config file

```yaml
version: 0.3.2

##
# AnchorSell plugin
# Authors: DadoGamer13, MatiasME
# Description: Minecraft plugin to get money using a Respawn anchor.
# GitHub: https://github.com/FranciscoDadone/AnchorSell.git
# Discord: Ddd#7413
##


reload-message: "&aConfig reloaded."
help-message:
  - "&7&m----------&r &5&lAnchor &7&m----------"
  - "&e/anchor buy &fTo buy an anchor"
  - "&e/anchor list &fGives a list of the placed anchors in the world"
  - "&e/anchor top [number] &fReturns the top."
  - ""
  - "&e/anchor authors"
  - "&7&m----------------------------"

help-message-admin:
  - "&7&m----------&r &5&lAnchor &7&m----------"
  - "&e/anchor reload &f Reloads the config file."
  - "&e/anchor give [name] [quantity] [level] &fGives an anchor to a player."
  - "&e/anchor list [username] &fGives a list of the placed anchors in the world."
  - "&e/anchor top [number] &fReturns the top."
  - "&e/anchor upgrades &fShows the cost in money of the anchor progression."
  - "&e/anchor changeUpgradeMultiplier [multiplier] &fChanges the upgrade multiplier. This affects the price of the anchor upgrade."
  - "&e/anchor changePrice [price] &fChanges the anchor price."
  - "&e/anchor changeSafeZone [zone] &fChanges the minimum radius to place an anchor."
  - "&e/anchor changeTotalAnchorsUserCanHave [number] &fChanges the total anchors one user can have."
  - "&e/anchor getUserFileName [username] &fShows the archive where the user information is stored."
  - "&e/anchor revalidate [username] &fRevalidates the anchors of that player."
  - "&e/anchor particles [all/low/off] &fChanges the particles on all anchors."
  - "&e/anchor createHologramTop &fCreates a hologram of the Top."
  - "&e/anchor version &fPlugin version"
  - ""
  - "&e/anchor authors"
  - "&7&m----------------------------"

unknown-command: "&5&lAnchor &7- &fUnknown command."
no-permissions: "&cYou don't have permission. (%permissionNode%)"

cant-give-anchor-message: "&fUse: &e/anchor give [name] [quantity] [level]"
total-anchors-user-can-have: 10

# Minimum distance to place an anchor next to another.
safe-anchor-area: 3

# If true anchors will pay if their chunk is loaded.
pay-if-chunk-is-loaded: false

# If false anchors will not pay afk players.
pay-afk-players: true

# If true, players can break others players anchors.
break-others: true
break-others-message: "&cYou cannot break %player%&c's anchor!"

anchor-value: 100000
radius-error: "&4You can't place anchors too close to each other."
cannot-place-more-anchors: "&4You can not have more than %quantity% anchors."
you-dont-own-this-anchor: "&4You don't own this anchor"

enable-in-worlds:
  - world
  - world1

world-not-enabled-error: "&cSorry, anchors can't be placed in this world :("

# Time period in which anchors will pay to players.
pay-timer-in-minutes: 15

anchor:
  title: "&5Anchor - level %level%"
  current-anchor-info:
    txt: "&6&lThis anchor"
    lore:
      - "&eLevel: &f%level% (%oreLevel%&f)"
      - "&eMoney per 15 minutes: &f$%moneyPer15Minutes%"
      - "&eMoney per minute: &f$%moneyPerMinute%"
  player:
    lore:
      - "&eBalance: &f$%playerBalance%"
      - "&eNumber of anchors:&f %playerAnchors% / %maxPlayerAnchors%"
      - "&eTotal money per 15 minutes: &f$%playerMoneyPer15Minutes%"
      - "&eNext Anchor pay: &f%timer%m"
  upgrades:
    txt: "&6&lUpgrades"
    lore:
      - "&eFrom:&f %level% (%oreLevel%&f)"
      - "&eTo: &f%nextLevel% (%nextLevelOre%&f)"
      - "&ePrice: &f$%priceOfUpgrade%"
  upgrade-menu:
    title: "&5Anchor Sell - Upgrades"
    current-level:
      txt: "%currentLevel%"
      lore:
        - "&7&m----------------------------"
        - "&ePrice: &r$%priceOfUpgrade%"
        - "&7&m----------------------------"
    upgrade-button:
      txt: "&6&lUpgrade"
      lore: "Upgrade current anchor"
    back: "&cGo back"
    upgrade-success:
      - "&7&m----------&r &5&lAnchor &7&m----------"
      - "&aSuccessfully upgraded the anchor"
      - "%previusLevel% -> %currentLevel%"
      - "&7&m----------------------------"
    upgrade-fail:
      - "&7&m----------&r &5&lAnchor &7&m----------"
      - "&cYou don't have money to upgrade :("
      - "&7&m----------------------------"

  # Explosion radius break: if an explosion occurs near an anchor, it will break it in that radius
  # to disable it set to "0", to set as default minecraft, change to "mc-default"
  explosion-radius-break: "3"

  # Upgrade multiplier: is how much the upgrade will cost depending on the money that the anchor
  # gives per hour in the next level.
  # Example: "upgrade-multiplier: 8".
  # Next level reward per hour: 10.
  # The cost of the upgrade will be 80.
  upgrade-multiplier: 6

  # Pay modifier: this modifies how much money the anchor will give to a player when it pays.
  # The formula to pay a player is:
  # f(anchorLevel, pay-modifier) = (0.1 * anchorLevel + anchorLevel^0.8) * 60 * pay-modifier
  # Excel sheet to see the levels and the money that it gives:
  #   https://docs.google.com/spreadsheets/d/1_bIag4v8MySS50nN3C6WwwLDBdjPWN3zo-AoybdyNVc/edit?usp=sharing
  # In the Excel you can modify the "Pay modifier" and "Pay timer" cells only and view the results.
  # Note: pay-modifier accepts decimals too. ie: 0.2
  pay-modifier: 1

  list:
    first-message: "&7&m----------&r &5&lAnchors &7&m----------"
    last-message: "&7&m-----------------------------"
    message:
      - ""
      - "&eLocation:&f %location%"
      - "&eLevel: %level%"
      - ""
  cantaccess: "&4You need direct contact with the anchor to use it."

anchorbuy:
  title: "&5Buy Anchor Sell"
  anchor-info:
    txt: "What is an Anchor Sell?"
    lore:
      - "&eAn Anchor Sell is a block with which"
      - "&eyou can earn money automatically, from"
      - "&etime to time, by placing it."
  buy:
    title: "&5&lBuy an anchor"
    lore: "&ePrice: &f$%price%"

confirmscreen:
  title: "&5Are you sure you want to buy?"
  you-have-an-anchor: "&5You have purchased an anchor."
  you-cant-afford: "&5You can't afford this."
  cancel: "&4&lCancel"
  confirm: "&a&lConfirm"

anchor-place:
  - "&7&m----------&r &5&lAnchor &7&m----------"
  - "&aNew anchor placed"
  - ""
  - "&eLevel: &f%level%"
  - "&7&m----------------------------"

anchor-break:
  - "&7&m----------&r &5&lAnchor &7&m----------"
  - "&cAnchor removed"
  - ""
  - "&eLevel: &f%level%"
  - "&7&m----------------------------"

paying-message: "&aYou have received &c$%amount% &afrom &5&lAnchors"

levels:
  1: "&8Coal"
  2: "&7Iron"
  3: "&eGold"
  4: "&bDiamond"
  5: "&6&lNETHERITE"
  maxed-out-level: "&c&lMaxed out!"

# Particles that anchors generate.
# Options: [ all / low / off ]
particles: "all"
```
You can find a SPANISH translated config file in languages folder.




## Discord
 + http://bit.ly/nightmarediscord
 + Contact me directly: Ddd#7413

## Authors
+ [Francisco Dadone](https://github.com/FranciscoDadone)
+ [MatiasME](https://github.com/MatiasvME)
