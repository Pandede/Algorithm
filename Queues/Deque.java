import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node prev = null;
        private Node next = null;
    }

    private Node first = null, last = null;
    private int count = 0;
    // construct an empty deque

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldFirst;
        if (isEmpty())
            last = first;
        else
            oldFirst.prev = first;
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        last.next = null;
        if (isEmpty())
            first = last;
        else
            oldLast.next = last;
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = first.item;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.prev = null;
        }
        count--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = last.item;
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            last = last.prev;
            last.next = null;
        }
        count--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node pointer = first;

        public boolean hasNext() {
            return pointer != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = pointer.item;
            pointer = pointer.next;
            return item;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> q = new Deque<>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            boolean face = StdRandom.bernoulli();
            if (face)
                q.addFirst(s);
            else
                q.addLast(s);
            System.out.println(face ? "addFirst" : "addLast");
            Iterator<String> iter = q.iterator();
            while (iter.hasNext())
                System.out.print(iter.next() + " ");
            System.out.print("\n");
        }

        Iterator<String> iter = q.iterator();
        while (iter.hasNext()) {
            boolean face = StdRandom.bernoulli();
            String output;
            if (face)
                output = q.removeFirst();
            else
                output = q.removeLast();
            System.out.println(face ? "removeFirst" : "removeLast");
            System.out.println(output);
            System.out.printf("first = %s, last = %s\n", q.first.item, q.last.item);
            Iterator<String> innerIter = q.iterator();
            while (innerIter.hasNext())
                System.out.print(innerIter.next() + " ");
            System.out.print("\n");
        }
    }
}
