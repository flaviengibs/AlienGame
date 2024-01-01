import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.util.HexFormat;
import java.util.Locale;

public class Player {
    private String name;
    private String firstName;
    private int nbMissiles;
    private String userName;
    private int age;
    private byte[] passwordHash = null;
    public final static byte[] salt = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
    public Player(String name, String firstName, Integer nbMissiles, Integer age,String userName, String password){
        this.name = name;
        this.firstName = firstName;
        this.nbMissiles = nbMissiles;
        this.age = age;
        this.userName = userName;
        this.passwordHash = getHash(password);
    }
    public static byte[] getHash(String pass) {
        SecureRandom random = new SecureRandom();
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), Player.salt, 65536, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("Requested crypto algorithm not available!");
        } catch (java.security.spec.InvalidKeySpecException e) {
            System.out.println("Requested Key Spec not available!");
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getNbMissiles() {
        return nbMissiles;
    }

    public void setNbMissiles(int nbMissiles) {
        this.nbMissiles = nbMissiles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
