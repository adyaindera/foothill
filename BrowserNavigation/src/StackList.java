import java.util.Iterator;

/**
 * Generic object type of singly linked list data structure
 * which implements Iterable
 *
 * @param <T> generic type class
 * @author Adya Putra Indera
 */
public class StackList<T> implements Iterable<T>
{
    private String name;
    protected Node top;
    private int mSize;

    /**
     * default constructor to put new generic data to the top of the new stack
     *
     * @param name the name of the stack
     * @param top generic object to be passed to the top of the stack
     */
    public StackList(String name, T top)
    {
        this.name = name;

        if (top == null)
        {
            this.top = null;
            mSize = 0;
        }
        else
        {
            this.top = new Node(top);
            mSize = 1;
        }
    }

    /**
     * push new data to the top of the stack
     *
     * @param x generic object to push
     */
    public void push(T x)
    {
        if (x.toString() == "" || x == null)
            return;

        Node topNode = new Node(x);
        topNode.next = top;
        top = topNode;
        mSize++;
    }

    /**
     * remove the top data in stack (last in first out)
     *
     * @return generic object of top data in stack
     */
    public T pop()
    {
        if (isEmpty())
            return null;

        T topData = top.getData();
        top = top.next;
        mSize--;

        return topData;
    }

    /**
     * see top data in stack without removing it
     *
     * @return generic object of top data in stack
     */
    public T peek()
    {
        if (isEmpty())
            return null;

        return top.getData();
    }

    /**
     * test if stack has no data
     *
     * @return true/false after test
     */
    public boolean isEmpty() { return size() == 0; }

    /**
     * get how many data available in stack
     *
     * @return the size of stack
     */
    public int size() { return mSize; }

    /**
     * print format of the stack
     *
     * @return the string output of the stack
     */
    public String toString()
    {
        String returnStr = name + " with " + mSize + " link(s): \n[";
        StackIterator iter = new StackIterator();

        while (iter.hasNext())
        {
            returnStr += iter.next();

            if (iter.hasNext())
                returnStr += ", ";
        }

        returnStr += "]";

        return returnStr;
    }

    /**
     * processing or adding iterator to traverse the stack
     *
     * @return object StackIterator
     */
    @Override
    public Iterator<T> iterator() { return new StackIterator(); }

    /**
     * inner class to store generic data in this linked list stack
     */
    private class Node
    {
        private Node next;
        private T data;

        /**
         * default constructor to create new node of generic data
         *
         * @param data generic object to store in node
         */
        private Node(T data)
        {
            this.next = null;
            this.data = data;
        }

        /**
         * accessor for data in node
         *
         * @return generic object in node
         */
        private T getData() { return data; }
    }

    /**
     * inner class iterator which implements Iterator for the stack
     */
    private class StackIterator implements Iterator<T>
    {
        private Node mCurrentNode;
        private Node mLastNodeReturned = null;
        private int mCurrentIndex;

        /**
         * default constructor for iterator
         */
        private StackIterator()
        {
            mCurrentNode = top;
            mCurrentIndex = 0;
        }

        /**
         * test if there is data in stack
         *
         * @return true/false after test
         */
        @Override
        public boolean hasNext() { return mCurrentIndex < mSize; }

        /**
         * get the data at the top of the stack
         *
         * @return the generic object at the top of the stack
         */
        @Override
        public T next()
        {
            if (!hasNext())
                throw new java.util.NoSuchElementException();

            T returnNext = mCurrentNode.getData();
            mLastNodeReturned = mCurrentNode;
            mCurrentNode = mCurrentNode.next;
            mCurrentIndex++;

            return returnNext;
        }
    }
}
