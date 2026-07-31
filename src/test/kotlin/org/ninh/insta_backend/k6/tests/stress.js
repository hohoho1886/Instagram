import {runWorkload} from "../shared/actions.js";

export const options = {
    scenarios: {
        stress_test: {
            executor: 'constant-arrival-rate',

            // number of iterations per second
            rate: 500,
            timeUnit: '1s',

            duration: '5m',

            // k6 starts with this many VUs
            preAllocatedVUs: 200,

            // maximum VUs k6 can create if needed
            maxVUs: 1000,
        },
    },
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

