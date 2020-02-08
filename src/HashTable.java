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
            if (!(item instanceof None) && !(item instanceof Placeholder)) {
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
            if(items.get(index).equals(item)) {
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
        HashTable<String> ht = new HashTable<>();
        String[] items = new String[]{"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "indiana", "juliet", "kilo", "lima", "maverick", "nancy", "oscar", "papa", "quebec", "romeo", "sierra", "tango", "uniform", "victory", "whiskey", "x-ray", "yankee", "zulu"};

        for (String item : items) {
            ht.add(item);
        }

        for (int i = 0; i < 20; i++) {
            ht.remove(items[i]);
        }

        ht = new HashTable<>();
        int[] numbers = new int[] {40, 23, 42, 92, 84, 48, 2, 7, 0, 823, 94, 78, 32, 98, 43, 90, 32, 43, 89, 75, 47};

        for(int number : numbers) {
            ht.add(number);
        }

        for (int i = 0; i < 15; i++) {
            ht.remove(numbers[i]);
        }

        ht = new HashTable<>();
        double[] float_numbers = new double[] {36.0, 25.0, 8.0, 48.5, 49.0, 93.0, 81.0, 44.5, 72.0, 13.0, 88.5, 69.5, 11.0, 4.0, 2.0, 47.0, 23.0, 95.0, 82.5, 25.5, 36.0, 15.5, 91.0, 2.5, 68.0, 53.0, 26.0, 92.5, 90.5, 35.0};

        for(double number : float_numbers) {
            ht.add(number);
        }

        for (int i = 0; i < 17; i++) {
            ht.remove(float_numbers[i]);
        }
    }
}