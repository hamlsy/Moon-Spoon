<template>
  <main>
    <nav class="navbar">
      <div class="navbar-container">
        <div class="navbar-brand">
          <router-link to="/mainPage" class="logo"><a style="color: #FFD700">Moon</a>-Spoon🥄</router-link>
        </div>
        <ul class="navbar-menu">
          <li><router-link to="/mainPage" class="nav-link">홈</router-link></li>
          <li><router-link to="/noticeList" class="nav-link">공지사항</router-link></li>
          <li v-if="!isLogin"><router-link to="/user/login" class="nav-link">로그인</router-link></li>
          <li v-if="isLogin"><a @click="logout" class="nav-link">로그아웃</a></li>
          <li><router-link to="/user/signup" class="nav-link">회원가입</router-link></li>
          <li><a @click="notValid" class="nav-link">프로필</a></li>
        </ul>
      </div>
    </nav>
    <router-view></router-view>
  </main>
</template>

<script>

export default {
  name: 'App',
  data() {
    return {
      isLogin: false,
      token: localStorage.getItem('token'),
    }
  },
  methods: {
    notValid() {
      alert("아직 구현되지 않은 기능입니다.");
    },
    checkLogin() {
      this.isLogin = !!localStorage.getItem('token');
    },
    logout() {
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
  },
  created() {
    this.checkLogin();

  },
  head() {
    return {
      title: "Moon-Spoon",
      meta: [
        { name: 'description', content: "스스로 학습, 테스트를 Moon-Spoon과 함께하세요."},
        { name: 'keywords', content: 'Custom, test, 테스트, 시험, cbt, 문제, 학습, 문제집, 커스텀, 훈련, 암기, 백지'},
        { name: 'author', content: 'hamlsy'}
      ]
    }
  }

  // components: {
  //
  // }
}
</script>

<style>
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0px;
}

:root {
  --secondary-blue: black; /* 스틸블루 */
  --background-light: #FFFAF0; /* 플로랄 화이트 */
  --text-dark: #333333;
  --text-light: #FFFFFF;
  --hover-yellow: #FFC000; /* 더 진한 노란색 */
}
a{
  text-decoration: none;
  color: inherit;
}

</style>
