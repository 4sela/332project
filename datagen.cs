using System;
using System.IO;

class Program
{
    static void Main(string[] args)
    {
        Console.WriteLine("Generating test data for 20 workers...");

        for (int worker = 1; worker <= 5; worker++)
        {
            GenerateRecords($"input{worker}/data", 10000);  // 10,000 records each
        }

        Console.WriteLine("Done! Generated 20 files with 1MB each.");
    }

    static void GenerateRecords(string path, int numRecords)
    {
        var random = new Random();
        var directory = Path.GetDirectoryName(path);
        if (!string.IsNullOrEmpty(directory))
            Directory.CreateDirectory(directory);

        using var file = File.Create(path);

        for (int i = 0; i < numRecords; i++)
        {
            var record = new byte[100];

            for (int j = 0; j < 10; j++)
                record[j] = (byte)random.Next(0, 256);

            for (int j = 10; j < 100; j++)
                record[j] = (byte)random.Next(0, 256);

            file.Write(record, 0, 100);
        }

        Console.WriteLine($"  Created {path} ({numRecords * 100:N0} bytes)");
    }
}
