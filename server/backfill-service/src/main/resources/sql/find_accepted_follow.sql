SELECT "followeeUsername"
FROM "Follows"
WHERE "followerUsername" = ?
  AND "status" = 'ACCEPTED';