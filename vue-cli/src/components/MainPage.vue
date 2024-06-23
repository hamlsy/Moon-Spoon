<template>
  <div class="main-page">
    <nav class="navbar">
      <div class="navbar-brand">Moon-Spoon</div>
      <ul class="navbar-menu">
        <li><a href="#" @click="navigateTo('home')">홈</a></li>
        <li><a href="#" @click="navigateTo('shared')">로그인</a></li>
        <li><a href="#" @click="navigateTo('my')">회원가입</a></li>
        <li><a href="#" @click="navigateTo('create')">로그아웃</a></li>
        <li><a href="#" @click="navigateTo('profile')">프로필</a></li>
      </ul>
    </nav>

    <main class="content">
      <h1>마음껏 이용하세요! 당신의 학습을 위해서라면..</h1>

      <div class="feature-cards">
        <div class="card" @click="navigateTo('shared')">
          <h2>공유된 문제지</h2>
          <p>다른 사용자들이 만든 문제지를 열람하고 학습해보세요.</p>
        </div>
        <div class="card" @click="navigateTo('my')">
          <h2>내 문제지</h2>
          <p>직접 만든 문제지로 학습하고 테스트해보세요.</p>
        </div>
      </div>

      <div class="additional-features">
        <h3>추가(예정) 기능</h3>
        <ul>
          <li>
            <a href="#" @click="navigateTo('stats')">학습 통계</a>
            - 나의 학습 진행 상황을 한눈에 확인하세요.
          </li>
          <li>
            <a href="#" @click="navigateTo('community')">학습 커뮤니티</a>
            - 다른 학습자들과 정보를 공유하고 소통하세요.
          </li>
          <li>
            <a href="#" @click="navigateTo('challenge')">일일 도전 과제</a>
            - 매일 새로운 문제로 실력을 향상시켜보세요.
          </li>
        </ul>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. GitHub: https://github.com/hamlsy.</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'MainPage',
  methods: {
    navigateTo(page) {
      console.log('Navigating to:', page);
    }
  }
}
</script>

<style scoped>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
}

.main-page {
  font-family: 'Arial', sans-serif;
  line-height: 1.6;
  color: #333;
  background-color: #F0F8FF; /* 매우 연한 하늘색 배경 */
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  background-color: #B0C4DE; /* 연한 파란색 */
  color: #333;
  padding: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4A4A4A;
}

.navbar-menu {
  list-style-type: none;
  display: flex;
}

.navbar-menu li {
  margin-left: 1rem;
}

.navbar-menu a {
  color: #4A4A4A;
  text-decoration: none;
  transition: color 0.3s;
}

.navbar-menu a:hover {
  color: #1E90FF; /* 밝은 파란색 */
}

.content {
  max-width: 1200px;
  margin: 80px auto 0; /* 상단 여백 추가 */
  padding: 2rem;
  flex: 1;
}

.feature-cards {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.card {
  background-color: #FFF5EE; /* 연한 베이지색 */
  border-radius: 8px;
  padding: 1.5rem;
  width: 45%;
  cursor: pointer;
  transition: background-color 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.card:hover {
  background-color: #FFE4B5; /* 약간 더 진한 베이지색 */
  box-shadow: 0 4px 8px rgba(0,0,0,0.15);
}

.additional-features {
  margin-top: 3rem;
}

.additional-features ul {
  list-style-type: none;
  padding-left: 0;
}

.additional-features li {
  margin-bottom: 1rem;
}

.additional-features a {
  color: #4682B4; /* 스틸블루 */
  text-decoration: none;
  transition: color 0.3s;
}

.additional-features a:hover {
  color: #1E90FF; /* 밝은 파란색 */
}

.footer {
  background-color: #B0C4DE; /* 연한 파란색 */
  color: #4A4A4A;
  text-align: center;
  padding: 1rem;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

h1, h2, h3 {
  color: #4A4A4A;
}

</style>