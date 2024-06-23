<template>
  <body>
  <div class="container">
    <h1 class="logo">Moon-Spoon</h1>
    <form @submit.prevent="signup">
      <div class="input-group">
        <label for="name">사용할 이름(닉네임)</label>
        <input type="text" id="name" v-model="name" required placeholder="이름(닉네임)을 입력하세요.">
      </div>
      <div class="input-group">
        <label for="userId">ID</label>
        <input type="text" id="userId" v-model="userId" required placeholder="아이디(영문 6~12자)">
      </div>
      <div class="input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="password" required placeholder="비밀번호(영문 6~12자)">
      </div>
      <button type="submit">회원가입</button>
    </form>
    <p class="login-link">이미 계정이 있나요? <router-link to="/user/login">로그인</router-link></p>
  </div>
  </body>

</template>

<script>
export default {
  data() {
    return {
      name: '',
      userId: '',
      password: ''
    }
  },
  methods: {
    signup() {
      // 여기에 회원가입 로직을 구현하세요
      console.log('회원가입 시도:', this.name, this.userId, this.password)
    }
  }
}
</script>

<style scoped>
/* CSS 스타일은 아래에 추가됩니다 */
body {
  font-family: Arial, sans-serif;
  background-color: #f5e6d3;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  color: #333;
}

.container {
  background-color: #fff;
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
  color: #555;
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
  text-decoration: underline;
}
</style>