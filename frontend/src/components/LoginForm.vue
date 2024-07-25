<template>
  <body>
  <div class="container">
    <h1 class="logo"><router-link to="/mainPage">Moon-Spoon</router-link></h1>
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
  </body>

</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      username: '',
      password: '',
    }
  },
  methods: {
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
  background-color: #FFFAF0; /* 플로랄 화이트 */
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0px;
  color: white;
}

.container {
  background-color: #1B2A49;
  padding: 3rem 2.5rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 400px; /* 컨테이너 너비 조정 */
  height: auto;
  min-height: 550px;
}

.logo {
  text-align: center;
  color: #ffd700;
  font-weight: bold;
  font-size: 2.5rem;
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
  color: #ffd700;
  text-decoration: none;
  font-weight: bold;
}

a:hover {
  //text-decoration: underline;
}
</style>