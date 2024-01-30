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
            
      1. You can access Reddit's data through their API. To do this, you'll need to create a Reddit account, register an application, and get your API credentials (client ID, client secret, and user agent).
      2. Create a ```.env``` file in the root directory of the project and specify these arguments.
         ```
            REDDIT_CLIENT_ID='YOUR_CLIENT_ID'
            REDDIT_CLIENT_SECRET='YOUR_CLIENT_SECRE'
            REDDIT_USER_AGENT='YOUR_APP_NAME/version by /u/YOUR_REDDIT_USERNAME'
         ```
   3. Running Apache Kafka:
         1. Start the Kafka server:
            Run this command in the root directory of installed Kafka:
            
            ```sh
            bin/kafka-server-start.sh config/server.properties
            ```
            
         3. Create a Topic:

            ```sh
            bin/kafka-topics.sh --create --topic worldnews --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
            ```
            
         5. Use a Process Manager to Start Kafka Broker Automatically:
            * To ensure that your Kafka broker starts automatically when the EC2 instance boots, you can create a systemd service unit for Kafka:
            Create a new service file in /etc/systemd/system/, e.g., kafka.service:

            ```sh
            sudo nano /etc/systemd/system/kafka_broker.service
            ```
            
            * Add the following content to the file, making sure to replace ```/path/to/kafka``` with the actual directory path where Kafka is installed:

            ```ini
            [Unit]
            Description=Apache Kafka Server
            Documentation=http://kafka.apache.org/documentation.html
            Requires=zookeeper.service
            After=zookeeper.service

            [Service]
            Type=simple
            User=kafka
            ExecStart=/path/to/kafka/bin/kafka-server-start.sh /path/to/kafka/config/server.properties
            ExecStop=/path/to/kafka/bin/kafka-server-stop.sh
            Restart=on-abnormal

            [Install]
            WantedBy=multi-user.target
            ```

            * After that, type following commands to Enable and Start Kafka Service:
            
            ```sh
            sudo systemctl daemon-reload
            sudo systemctl enable kafka_broker.service
            sudo systemctl start kafka_broker.service
            ```
            
      5. Use a Process Manager to Automatically Publish Data to Kafka Topics:
         * Create a new service file in ```/etc/systemd/system/```, e.g., ```call_reddit_kafka.service```:

         ```sh
         sudo nano /etc/systemd/system/call_reddit_kafka.service
          ```
         
         * Add following to the file:
           
         ```sh
         [Unit]
         Description=Reddit Kafka Service
         After=network.target

         [Service]
         User=ec2-user
         ExecStart=/usr/bin/python3 /path/to/call-apis.py
         Restart=always

         [Install]
         WantedBy=multi-user.target
         ```
         
      
