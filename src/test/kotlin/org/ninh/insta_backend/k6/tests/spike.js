import {runWorkload} from "../shared/actions.js";

export const options = {
    stages: [
        { duration: "2m", target: 100 },
        { duration: "30s", target: 1000 },
        { duration: "2m", target: 1000 },
        { duration: "30s", target: 100 }


    ],
    thresholds: {
        // ++ Separate metrics per action to identify specific bottlenecks
        'http_req_duration{name:POST /api/feed/get}': ['p(95)<200'],
        'http_req_duration{name:POST /api/likes/post}': ['p(95)<150'],
        'http_req_duration{name:POST /api/content/post}': ['p(95)<400'],
        'http_req_duration{name:POST /api/comment/post}': ['p(95)<400'],
        'http_req_failed': ['rate<0.01'], // Global error rate < 1%
    },
};

export default function () {
    runWorkload();
}