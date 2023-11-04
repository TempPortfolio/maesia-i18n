# I18n Plugin
Ce programme est un plugin Bukkit qui sert à mettre en oeuvre l'internationalisation en proposant une API simple. Ce plugin est utilisé dans les autres plugins du projet Maesia.
## Bukkit
Bukkit est une API permettant d'interagir avec le logiciel serveur du jeu Minecraft. L'implémentation de Bukkit est CraftBukkit. Nous utilisons techniquement Spigot qui est un fork compatible avec Bukkit.
## Maesia
Maesia est un projet de serveur Minecraft, pour lequel j'ai développé I18nPlugin.
## I18n
Concrètement, nous voulons envoyer des messages aux joueurs sans nous soucier de leur langue. Lorsqu'il faut envoyer un message à un joueur, on demande à I18nPlugin de nous fournir un message pour le joueur et la clé spécifiée. Il s'occupe aussi de placer les arguments et du formatage des temps et de l'économie.
## Usage
Quand on veut utiliser I18nPlugin dans notre plugin, il faut d'abord initialiser les fichiers de messages. Une fois que c'est fait, on demande à I18nPlugin de nous fournir un objet I18n qui nous permet d'obtenir les messages finaux en fonction du joueur et de la clé du message.
### Fichier `fr_FR.properties`
Ce fichier contient les messages pour la locale `fr_FR`
```properties
hdv.advert_seller=Un joueur vous a achté un item pour {0} à l'hôtel des ventes
hdv.performed_buy=Achat effectué
```
### Ficher `MyPlugin.java`
```java
public class MyPlugin extends JavaPlugin {
    private static MyPlugin instance; 
    private I18n i18n;
    
    @Override
    public void onEnable() {
        MyPlugin.instance = this;
        
        //Obtention de notre object I18n
        this.i18n = I18nProvider.get().getI18n(this);
        try {
            //Chargement des fichiers messages par locale
            I18nProvider.get().loadTranslation(this, "/fr_FR.properties");
            I18nProvider.get().loadTranslation(this, "/en_US.properties");
        }
        catch(IOException exception) {
            exception.printSatcktrace();
            //log un message d'erreur : Impossible de charger les messages
        }
    }
    
    //Cette fonction sera utilisée dans tous le plugin en tant que import statique
    public static String tl(Player player, String key, String... replaces) {
        return MyPlugin.instance.translate(player, key, replaces);
    }
}
```
### Exemple d'utilisation
```java
public void advertPlayer(Player player, BigDeciaml price) {
    player.sendMessage(tl(player, "hdv.advert_seller", new Eco(price)));
}
```
