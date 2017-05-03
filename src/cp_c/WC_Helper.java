/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp_c;

import static cp_c.SortMapByValue.sortMapByValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author rajor
 */
public class WC_Helper {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scn = new Scanner(new File("input.txt"));
        HashMap<String, HashMap<String, Integer>> db = new HashMap<String, HashMap<String, Integer>>();

        while (scn.hasNextLine()) {
            String line = scn.nextLine();
            StringTokenizer strtok = new StringTokenizer(line, "-");
            if (strtok.countTokens() != 2) {
                continue;
            }
            String country = strtok.nextToken();
            String word = strtok.nextToken();
            word = word.substring(0, word.indexOf("\t"));
            // System.out.println("--------------");
            incWord(db, country, word);
        }

        String total_rank = getRank(db.get("total"))[0];
        for (String country : db.keySet()) {
            HashMap<String, Integer> c_db = db.get(country);
            String rank = getMostPopularWords(c_db);
            //  System.out.println(c_db);
            String output = (country + ":" + rank);

            System.out.println(output);

        }

    }

    public static String[] getRank(HashMap<String, Integer> c_db) {
        String[] ret = {"", ""};
        TreeMap<String, Integer> sortedMap = sortMapByValue(c_db);
        for (String word : sortedMap.keySet()) {
            ret[0] = ret[0] + "-" + word;
        }
        ret[0] = ret[0].substring(1);
        ret[1] = sortedMap.toString();
        // System.out.println(ret[1]);
        return ret;
    }

    public static String getMostPopularWords(HashMap<String, Integer> c_db) {
        String ret = "";
        TreeMap<String, Integer> sortedMap = sortMapByValue(c_db);
        int i = 0;
        for (Map.Entry e : sortedMap.entrySet()) {
            ret = ret + "-" + e.getKey() + "(" + e.getValue() + ")";
            i++;
            if (i >= 3) {
                break;
            }
        }
        ret = ret.substring(1);
        // System.out.println(ret[1]);
        return ret;
    }

    public static HashMap<String, HashMap<String, Integer>> incWord(HashMap<String, HashMap<String, Integer>> db, String Country, String Word) {

        //   System.out.println(Country+": "+ Word);
        HashMap<String, Integer> Country_db = db.get(Country);

        if (Country_db == null) {
            Country_db = new HashMap<String, Integer>();
            Country_db.put(Word, 1);
            db.put(Country, Country_db);

        } else if (Country_db.containsKey(Word)) {
            int value = Country_db.get(Word);
            Country_db.put(Word, value + 1);

        } else {
            Country_db.put(Word, 1);
        }
        db.put(Country, Country_db);

        HashMap<String, Integer> total_db = db.get("total");
        if (total_db == null) {
            total_db = new HashMap<String, Integer>();
            total_db.put(Word, 1);
            db.put("total", total_db);
        } else if (total_db.containsKey(Word)) {
            int value = total_db.get(Word);
            total_db.put(Word, value + 1);

        } else {
            total_db.put(Word, 1);
        }

        return db;

    }

    public LinkedHashMap<Integer, String> sortHashMapByValues(HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<String> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, String> sortedMap = new LinkedHashMap<>();

        Iterator<String> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            String val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                String comp1 = passedMap.get(key);
                String comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

}
