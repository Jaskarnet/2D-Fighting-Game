package com.mygdx.utils;

public class CircularBuffer<T> {
    private T[] buffer;
    private int size;
    private int head; // Index of the first element in the buffer
    private int tail; // Index where the next element will be inserted

    public CircularBuffer(int capacity) {
        buffer = (T[]) new Object[capacity];
        size = 0;
        head = 0;
        tail = 0;
    }

    public void add(T element) {
        if (size == buffer.length) {
            // Buffer is full, overwrite the oldest element
            head = (head + 1) % buffer.length;
        } else {
            size++;
        }

        buffer[tail] = element;
        tail = (tail + 1) % buffer.length;
        System.out.println("buffer.length: " + buffer.length + " size: " + size + " head: " + head + " tail: " + tail);
    }

    public T getLast(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of range");
        }

        // Calculate the actual index in the buffer
        int actualIndex = (head + index) % buffer.length;
        return buffer[actualIndex];
    }

    public T get(int index) {
        return buffer[index];
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buffer.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == buffer.length;
    }

    public int getHead() {
        return head;
    }

    public int getTail() {
        return tail;
    }

    public int popCurrent() {
        System.out.println("buffer.length: " + buffer.length + " size: " + size + " head: " + head + " tail: " + tail);
        tail = (tail - 1 + buffer.length) % buffer.length;
        return tail;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CircularBuffer contents: [");

        if (!isEmpty()) {
            int index = head;
            int count = 0;

            while (count < size) {
                sb.append(buffer[index]);
                index = (index + 1) % buffer.length;
                count++;

                if (count < size) {
                    sb.append(", ");
                }
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
