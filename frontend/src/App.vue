<template>
  <main @click="handleGlobalClick">
    <nav class="navbar">
      <div class="navbar-container" >
        <div class="navbar-brand" >
          <router-link to="/mainPage" class="logo"><a style="color: #FFD700">Moon</a>-Spoon🥄</router-link>
        </div>
        <button class="navbar-toggle" @click.stop="toggleMenu">
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </button>
        <ul class="navbar-menu" :class="{ 'active': menuActive }" @click.stop>
          <li><router-link to="/mainPage" class="nav-link" @click.native="closeMenu">홈</router-link></li>
          <li><router-link to="/noticeList" class="nav-link" @click.native="closeMenu">공지사항</router-link></li>
          <li v-if="!isLogin"><router-link to="/user/login" class="nav-link" @click.native="closeMenu">로그인</router-link></li>
          <li v-if="isLogin"><a @click="logout" class="nav-link" @click.native="closeMenu">로그아웃</a></li>
          <li><router-link to="/user/signup" class="nav-link" @click.native="closeMenu">회원가입</router-link></li>
          <li><router-link to="/profile" class="nav-link" @click.native="closeMenu">프로필</router-link></li>
        </ul>
      </div>
    </nav>
    <router-view></router-view>
  </main>
</template>

<script>

import axios from "axios";

export default {
  name: 'App',
  data() {
    return {
      isLogin: false,
      token: localStorage.getItem('token'),
      menuActive: false
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
    toggleMenu() {
      this.menuActive = !this.menuActive;
    },
    closeMenu() {
      this.menuActive = false;
    },
    handleGlobalClick() {
      if (this.menuActive) {
        this.closeMenu();
      }
    },
    checkTokenExpiration() {
      const tokenExpiration = localStorage.getItem('tokenExpiration');
      const now = Date.now();

      if (tokenExpiration && now > parseInt(tokenExpiration)) {
        this.autoLogout();
      }
    },
    autoLogout() {
      // 토큰 및 만료 시간 제거
      localStorage.removeItem('token');
      localStorage.removeItem('tokenExpiration');
      // 로그아웃 후 로그인 페이지로 리디렉션
      this.$router.push('/mainPage');
    }




  },
  created() {
    this.checkLogin();
    this.checkTokenExpiration();
    // 주기적인 토큰 만료 검사 (예: 1분마다)
    setInterval(this.checkTokenExpiration, 60000);

    // Axios 인터셉터 설정
    axios.interceptors.request.use(request => {
      const token = localStorage.getItem('token');
      const tokenExpiration = localStorage.getItem('tokenExpiration');
      const now = Date.now();

      if (token && tokenExpiration && now > parseInt(tokenExpiration)) {
        this.autoLogout();
      }

      return request;
    }, error => {
      return Promise.reject(error);
    });
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
