/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Deque is basically a stack and queue combined
 * Makes no sense to do this with an array
 */
public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node<Item> begin;
    private Node<Item> end;

    private class Node<Item> {
        private final Item item;
        private Node<Item> next;
        private Node<Item> previous;

        public Node(Item item) {
            this.item = item;
        }
    }

    public Deque() {
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (this.size == 0);
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            this.begin = new Node<Item>(item);
            this.end = this.begin;
        }
        else {
            Node<Item> newNode = new Node<Item>(item);
            newNode.previous = this.begin;
            this.begin.next = newNode;
            this.begin = newNode;
        }
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            this.end = new Node<Item>(item);
            this.begin = this.end;
        }
        else {
            Node<Item> newNode = new Node<Item>(item);
            this.end.previous = newNode;
            newNode.next = this.end;
            this.end = newNode;
        }
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        if (this.size == 1) {
            Item stored = this.begin.item;
            this.begin = null;
            this.end = null;
            this.size = 0;
            return stored;
        }
        else {
            Item stored = this.begin.item;
            this.begin.previous.next = null;
            this.begin = this.begin.previous;
            this.size--;
            return stored;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        if (this.size == 1) {
            Item stored = this.end.item;
            this.begin = null;
            this.end = null;
            this.size = 0;
            return stored;
        }
        else {
            Item stored = this.end.item;
            this.end.next.previous = null;
            this.end = this.end.next;
            this.size--;
            return stored;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> pointer = Deque.this.begin;

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Item saved = this.pointer.item;
            this.pointer = this.pointer.previous;
            return saved;
        }

        public boolean hasNext() {
            return this.pointer != null;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        DequeIterator it = new DequeIterator();
        return it;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("Nigger");
        deque.addFirst("Cunt");
        deque.removeLast();
        Iterator<String> it = deque.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
