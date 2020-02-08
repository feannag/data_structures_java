import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HashTable<T> {
    public static final Logger logger = Logger.getLogger("HashTable");

    private List<Object> items;
    private double itemsCapacity;
    private double numberOfItems;
    private double maxLoadFactor;

    public HashTable() {
        itemsCapacity = 10;
        items = new ArrayList<>();
        numberOfItems = 0;
        maxLoadFactor = 0.7;

        initializeItems();
    }

    private void initializeItems() {
        for (int i = 0; i < items.size(); i++) {
            if(!(items.get(i) instanceof None)) {
                items.set(i, new None());
            }
        }

        for (int i = items.size(); i < itemsCapacity; i++) {
            items.add(i, new None());
        }
    }

    private static class None {
        public None() {}
    }

    private static class Placeholder {
        public Placeholder() {}
    }

    private List<Object> backup() {
        List<Object> temp_list = new ArrayList<>();

        for (Object item : items) {
            if (!(item instanceof None)) {
                temp_list.add(item);
            }
        }

        return temp_list;
    }

    private void rehash(double factor) {
        List<Object> temp_list = backup();

        itemsCapacity = (int)(itemsCapacity * factor);
        initializeItems();
        numberOfItems = 0;

        for(Object item : temp_list) {
            add(item);
        }
    }

    public boolean add(Object item) {
        double loadFactor = numberOfItems / itemsCapacity;

        if (loadFactor <= maxLoadFactor) {
            int index = (int)( (item.hashCode() > 0 ? item.hashCode() : item.hashCode() * -1) % itemsCapacity);

            while (!(items.get(index) instanceof None)) {
                if (items.get(index) == item) {
                    return false;
                }

                index = (int)((index + 1) % itemsCapacity);
            }

            items.set(index, item);
            numberOfItems++;
        } else {
            rehash(2);
            add(item);
        }

        return true;
    }

    public Object[] contains(Object item) {
        int index = (int)( (item.hashCode() > 0 ? item.hashCode() : item.hashCode() * -1) % itemsCapacity);

        while(!(items.get(index) instanceof None)) {
            if(items.get(index) == item) {
                return new Object[]{true, index};
            }

            index = (int)((index + 1) % itemsCapacity);
        }

        return new Object[]{false};
    }

    public void remove(Object item) {
        Object[] itemInfo = contains(item);

        if(itemInfo[0].equals(true)) {
            items.set((int)itemInfo[1], new Placeholder());
            numberOfItems--;
        }

        double loadFactor = numberOfItems / itemsCapacity;
        if(loadFactor < 0.25) {
            rehash(0.5);
        }
    }

    public static void main(String[] args) {
        HashTable<String> hs = new HashTable<>();
        String[] items = new String[]{"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "indiana", "juliet", "kilo", "lima", "maverick", "nancy", "oscar", "papa", "quebeck", "romeo", "sierra", "tango", "uniform", "victory", "whiskey", "xray", "yankee", "zulu"};

        for (String item : items) {
            hs.add(item);
        }

        for (int i = 0; i < 20; i++) {
            hs.remove(items[i]);
        }
    }
}