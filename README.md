# 📊 Distributed Log Analyzer

A multi-threaded, pluggable log analysis tool built in Java.  
Designed to parse and analyze log files using customizable analyzers, then aggregate and output results as JSON reports.

---

## 🚀 Features

- ✅ Modular analyzer system (`COUNT_LEVELS`, `FIND_COMMON_SOURCE`, `DETECT_ANOMALIES`)
- 🧵 Multi-threaded log file processing
- 📂 Reads logs from a specified directory
- 🧠 Aggregates results across multiple log files
- 📄 Outputs final report as a pretty-printed JSON file

---

## 🛠️ Setup

1. Clone the repo:

   ```bash
   git clone https://github.com/yourusername/distributed-log-analyzer.git
   cd distributed-log-analyzer
   ```

2. Build the project (e.g. with Maven or your IDE)

---

## ⚙️ Configuration

Set up your `config.properties` file (in the project root or resources directory):

```properties
logDirectory=logs/
outputFile=output/report.json
analysisTypes=COUNT_LEVELS,FIND_COMMON_SOURCE,DETECT_ANOMALIES
anomalyLevels=ERROR,WARN
anomalyWindowSeconds=30
anomalyThreshold=5
threadPoolSize=4
```

---

## 📦 Running the Program

```bash
java -cp target/your-jar-name.jar src.Main
```

Or run it directly in your IDE via the `Main` class.

---

## 🧪 Example Output

```json
{
  "COUNT_LEVELS": {
    "info": 123,
    "error": 12,
    "warn": 20
  },
  "FIND_COMMON_SOURCE": {
    "sources": [
      "app.service",
      "db.connector"
    ],
    "source_counts": [
      90,
      45
    ],
    "most_common_source": "app.service",
    "most_common_source_count": 90,
    "least_common_source": "db.connector",
    "least_common_source_count": 45
  },
  "DETECT_ANOMALIES": {
    "server1.log": {
      "anomalies": [
        "2024-04-12T12:30:45",
        "2024-04-12T12:31:00"
      ],
      "anomalies_count": 2
    }
  }
}
```

---

## 📁 Project Structure

```
src/
│
├── analyzers/             # Analyzer implementations
├── domain/                # LogEntry and data classes
├── parsers/               # LogParser logic
├── providers/             # Config, thread pool, report writer
├── services/              # Aggregation, file reading
├── tasks/                 # LogFileProcessingTask (Callable)
├── utils/                 # Timer, ExitCodes
└── Main.java              # Entry point
```

---

## 🙌 Contributions

- **Anas Dweik - 322362013**
- **Amr Shwiki - 000000000**

---

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/w1uqWHUp)
