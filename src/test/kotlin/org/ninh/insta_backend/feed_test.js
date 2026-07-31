import http from 'k6/http';
import { SharedArray } from 'k6/data';
import { check, sleep } from 'k6';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';

const users = new SharedArray('user_ids', function () {
    const fileContent = open('./user_account.csv');

    // Parse CSV into objects using headers
    const parsedData = papaparse.parse(fileContent, {
        header: true,
        skipEmptyLines: true,
    }).data;

    // Extract ONLY the clean user_id string from each row
    return parsedData
        .map(row => row.user_id ? row.user_id.trim() : '')
        .filter(id => id.length === 36); // Keeps strictly valid 36-char UUIDs
});

export const options = {
    stages: [
        { duration: '30s', target: 15 }, // Warm-up: Ramp up to normal load (15 VUs / ~6 RPS)
        { duration: '2m',  target: 30 }, // Peak Traffic: Hold at peak hour load (30 VUs / ~12-15 RPS)
        { duration: '2m',  target: 75 }, // Safety Buffer: Push to 2.5x peak (75 VUs / ~35 RPS)
        { duration: '30s', target: 0  }, // Ramp-down
    ],
    thresholds: {
        'http_req_duration{name:POST /api/feed/get}': ['p(95)<200'], // Goal: p95 under 200ms
        'http_req_failed': ['rate<0.001'],                 // Error rate below 0.1%
    },
};

export default function () {
    // ++ Step 2: Pick a random user from the loaded CSV array
    const userId = users[Math.floor(Math.random() * users.length)];

    // ++ Step 3: Pass user_id in the GET request (Query Param or Header)

    // Option A: Pass user_id as a Query Parameter (e.g. /feed?user_id=UUID)
    const url = `http://localhost:8080/api/feed/get`;

    const payload = JSON.stringify({
        userId: userId,
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
        tags: { name: 'POST /api/feed/get' },
    };

    // Execute GET request
    const response = http.post(url, payload, params);
    //console.log(`STATUS: ${response.status} \vert{} BODY:${response.body}`);


    // Validate response
    check(response, {
        'status is 200': (r) => r.status === 200,
        'has feed array': (r) => {
            try {
                const body = JSON.parse(r.body);
                return body.error === null || body.error === undefined;
            } catch (e) {
                return false;
            }
        },
    });


    // Simulate user scroll wait time (1 to 3 seconds)
    sleep(Math.random() * 2 + 1);
}