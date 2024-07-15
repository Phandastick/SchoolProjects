#include <iostream>
#include <algorithm>
#include <iterator>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <cctype>
#include <chrono>
#include <regex>
#include <thread>

// for sleep
#include <windows.h> // for windows
using namespace std;
// contains all fields
struct Property
{
    string ads_id;
    string prop_name;
    string completion_year;
    string monthly_rent;
    string location;
    string property_type;
    string rooms;
    string parking;
    string bathroom;
    string size;
    string furnished;
    string facilities;
    string additional_facilities;
    string region;

public:
    // Function to get the rent as an integer for comparison

    // Property() {}

    // int getRent(string numberString)
    // {

    //     numberString.erase(0, 2); // erases RM and first whitespace
    //     numberString.erase(remove_if(numberString.begin(), numberString.end(), [](unsigned char x)
    //                                  { return isspace(x); }),
    //                        numberString.end()); // erases all whitespaces
    //     // cout << numberString << endl;           // should return 50000YAY

    //     // calling stoi() passing string as parameter.
    //     int value = stoi(numberString);

    //     // cout << n;

    //     return value;
    // }

    int getRent()
    {
        string numberString = this->monthly_rent;

        if (numberString == "0")
        {
            return 0;
        }
        numberString.erase(0, 2); // erases RM and first whitespace
        numberString.erase(remove_if(numberString.begin(), numberString.end(), [](unsigned char x)
                                     { return isspace(x); }),
                           numberString.end()); // erases all whitespaces
        // cout << numberString << endl;           // should return 50000YAY

        // calling stoi() passing string as parameter.
        int value = stoi(numberString);

        // cout << n;

        return value;
    }
    // Function to get the size as an integer
    int getSize() const
    {
        return stoi(this->size);
    }
};

// Function to trim whitespace from a string
string trim(const string &str)
{
    size_t first = str.find_first_not_of(' ');
    if (first == string::npos)
        return "";

    size_t last = str.find_last_not_of(' ');
    return str.substr(first, last - first + 1);
}

vector<string> parseCSVLine(const string &line)
{
    vector<string> result;
    string token;
    bool isQuote = false;
    string currentField;

    for (char ch : line)
    {
        if (ch == '"')
        {
            isQuote = !isQuote;
        }
        else if (ch == ',' && !isQuote)
        {
            result.push_back(currentField);
            currentField.clear();
        }
        else
        {
            currentField += ch;
        }
    }
    result.push_back(currentField);

    for (string &field : result)
    {
        field = trim(field);
        if (!field.empty() && field.front() == '"' && field.back() == '"')
        {
            field = field.substr(1, field.size() - 2);
        }
    }
    return result;
}

vector<Property> inputData(const string &filePath)
{
    // check if filepath is valid
    ifstream file(filePath);
    if (!file.is_open())
    {
        cerr << "Failed to open the file" << endl;
        exit(1);
    }

    cout << "> Inputing data into array..." << endl;
    vector<Property> properties;
    string line;

    getline(file, line); // skip header

    while (getline(file, line))
    {
        auto fields = parseCSVLine(line);
        if (fields.size() != 14) // check if unexpected amount of records
        {
            cerr << "Unexpected number of fields in line: " << line << endl;
            continue;
        }

        Property row;
        try
        {
            row.ads_id = trim(fields[0]);
            row.prop_name = trim(fields[1]);
            row.completion_year = fields[2];
            row.monthly_rent = trim(fields[3]);
            row.location = trim(fields[4]);
            row.property_type = trim(fields[5]);
            row.rooms = (fields[6] == "More than 10") ? "11" : fields[6];
            row.parking = fields[7];
            row.bathroom = fields[8].empty() ? 0 : (fields[8]);
            row.size = trim(fields[9]);
            row.furnished = trim(fields[10]);
            row.facilities = trim(fields[11]);
            row.additional_facilities = trim(fields[12]);
            row.region = trim(fields[13]);

            properties.push_back(row);
        }
        catch (const invalid_argument &e)
        {
            cerr << "Invalid argument encountered: " << e.what() << "\n";
            cerr << "Offending line: " << line << "\n";
        }
        catch (const out_of_range &e)
        {
            cerr << "Out of range error: " << e.what() << "\n";
            cerr << "Offending line: " << line << "\n";
        }

        // stringstream ss(line);
        // Property prop;
        // getline(ss, prop.ads_id, ',');
        // getline(ss, prop.prop_name, ',');
        // getline(ss, prop.completion_year, ',');
        // getline(ss, prop.monthly_rent, ',');
        // getline(ss, prop.location, ',');
        // getline(ss, prop.property_type, ',');
        // getline(ss, prop.rooms, ',');
        // getline(ss, prop.parking, ',');
        // getline(ss, prop.bathroom, ',');
        // getline(ss, prop.size, ',');
        // getline(ss, prop.furnished, ',');
        // getline(ss, prop.facilities, ',');
        // getline(ss, prop.additional_facilities, ',');
        // getline(ss, prop.region);
        // properties.push_back(prop);
    }
    cout << "> Finish inputing data into array..." << endl;

    file.close();
    return properties;
}

