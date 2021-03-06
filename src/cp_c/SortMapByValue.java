/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cp_c;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SortMapByValue {

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 10);
        map.put("b", 30);
        map.put("c", 40);
        map.put("d", 40);
        map.put("e", 20);
        System.out.println(map);

        TreeMap<String, Integer> sortedMap = sortMapByValue(map);
        System.out.println(sortedMap);
        for (Map.Entry e : sortedMap.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }

    }

    public static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map) {
        Comparator<String> comparator = new ValueComparator(map);
        //TreeMap is a map sorted by its keys. 
        //The comparator is used to sort the TreeMap by keys. 
        TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
        result.putAll(map);
        return result;
    }

// a comparator that compares Strings
}

class ValueComparator implements Comparator<String> {

    HashMap<String, Integer> map = new HashMap<String, Integer>();

    public ValueComparator(HashMap<String, Integer> map) {
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {

        if (map.get(s1) == map.get(s2)) {
            int x = s1.compareTo(s2);
            if (x > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        if (map.get(s1) > map.get(s2)) {
            return -1;
        } else {
            return 1;
        }
    }
}
