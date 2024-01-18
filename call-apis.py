import praw
import os
from dotenv import load_dotenv

# Load the environment variables from .env file
load_dotenv()

reddit = praw.Reddit(
    client_id=os.getenv('REDDIT_CLIENT_ID'),
    client_secret=os.getenv('REDDIT_CLIENT_SECRET'),
    user_agent=os.getenv('REDDIT_USER_AGENT')
)

hot_posts = reddit.subreddit('Python').hot(limit=10)
for post in hot_posts:
    print(post.title)
