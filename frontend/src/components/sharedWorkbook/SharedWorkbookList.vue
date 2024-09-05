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
      <div class="search-sort-container">
        <input class="search-input-box" v-model="searchQuery" placeholder="문제집 검색"/>
        <button class="search-btn" @click="getSharedWorkbook(1)">🔎 검색</button>
      </div>
      <section class="workbook-grid">
        <div v-for="workbook in sharedWorkbooks" :key="workbook.id" class="workbook-card" @click="goToWorkbookDetail(workbook.id)" @mouseover="hoveredWorkbook = workbook.id" @mouseleave="hoveredWorkbook = null">
          <div class="workbook-info">
            <h3>{{ truncateText(workbook.title) }}</h3>
            <p>작성자: {{ workbook.author }}</p>
            <p>작성일: {{ formatDate(workbook.createDate) }}</p>
<!--            <p>댓글: {{ workbook.commentCount }}</p>-->
          </div>
          <div class="workbook-actions">
<!--            <button class="like-button" @click.stop="likeWorkbook(workbook.id)">-->
<!--              👍 {{ workbook.likes }}-->
<!--            </button>-->
          </div>
        </div>

      </section>
      <div class="pagination">
        <button v-for="page in totalPages" :key="page"
                :class="{ 'active': currentPage === page }"
                @click="getSharedWorkbook(page)">
          {{ page }}
        </button>
      </div>
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
      sharedWorkbooks: [],
      searchQuery: '',
      totalPages: '',
      currentPage: 1,
      pageSize: 12
    }
  },
  computed: {

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
    getSharedWorkbook(page){
      axios.get(`/api/sharedWorkbook/all?keyword=${this.searchQuery}&page=${page - 1}&size=${this.pageSize}`)
          .then((res) => {
            this.sharedWorkbooks = res.data.content;
            this.currentPage = page;
            this.totalPages = res.data.totalPages;
            console.log(res,"fetch page");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    },
    truncateText(text, maxLength = 30) {
      return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
    },

  },
  created() {
    this.getSharedWorkbook(1);

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
  /** background: linear-gradient(rgba(255,244,255,0.05) 60%, rgba(232,221,0,0.23)); **/
  color: #191f28;
  min-height: 100vh;
  display: flex;
  width: 100%;
  flex-direction: column;
  font-family: 'Noto Sans KR', sans-serif;

}

.content {
  /** max-width: 1200px; **/
  /** margin: 80px auto 0px; **/
  margin-top: 80px;
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
  justify-content: center;
}

.workbook-card {
  background-color: white;
  border-radius: 12px;
  border-left:3.5px solid #FFD700;;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 6px 16px rgba(0,0,0,0.1);
  word-break: break-all;
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

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination button {
  background-color: #FFD700;
  border: none;
  color: #191f28;
  padding: 0.5rem 1rem;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1rem;
  margin: 0 0.25rem;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.pagination button:hover {
  background-color: #FFC000;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.pagination button.active {
  background-color: #1B2A49;
  color: #fff;
  font-weight: bold;
}

.search-sort-container {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;

  gap: 20px;
}

.search-sort-container input {
  flex-grow: 1;
  min-width: 200px;
  max-width: 500px; /* 최대 너비를 고정 */
  box-sizing: border-box;
  padding: 0.5rem;
  border-radius: 4px;
  border: 1px solid #ccc;

}
.search-btn{
  background-color: #FFD700;
  color: #191f28;
  border: none;
  border-radius: 10px;
  /** padding: 10px 24px; **/
  padding-right: 24px;
  padding-left: 14px;

  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
}

.search-btn:hover{
  background-color: #FFC000;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
</style>