/*
 * MIT License
 *
 * Copyright (c) 2018 2B2TMCBE
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package randomSpawn_2b2tbe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//import all dependencies from java libraries
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.charset.Charset;
import java.util.LinkedHashMap;
//import java.util.Set;
//import com.google.common.io.Files;
//import all dependencies from nukkitx
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

//Create class
public class randomSpawn extends PluginBase {
  //define public variables
  public int maxRange;
  public String maxRangeInStr;
  public int rnge;
  public Position worldspawn;
  public EventListener listener;
  public HashSet<String> playerList = new HashSet<String>(Arrays.asList(""));
  //define private variables
  private Config config;
  private File file;
  private String filePath = "plugins/randomSpawn_2b2tbe/playerlist.txt";

  //private FileWriter name;
  //private File information;
  //private Config list;
  //When the plugin is loaded
  //private EventListener event; 
  @Override
  public void onLoad() {
    this.getLogger().info(TextFormat.GREEN + "randomSpawn is loaded!");

  }

  //When the plugin is enabled
  @SuppressWarnings({"deprecation"})
  @Override
  public void onEnable() {
    //register EventListener so it will work
    this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    this.getLogger().info(TextFormat.YELLOW + "randomSpawn enabled!");

    //get the world spawn as a position object
    worldspawn = this.getServer().getDefaultLevel().getSpawnLocation();


    //Create config.yml
    this.config = new Config(new File(this.getDataFolder(), "config.yml"), Config.YAML,

        new LinkedHashMap<String, Object>() {
          /**
           * 
           */
          private static final long serialVersionUID = 1L;

          //create key spawnRange with default input 900
          {
            //default settings specific to 2b2tbe
            put("spawnRange", "300");
          }
        });

    //Create playerList.txt
    file = new File(filePath);
    //Check if the parent directory exist, create it if not exist
    if (!file.getParentFile().mkdir()) {
      file.getParentFile().mkdir();
    }
    //create the playerlist.txt
    try {
      file.createNewFile();
    } catch (IOException e) {
      // Auto-generated catch block
      e.printStackTrace();
    }

    //get all info
    maxRangeInStr = String.valueOf(config.get("spawnRange"));
    playerList = readPlayerList(filePath);


    //save config file
    config.save();


    //set maxRange variable to the max range defined in config.yml
    setMaxRange(getMaxRange(maxRangeInStr));


  }


  //This method Convert String value rangeInStr to Int value
  public int getMaxRange(String rangeInStr) {
    int range = Integer.valueOf(rangeInStr);
    return range;
  }

  //This Set maxRange to the an Int value 
  public void setMaxRange(int range) {
    maxRange = range;
  }

  //This function create an empty file
  @Deprecated
  public void createFile(String filename) {
    try {
      FileWriter writer = new FileWriter(filename);

      BufferedWriter output = new BufferedWriter(writer);
      output.write("");
      output.newLine();
      output.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //This function read the file line-by-line and store it into a HashSet
  public HashSet<String> readPlayerList(String filename) {
    HashSet<String> set = new HashSet<>();
    try {
      BufferedReader in = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = in.readLine()) != null) {
        set.add(line);
      }
      in.close();
      return set;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return set;
  }

  //Another function to store a HashSet into a file
  public void writePlayerList(HashSet<String> set, String filename) {
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(filename));
      for (String s : set) {
        out.write(s);
        out.newLine();
      }
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Action to do when plugin is disabled
  public void onDisable() {
    this.getLogger().info(TextFormat.RED + "randomSpawn disabled!");
    writePlayerList(playerList, filePath);
  }

  //just for compile in eclipse
  public static void main(String[] args) {
    System.out.println("compiled");
  }

}
