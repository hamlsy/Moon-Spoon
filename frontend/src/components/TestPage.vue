<template>
  <div class="profile-page">
    <main class="content">
      <section class="hero slide-in-fade">
        <h1 class="main-title">내 프로필</h1>
        <p class="subtitle">{{ userData.nickname }}님의 Moon-Spoon 활동 정보</p>
      </section>

      <section class="profile-info">
        <div class="profile-card slide-in-fade">
          <div class="card-icon">👤</div>
          <h2>기본 정보</h2>
          <ul>
            <li><strong>이름(닉네임):</strong> {{ userData.nickname }}</li>
            <li><strong>아이디:</strong> {{ userData.username }}</li>
            <li><strong>가입날짜:</strong> {{ userData.joinDate }}</li>
            <li><strong>회원 등급:</strong> {{ userData.membershipLevel }}</li>
          </ul>
        </div>

        <div class="profile-card slide-in-fade">
          <div class="card-icon">📊</div>
          <h2>활동 통계</h2>
          <ul>
            <li><strong>내 문제집 수:</strong> {{ userData.myWorkbooksCount }}</li>
            <li><strong>공유한 문제집 수:</strong> {{ userData.sharedWorkbooksCount }}</li>
            <li><strong>내 문제집 테스트 수:</strong> {{ userData.myWorkbookTestsCount }}</li>
            <li><strong>공유 문제집 테스트 수:</strong> {{ userData.sharedWorkbookTestsCount }}</li>
            <li><strong>내 댓글 수:</strong> {{ userData.commentsCount }}</li>
            <li><strong>방문 수:</strong> {{ userData.visitCount }}</li>
          </ul>
        </div>
      </section>

      <section class="additional-features">
        <h3 class="notice-icon">🏆 최근 활동</h3>
        <ul>
          <li v-for="activity in recentActivities" :key="activity.id" class="notice">
            {{ activity.description }}
          </li>
        </ul>
      </section>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'ProfilePage',
  data() {
    return {
      userData: {
        nickname: '문스푼러',
        username: 'moonspoon123',
        joinDate: '2023-09-15',
        membershipLevel: '골드',
        myWorkbooksCount: 15,
        sharedWorkbooksCount: 5,
        myWorkbookTestsCount: 50,
        sharedWorkbookTestsCount: 30,
        commentsCount: 25,
        visitCount: 100
      },
      recentActivities: [
        { id: 1, description: '새 문제집 "JavaScript 기초" 생성' },
        { id: 2, description: '"Python 고급" 문제집 공유' },
        { id: 3, description: '"데이터 구조" 문제집 테스트 완료' }
      ]
    }
  }
}
</script>

<style scoped>
/* 기존 스타일을 유지하면서 프로필 페이지에 맞게 일부 수정 */

.profile-page {
  background: linear-gradient(rgba(255,244,255,0.05) 40%, rgba(232,221,0,0.53));
  color: #191f28;
  min-height: 100vh;
  display: flex;
  width: 100%;
  flex-direction: column;
}

.content {
  max-width: 1100px;
  width: 85%;
  margin: 80px auto 0;
  padding: 2rem;
  flex: 1;
}

.hero {
  text-align: center;
  padding: 3rem 0;
  background-color: white;
  border-radius: 12px;
  margin-bottom: 3rem;
}

.main-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: black;
}

.subtitle {
  font-size: 1.2rem;
  color: black;
}

.profile-info {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.profile-card {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  width: 45%;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border: 2px solid #FFD700;
}

.profile-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.card-icon {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.profile-card h2 {
  color: black;
  margin-bottom: 1rem;
}

.profile-card ul {
  list-style-type: none;
  padding-left: 0;
}

.profile-card li {
  margin-bottom: 0.5rem;
}

.additional-features {
  margin-top: 3rem;
  background-color: white;
  padding: 2rem;
  border-radius: 12px;
}

.notice-icon {
  font-size: 1.4rem;
  margin-bottom: 1rem;
}

.notice {
  cursor: pointer;
  background-color: white;
  border-radius: 12px;
  padding: 1rem;
  margin-bottom: 1rem;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border-left: 2px solid lightgray;
}

.notice:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.footer {
  background-color: #f2f4f6;
  color: #191f28;
  text-align: center;
  padding: 1rem;
  margin-top: 2rem;
}

.footer a {
  color: black;
  text-decoration: none;
}

/* 애니메이션 효과 */
@keyframes slideInFade {
  0% {
    opacity: 0;
    transform: translateY(-20%);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

.slide-in-fade {
  display: inline-block;
  animation: slideInFade 1.4s cubic-bezier(0.5, 0.01, 0.115, 0.5);
}
</style>