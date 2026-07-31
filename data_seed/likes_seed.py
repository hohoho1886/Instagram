import numpy as np
import psycopg2
from datetime import datetime, timedelta, timezone

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="123456",
    host="localhost",
    port="5432"
)
cur = conn.cursor()

# pull post_id (and its own author, if you want to exclude self-likes)
cur.execute("SELECT id, author_id FROM posts ORDER BY id")
post_rows = cur.fetchall()
post_ids = np.array([r[0] for r in post_rows])
post_author_ids = np.array([r[1] for r in post_rows])  # remove this if not needed

# pull all user_ids (who can "like")
cur.execute("SELECT user_id FROM user_account ORDER BY user_id")
user_ids = np.array([row[0] for row in cur.fetchall()])

cur.close()
conn.close()

n_posts = len(post_ids)
n_users = len(user_ids)
LIKES_PER_POST = 5

# repeat each post 5 times -> post_id column
post_col = np.repeat(post_ids, LIKES_PER_POST)
post_author_col = np.repeat(post_author_ids, LIKES_PER_POST)  # to check self-likes

# random liker picks
liker_col = user_ids[np.random.randint(0, n_users, size=n_posts * LIKES_PER_POST)]

# OPTIONAL: prevent a post's own author from liking their own post
self_like_mask = liker_col == post_author_col
while self_like_mask.any():
    liker_col[self_like_mask] = user_ids[np.random.randint(0, n_users, size=self_like_mask.sum())]
    self_like_mask = liker_col == post_author_col

# dedupe (post_id, author_id) pairs -- a user can't like the same post twice
pairs = np.unique(np.stack([post_col, liker_col], axis=1), axis=0)
print(f"{len(pairs)} rows after dedup (started with {len(post_col)})")

np.savetxt(
    "data/likes.csv",
    pairs,
    fmt="%s",
    delimiter=",",
    header="post_id,author_id",
    comments=""
)