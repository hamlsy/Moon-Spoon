<template>
  <div class="main-page">
    <nav class="navbar">
      <div class="navbar-brand"><router-link to="/mainPage">Moon-Spoon</router-link></div>
      <ul class="navbar-menu">
        <li><router-link to="/mainPage">홈</router-link></li>
        <li><router-link to="/user/login" v-if="!isLogin">로그인</router-link></li>
        <li v-if="isLogin"><a @click="logout">로그아웃</a></li>
        <li><router-link to="/user/signup">회원가입</router-link></li>
        <li><a @click="notValid">프로필</a></li>
      </ul>
    </nav>

    <main class="content">
      <h1>마음껏 이용하세요! 당신의 학습을 위해서라면..</h1>

      <div class="feature-cards">
        <div class="card" @click="notValid">
          <h2>공유된 문제지</h2>
          <p>다른 사용자들이 만든 문제지를 열람하고 학습해보세요.</p>
        </div>
        <div class="card" @click="goMyWorkbook">
            <h2>내 문제지</h2>
            <p>직접 만든 문제지로 학습하고 테스트해보세요.</p>
        </div>
      </div>

      <div class="additional-features">
        <h3>추가(예정) 기능</h3>
        <ul>
          <li>
            <a @click="notValid">공지사항</a>
            - 나의 학습 진행 상황을 한눈에 확인하세요.
          </li>
          <li>
            <a @click="notValid">학습 커뮤니티</a>
            - 다른 학습자들과 정보를 공유하고 소통하세요.
          </li>
          <li>
            <a @click="notValid">...</a>
            - ...추가 될 내용 ...
          </li>
        </ul>
      </div>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. GitHub: https://github.com/hamlsy</p>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'MainPage',
  data(){
    return {
      isLogin: false,
      token: localStorage.getItem('token')
    }
  },
  methods: {
    notValid(){
      alert("아직 구현되지 않은 기능입니다.");
    },
    checkLogin(){
      this.isLogin = !!localStorage.getItem('token');
    },
    goMyWorkbook(){
      if(!this.token){
        alert("로그인이 필요한 서비스입니다.");
      }else{
        this.$router.push("/api/myWorkbook");
      }
    },
    navigateTo(page) {
      console.log('Navigating to:', page);
    },
    logout(){
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    }
  },
  created() {
    this.checkLogin()
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
  color: var(--text-dark);
  background-color: #FFFAF0; /* 플로랄 화이트 */
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  background-color: #1B2A49;
  color: var(--text-light);
  padding: 0.1rem 2rem;
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
  color: #FFD700;
}

.navbar-brand::after {
  content: "🥄";
  margin-right: 5px;
}

.navbar-menu {
  list-style-type: none;
  display: flex;
}

.navbar-menu li {
  margin-left: 1rem;
}

.navbar-menu a {
  color: var(--text-light);
  text-decoration: none;
  transition: color 0.3s;
}

.navbar-menu a:hover {
  color: #FFD700;
}

.content {
  max-width: 1200px;
  margin: 80px auto 0;
  padding: 2rem;
  flex: 1;
}

.feature-cards {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.card {
  background-color: var(--text-light);
  border-radius: 8px;
  padding: 1.5rem;
  width: 45%;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  border-left: 4px solid #FFD700;
}

.card:hover {
  background-color: #FFD700;
  color: var(--text-dark);
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
  color: #1B2A49;
  text-decoration: none;
  transition: color 0.3s;
}

.additional-features a:hover {
  color: #FFD700;
}

.footer {
  background-color: #1B2A49;
  color: var(--text-light);
  text-align: center;
  padding: 0.1rem;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
}

h1, h2, h3 {
  color: #1B2A49;
}

h1::after, h2::after, h3::after {
  content: "";
  display: block;
  width: 50px;
  height: 3px;
  background-color: #FFD700;
  margin-top: 10px;
}

</style>