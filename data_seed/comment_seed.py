import numpy as np
import psycopg2
import uuid
from datetime import datetime, timedelta, timezone

conn = psycopg2.connect(
    dbname="postgres",
    user="postgres",
    password="123456",
    host="localhost",
    port="5432"
)
cur = conn.cursor()

cur.execute("SELECT id FROM posts ORDER BY id")
post_ids = np.array([row[0] for row in cur.fetchall()])

cur.execute("SELECT user_id FROM user_account ORDER BY user_id")
user_ids = np.array([row[0] for row in cur.fetchall()])

cur.close()
conn.close()

n_posts = len(post_ids)
n_users = len(user_ids)
COMMENTS_PER_POST = 3
total_rows = n_posts * COMMENTS_PER_POST

post_col = np.repeat(post_ids, COMMENTS_PER_POST)
author_col = user_ids[np.random.randint(0, n_users, size=total_rows)]

comment_id_col = np.array([str(uuid.uuid4()) for _ in range(total_rows)])

# fixed content for every row
content_col = np.array(["Nice post!"] * total_rows)

START_DATE = datetime(2023, 1, 1, tzinfo=timezone.utc)
END_DATE = datetime(2026, 7, 30, tzinfo=timezone.utc)
span_seconds = int((END_DATE - START_DATE).total_seconds())
random_offsets = np.random.randint(0, span_seconds, size=total_rows)
created_at_col = np.array([
    (START_DATE + timedelta(seconds=int(s))).strftime("%Y-%m-%d %H:%M:%S") + "+00"
    for s in random_offsets
])

full_data = np.column_stack([comment_id_col, author_col, content_col, created_at_col, post_col])

np.savetxt(
    "data/comments.csv",
    full_data,
    fmt="%s",
    delimiter=",",
    header="comment_id,author_id,content,created_at,post_id",
    comments=""
)

print(f"Generated {total_rows} comments")