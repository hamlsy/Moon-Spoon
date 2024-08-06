<template>
  <div class="main-page">

    <main class="content">
      <section class="hero">
        <h1 class="main-title slide-in-fade">나만의 학습, Moon-Spoon과 함께</h1><br>
        <p class="subtitle slide-in-fade">Moon Spoon을 통한 사용자 테스트로 학습의 효율을 높여보세요.</p>
      </section>

      <section class="features">
        <div class="feature-card slide-in-fade" @click="goSharedWorkbookList" @mouseover="hover = 1" @mouseleave="hover = null" :class="{ 'hovered': hover === 1 }">
          <div class="card-icon">📚</div>
          <h2>공유된 문제집</h2>
          <p>다른 사용자들이 만든 문제집을 열람하고 학습해보세요.</p>
          <div class="card-action">바로가기 →</div>
        </div>
        <div class="feature-card slide-in-fade" @click="goMyWorkbook" @mouseover="hover = 2" @mouseleave="hover = null" :class="{ 'hovered': hover === 2 }">
          <div class="card-icon">📝</div>
          <h2>내 문제집</h2>
          <p>직접 만든 문제집으로 학습하고 테스트해보세요.</p>
          <div class="card-action">바로가기 →</div>
        </div>
      </section>

      <section class="additional-features">
        <h1 class="notice-icon"><router-link to="/noticeList"> 📢 공지사항</router-link></h1>
        <ul>
          <div v-for="(notice) in notices" :key="notice.id" class="notice-list">
            <li @click="goNoticeDetail(notice.id)" class="notice" ><a style="color:red">[공지]</a>{{ notice.title }}</li>
          </div>
        </ul>
      </section>
    </main>

    <footer class="footer">
      <p>&copy; 2024 Moon-Spoon. <a href="https://github.com/hamlsy" target="_blank" rel="noopener noreferrer">GitHub</a></p>
    </footer>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: 'MainPage',
  data() {
    return {
      isLogin: false,
      token: localStorage.getItem('token'),
      hover: null,
      hoverFeature: null,
      notices: []
    }
  },
  methods: {
    notValid() {
      alert("아직 구현되지 않은 기능입니다.");
    },
    goMyWorkbook() {
      if (!this.token) {
        alert("로그인이 필요한 서비스입니다.");
      } else {
        this.$router.push("/myWorkbook");
      }
    },
    goNoticeDetail(id){
      this.$router.push(`/notice/${id}`);
    },
    fetchNotices(){
      axios.get("/api/notice/recentNotices")
          .then((res) => {
            this.notices = res.data;
            console.log(res, "fetch notices");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    goSharedWorkbookList(){
      this.$router.push("/sharedWorkbookList")
    }
  },
  created() {
    this.fetchNotices();
  }
}
</script>

<style scoped>
/** slide fade **/
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
/** slide fade end **/
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}

.main-page {
  background: linear-gradient(rgba(255,244,255,0.05) 40%, rgba(232,221,0,0.53));
  color: #191f28;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.content {
  max-width: 1200px;
  margin: 80px auto 0;
  padding: 2rem;
  flex: 1;
}

.hero {
  text-align: center;
  padding: 4rem 0;
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

.features {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
}

.feature-card {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  width: 45%;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  border: 2px solid #FFD700;
}

.feature-card:hover, .feature-card.hovered {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.card-icon {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.notice-icon {
  font-size: 1.4rem;
  margin-bottom: 1rem;
}

.feature-card h2 {
  color: black;
  margin-bottom: 1rem;
}

.card-action {
  margin-top: 1rem;
  color: black;
  font-weight: bold;
}

.additional-features {
  margin-top: 3rem;
  background-color: white;
  padding: 2rem;
  border-radius: 12px;
}

.additional-features h3 {
  color: black;
  margin-bottom: 1.5rem;
}

.additional-features ul {
  list-style-type: none;
  padding-left: 0;
}

.additional-features li {
  margin-bottom: 1rem;
  padding: 1rem;
  border-radius: 8px;
  background-color: white;
}

.additional-features li.feature-hovered {
  background-color: white;
}

.additional-features a {
  color: black;
  text-decoration: none;
  font-weight: bold;
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

.notice {
  cursor: pointer;
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  width: 80%;
  transition: all 0.3s;
  box-shadow: 0 4px 12px white;
  border-left: 2px solid lightgray;
}
.notice:hover{
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}
</style>