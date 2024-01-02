import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;

public class Game {
    private Map<String, Player> players = new HashMap<>();
    boolean findPassword = false;
    boolean loggedAsPlayer = false;

    private Player currentPlayer = null;

    private Game() {

    }

    private static Game gameInstance;

    public static Game GetGameInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public void gameLaunch() {
        String password = "ALIENS";
        Scanner input = new Scanner(System.in);
        int nbTry = 3;
        System.out.println("Les aliens vont envahir la Terre !");
        System.out.println("Vous faites partie de l'armée mondiale, créée par tous les pays, pour lutter contre l'envahisseur !");
        System.out.println("Vous êtes le général en chef, votre but est de repousser l'envahisseur en développant une stratégie, ou simplement en chargeant à l'ancienne !");
        System.out.println("Si vous réussissez, la Terre sera sauvée !");
        System.out.println("Toute la Terre compte sur votre aptitude à développer une stratégie !");
        System.out.println("Pour commencer le jeu, il vous faudra d'abord pénétrer dans l'interface de commande de l'armée. Tu as 3 essais pour tenter de trouver le mot de passe.");
        System.out.println("--------------------Armée Mondiale-------------------");
        System.out.println("----------------Interface de commande----------------");
        while (!findPassword) {
            System.out.println("Entrez le mot de passe : ");
            System.out.println("Indice : Ceux qui nous envahissent, tout en majuscules");
            String providedPassword = input.nextLine();
            if (password.equals(providedPassword)) {
                findPassword = true;
                runGame();
            } else {
                nbTry -= 1;
                System.out.println("Ce n'est pas le bon mot de passe !");
            }
            if (nbTry == 0) {
                System.out.println("Vous n'avez pas trouvé le mot de passe ! Relancez le jeu pour recommencer.");
                break;
            }
        }
    }

    private void runGame() {

    }

    @SuppressWarnings("SpellCheckingInspection")
    public void registerAsPlayer() {
        int nbMissiles = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("Veuillez entre votre nom de famille : ");
        String name = input.nextLine();
        System.out.println("Veuillez entrer votre prénom : ");
        String firstName = input.nextLine();
        System.out.println("Veuillez entrer votre âge : ");
        int age = input.nextInt();
        input.nextLine();
        System.out.println("Veuillez définir un nom d'utilisateur : ");
        String userName = input.nextLine();
        System.out.println("Enfin, veuillez définir un mot de passe qui servira à vous authentifier lors de vos prochaines connexions : ");
        String password = input.nextLine();
        Player player = new Player(name, firstName, nbMissiles, age, userName, password);
        players.put(player.getUserName(), player);

    }

    public void savePlayerData() {
        final GsonBuilder builder = new GsonBuilder();
        final Gson gson = builder.create();

        try {
            final FileWriter fileWriter = new FileWriter("save.json", false);
            final JsonWriter jsonWriter = new JsonWriter(fileWriter);
            jsonWriter.beginObject();
            jsonWriter.name("Players");
            jsonWriter.beginArray();
            for (Player p : players.values()) {
                gson.toJson(p, Player.class, jsonWriter);
            }
            jsonWriter.endArray();
            jsonWriter.endObject();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @SuppressWarnings("SpellCheckingInspection")
    public void showMenu() {
        System.out.println("1. Créer une nouvelle partie");
        System.out.println("2. Se connecter en tant que joueur (pour recevoir votre nombre de missiles)");
        System.out.println("3. QUITTER");

    }

    public void runMenu() {
        boolean exit = false;
        Scanner input = new Scanner(System.in);
        while (!exit) {
            showMenu();
            int a = input.nextInt();
            switch (a) {
                case 1:
                    gameLaunch();
                    break;
                case 2:
                    Player p = login();
                    break;
                case 3:
                    exit = true;
                    break;
            }

        }
    }

    public Player login() {
        Scanner input = new Scanner(System.in);
        Player p = null;
        while (p == null) {
            System.out.println("To login as player, please enter your username");
            String nameLoginPlayer = input.nextLine();
            if(players.containsKey(nameLoginPlayer)) {
                p = players.get(nameLoginPlayer);
                break;
            }
            System.out.println("Sorry, user " + nameLoginPlayer
                    + " does not exist. Please enter an existing username.");
        }

        System.out.println("Now please enter your password");
        String providedPwd = input.nextLine();

        byte[] pwdHash = Player.getHash(providedPwd);
        if(Arrays.equals(pwdHash, p.getPasswordHash())){
            currentPlayer = p;
            System.out.println("Logged in successfully !");
            return p;
        }

        System.out.println("Incorrect password, login cancelled.");
        return null;
    }
}