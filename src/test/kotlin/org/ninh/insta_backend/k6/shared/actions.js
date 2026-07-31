import http from 'k6/http';
import { SharedArray } from 'k6/data';
import { check, sleep } from 'k6';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';

const users = new SharedArray('user_ids', function () {
    const fileContent = open('../../user_account.csv');
    const parsedData = papaparse.parse(fileContent, {
        header: true,
        skipEmptyLines: true,
    }).data;

    return parsedData
        .map(row => row.user_id ? row.user_id.trim() : '')
        .filter(id => id.length === 36);
});

const posts = new SharedArray('post_ids', function () {
    const fileContent = open('../../posts.csv');

    const parsedData = papaparse.parse(fileContent, {
        header: true,
        skipEmptyLines: true,
    }).data;

    if (parsedData.length > 0) {
        console.log(`[CSV CHECK] First row raw object: ${JSON.stringify(parsedData[0])}`);
    }

    const cleanPosts = parsedData
        .map(row => {
            // ++ Extract post_id column cleanly
            const val = row.post_id || row.postId || row.id;
            return val ? val.trim() : '';
        })
        .filter(id => id.length === 36);

    if (cleanPosts.length > 0) {
        console.log(`[CSV CHECK] Sample post_id[0]: "${cleanPosts[0]}" (Length: ${cleanPosts[0].length})`);
    } else {
        console.log(`[CSV CHECK] ❌ WARNING: 0 valid UUIDs were found! Check your column headers.`);
    }

    return cleanPosts;
});

const BASE_URL = 'http://localhost:8080/api';

export function runWorkload() {
    const userId = users[(__VU - 1) % users.length];
    const roll = Math.random(); // Pick a float between 0.0 and 1.0

    if (roll < 0.80) {
        getFeedAction(userId);
    }

    else if (roll < 0.95) {
        likePostAction(userId);
    }

    else if (roll < 0.98) {
        commentPostAction(userId);
    }
    else {
        createPostAction(userId);
    }

    sleep(Math.random() * 2 + 1);
}

// ---------------------------------------------------------------------------
// Helper Functions for Each API Endpoint
// ---------------------------------------------------------------------------

function getFeedAction(userId) {
    const payload = JSON.stringify({ userId: userId });
    const params = {
        headers: { 'Content-Type': 'application/json' },
        tags: { name: 'POST /api/feed/get' },
    };

    const res = http.post(`${BASE_URL}/feed/get`, payload, params);

    check(res, {
        'feed status 200': (r) => r.status === 200,
        'feed valid body': (r) => {
            try {
                return !JSON.parse(r.body).error;
            } catch (e) { return false; }
        },
    });
}

function likePostAction(userId) {
    // ++ Pick a random REAL post from the seeded CSV
    const randomPostId = posts[Math.floor(Math.random() * posts.length)];

    const payload = JSON.stringify({
        postId: randomPostId,
        authorId: userId,
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
        tags: { name: 'POST /api/likes/post' },
    };
    const res = http.post(`${BASE_URL}/likes/post`, payload, params);
    if (res.status !== 200 && res.status !== 201) {

        console.log(`STATUS: ${res.status} \vert{} BODY:${res.body}`);
    }
    check(res, {
        'create status 200/201': (r) => r.status === 200 || r.status === 201,
    });
}

function commentPostAction(userId) {
    // ++ Pick a random REAL post from the seeded CSV
    const randomPostId = posts[Math.floor(Math.random() * posts.length)];

    const payload = JSON.stringify({
        postId: randomPostId,
        authorId: userId,
        content: 'Sample comment',
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
        tags: { name: 'POST /api/comment/post' },
    };
    const res = http.post(`${BASE_URL}/comment/post`, payload, params);
    if (res.status !== 200 && res.status !== 201) {
        console.log(`STATUS: ${res.status} \vert{} BODY:${res.body}`);
    }
    check(res, {
        'create status 200/201': (r) => r.status === 200 || r.status === 201,
    });
}

function createPostAction(userId) {
    const payload = JSON.stringify({
        authorId: userId,
        caption: `Sample caption`,
        mediaUrl: `idk`,
    });
    const params = {
        headers: { 'Content-Type': 'application/json' },
        tags: { name: 'POST /api/content/post' },
    };

    const res = http.post(`${BASE_URL}/content/post`, payload, params);
    if (res.status !== 200 && res.status !== 201) {

        console.log(`STATUS: ${res.status} \vert{} BODY:${res.body}`);
    }

    check(res, {
        'create status 200/201': (r) => r.status === 200 || r.status === 201,
    });
}