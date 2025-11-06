import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    // ⚠️ 로컬 테스트는 VUs(가상 유저)를 낮게 시작하세요.
    vus: 100,
    duration: '10m', // 시간도 짧게
};

// ⚠️ 사전 준비:
// 로컬 DB에 'student001' ~ 'student010'까지
// 10명의 학생 데이터가 미리 INSERT 되어 있어야 합니다.
// (비밀번호는 모두 'test1234'라고 가정)

export default function () {
    // 1. ✅ 로컬에서 실행할 것이므로 'localhost' 사용
    let url = 'http://localhost:8080/api/auth/login';

    // 2. 각 가상 유저(VU)마다 고유한 학번 생성
    let studentNumber = `student${String(__VU).padStart(3, '0')}`;
    let password = '1234'; // 모든 테스트 학생의 공통 비밀번호

    // 3. JSON 데이터 생성
    let payload = JSON.stringify({
        studentNumber: studentNumber,
        password: password,
    });

    // 4. 헤더 설정
    let params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    // 5. POST 요청
    let res = http.post(url, payload, params);

    // 6. 응답 확인
    check(res, {
        '로그인 성공 (status 200)': (r) => r.status === 200,
        '액세스 토큰이 반환되었는가?': (r) => r.json('accessToken') !== null,
    });

    sleep(1);
}