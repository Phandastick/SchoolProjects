#include <iostream>
#include "Cleaner.h" // to use input data
using namespace std;

/*
Assumption
- heap property is determined by adding nodes left to right, and ignoring left-right branch balance
- using min-heap to sort ascending order

Process:
- Store all data:
    - where each slot in the array is of struct Property (contains all fields)      /
    - each slot represents a record in the dataset                                  / done with vector<Property>
    - each node of binary is determined by the pointer index and the formula used
- Sorting:
    - pull the root node each time we add to the sorted vector
    - heapify (put lowest node in root and 'reheap')
    - sorted dataset should be ascending/descending order
*/
class BinaryHeap
{
private:
    const static int MAX_SIZE = 30;
    vector<Property> arr;    // stores the Number of elements in heap.
    vector<Property> heap;   // array that will take the valuse in arr in min heap format
    vector<Property> sorted; // sorted array that will be sorted ascending  using min heap
    int size;

public:
    // constructor to input data with file path specified
    // will be used to construct heap
    BinaryHeap(string &inputFilePath)
    {
        cout << "Initializing Binary Heap Class..." << endl;
        this->arr = inputData(inputFilePath);
        // display200(arr); // display first 200 rows to
        // confirm that its the correct dataset

        // reorder and form a new arranged array by inserting one by one
        heapify();
    }

    vector<Property> getHeap()
    {
        return heap;
    }

    vector<Property> getInputArray()
    {
        return arr;
    }

    vector<Property> getSortedArray()
    {
        return sorted;
    }

private:
    // TO BUILD
    // converts arrangement in array of data to heap
    void heapify()
    {
        // Copy elements from arr to heap
        heap = this->arr;

        // Apply the heapify process
        for (int i = (heap.size() / 2) - 1; i >= 0; i--)
        {
            bubbleDown(i); //
        }
    }

    // Helper function to maintain min-heap property
    void bubbleDown(int i)
    {
        int smallest = i;
        int leftChild = 2 * i + 1;
        int rightChild = 2 * i + 2;

        // Check if leftChild child exists and is smaller than root
        if (leftChild < heap.size() && heap[leftChild].getRent() < heap[smallest].getRent())
        {
            smallest = leftChild;
        }

        // Check if rightChild child exists and is smaller than smallest so far
        if (rightChild < heap.size() && heap[rightChild].getRent() < heap[smallest].getRent())
        {
            smallest = rightChild;
        }

        // Swap and continue heapifying if root is not the smallest
        if (smallest != i)
        {
            swap(heap[i], heap[smallest]);
            bubbleDown(smallest);
        }
    }

    // Extract the minimum element from the heap
    Property extractMin()
    {
        if (heap.empty())
        {
            throw std::runtime_error("Heap is empty");
        }

        Property minElement = heap[0];
        heap[0] = heap.back();
        heap.pop_back();
        bubbleDown(0);

        return minElement;
    }

public:
    // Function to sort the array using heap
    void sort()
    {
        // heapify(); // Build the heap

        vector<Property> sortedList;
        while (!heap.empty())
        {
            sortedList.push_back(extractMin());
        }

        this->sorted = sortedList;
    }

    // Function to set the original array
    void setArray(const vector<Property> &inputArray)
    {
        arr = inputArray;
    }

    void writeCSV(string outputFilePath)
    {
        writeToCSV(sorted, outputFilePath);
    }
};
