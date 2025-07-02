package db;

import com.google.gson.Gson;
import util.MaterialCost;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    public static final Config instance = getInstance();

    public WeaponCosts weaponCosts = new WeaponCosts();

    public static class WeaponCosts {
        public MaterialCost spear;
        public MaterialCost sword;
        public MaterialCost heavySword;
        public MaterialCost lightSword;
        public MaterialCost mace;
        public MaterialCost slingshot;
        public MaterialCost bow;
        public MaterialCost shortBow;
        public MaterialCost longBow;
        public MaterialCost crossBow;
        public MaterialCost catapult;

        public MaterialCost spearUnlock;
        public MaterialCost swordUnlock;
        public MaterialCost heavySwordUnlock;
        public MaterialCost lightSwordUnlock;
        public MaterialCost maceUnlock;
        public MaterialCost slingshotUnlock;
        public MaterialCost bowUnlock;
        public MaterialCost shortBowUnlock;
        public MaterialCost longBowUnlock;
        public MaterialCost crossBowUnlock;
        public MaterialCost catapultUnlock;

        public WeaponCosts(MaterialCost spear, MaterialCost sword, MaterialCost heavySword, MaterialCost lightSword, MaterialCost mace, MaterialCost slingshot, MaterialCost bow, MaterialCost shortBow, MaterialCost longBow, MaterialCost crossBow, MaterialCost catapult, MaterialCost spearUnlock, MaterialCost swordUnlock, MaterialCost heavySwordUnlock, MaterialCost lightSwordUnlock, MaterialCost maceUnlock, MaterialCost slingshotUnlock, MaterialCost bowUnlock, MaterialCost shortBowUnlock, MaterialCost longBowUnlock, MaterialCost crossBowUnlock, MaterialCost catapultUnlock) {

            this.spear = spear;
            this.sword = sword;
            this.heavySword = heavySword;
            this.lightSword = lightSword;
            this.mace = mace;
            this.slingshot = slingshot;
            this.bow = bow;
            this.shortBow = shortBow;
            this.longBow = longBow;
            this.crossBow = crossBow;
            this.catapult = catapult;
            this.spearUnlock = spearUnlock;
            this.swordUnlock = swordUnlock;
            this.heavySwordUnlock = heavySwordUnlock;
            this.lightSwordUnlock = lightSwordUnlock;
            this.maceUnlock = maceUnlock;
            this.slingshotUnlock = slingshotUnlock;
            this.bowUnlock = bowUnlock;
            this.shortBowUnlock = shortBowUnlock;
            this.longBowUnlock = longBowUnlock;
            this.crossBowUnlock = crossBowUnlock;
            this.catapultUnlock = catapultUnlock;
        }

        public WeaponCosts() {
            this(
                new MaterialCost(0,1,1,0,0),
                new MaterialCost(0, 0,2,0, 2),
                new MaterialCost(0,0,2,0,3),
                new MaterialCost(0,0,3,0, 1),
                new MaterialCost(0,4,1,0, 0),
                new MaterialCost(0, 0,1,1, 0),
                new MaterialCost(0,0,3,1, 0),
                new MaterialCost(0,0,2,0,0),
                new MaterialCost(0,0,2,0,1),
                new MaterialCost(0,0,4,0,2),
                new MaterialCost(0,4,25,1,5),

                new MaterialCost(0,0,0,0,0),
                new MaterialCost(2,0,2,0,4),
                new MaterialCost(2,0,2,0,3),
                new MaterialCost(3,0,2,0,2),
                new MaterialCost(3,5,5,0,0),
                new MaterialCost(4,0,5,0,0),
                new MaterialCost(4,0,10,0,0),
                new MaterialCost(5,0,5,0,0),
                new MaterialCost(5,0,4,0,0),
                new MaterialCost(6,0,3,0,0),
                new MaterialCost(10,3,5,0,0)
            );
        }
    }

    public static Config getInstance() {
        Gson gson = new Gson();
        try {
            FileReader fileReader = new FileReader("config.json");

            Config config = gson.fromJson(fileReader, Config.class);
            fileReader.close();
            return config;
        } catch (FileNotFoundException e) {
            Config config = new Config();
            try {
                FileWriter fileWriter = new FileWriter("config.json");
                gson.toJson(config, Config.class, fileWriter);
                fileWriter.close();
            } catch (IOException ex) {
                return new Config();
            }
            return config;
        } catch (IOException e) {
            return new Config();
        }
    }

    public void save() throws IOException {
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter("config.json");
        gson.toJson(this, Config.class, fileWriter);
        fileWriter.close();
    }
}
