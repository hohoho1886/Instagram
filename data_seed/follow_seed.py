import numpy as np
import psycopg2
from datetime import datetime, timedelta, timezone


conn = psycopg2.connect("dbname=postgres user=postgres password=123456 host=localhost port=5432")
cur = conn.cursor()
cur.execute("SELECT user_id FROM user_account ORDER BY user_id")
user_ids = np.array([row[0] for row in cur.fetchall()])
cur.close()
conn.close()

n_users = len(user_ids)
FOLLOWS_PER_USER = 10

# repeat each user_id 10 times -> follower column
followers = np.repeat(user_ids, FOLLOWS_PER_USER)

# random following picks, same shape
following = user_ids[np.random.randint(0, n_users, size=n_users * FOLLOWS_PER_USER)]

# remove self-follows by reshuffling collisions
self_mask = followers == following
while self_mask.any():
    following[self_mask] = user_ids[np.random.randint(0, n_users, size=self_mask.sum())]
    self_mask = followers == following

# dedupe (follower, following) pairs
pairs = np.unique(np.stack([followers, following], axis=1), axis=0)

START_DATE = datetime(2023, 1, 1)
END_DATE = datetime(2026, 7, 30)  # today, or whatever cutoff makes sense
span_seconds = int((END_DATE - START_DATE).total_seconds())
random_offsets = np.random.randint(0, span_seconds, size=len(pairs))
created_ats = np.array([
    (START_DATE + timedelta(seconds=int(s))).strftime("%Y-%m-%d %H:%M:%S") + "+00"
    for s in random_offsets
])

print(f"{len(pairs)} rows after dedup (started with {len(followers)})")

full_data = np.column_stack([pairs, created_ats])

np.savetxt(
    "data/follows.csv",
    full_data,
    fmt="%s",
    delimiter=",",
    header="follower_id,following_id,created_at",
    comments=""
)