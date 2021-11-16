import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int n = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (n == s.length)
            resize(2 * s.length);
        s[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int i = StdRandom.uniform(0, n);
        Item item = s[i];
        s[i] = s[n - 1];
        s[n-- - 1] = null;
        if (n > 0 && n == s.length / 4)
            resize(s.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        return s[StdRandom.uniform(0, n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] temp = (Item[]) new Object[n];
        private int size = n;

        public RandomizedQueueIterator() {
            for (int i = 0; i < n; i++)
                temp[i] = s[i];
        }

        public boolean hasNext() {
            return size > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            int i = StdRandom.uniform(0, size);
            Item item = temp[i];
            temp[i] = temp[size - 1];
            temp[size-- - 1] = null;
            return item;
        }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++)
            copy[i] = s[i];
        s = copy;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int[] data = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

        System.out.println("Testing: isEmpty and size");
        System.out.println("isEmpty: " + q.isEmpty() + ", size: " + q.size());

        System.out.println("Testing: enqueue");
        for (int i : data)
            q.enqueue(i);

        System.out.println("isEmpty: " + q.isEmpty() + ", size: " + q.size());

        System.out.println("Testing: dequeue");
        try {
            for (int i = 0; i < 5; i++)
                System.out.println(q.dequeue());
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException is caught");
        }

        System.out.println("Testing: sample");
        try {
            for (int i = 0; i < 10; i++)
                System.out.println(q.sample());
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException is caught");
        }

        System.out.println("Testing: iterator");
        Iterator<Integer> iter = q.iterator();
        try {
            while (iter.hasNext())
                System.out.println(iter.next());
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException is caught");
        }

        System.out.println("=====Exception Testing=====");
        System.out.println("Enqueue null item in queue");
        try {
            q.enqueue(null);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException is caught");
        }

        System.out.println("Dequeue empty queue");
        try {
            while (true)
                q.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException is caught");
        }

        System.out.println("Sample empty queue");
        try {
            q.sample();
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException is caught");
        }

        System.out.println("Call remove() function");
        try {
            iter.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("UnsupportedOperationException is caught");
        }
    }

}