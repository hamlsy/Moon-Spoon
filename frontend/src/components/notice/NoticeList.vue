<template>
  <div class="main-page">

    <main class="content">
      <div class="notice-container">
        <h1 class="page-title">공지사항</h1>

        <div class="notice-list">
          <div v-for="notice in paginatedNotices" :key="notice.id" class="notice-item" @click="goToDetail(notice.id)">
            <span class="notice-tag">[공지] </span>
            <pre> </pre>
            <span class="notice-title"> {{ notice.title }}</span>
            <span class="notice-info">
            <span>{{ notice.author }}</span>
            <span> {{ formatDate(notice.createDate) }}</span>
            <span v-if="notice.updateDate">수정: {{ formatDate(notice.updateDate) }}</span>
          </span>
          </div>
        </div>

        <div class="pagination" v-if="totalPages > 1">
          <button
              v-for="page in totalPages"
              :key="page"
              @click="changePage(page)"
              :class="{ active: currentPage === page }"
          >
            {{ page }}
          </button>
        </div>

        <button v-if="isAdmin" @click="goToWrite" class="write-button">공지사항 작성</button>
      </div>
    </main>

  </div>
</template>


<script>
import axios from "axios";
import dayjs from "dayjs";

export default {
  name: 'NoticePage',
  data() {
    return {
      isAdmin: false,
      notices: [], // 서버에서 받아올 공지사항 목록
      currentPage: 1,
      itemsPerPage: 7,
      token: localStorage.getItem('token'),
    }
  },
  computed: {
    totalPages() {
      return Math.ceil(this.notices.length / this.itemsPerPage);
    },
    paginatedNotices() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.notices.slice(start, end);
    }
  },
  methods: {
    notValid() {
      alert("아직 구현되지 않은 기능입니다.");
    },
    logout() {
      alert("로그아웃 되었습니다.");
      localStorage.removeItem("token");
      this.$router.go(0);
    },
    goToDetail(id) {
      this.$router.push(`/notice/${id}`);
    },
    goToWrite() {
      this.$router.push('/noticeForm');
    },
    checkAdmin() {
      const headers = {
        'Authorization': this.token
      };
      axios.get("/api/user/isAdmin", {headers})
          .then((res) => {
            this.isAdmin = res.data.admin;
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })
    },
    changePage(page) {
      this.currentPage = page;
    },
    fetchNotices() {
      const headers = {
        'Authorization': this.token
      };
      axios.get("/api/notice/list", {headers})
          .then((res) => {
            this.notices = res.data;
            console.log(res, "notice loaded");
          })
          .catch((err) => {
            console.log(err, "ERROR");
          })

    },
    formatDate(dateString) {
      return dayjs(dateString).format('YY.MM.DD HH:mm');
    }
  },
  created() {
    this.checkAdmin();
    this.fetchNotices();
  }
}
</script>

<style scoped>
/* 기존 스타일 유지 */

/** slide fade end **/
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Noto Sans KR', sans-serif;
}

.main-page {
  /** background: linear-gradient(rgba(255,244,255,0.05) 40%, rgba(232,221,0,0.53)); **/
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


.feature-card h2 {
  color: black;
  margin-bottom: 1rem;
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
  transition: background-color 0.3s;
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

.content {
  display: flex;
  justify-content: center;
  padding: 1rem;

  margin: 40px;
}

.page-title {
  text-align: center;
  margin-bottom: 2rem;
}

.notice-container {
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 200px); /* 대략적인 높이 조정 */

  width: 100%;
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
}
.notice-list {
  border-top: 2px solid #e0e0e0;
  flex-grow: 1;
}

.notice-item {
  display: flex;
  align-items: center;
  padding: 1rem 0;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-title {
  flex-grow: 1;
  font-weight: bold;
}

.notice-tag {
  color: red;
  font-weight: bold;
  margin-left: 0.5rem;
}

.notice-info {
  color: #666;
  font-size: 0.9rem;
}

.notice-info span {
  margin-left: 1rem;
}
.notice-info span {
  margin-left: 1rem;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
}

.pagination button {
  margin: 0 0.5rem;
}

.write-button {
  display: block;
  margin: 2rem auto 0;
  padding: 0.5rem 1rem;
  background-color: #FFD700;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  position: sticky;
  bottom: 1rem;
  background-color: white;
  padding: 1rem 0;
}

.pagination button {
  margin: 0 0.25rem;
  padding: 0.5rem 1rem;
  border: 1px solid #e0e0e0;
  background-color: white;
  cursor: pointer;
}

.pagination button.active {
  background-color: #FFD700;
  color: white;
  border-color: #FFD700;
}
</style>