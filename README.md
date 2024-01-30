# RRAP

A real-time trend analysis platform for Reddit's worldnews subreddit, capable of processing and visualizing the popularity of topics over time.

## Features

   - Real-time data ingestion from Reddit using PRAW
   - Stream processing with Apache Flink
   - Persistent storage in Amazon RDS
   - Data visualization with Grafana

## Prerequisites
Before you begin, ensure you meet the following requirements:
   - Java 8 or higher is installed on your system.
   - An active AWS account with an RDS instance setup.
   - Apache Flink and Kafka are installed and configured on your system or in your cloud environment.
   - Grafana is installed and configured for visualizing the processed data.
Some necessary libraries' versions have been specified in the ```requirements.txt```

## Installation
To get this project up and running on your system, follow these steps:
   1. Clone the project repository:
      ```sh
      git clone https://github.com/yourusername/RRAP.git
      ```
   2. Access Reddit Data Stream:
         Reddit offers a vast stream of data from various subreddits that can be used for real-time analytics. 
         <details><summary><b>Show instructions</b></summary>
