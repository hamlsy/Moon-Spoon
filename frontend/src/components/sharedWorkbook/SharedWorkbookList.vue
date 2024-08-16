<template>
  <div class="main-page">
    <main class="content">
      <section class="hero">
        <h1 class="main-title slide-in-fade"><div class="card-icon">📚 공유된 문제집</div></h1>
        <hr>
        <p class="subtitle slide-in-fade">다른 사용자들의 문제집으로 함께 학습해보세요.</p>
      </section>

      <section class="workbook-categories">
<!--        <h2>카테고리</h2>-->
<!--        <div class="category-buttons">-->
<!--          <button @click="setCategory('recent')" :class="{ active: currentCategory === 'recent' }">최근 올라온 문제집</button>-->
<!--          <button @click="setCategory('popular')" :class="{ active: currentCategory === 'popular' }">인기 문제집</button>-->
<!--          <button @click="setCategory('all')" :class="{ active: currentCategory === 'all' }">전체 문제집</button>-->
<!--        </div>-->
      </section>

      <section class="workbook-grid">
        <div v-for="workbook in filteredWorkbooks" :key="workbook.id" class="workbook-card" @click="goToWorkbookDetail(workbook.id)" @mouseover="hoveredWorkbook = workbook.id" @mouseleave="hoveredWorkbook = null">
          <div class="workbook-info">
            <h3>{{ workbook.title }}</h3>
            <p>작성자: {{ workbook.author }}</p>
            <p>작성일: {{ formatDate(workbook.sharedDate) }}</p>
<!--            <p>댓글: {{ workbook.commentCount }}</p>-->
          </div>
          <div class="workbook-actions">
<!--            <button class="like-button" @click.stop="likeWorkbook(workbook.id)">-->
<!--              👍 {{ workbook.likes }}-->
<!--            </button>-->
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import axios from "axios";
import dayjs from "dayjs";

export default {
  name: 'MainPage',
  data() {
    return {
      currentCategory: 'recent',
      hoveredWorkbook: null,
      sharedWorkbooks: []
    }
  },
  computed: {
    filteredWorkbooks() {
      // 실제로는 여기서 카테고리에 따라 필터링을 구현해야 합니다
      return this.sharedWorkbooks;
    }
  },
  methods: {
    setCategory(category) {
      this.currentCategory = category;
    },
    goToWorkbookDetail(id) {
      this.$router.push(`/sharedWorkbook/${id}`)
    },
    likeWorkbook(id) {
      console.log(`문제집 ${id}에 좋아요`);
    },
    fetchSharedWorkbook(){
      axios.get("/api/sharedWorkbook/all")
          .then((res) => {
            this.sharedWorkbooks = res.data;
            console.log(res, "fetch data");
          })
          .catch((err) => {
            console.log(err, "ERROR!");
          })
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    },
  },
  created() {
    this.fetchSharedWorkbook();
  }
}
</script>

<style scoped>
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

.main-page {
  background: linear-gradient(rgba(255,244,255,0.05) 60%, rgba(232,221,0,0.23));
  color: #191f28;
  min-height: 100vh;
  display: flex;
  width: 100%;
  flex-direction: column;
  font-family: 'Noto Sans KR', sans-serif;
}

.content {
  max-width: 1200px;
  width: 120vh;
  margin: 80px auto 0;
  padding: 2rem;
  flex: 1;
}

.hero {
  text-align: center;
  padding: 0.5rem 0;
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

.workbook-categories {
  margin-bottom: 2rem;
}

.category-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.category-buttons button {
  padding: 0.5rem 1rem;
  border: none;
  background-color: #f0f0f0;
  cursor: pointer;
  border-radius: 20px;
  transition: background-color 0.3s;
}

.category-buttons button.active {
  background-color: #FFD700;
  color: black;
}

.workbook-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
}

.workbook-card {
  background-color: white;
  border-radius: 12px;
  border-left: 1.5px solid yellow;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.workbook-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}

.workbook-info h3 {
  margin-top: 20px;
  margin-bottom: 0px;
  text-align: left;
  margin-left: 20px;
}

.workbook-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
}

.like-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
}

</style>