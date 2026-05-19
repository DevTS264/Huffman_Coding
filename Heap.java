import java.util.Collection;
import java.util.Objects;

public class Heap<T extends Comparable<? super T>>
{
    private T[] entries;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public Heap()
    {
        entries = (T[])new Comparable[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public Heap(Collection<? extends T> collection)
    {
        entries = (T[])new Comparable[Math.max(DEFAULT_CAPACITY, collection.size())];

        int i = 0;
        for(T value: collection)
        {
            if(value == null)
                throw new NullPointerException();
            entries[i++] = value;
        }
        size = collection.size();

        buildHeap();
    }

    @SuppressWarnings("unchecked")
    private void resize()
    {
        T[] temp = (T[])new Comparable[entries.length * 2];
        System.arraycopy(entries, 0, temp, 0, entries.length);
        entries = temp;
    }

    private int getParent(int index)
    {
        return (index - 1) / 2;
    }

    private int getLeftChild(int index)
    {
        return 2 * index + 1;
    }

    private int getRightChild(int index)
    {
        return 2 * index + 2;
    }

    private void swap(int i, int j)
    {
        T temp = entries[i];
        entries[i] = entries[j];
        entries[j] = temp;
    }

    private void heapifyUp(int index)
    {
        while(index > 0 && entries[index].compareTo(entries[getParent(index)]) < 0)
        {
            swap(index, getParent(index));
            index = getParent(index);
        }
    }

    private void heapifyDown(int index)
    {
        int smallest = index;
        int leftChild = getLeftChild(index);
        int rightChild = getRightChild(index);

        if(leftChild < size && entries[smallest].compareTo(entries[leftChild]) > 0)
            smallest = leftChild;
        if(rightChild < size && entries[smallest].compareTo(entries[rightChild]) > 0)
            smallest = rightChild;

        if(smallest != index)
        {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    private void buildHeap()
    {
        for(int i = size / 2 - 1; i >= 0; i--)
            heapifyDown(i);
    }

    public void insert(T value)
    {
        if(value == null)
            throw new NullPointerException();

        if(size == entries.length)
            resize();
        int index = size;
        entries[size++] = value;
        heapifyUp(index);
    }

    public T peek()
    {
        if(isEmpty())
            return null;

        return entries[0];
    }

    public T extractMin()
    {
        if(isEmpty())
            return null;

        T min = entries[0];
        entries[0] = entries[size - 1];
        entries[size - 1] = null;
        size--;

        heapifyDown(0);
        return min;
    }

    public void decreaseKey(int index, T newValue)
    {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        if(newValue == null)
            throw new NullPointerException();
        if(newValue.compareTo(entries[index]) > 0)
            throw new IllegalArgumentException();

        entries[index] = newValue;
        heapifyUp(index);
    }

    public boolean contains(T value)
    {
        if(value == null)
            throw new NullPointerException();

        for(int i = 0; i < size; i++)
        {
            if(Objects.equals(value, entries[i]))
                return true;
        }

        return false;
    }

    public void remove(T value)
    {
        if(value == null)
            throw new NullPointerException();

        int index = -1;
        for(int i = 0; i < size; i++)
        {
            if(Objects.equals(value, entries[i]))
            {
                index = i;
                break;
            }
        }

        if(index < 0)
            return;

        entries[index] = entries[size - 1];
        entries[size - 1] = null;
        size--;

        heapifyDown(index);
    }

    @SuppressWarnings("unchecked")
    public void clear()
    {
        entries = (T[])new Comparable[DEFAULT_CAPACITY];
        size = 0; 
    }

    @Override
    public String toString()
    {
        if(isEmpty())
            return "[]";

        StringBuilder result = new StringBuilder("[");
        for(int i = 0; i < size - 1; i++)
            result.append(entries[i] + ", ");
        result.append(entries[size - 1] + "]");
        return result.toString(); 
    }
}