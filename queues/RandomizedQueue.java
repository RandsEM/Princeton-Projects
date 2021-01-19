/* *****************************************************************************
 *  Name: Darren
 *  Date:
 *  Description: The heart of this project is basically removing a
 *  random element from an array and realizing that you can just add the
 *  last element in the array and stick it into the array missing spot
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] storage;
    private int currentIndex;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.size = 0;
        this.storage = (Item[]) new Object[1];
        this.currentIndex = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.storage[currentIndex] = item;
        this.size++;
        this.currentIndex++;
        if (currentIndex >= this.storage.length) {
            Object[] newArray = new Object[this.storage.length * 2];
            for (int i = 0; i < this.storage.length; i++) {
                newArray[i] = this.storage[i];
            }

            this.storage = (Item[]) newArray;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(this.currentIndex);
        Item saved = this.storage[random];
        this.storage[random] = null;
        this.size--;
        this.storage[random] = this.storage[currentIndex - 1];
        this.storage[currentIndex - 1] = null;
        this.currentIndex--;
        if (this.size <= (1.0 / 4.0) * this.storage.length) {
            int ceiling = (int) ((1.0 / 2.0) * this.storage.length);
            Item[] newArray = (Item[]) new Object[ceiling];
            for (int i = 0; i < this.size; i++) {
                newArray[i] = this.storage[i];
            }
            this.storage = newArray;

        }
        return saved;

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        if (this.size == 1) {
            return this.storage[0];
        }
        else {
            return this.storage[StdRandom.uniform(this.currentIndex)];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        RQIterator it = new RQIterator();
        return it;
    }

    private class RQIterator implements Iterator<Item> {
        int counter = 0;
        int[] randomOrder = new int[RandomizedQueue.this.size];

        public RQIterator() {
            for (int i = 0; i < randomOrder.length; i++) {
                this.randomOrder[i] = i;
            }

            StdRandom.shuffle(this.randomOrder);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (this.counter >= this.randomOrder.length) {
                throw new NoSuchElementException();
            }
            Item saved = RandomizedQueue.this.storage[this.randomOrder[this.counter]];
            this.counter++;
            return saved;
        }

        public boolean hasNext() {
            return (this.counter != this.randomOrder.length);
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> lmao = new RandomizedQueue<>();
        lmao.enqueue(5);
        System.out.println(lmao.dequeue());
        System.out.println();
        lmao.enqueue(11);
        System.out.println(lmao.dequeue());
    }
}
