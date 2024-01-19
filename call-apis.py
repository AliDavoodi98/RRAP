from kafka import KafkaProducer
import json
import praw
import os
from dotenv import load_dotenv

# Load the environment variables from .env file
load_dotenv()

# Setup the Reddit connection
reddit = praw.Reddit(
    client_id=os.getenv('REDDIT_CLIENT_ID'),
    client_secret=os.getenv('REDDIT_CLIENT_SECRET'),
    user_agent=os.getenv('REDDIT_USER_AGENT')
)

# Setup Kafka producer
producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                         value_serializer=lambda m: json.dumps(m).encode('ascii'))

# Fetch and send data to Kafka
for submission in reddit.subreddit('worldnews').stream.submissions():
    message = {
        'title': submission.title,
        'created_utc': submission.created_utc,
        'upvotes': submission.score
    }
    producer.send('worldnews', message)
    producer.flush()