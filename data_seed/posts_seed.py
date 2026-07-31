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

cur.execute("SELECT user_id FROM user_account ORDER BY user_id")
user_ids = np.array([row[0] for row in cur.fetchall()])

cur.close()
conn.close()

n_users = len(user_ids)
POSTS_PER_USER = 5
total_rows = n_users * POSTS_PER_USER

# id + author_id
id_col = np.array([str(uuid.uuid4()) for _ in range(total_rows)])
author_col = np.repeat(user_ids, POSTS_PER_USER)

# fixed content
caption_col = np.array(["My new post"] * total_rows)
media_url_col = np.array(["https://picsum.photos/600/400"] * total_rows)

# random created_at, timezone-aware
START_DATE = datetime(2023, 1, 1, tzinfo=timezone.utc)
END_DATE = datetime(2026, 7, 30, tzinfo=timezone.utc)
span_seconds = int((END_DATE - START_DATE).total_seconds())
random_offsets = np.random.randint(0, span_seconds, size=total_rows)
created_at_col = np.array([
    (START_DATE + timedelta(seconds=int(s))).strftime("%Y-%m-%d %H:%M:%S") + "+00"
    for s in random_offsets
])

full_data = np.column_stack([id_col, author_col, caption_col, created_at_col, media_url_col])

np.savetxt(
    "data/posts.csv",
    full_data,
    fmt="%s",
    delimiter=",",
    header="id,author_id,caption,created_at,media_url",
    comments=""
)

print(f"Generated {total_rows} posts")