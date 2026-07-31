import {runWorkload} from "../shared/actions.js";
export const options = {
    stages: [
        // Normal test
        { duration: '1m', target: 50 },
        { duration: '5m', target: 190 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        'http_req_duration{name:POST /api/feed/get}': ['p(95)<200'],
        'http_req_duration{name:POST /api/likes/post}': ['p(95)<150'],
        'http_req_duration{name:POST /api/content/post}': ['p(95)<400'],
        'http_req_duration{name:POST /api/comment/post}': ['p(95)<400'],
        'http_req_failed': ['rate<0.01'], // Global error rate < 1%
    },
};

export default function () { runWorkload() };