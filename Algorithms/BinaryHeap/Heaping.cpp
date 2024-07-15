#include "Binary.h"
#include <conio.h>

int main()
{
    string inputFilePath = "../CleanedDataset.csv";
    string outPath = "OUTPUTCSV.csv";
    BinaryHeap *heap = new BinaryHeap(inputFilePath);

    // cout << "Printing heap array..." << endl;
    // printWithSleep(heap->getInputArray(), 10);
    // printWithSleep(heap->getHeap(), 10);

    auto start = std::chrono::high_resolution_clock::now(); // gets the start time

    heap->sort();

    auto end = std::chrono::high_resolution_clock::now(); // gets end time
    std::chrono::duration<double> duration = end - start; // get the difference to get the duration taken
    std::cout << "Time taken to sort: " << duration.count() << " seconds" << std::endl;

    // cout << "Press enter to start printing sorted";
    // getch();

    // printWithSleepy(heap->getSortedArray(), 1000);

    heap->writeCSV(outPath);

    return 0;
}