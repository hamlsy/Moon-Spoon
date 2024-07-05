<template>
  <body>
  <div class="container">
    <h1 class="logo"><router-link to="/mainPage">Moon-Spoon</router-link></h1>
    <form @submit.prevent="signup">
      <div class="input-group">
        <label for="name">사용할 이름(닉네임)</label>
        <div class="input-with-button">
          <input type="text" id="name" v-model="name" required placeholder="이름(닉네임)(4~16자)"
                 :class="{ 'error': nameError, 'success': nameSuccess}">
          <button type="button" @click="checkNameDuplicate">중복확인</button>
        </div>
        <p v-if="nameError" class="error-message">{{ nameError }}</p>
        <p v-if="nameSuccess" class="success-message">올바른 닉네임입니다.</p>
      </div>
      <div class="input-group">
        <label for="userId">ID</label>
        <div class="input-with-button">
          <input type="text" id="userId" v-model="username" required placeholder="아이디(영문 6~12자)"
                 :class="{ 'error': usernameError, 'success': usernameSuccess  }">
          <button type="button" @click="checkUsernameDuplicate">중복확인</button>
        </div>
        <p v-if="usernameError" class="error-message">{{ usernameError }}</p>
        <p v-if="usernameSuccess" class="success-message">올바른 아이디입니다.</p>
      </div>
      <div class="input-group">
      <label for="password">Password</label>
      <input type="password" id="password" v-model="password" required placeholder="비밀번호(영문, 숫자 6~24자)"
             :class="{ 'error': passwordError }">
        <p v-if="passwordError" class="error-message">{{ passwordError }}</p>
        <p v-if="isPasswordValid" class="success-message">올바른 비밀번호입니다.</p>
      </div>
      <button type="submit" :disabled="!isFormValid">회원가입</button>
    </form>
    <p class="login-link">이미 계정이 있나요? <router-link to="/user/login">로그인</router-link></p>
  </div>
  </body>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      name: '',
      username: '',
      password: '',
      nameError: '',
      usernameError: '',
      passwordError: '',
      nameSuccess: false,
      usernameSuccess: false,
      isNameValid: false,
      isUsernameValid: false
    }
  },
  computed: {
    isFormValid() {
      return this.isNameValid && this.isUsernameValid && this.isPasswordValid;
    },
    isPasswordValid() {
      return /^[A-Za-z0-9]{6,24}$/.test(this.password);
    },
    isNameFormatValid() {
      return this.name.length >= 4 && this.name.length < 16 && !/[!@#$%^&*(),.?":{}|<>]/.test(this.name);
    },
    isUsernameFormatValid() {
      return /^[A-Za-z0-9]{6,20}$/.test(this.username);
    }
  },
  watch: {
    name() {
      this.validateName();
      this.isNameValid = false;
      this.nameError = '';
    },
    username() {
      this.validateUsername();
      this.isUsernameValid = false;
      this.usernameError = '';
    },
    password() {
      this.validatePassword();
    }
  },
  methods: {
    validateName() {
      this.isNameValid = false;
      this.nameSuccess = false;
      if (!this.isNameFormatValid) {
        this.nameError = '닉네임은 4~20자 사이여야 하며 특수문자를 포함할 수 없습니다.';
      } else {
        this.nameError = '';
      }
    },
    validateUsername() {
      this.isUsernameValid = false;
      this.usernameSuccess = false;
      if (!this.isUsernameFormatValid) {
        this.usernameError = '아이디는 영문과 숫자로만 6~12자로 구성되어야 합니다.';
      } else {
        this.usernameError = '';
      }
    },
    checkNameDuplicate() {
      axios.post("/user/checkName", { name: this.name })
          .then(response => {
            if (response.data.validate) {
              this.isNameValid = true;
              this.nameSuccess = true;
              this.nameError = '';
            } else {
              this.nameError = '이미 사용 중인 이름입니다.';
              this.nameSuccess = false;
            }
          })
          .catch(error => {
            this.nameError = '이름 중복 확인 중 오류가 발생했습니다.';
            this.nameSuccess = false;
            console.error(error);
          });
    },
    checkUsernameDuplicate() {
      axios.post("/user/checkUsername", { username: this.username })
          .then(response => {
            if (response.data.validate) {
              this.isUsernameValid = true;
              this.usernameSuccess = true;
              this.usernameError = '';
            } else {
              this.usernameError = '이미 사용 중인 아이디입니다.';
              this.usernameSuccess = false;
            }
          })
          .catch(error => {
            this.usernameError = '아이디 중복 확인 중 오류가 발생했습니다.';
            this.usernameSuccess = false;
            console.error(error);
          });
    },
    validatePassword() {
      if (!this.isPasswordValid) {
        this.passwordError = '비밀번호는 영문과 숫자로만 6~12자로 구성되어야 합니다.';
      } else {
        this.passwordError = '';
      }
    },
    signup() {
      axios.post("/user/signup", {
        name: this.name,
        username: this.username,
        password: this.password
      })
          .then((res) =>{
            alert("회원가입 성공!");
            this.$router.push("/user/login");
            console.log("회원가입 성공", res);
          })
          .catch((error) => {
            alert(error.response.data.message);
            console.log("로그인 실패", error);
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
  margin: 0;
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

.input-with-button {
  display: flex;
  gap: 10px;
}

.input-with-button input {
  flex-grow: 1;
}

.input-with-button button {
  padding: 0.5rem 1rem;
  color: #1B2A49;
  width: 140px;
  margin-top: 0;
}

.error {
  border-color: red;
}

.error-message {
  color: red;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}
.success {
  border-color: green;
}

.success-message {
  color: green;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}
</style>