// cleans missing values in the vector
void cleanMissing(vector<Property> &v)
{
    for (auto &prop : v)
    {
        if (prop.ads_id.empty())
            prop.ads_id = "NA";
        if (prop.prop_name.empty())
            prop.prop_name = "NA";
        if (prop.completion_year.empty())
            prop.completion_year = "0";
        if (prop.monthly_rent.empty())
            prop.monthly_rent = "0";
        if (prop.location.empty())
            prop.location = "NA";
        if (prop.property_type.empty())
            prop.property_type = "NA";
        if (prop.rooms.empty())
            prop.rooms = "0";
        if (prop.parking.empty())
            prop.parking = "0";
        if (prop.bathroom.empty())
            prop.bathroom = "0";
        if (prop.size.empty())
            prop.size = "0";
        if (prop.furnished.empty())
            prop.furnished = "NA";
        if (prop.facilities.empty())
            prop.facilities = "NA";
        if (prop.additional_facilities.empty())
            prop.additional_facilities = "NA";
        if (prop.region.empty())
            prop.region = "NA";
    }
}

// displays the contents in vector
void display200(vector<Property> &v)
{
    cout << "> Printing first 200 rows of array..." << endl;

    for (size_t i = 0; i < v.size() && i < 200; ++i)
    {
        Property prop = v[i];
        cout << i << ": " << v[i].ads_id << ": " << v[i].getRent() << " rent & " << v[i].getSize() << " Size (sqft)" << endl;
    }
}

// displays full vector, care - many lines
// Function to print all the contents in the vector with sleep
void printWithSleep(const vector<Property> &v, int sleepDurationMs)
{
    int count = 0;
    for (Property prop : v)
    {
        cout << count << ": " << prop.ads_id << ": " << prop.monthly_rent << " rent & " << prop.getSize() << " Size (sqft)" << endl;
        cout << count << ": " << prop.ads_id << ": " << prop.getRent() << " rent & " << prop.getSize() << " Size (sqft)" << endl;

        if (++count % 500 == 0)
        {
            this_thread::sleep_for(chrono::milliseconds(sleepDurationMs));
        }
    }
}

// displays full vector, care - many lines
// Function to print all the contents in the vector with sleep
void printWithSleepy(const vector<Property> &v, int sleepDurationMs)
{
    int count = 0;
    for (Property prop : v)
    {
        cout << count << ": " << prop.ads_id << ": " << prop.bathroom << " rent & " << prop.parking << " Size (sqft)" << endl;

        if (++count % 500 == 0)
        {
            this_thread::sleep_for(chrono::milliseconds(sleepDurationMs));
        }
    }
}

// Function to escape CSV special characters
string escapeCSV(const string &s)
{
    if (s.find(',') != string::npos)
    {
        string result = "\"" + s + "\"";
        return result;
    }
    return s;
}

void writeToCSV(const vector<Property> &v, const string &filePath)
{
    ofstream file(filePath);

    // Check if the file was opened successfully
    if (!file.is_open())
    {
        cerr << "Error: Could not open the file for writing." << endl;
        return;
    }

    // Write the header
    file << "ads_id,prop_name,completion_year,monthly_rent,location,property_type,rooms,parking,bathroom,size,furnished,facilities,additional_facilities,region\n";
    string rooom, paark;

    for (Property prop : v)
    {
        file << prop.ads_id << ","
             << escapeCSV(prop.prop_name) << ","
             << prop.completion_year << ","
             << prop.monthly_rent << ","
             << escapeCSV(prop.location) << ","
             << escapeCSV(prop.property_type) << ","
             << prop.rooms << ","
             << prop.parking << ","
             << prop.bathroom << ","
             << prop.size << ","
             << prop.furnished << ","
             << escapeCSV(prop.facilities) << ","
             << escapeCSV(prop.additional_facilities) << ","
             << escapeCSV(prop.region) << endl;
    }
    file.close();
}