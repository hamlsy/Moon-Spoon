<template>
  <body>
  <div class="page-container">
    <nav class="navbar">
      <div class="navbar-container">
        <div class="navbar-brand">
          <router-link to="/mainPage" class="logo"><a style="color: #FFD700">Moon</a>-Spoon🥄</router-link>
        </div>
        <ul class="navbar-menu">
          <li><router-link to="/mainPage" class="nav-link">홈</router-link></li>
          <li v-if="!isLogin"><router-link to="/user/login" class="nav-link">로그인</router-link></li>
          <li v-if="isLogin"><a @click="logout" class="nav-link">로그아웃</a></li>
          <li><router-link to="/user/signup" class="nav-link">회원가입</router-link></li>
          <li><a @click="notValid" class="nav-link">프로필</a></li>
        </ul>
      </div>
    </nav>
    <div class="moon"></div>
    <div class="container">
      <h1 class="login_logo"><router-link to="/mainPage">Moon-Spoon</router-link></h1>
      <form @submit.prevent="login">
        <div class="input-group">
          <label for="userId">ID</label>
          <input type="text" id="username" v-model="username" required placeholder="아이디를 입력하세요.">
        </div>
        <div class="input-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="password" required placeholder="비밀번호를 입력하세요.">
        </div>
        <button type="submit">로그인</button>
      </form>
      <p class="signup-link">계정이 없나요? <router-link to="/user/signup">회원가입</router-link></p>
    </div>
  </div>
  </body>

</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      username: '',
      password: '',
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
    login() {
      // 여기에 로그인 로직을 구현하세요
      axios.post("/api/login", {
        username: this.username,
        password: this.password
      })
          .then((res) =>{
            alert("로그인 성공!");
            const token = JSON.stringify(res.headers.get('Authorization')).replaceAll(`"`, "");
            localStorage.setItem("token", token);
            this.$router.push("/mainPage");
            console.log("로그인 성공", res);
          })
          .catch((res) => {
            alert("로그인 실패")
            console.log("로그인 실패", res);
          })
    }
  }
}
</script>

<style scoped>
/* CSS 스타일은 아래에 추가됩니다 */
body {
  font-family: Arial, sans-serif;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0px;
  color: white;
}

.container {
  margin-top: 60px;
  background-color: #1B2A49;
  padding: 1.5rem 2.5rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 300px; /* 컨테이너 너비 조정 */
  height: auto;
  min-height: 400px;
}

.login_logo {
  text-align: center;
  color: #ffd700;
  font-weight: bold;
  font-size: 2.2rem;
  margin-bottom: 3rem;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

form {
  display: flex;
  flex-direction: column;
}

.input-group {
  margin-bottom: 2.5rem;
  width: 100%; /* 입력 그룹의 너비를 100%로 설정 */
}

label {
  display: block;
  margin-bottom: 0.7rem;
  font-weight: bold;
  color: white;
  font-size: 1.1rem;
}

input {
  width: 100%; /* 입력 필드의 너비를 100%로 설정 */
  padding: 0.9rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s, box-shadow 0.3s;
  box-sizing: border-box; /* padding을 width에 포함 */
}

input:focus {
  border-color: #ffd700;
  box-shadow: 0 0 5px rgba(255, 215, 0, 0.5);
  outline: none;
}

button {
  width: 100%; /* 버튼의 너비를 100%로 설정 */
  background-color: #ffd700;
  color: #fff;
  border: none;
  padding: 1rem;
  border-radius: 4px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-top: 1.5rem;
  font-weight: bold;
  box-sizing: border-box; /* padding을 width에 포함 */
}

button:hover {
  background-color: #e6c200;
}

.signup-link, .login-link {
  text-align: center;
  margin-top: 2rem;
  font-size: 1rem;
}

a {
  /** color: #ffd700; **/
  text-decoration: none;
  font-weight: bold;
}

a:hover {
//text-decoration: underline;
}

.page-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background:  linear-gradient(rgba(255,244,255,0.05), rgba(232,221,0,0.53));
  display: flex;
  justify-content: center;
  align-items: center;
  background-size: 400% 400%;
  animation: change-background 3s ease-out forwards;
}
.moon {
  position: absolute;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: #ffd700;
  box-shadow: 0 0 20px #ffd700;
  top: calc(40% - 200px); /* 로그인 폼의 약 절반 높이에서 시작 */
  left: calc(50% - 220px); /* 로그인 폼의 왼쪽 상단에 걸치도록 조정 */
  animation: rise-moon 1s ease-out forwards;
}
@keyframes change-background {
  0% {
    /** background: linear-gradient(rgba(255,244,255,0.05), rgba(232,221,0,0.53)); **/
    background-color: white;
  }
  100% {
    background-color: rgba(0,13,181,0.42);
  }
}


@keyframes rise-moon {
  from { transform: translateY(40px); opacity: 0; }
  to { transform: translateY(0px); opacity: 1; }
}

.navbar-container{
  color: #191f28;
}
</style>